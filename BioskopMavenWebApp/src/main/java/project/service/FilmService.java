package project.service;

import java.util.List;

import project.model.Film;
import project.model.Zanr;

public interface FilmService {

	Film findOne(Long id);
	List<Film> findAll();
	Film save(Film film);
	List<Film> save(List<Film> filmovi);
	Film update(Film film);
	List<Film> update(List<Film> filmovi);
	Film delete(Long id);
	List<Film> deleteAll(Zanr zanr);
	void delete(List<Long> ids);
	List<Film> find(String naziv, Long zanrId, Integer trajanjeOd, Integer trajanjeDo);
	List<Film> findByZanrId(Long zanrId);

}
