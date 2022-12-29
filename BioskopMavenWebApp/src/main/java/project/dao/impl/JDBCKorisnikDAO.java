package project.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import project.dao.KorisnikDAO;
import project.model.Korisnik;

@Repository
public class JDBCKorisnikDAO implements KorisnikDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static class KorisnikRowMapper implements RowMapper<Korisnik> {

		@Override
		public Korisnik mapRow(ResultSet rs, int rowNum) throws SQLException {
			int index = 1;
			String korisnickoIme = rs.getString(index++);
			String eMail = rs.getString(index++);
			String pol = rs.getString(index++);
			Boolean administrator = rs.getBoolean(index++);

			Korisnik korisnik = new Korisnik(korisnickoIme, null, eMail, pol, administrator);
			return korisnik;
		}

	}

	@Override
	public Korisnik findOne(String korisnickoIme) {
		try {
			String sql = "SELECT korisnickoIme, eMail, pol, administrator FROM korisnici WHERE korisnickoIme = ?";
			return jdbcTemplate.queryForObject(sql, new KorisnikRowMapper(), korisnickoIme);
		} catch (EmptyResultDataAccessException ex) {
			// ako korisnik nije pronađen
			return null;
		}
	}

	@Override
	public Korisnik findOne(String korisnickoIme, String lozinka) {
		try {
			String sql = "SELECT korisnickoIme, eMail, pol, administrator FROM korisnici WHERE korisnickoIme = ? AND lozinka = ?";
			return jdbcTemplate.queryForObject(sql, new KorisnikRowMapper(), korisnickoIme, lozinka);
		} catch (EmptyResultDataAccessException ex) {
			// ako korisnik nije pronađen
			return null;
		}
	}

	@Override
	public List<Korisnik> findAll() {
		String sql = "SELECT korisnickoIme, eMail, pol, administrator FROM korisnici";
		return jdbcTemplate.query(sql, new KorisnikRowMapper());
	}

	@Override
	public List<Korisnik> find(String korisnickoIme, String eMail, String pol, Boolean administrator) {
		korisnickoIme = "%" + korisnickoIme + "%";
		eMail = "%" + eMail + "%";
		pol = (pol.equals(""))? "%" + pol + "%": pol;
		String administratorSql = (administrator)? "administrator = 1": "administrator >= 0";
		String sql = "SELECT korisnickoIme, eMail, pol, administrator FROM korisnici WHERE korisnickoIme LIKE ? AND eMail LIKE ? AND pol LIKE ? AND " + administratorSql;
		return jdbcTemplate.query(sql, new KorisnikRowMapper(), korisnickoIme, eMail, pol);
	}

	@Override
	public void save(Korisnik korisnik) {
		String sql = "INSERT INTO korisnici (korisnickoIme, lozinka, eMail, pol, administrator) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, korisnik.getKorisnickoIme(), korisnik.getLozinka(), korisnik.getEMail(), korisnik.getPol(), korisnik.isAdministrator());
	}

	@Override
	public void update(Korisnik korisnik) {
		if (korisnik.getLozinka() == null) {
			String sql = "UPDATE korisnici SET eMail = ?, pol = ?, administrator = ? WHERE korisnickoIme = ?";
			jdbcTemplate.update(sql, korisnik.getEMail(), korisnik.getPol(), korisnik.isAdministrator(), korisnik.getKorisnickoIme());
		} else {
			String sql = "UPDATE korisnici SET lozinka = ?, eMail = ?, pol = ?, administrator = ? WHERE korisnickoIme = ?";
			jdbcTemplate.update(sql, korisnik.getLozinka(), korisnik.getEMail(), korisnik.getPol(), korisnik.isAdministrator(), korisnik.getKorisnickoIme());
		}
	}

	@Override
	public void delete(String korisnickoIme) {
		String sql = "DELETE FROM korisnici WHERE korisnickoIme = ?";
		jdbcTemplate.update(sql, korisnickoIme);
	}

}
