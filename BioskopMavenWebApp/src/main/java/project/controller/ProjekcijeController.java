package project.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project.model.Film;
import project.model.Korisnik;
import project.model.Projekcija;
import project.service.FilmService;
import project.service.ProjekcijaService;

@Controller
@RequestMapping(value="/Projekcije")
public class ProjekcijeController {

	@Autowired
	private ProjekcijaService projekcijaService;

	@Autowired
	private FilmService filmService;

	@GetMapping
	public String Index(
			@RequestParam(required=false, defaultValue="2020-01-01") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate datumOd, 
			@RequestParam(required=false, defaultValue="08:00") @DateTimeFormat(iso=DateTimeFormat.ISO.TIME) LocalTime vremeOd, 
			@RequestParam(required=false, defaultValue="3000-01-01") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate datumDo, 
			@RequestParam(required=false, defaultValue="23:00") @DateTimeFormat(iso=DateTimeFormat.ISO.TIME) LocalTime vremeDo, 
			@RequestParam(required=false, defaultValue="0") Long filmId, 
			@RequestParam(required=false, defaultValue="0") Integer sala, 
			@RequestParam(required=false, defaultValue="") String tip, 
			@RequestParam(required=false, defaultValue="0") Double cenaKarteOd, 
			@RequestParam(required=false, defaultValue="1.7976931348623157e+308") Double cenaKarteDo, 
			HttpSession session, ModelMap model) throws IOException {
		
		LocalDateTime datumIVremeOd = LocalDateTime.of(datumOd, vremeOd);
		LocalDateTime datumIVremeDo = LocalDateTime.of(datumDo, vremeDo);
		List<Projekcija> projekcije = projekcijaService.find(datumIVremeOd, datumIVremeDo, filmId, tip, sala, cenaKarteOd, cenaKarteDo);
		List<Film> filmovi = filmService.findAll();

	
		model.addAttribute("projekcije", projekcije);
		model.addAttribute("filmovi", filmovi);

		return "projekcije"; 
	}

	@GetMapping(value="/Details")
	public String Details(@RequestParam Long id, 
			HttpSession session, ModelMap model) throws IOException {
	
		Projekcija projekcija = projekcijaService.findOne(id);
		if (projekcija == null) {
			return "redirect:/Projekcije";
		}

		List<Film> filmovi = filmService.findAll();

		
		model.addAttribute("projekcija", projekcija);
		model.addAttribute("filmovi", filmovi);

		return "projekcija";
	}

	@GetMapping(value="/Create")
	public String Create(@RequestParam(name="filmId", required=false) Long filmId, 
			HttpSession session, ModelMap model) throws IOException {
		
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (prijavljeniKorisnik == null || prijavljeniKorisnik.isAdministrator() == false) {
			return "redirect:/Projekcije";
		}

	
		List<Film> filmovi = filmService.findAll();

		
		model.addAttribute("filmovi", filmovi);

		return "dodavanjeProjekcije"; 
	}

	@PostMapping(value="/Create")
	public String Create(
			@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate datum, 
			@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.TIME) LocalTime vreme, 
			@RequestParam Long filmId, @RequestParam(defaultValue="4D") String tip, @RequestParam int sala,
			@RequestParam double cenaKarte, 
			HttpSession session, ModelMap model) throws IOException {
	
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			return "redirect:/Projekcije";
		}

	
		LocalDateTime datumIVreme = LocalDateTime.of(datum, vreme);
		
		Film film = filmService.findOne(filmId);
		if (film == null) {
			return "redirect:/Projekcije";
		}

		// kreiranje
		Projekcija projekcija = new Projekcija(datumIVreme, film, sala, tip, cenaKarte);
		projekcijaService.save(projekcija);
		
		return "redirect:/Projekcije";
	}

	@PostMapping(value="/Edit")
	public String Edit(
			@RequestParam Long id, 
			@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate datum, 
			@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.TIME) LocalTime vreme, 
			@RequestParam Long filmId, @RequestParam String tip, @RequestParam int sala,
			@RequestParam double cenaKarte, 
			HttpSession session, ModelMap model) throws IOException {
		
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			return "redirect:/Projekcije";
		}

		
		Projekcija projekcija = projekcijaService.findOne(id);
		if (projekcija == null) {
			return "redirect:/Projekcije";
		}
		
		LocalDateTime datumIVreme = LocalDateTime.of(datum, vreme);
		
		Film film = filmService.findOne(filmId);
		if (film == null) {
			return "redirect:/Projekcije?id=" + id;
		}

		
		projekcija.setDatumIVreme(datumIVreme);
		projekcija.setFilm(film);
		projekcija.setTip(tip);
		projekcija.setSala(sala);
		projekcija.setCenaKarte(cenaKarte);
		projekcijaService.update(projekcija);
	
		return "redirect:/Projekcije";
	}

	@PostMapping(value="/Delete")
	public String Delete(@RequestParam Long id, 
			HttpSession session, ModelMap model) throws IOException {
		
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			return "redirect:/Projekcije";
		}

	
		projekcijaService.delete(id);

		return "redirect:/Projekcije";
	}

}
