package project.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import project.dao.FilmDAO;
import project.dao.ZanrDAO;
import project.model.Film;
import project.model.Zanr;

@Repository
public class JDBCFilmDAO implements FilmDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ZanrDAO zanrDAO; 
	
	private class FilmZanrRowCallBackHandler implements RowCallbackHandler { 

		private Map<Long, Film> filmovi = new LinkedHashMap<>(); 
		
		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
			int index = 1;
			Long filmId = resultSet.getLong(index++);
			String filmNaziv = resultSet.getString(index++);
			Integer filmTrajanje = resultSet.getInt(index++);

			Film film = filmovi.get(filmId);
			if (film == null) {
				film = new Film(filmId, filmNaziv, filmTrajanje);
				filmovi.put(film.getId(), film);
			}

			Long zanrId = resultSet.getLong(index++);
			Zanr zanr = zanrDAO.findOne(zanrId); 
			film.getZanrovi().add(zanr);
		}

		public List<Film> getFilmovi() {
			return new ArrayList<>(filmovi.values()); 
		}

	}

	private static class FilmZanrJOINRowCallBackHandler implements RowCallbackHandler {

		private Map<Long, Film> filmovi = new LinkedHashMap<>(); 
		
		@Override
		public void processRow(ResultSet resultSet) throws SQLException { 
			int index = 1;
			Long filmId = resultSet.getLong(index++);
			String filmNaziv = resultSet.getString(index++);
			Integer filmTrajanje = resultSet.getInt(index++);

			Film film = filmovi.get(filmId);
			if (film == null) {
				film = new Film(filmId, filmNaziv, filmTrajanje);
				filmovi.put(film.getId(), film); 
			}

			Long zanrId = resultSet.getLong(index++);
			String zanrNaziv = resultSet.getString(index++);
			Zanr zanr = new Zanr(zanrId, zanrNaziv);
			film.getZanrovi().add(zanr);
		}

		public List<Film> getFilmovi() {
			return new ArrayList<>(filmovi.values());
		}

	}
	
	@Override
	public Film findOne(Long id) {
		String sql = 
				"SELECT f.id, f.naziv, f.trajanje, fz.zanrId FROM filmovi f " + 
				"LEFT JOIN filmZanr fz ON fz.filmId = f.id " + 
				"WHERE f.id = ? " + 
				"ORDER BY f.id";

		FilmZanrRowCallBackHandler rowCallbackHandler = new FilmZanrRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, id);

		return rowCallbackHandler.getFilmovi().get(0);
	}

	@Override
	public List<Film> findAll() {
		String sql = 
				"SELECT f.id, f.naziv, f.trajanje, z.id, z.naziv FROM filmovi f " + 
				"LEFT JOIN filmZanr fz ON fz.filmId = f.id " + 
				"LEFT JOIN zanrovi z ON fz.zanrId = z.id " + 
				"ORDER BY f.id";

		FilmZanrJOINRowCallBackHandler rowCallbackHandler = new FilmZanrJOINRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler);

		return rowCallbackHandler.getFilmovi(); 
	}

	@Transactional 
	@Override
	public void save(Film film) {
		
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO filmovi (naziv, trajanje) VALUES (?, ?)";

				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); 
				int index = 1;
				preparedStatement.setString(index++, film.getNaziv());
				preparedStatement.setInt(index++, film.getTrajanje());

				return preparedStatement;
			}

		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		if (uspeh) {
			String sql = "INSERT INTO filmZanr (filmId, zanrId) VALUES (?, ?)";
			for (Zanr itZanr: film.getZanrovi()) {	
				jdbcTemplate.update(sql, keyHolder.getKey(), itZanr.getId()); 
			}
		}
	}

	@Transactional
	@Override
	public void update(Film film) {
		String sql = "DELETE FROM filmZanr WHERE filmId = ?";
		jdbcTemplate.update(sql, film.getId());
	
		sql = "INSERT INTO filmZanr (filmId, zanrId) VALUES (?, ?)";
		for (Zanr itZanr: film.getZanrovi()) {	
			jdbcTemplate.update(sql, film.getId(), itZanr.getId());
		}

		sql = "UPDATE filmovi SET naziv = ?, trajanje = ? WHERE id = ?";	
		jdbcTemplate.update(sql, film.getNaziv(), film.getTrajanje(), film.getId());
	}

	@Transactional
	@Override
	public void delete(Long id) {
		String sql = "DELETE FROM filmZanr WHERE filmId = ?";
		jdbcTemplate.update(sql, id);

		sql = "DELETE FROM filmovi WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}

}
