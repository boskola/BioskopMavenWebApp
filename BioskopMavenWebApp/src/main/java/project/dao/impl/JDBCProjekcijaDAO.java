package project.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import project.dao.FilmDAO;
import project.dao.ProjekcijaDAO;
import project.model.Film;
import project.model.Projekcija;

@Repository
public class JDBCProjekcijaDAO implements ProjekcijaDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private FilmDAO filmDAO;
	
	private class ProjekcijaRowMapper implements RowMapper<Projekcija> {

		@Override
		public Projekcija mapRow(ResultSet rs, int rowNum) throws SQLException {
			int index = 1;
			Long projekcijaId = rs.getLong(index++);
			LocalDateTime projekcijaDatumIVreme = rs.getTimestamp(index++).toLocalDateTime();
			String projekcijaTip = rs.getString(index++);
			Integer projekcijaSala = rs.getInt(index++);
			Double projekcijaCenaKarte = rs.getDouble(index++);

			Long filmId = rs.getLong(index++);
			Film film = filmDAO.findOne(filmId);

			Projekcija projekcija = new Projekcija(projekcijaId, projekcijaDatumIVreme, film, projekcijaSala, projekcijaTip, projekcijaCenaKarte);
			return projekcija;
		}

	}

	private static class ProjekcijaJOINRowMapper implements RowMapper<Projekcija> {

		@Override
		public Projekcija mapRow(ResultSet rs, int rowNum) throws SQLException {
			int index = 1;
			Long projekcijaId = rs.getLong(index++);
			LocalDateTime projekcijaDatumIVreme = rs.getTimestamp(index++).toLocalDateTime();
			String projekcijaTip = rs.getString(index++);
			Integer projekcijaSala = rs.getInt(index++);
			Double projekcijaCenaKarte = rs.getDouble(index++);

			Long filmId = rs.getLong(index++);
			String filmNaziv = rs.getString(index++);
			Integer filmTrajanje = rs.getInt(index++);
			Film film = new Film(filmId, filmNaziv, filmTrajanje);

			Projekcija projekcija = new Projekcija(projekcijaId, projekcijaDatumIVreme, film, projekcijaSala, projekcijaTip, projekcijaCenaKarte);
			return projekcija;
		}

	}
	
	@Override
	public Projekcija findOne(Long id) {
		String sql = 
				"SELECT p.id, p.datumIVreme, p.tip, p.sala, p.cenaKarte, p.filmId FROM projekcije p " + 
				"WHERE p.id = ? " + 
				"ORDER BY p.id";
		return jdbcTemplate.queryForObject(sql, new ProjekcijaRowMapper(), id);
	}

	@Override
	public List<Projekcija> findAll() {
		String sql = 
				"SELECT p.id, p.datumIVreme, p.tip, p.sala, p.cenaKarte, f.id, f.naziv, f.trajanje FROM projekcije p " + 
				"LEFT JOIN filmovi f ON p.filmId = f.id " + 
				"ORDER BY p.id";
		return jdbcTemplate.query(sql, new ProjekcijaJOINRowMapper());
	}

	@Override
	public List<Projekcija> find(LocalDateTime datumIVremeOd, LocalDateTime datumIVremeDo, Long filmId, String tip, Integer sala, Double cenaKarteOd, Double cenaKarteDo) {
		tip = (tip.equals(""))? "%" + tip + "%": tip;
		if (filmId == 0 && sala == 0) {
			String sql = 
					"SELECT p.id, p.datumIVreme, p.tip, p.sala, p.cenaKarte, f.id, f.naziv, f.trajanje FROM projekcije p " + 
					"LEFT JOIN filmovi f ON p.filmId = f.id " + 
					"WHERE p.datumIVreme >= ? AND p.datumIVreme <= ? AND p.tip LIKE ? AND p.cenaKarte >= ? AND p.cenaKarte <= ? " + 
					"ORDER BY p.id";
			return jdbcTemplate.query(sql, new ProjekcijaRowMapper(), datumIVremeOd, datumIVremeDo, tip, cenaKarteOd, cenaKarteDo);
		} else if (filmId == 0 && sala > 0) {
			String sql = 
					"SELECT p.id, p.datumIVreme, p.tip, p.sala, p.cenaKarte, f.id, f.naziv, f.trajanje FROM projekcije p " + 
					"LEFT JOIN filmovi f ON p.filmId = f.id " + 
					"WHERE p.datumIVreme >= ? AND p.datumIVreme <= ? AND p.tip LIKE ? AND p.sala = ? AND p.cenaKarte >= ? AND p.cenaKarte <= ? " + 
					"ORDER BY p.id";
			return jdbcTemplate.query(sql, new ProjekcijaRowMapper(), datumIVremeOd, datumIVremeDo, tip, sala, cenaKarteOd, cenaKarteDo);
		} else if (filmId > 0 && sala == 0) {
			String sql = 
					"SELECT p.id, p.datumIVreme, p.tip, p.sala, p.cenaKarte, f.id, f.naziv, f.trajanje FROM projekcije p " + 
					"LEFT JOIN filmovi f ON p.filmId = f.id " + 
					"WHERE p.datumIVreme >= ? AND p.datumIVreme <= ? AND p.filmId = ? AND p.tip LIKE ? AND p.cenaKarte >= ? AND p.cenaKarte <= ? " + 
					"ORDER BY p.id";
			return jdbcTemplate.query(sql, new ProjekcijaRowMapper(), datumIVremeOd, datumIVremeDo, filmId, tip, cenaKarteOd, cenaKarteDo);
		} else {
			String sql = 
					"SELECT p.id, p.datumIVreme, p.tip, p.sala, p.cenaKarte, f.id, f.naziv, f.trajanje FROM projekcije p " + 
					"LEFT JOIN filmovi f ON p.filmId = f.id " + 
					"WHERE p.datumIVreme >= ? AND p.datumIVreme <= ? AND p.filmId = ? AND p.tip LIKE ? AND p.sala = ? AND p.cenaKarte >= ? AND p.cenaKarte <= ? " + 
					"ORDER BY p.id";
			return jdbcTemplate.query(sql, new ProjekcijaRowMapper(), datumIVremeOd, datumIVremeDo, filmId, tip, sala, cenaKarteOd, cenaKarteDo);
		}
	}

	@Override
	public void save(Projekcija projekcija) {
		String sql = "INSERT INTO projekcije (datumIVreme, filmId, tip, sala, cenaKarte) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, projekcija.getDatumIVreme(), projekcija.getFilm().getId(), projekcija.getTip(), projekcija.getSala(), projekcija.getCenaKarte());
	}

	@Override
	public void update(Projekcija projekcija) {
		String sql = "UPDATE projekcije SET datumIVreme = ?, filmID = ?, tip = ?, sala = ?, cenaKarte = ? WHERE id  = ?";
		jdbcTemplate.update(sql, projekcija.getDatumIVreme(), projekcija.getFilm().getId(), projekcija.getTip(), projekcija.getSala(), projekcija.getCenaKarte(), projekcija.getId());
	}

	@Override
	public void delete(Long id) {
		String sql = "DELETE FROM projekcije WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}
	
}
