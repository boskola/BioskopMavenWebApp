package project.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.dao.FilmDAO;
import project.model.Film;
import project.model.Zanr;
import project.service.FilmService;

@Service
public class DatabaseFilmService implements FilmService {

	@Autowired
	private FilmDAO filmDAO;
	
	@Override
	public Film findOne(Long id) {
		return filmDAO.findOne(id);
	}

	@Override
	public List<Film> findAll() {
		return filmDAO.findAll();
	}

	@Override
	public Film save(Film film) {
		filmDAO.save(film);
		return film;
	}

	@Override
	public List<Film> save(List<Film> filmovi) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Film update(Film film) {
		filmDAO.update(film);
		return film;
	}

	@Override
	public List<Film> update(List<Film> filmovi) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Film delete(Long id) {
		Film film = findOne(id);
		if (film != null)
			filmDAO.delete(id);
		
		return film;
	}

	@Override
	public List<Film> deleteAll(Zanr zanr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(List<Long> ids) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Film> find(String naziv, Long zanrId, Integer trajanjeOd, Integer trajanjeDo) {
		List<Film> filmovi = filmDAO.findAll();

		if (naziv == null) {
			naziv = "";
		}
		if (zanrId == null) {
			zanrId = 0L;
		}
		if (trajanjeOd == null) {
			trajanjeOd = 0;
		}
		if (trajanjeDo == null) {
			trajanjeDo = Integer.MAX_VALUE;
		}
		
		List<Film> rezultat = new ArrayList<>();
		for (Film itFilm: filmovi) {
		
			if (!itFilm.getNaziv().toLowerCase().contains(naziv.toLowerCase())) {
				continue;
			}
			if (zanrId > 0) { 
				boolean pronadjen = false;
				for (Zanr itZanr: itFilm.getZanrovi()) {
					if (itZanr.getId() == zanrId) {
						pronadjen = true;
						break;
					}
				}
				if (!pronadjen) {
					continue;
				}
			}
			if (!(itFilm.getTrajanje() >= trajanjeOd && itFilm.getTrajanje() <= trajanjeDo)) {
				continue;
			}

			rezultat.add(itFilm);
		}

		return rezultat;
	}

	@Override
	public List<Film> findByZanrId(Long zanrId) {
		// TODO Auto-generated method stub
		return null;
	}

}
