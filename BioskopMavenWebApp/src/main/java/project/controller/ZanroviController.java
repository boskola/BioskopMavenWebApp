package project.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.model.Korisnik;
import project.model.Zanr;
import project.service.ZanrService;

@RestController
@RequestMapping(value="/Zanrovi")
public class ZanroviController {

	@Autowired
	private ZanrService zanrService;

	@GetMapping
	public Map<String, Object> index(@RequestParam(required=false, defaultValue="") String naziv) throws IOException {
		List<Zanr> zanrovi = zanrService.find(naziv);

		Map<String, Object> odgovor = new LinkedHashMap<>();
		odgovor.put("status", "ok");
		odgovor.put("zanrovi", zanrovi);
		return odgovor;
	}

	@GetMapping(value="/Details")
	public Map<String, Object> details(@RequestParam Long id, 
			HttpSession session, ModelMap model) throws IOException {
		Zanr zanr = zanrService.findOne(id);
		
		Map<String, Object> odgovor = new LinkedHashMap<>();
		odgovor.put("status", "ok");
		odgovor.put("zanr", zanr);
		return odgovor;
	}

	@PostMapping(value="/Create")
	public Map<String, Object> create(@RequestParam String naziv, 
			HttpSession session) throws IOException {
		
		Korisnik korisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (korisnik == null || !korisnik.isAdministrator()) {
			Map<String, Object> odgovor = new LinkedHashMap<>();
			odgovor.put("status", "odbijen");
			return odgovor;
		}

		if (naziv.equals("")) {
			Map<String, Object> odgovor = new LinkedHashMap<>();
			odgovor.put("status", "greska");
			odgovor.put("poruka", "Naziv ne sme biti prazan!");
			return odgovor;
		}

		
		Zanr zanr = new Zanr(naziv);
		zanrService.save(zanr);

		Map<String, Object> odgovor = new LinkedHashMap<>();
		odgovor.put("status", "ok");
		return odgovor;
	}

	@PostMapping(value="/Edit")
	public Map<String, Object> edit(@RequestParam Long id, 
			@RequestParam String naziv, 
			HttpSession session) throws IOException {
	
		Korisnik korisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (korisnik == null || !korisnik.isAdministrator()) {
			Map<String, Object> odgovor = new LinkedHashMap<>();
			odgovor.put("status", "odbijen");
			return odgovor;
		}

	
		Zanr zanr = zanrService.findOne(id);
		if (zanr == null) {
			Map<String, Object> odgovor = new LinkedHashMap<>();
			odgovor.put("status", "greska");
			odgovor.put("poruka", "Žanr nije pronađen!");
			return odgovor;
		}	
		if (naziv == null || naziv.equals("")) {
			Map<String, Object> odgovor = new LinkedHashMap<>();
			odgovor.put("status", "greska");
			odgovor.put("poruka", "Naziv ne sme biti prazan!");
			return odgovor;
		}

		zanr.setNaziv(naziv);
		zanrService.update(zanr);

		Map<String, Object> odgovor = new LinkedHashMap<>();
		odgovor.put("status", "ok");
		return odgovor;
	}

	@PostMapping(value="/Delete")
	public Map<String, Object> delete(@RequestParam Long id, 
			HttpSession session) throws IOException {
	
		Korisnik korisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (korisnik == null || !korisnik.isAdministrator()) {
			Map<String, Object> odgovor = new LinkedHashMap<>();
			odgovor.put("status", "odbijen");
			return odgovor;
		}

		zanrService.delete(id);

		Map<String, Object> odgovor = new LinkedHashMap<>();
		odgovor.put("status", "ok");
		return odgovor;
	}

}
