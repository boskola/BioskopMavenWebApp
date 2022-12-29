package project.service;

import java.time.LocalDateTime;
import java.util.List;

import project.model.Film;
import project.model.Projekcija;

public interface ProjekcijaService {
	
	Projekcija findOne(Long id);
	List<Projekcija> findAll();
	Projekcija save(Projekcija projekcija);
	List<Projekcija> save(List<Projekcija> projekcije);
	Projekcija update(Projekcija projekcija);
	List<Projekcija> update(List<Projekcija> projekcije);
	Projekcija delete(Long id);
	List<Projekcija> deleteAll(Film film);
	List<Projekcija> deleteAll(List<Film> filmovi);
	void delete(List<Long> ids);
	List<Projekcija> find(LocalDateTime datumIVremeOd, LocalDateTime datumIVremeDo, Long filmId, String tip, Integer sala, Double cenaKarteOd, Double cenaKarteDo);
	List<Projekcija> findByFilmId(Long filmId);

}
