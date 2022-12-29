package project.service;

import java.util.List;

import project.model.Korisnik;

public interface KorisnikService {

	Korisnik findOne(String korisnickoIme);
	Korisnik findOne(String korisnickoIme, String lozinka);
	List<Korisnik> findAll();
	Korisnik save(Korisnik korisnik);
	List<Korisnik> save(List<Korisnik> korisnici);
	Korisnik update(Korisnik korisnik);
	List<Korisnik> update(List<Korisnik> korisnici);
	Korisnik delete(String korisnickoIme);
	void delete(List<String> korisnickaImena);
	List<Korisnik> find(String korisnickoIme, String eMail, String pol, Boolean administrator);
	List<Korisnik> findByKorisnickoIme(String korisnickoIme);

}
