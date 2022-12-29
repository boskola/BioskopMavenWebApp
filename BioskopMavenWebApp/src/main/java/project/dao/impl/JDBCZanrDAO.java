package project.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import project.dao.ZanrDAO;
import project.model.Zanr;

@Repository
public class JDBCZanrDAO implements ZanrDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static class ZanrRowMapper implements RowMapper<Zanr> {

		@Override
		public Zanr mapRow(ResultSet rs, int rowNum) throws SQLException {
			int index = 1;
			Long id = rs.getLong(index++);
			String naziv = rs.getString(index++);

			Zanr zanr = new Zanr(id, naziv);
			return zanr;
		}

	}

	@Override
	public Zanr findOne(Long id) {
		String sql = "SELECT id, naziv FROM zanrovi WHERE id = ?";
		return jdbcTemplate.queryForObject(sql, new ZanrRowMapper(), id);
	}

	@Override
	public List<Zanr> findAll() {
		String sql = "SELECT id, naziv FROM zanrovi";
		return jdbcTemplate.query(sql, new ZanrRowMapper());
	}

	@Override
	public List<Zanr> find(String naziv) {
		naziv = "%" + naziv + "%";
		String sql = "SELECT id, naziv FROM zanrovi WHERE naziv LIKE ?";
		return jdbcTemplate.query(sql, new ZanrRowMapper(), naziv);
	}

	@Override
	public void save(Zanr zanr) {
		String sql = "INSERT INTO zanrovi (naziv) VALUES (?)";
		jdbcTemplate.update(sql, zanr.getNaziv());
	}

	@Override
	public void update(Zanr zanr) {
		String sql = "UPDATE zanrovi SET naziv = ? WHERE id = ?";
		jdbcTemplate.update(sql, zanr.getNaziv(), zanr.getId());
	}

	@Override
	public void delete(Long id) {
		String sql = "DELETE FROM zanrovi WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}
	
}
