package project.dao;

import java.util.List;

import project.model.Film;

public interface FilmDAO {

	public Film findOne(Long id);
	public List<Film> findAll();
	public void save(Film film);
	public void update(Film film);
	public void delete(Long id);

}
