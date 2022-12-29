package project.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletContextAware;

import project.model.Film;
import project.model.FilmStatistika;
import project.model.Korisnik;
import project.service.FilmService;
import project.service.ZanrService;

@RestController
@RequestMapping(value="/Filmovi")
public class FilmoviController implements ServletContextAware {

	public static final String STATISTIKA_FILMOVA_KEY = "statistikaFilmova";
	public static final String POSECENI_FILMOVI_ZA_KORISNIKA_KEY = "poseceniFilmoviZaKorisnika";
	
	@Autowired
	private FilmService filmService;

	@Autowired
	private ZanrService zanrService;
	
	@Autowired
	private ServletContext servletContext;

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	} 

	@GetMapping
	public Map<String, Object> index(
			@RequestParam(required=false, defaultValue="") String naziv, 
			@RequestParam(required=false, defaultValue="0") Long zanrId, 
			@RequestParam(required=false, defaultValue="0") Integer trajanjeOd, 
			@RequestParam(required=false, defaultValue="2147483647") Integer trajanjeDo)  throws IOException {
		List<Film> filmovi = filmService.find(naziv, zanrId, trajanjeOd, trajanjeDo);

		Map<String, Object> odgovor = new LinkedHashMap<>();
		odgovor.put("status", "ok");
		odgovor.put("filmovi", filmovi);
		return odgovor;
	}

	@GetMapping(value="/Details")
	@SuppressWarnings("unchecked")
	public Map<String, Object> details(@RequestParam Long id, 
			HttpSession session) throws IOException {
		Film film = filmService.findOne(id);

		FilmStatistika statistikaFilmova = (FilmStatistika) servletContext.getAttribute(FilmoviController.STATISTIKA_FILMOVA_KEY);
		statistikaFilmova.incrementBrojac(film);

		List<Film> poseceniFilmovi = (List<Film>) session.getAttribute(FilmoviController.POSECENI_FILMOVI_ZA_KORISNIKA_KEY);
		if (!poseceniFilmovi.contains(film)) {
			poseceniFilmovi.add(film);
		}

		Map<String, Object> odgovor = new LinkedHashMap<>();
		odgovor.put("status", "ok");
		odgovor.put("film", film);
		return odgovor;
	}

	@GetMapping(value="/StatistikaFilmova")
	public Map<String, Object> statistikaFilmova() throws IOException {

		FilmStatistika statistikaFilmova = (FilmStatistika) servletContext.getAttribute(FilmoviController.STATISTIKA_FILMOVA_KEY);

		Map<String, Object> odgovor = new LinkedHashMap<>();
		odgovor.put("status", "ok");
		odgovor.put("statistikaFilmova", statistikaFilmova);
		return odgovor;
	}

	@GetMapping(value="/PoseceniFilmovi")
	@SuppressWarnings("unchecked")
	public Map<String, Object> poseceniFilmovi(HttpSession session) throws IOException {
	
		List<Film> poseceniFilmovi = (List<Film>) session.getAttribute(FilmoviController.POSECENI_FILMOVI_ZA_KORISNIKA_KEY);

		Map<String, Object> odgovor = new LinkedHashMap<>();
		odgovor.put("status", "ok");
		odgovor.put("poseceniFilmovi", poseceniFilmovi);
		return odgovor;
	}
	
	@PostMapping(value="/Create")
	public Map<String, Object> create(@RequestParam String naziv, @RequestParam int trajanje, @RequestParam(name="zanrId", required=false) Long[] zanrIds, 
			HttpSession session) throws IOException {
		
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			Map<String, Object> odgovor = new LinkedHashMap<>();
			odgovor.put("status", "odbijen");
			return odgovor;
		}
		try {
			
			if (naziv == null || naziv.equals("")) {
				throw new Exception("Naziv ne sme biti prazan!");
			}
			if (trajanje < 5) {
				throw new Exception("Trajanje mora biti ceo broj veći ili jednak 5!");
			}
			if (zanrIds == null || zanrIds.length <= 0 ) {
				throw new Exception("Morate odabrati bar 1 žanr!");
			}

	
			Film film = new Film(naziv, trajanje);
			film.setZanrovi(zanrService.find(zanrIds));
			filmService.save(film);
			
			Map<String, Object> odgovor = new LinkedHashMap<>();
			odgovor.put("status", "ok");	
			return odgovor;
		} catch (Exception ex) {
			
			String poruka = ex.getMessage();
			if (poruka == "") {
				poruka = "Neuspešno dodavanje!";
			}
			
			Map<String, Object> odgovor = new LinkedHashMap<>();
			odgovor.put("status", "greska");
			odgovor.put("poruka", poruka);
			return odgovor;
		}
	}

	@PostMapping(value="/Edit")
	public Map<String, Object> edit(@RequestParam Long id, 
			@RequestParam String naziv, @RequestParam int trajanje, @RequestParam(name="zanrId", required=false) Long[] zanrIds, 
			HttpSession session) throws IOException {
		
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			Map<String, Object> odgovor = new LinkedHashMap<>();
			odgovor.put("status", "odbijen");
			return odgovor;
		}
		try {
			
			Film film = filmService.findOne(id);
			if (film == null) {
				throw new Exception("Film nije pronađen!");
			}	
			if (naziv == null || naziv.equals("")) {
				throw new Exception("Naziv ne sme biti prazan!");
			}
			if (trajanje < 5) {
				throw new Exception("Trajanje mora biti ceo broj veći ili jednak 5!");
			}
			if (zanrIds == null || zanrIds.length <= 0 ) {
				throw new Exception("Morate odabrati bar 1 žanr!");
			}

			
			film.setNaziv(naziv);
			film.setTrajanje(trajanje);
			film.setZanrovi(zanrService.find(zanrIds));
			filmService.update(film);
			
			Map<String, Object> odgovor = new LinkedHashMap<>();
			odgovor.put("status", "ok");	
			return odgovor;
		} catch (Exception ex) {
		
			String poruka = ex.getMessage();
			if (poruka == "") {
				poruka = "Neuspešno dodavanje!";
			}
			
			Map<String, Object> odgovor = new LinkedHashMap<>();
			odgovor.put("status", "greska");
			odgovor.put("poruka", poruka);
			return odgovor;
		}
	}

	@PostMapping(value="/Delete")
	public Map<String, Object> delete(@RequestParam Long id, 
			HttpSession session) throws IOException {
	
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			Map<String, Object> odgovor = new LinkedHashMap<>();
			odgovor.put("status", "odbijen");
			return odgovor;
		}

	
		filmService.delete(id);
	
		Map<String, Object> odgovor = new LinkedHashMap<>();
		odgovor.put("status", "ok");
		return odgovor;
	}

}
