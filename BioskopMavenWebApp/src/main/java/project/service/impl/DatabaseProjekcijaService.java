package project.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.dao.ProjekcijaDAO;
import project.model.Film;
import project.model.Projekcija;
import project.service.ProjekcijaService;

@Service
public class DatabaseProjekcijaService implements ProjekcijaService {

	@Autowired
	private ProjekcijaDAO projekcijaDAO;
	
	@Override
	public Projekcija findOne(Long id) {
		return projekcijaDAO.findOne(id);
	}

	@Override
	public List<Projekcija> findAll() {
		return projekcijaDAO.findAll();
	}

	@Override
	public Projekcija save(Projekcija projekcija) {
		projekcijaDAO.save(projekcija);
		return projekcija;
	}

	@Override
	public List<Projekcija> save(List<Projekcija> projekcije) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Projekcija update(Projekcija projekcija) {
		projekcijaDAO.update(projekcija);
		return projekcija;
	}

	@Override
	public List<Projekcija> update(List<Projekcija> projekcije) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Projekcija delete(Long id) {
		Projekcija projekcija = findOne(id);
		if (projekcija != null) {
			projekcijaDAO.delete(id);
		}
		return projekcija;
	}

	@Override
	public List<Projekcija> deleteAll(Film film) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Projekcija> deleteAll(List<Film> filmovi) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(List<Long> ids) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Projekcija> find(LocalDateTime datumIVremeOd, LocalDateTime datumIVremeDo, Long filmId, String tip,
			Integer sala, Double cenaKarteOd, Double cenaKarteDo) {
	
		if (filmId == null) {
			filmId = 0L;
		}
		if (sala == null) {
			sala = 0;
		}
		if (cenaKarteOd == null) {
			cenaKarteOd = 0.0;
		}
		if (cenaKarteDo == null) {
			cenaKarteDo = Double.MAX_VALUE;
		}
		return projekcijaDAO.find(datumIVremeOd, datumIVremeDo, filmId, tip, sala, cenaKarteOd, cenaKarteDo);
	}

	@Override
	public List<Projekcija> findByFilmId(Long filmId) {
		// TODO Auto-generated method stub
		return null;
	}

}
