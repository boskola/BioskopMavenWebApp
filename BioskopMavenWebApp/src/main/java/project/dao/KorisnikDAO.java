package project.dao;

import java.util.List;

import project.model.Korisnik;

public interface KorisnikDAO {

	public Korisnik findOne(String korisnickoIme);
	public Korisnik findOne(String korisnickoIme, String lozinka);
	public List<Korisnik> findAll();
	public List<Korisnik> find(String korisnickoIme, String eMail, String pol, Boolean administrator);
	public void save(Korisnik korisnik);
	public void update(Korisnik korisnik);
	public void delete(String korisnickoIme);

}
