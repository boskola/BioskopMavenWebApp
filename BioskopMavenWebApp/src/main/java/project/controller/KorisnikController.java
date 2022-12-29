package project.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import project.model.Korisnik;
import project.service.KorisnikService;

@Controller
@RequestMapping(value="/Korisnici")
public class KorisnikController {

	public static final String KORISNIK_KEY = "prijavljeniKorisnik";
	
	@Autowired
	private KorisnikService korisnikService;

	@GetMapping(value="PrijavljeniKorisnik")
	@ResponseBody
	public Map<String, Object> prijavljeniKorisnik(HttpSession session) {
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		
		Map<String, Object> odgovor = new LinkedHashMap<>();
		odgovor.put("status", "ok");
		odgovor.put("prijavljeniKorisnik", prijavljeniKorisnik);	
		return odgovor;
	}
	
	@GetMapping
	public String index(
			@RequestParam(required=false, defaultValue="") String korisnickoIme,
			@RequestParam(required=false, defaultValue="") String eMail,
			@RequestParam(required=false, defaultValue="") String pol,
			@RequestParam(required=false, defaultValue="false") Boolean administrator,
			HttpSession session, ModelMap model) throws IOException {		
	
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			return "redirect:/";
		}

	
		List<Korisnik> korisnici = korisnikService.find(korisnickoIme, eMail, pol, administrator);

	
		model.addAttribute("korisnici", korisnici);

		return "korisnici";
	}

	@GetMapping(value="/Details")
	public String details(@RequestParam String korisnickoIme, 
			HttpSession session, ModelMap model) throws IOException {
		
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		// samo administrator može da vidi druge korisnike; svaki korisnik može da vidi sebe
		if (prijavljeniKorisnik == null || (!prijavljeniKorisnik.isAdministrator() && !prijavljeniKorisnik.getKorisnickoIme().equals(korisnickoIme))) {
			return "redirect:/Korisnici";
		}

		
		Korisnik korisnik = korisnikService.findOne(korisnickoIme);
		if (korisnik == null) {
			return "redirect:/Korisnici";
		}

		
		model.addAttribute("korisnik", korisnik);

		return "korisnik";
	}

	@GetMapping(value="/Create")
	public String create(HttpSession session, ModelMap model) throws IOException {
		
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		// samo administrator može da kreira korisnike
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			return "redirect:/";
		}

		return "dodavanjeKorisnika";
	}
	
	@PostMapping(value="/Create")
	public String create(@RequestParam String korisnickoIme, @RequestParam String lozinka, 
			@RequestParam String eMail, @RequestParam String pol, @RequestParam(required=false) String administrator,
			HttpSession session) throws IOException {
		
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		// samo administrator može da kreira korisnike
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator()) {
			return "redirect:/Korisnici";
		}

		
		Korisnik postojeciKorisnik = korisnikService.findOne(korisnickoIme);
		if (postojeciKorisnik != null) {
			return "redirect:/Korisnici/Create";
		}
		if (korisnickoIme.equals("") || lozinka.equals("")) {
			return "redirect:/Korisnici/Create";
		}
		if (eMail.equals("")) {
			return "redirect:/Korisnici/Create";
		}
		if (!pol.equals("muški") && !pol.equals("ženski")) {
			return "redirect:/Korisnici/Create";
		}

		
		Korisnik korisnik = new Korisnik(korisnickoIme, lozinka, eMail, pol, administrator != null);
		korisnikService.save(korisnik);

		return "redirect:/Korisnici";
	}

	@PostMapping(value="/Edit")
	public String edit(@RequestParam String korisnickoIme, 
			@RequestParam String lozinka, String eMail, @RequestParam String pol, @RequestParam(required=false) String administrator,
			HttpSession session) throws IOException {
		
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		// samo administrator može da menja druge korisnike; svaki korisnik može da menja sebe
		if (prijavljeniKorisnik == null || (!prijavljeniKorisnik.isAdministrator() && !prijavljeniKorisnik.getKorisnickoIme().equals(korisnickoIme))) {
			return "redirect:/Korisnici";
		}

		// validacija
		Korisnik korisnik = korisnikService.findOne(korisnickoIme);
		if (korisnik == null) {
			return "redirect:/Korisnici";
		}
		if (eMail.equals("")) {
			return "redirect:/Korisnici?korisnicoIme=" + korisnickoIme;
		}
		if (!pol.equals("muški") && !pol.equals("ženski")) {
			return "redirect:/Korisnici?korisnicoIme=" + korisnickoIme;
		}

		
		if (!lozinka.equals("")) {
			korisnik.setLozinka(lozinka);
		}
		korisnik.setEMail(eMail);
		korisnik.setPol(pol);
		// privilegije može menjati samo administrator i to drugim korisnicima
		if (prijavljeniKorisnik.isAdministrator() && !prijavljeniKorisnik.equals(korisnik)) {
			korisnik.setAdministrator(administrator != null);
		}
		korisnikService.update(korisnik);

		// sigurnost
		if (!prijavljeniKorisnik.equals(korisnik)) {
			// TODO odjaviti korisnika (jer mu je administrator možda promenio privilegije, a on je možda u ovom momentu prijavljen)
		}

		return "redirect:/Korisnici";
	}

	@PostMapping(value="/Delete")
	public String delete(@RequestParam String korisnickoIme, 
			HttpSession session) throws IOException {
		
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(KorisnikController.KORISNIK_KEY);
		// samo administrator može da briše korisnike, ali ne i sebe
		if (prijavljeniKorisnik == null || !prijavljeniKorisnik.isAdministrator() || prijavljeniKorisnik.getKorisnickoIme().equals(korisnickoIme)) {
			return "redirect:/Korisnici";
		}

	
		korisnikService.delete(korisnickoIme);

		// sigurnost
		// TODO odjaviti korisnika (jer je on u možda ovom momentu prijavljen)
		
		return "redirect:/Korisnici";
	}

	@PostMapping(value="/Register")
	public String register(@RequestParam String korisnickoIme, @RequestParam String lozinka, 
			@RequestParam String eMail, @RequestParam String pol, @RequestParam String ponovljenaLozinka,
			HttpSession session, ModelMap model) throws IOException {
		try {
		
			Korisnik postojeciKorisnik = korisnikService.findOne(korisnickoIme);
			if (postojeciKorisnik != null) {
				throw new Exception("Korisničko ime već postoji!");
			}
			if (korisnickoIme.equals("") || lozinka.equals("")) {
				throw new Exception("Korisničko ime i lozinka ne smeju biti prazni!");
			}
			if (!lozinka.equals(ponovljenaLozinka)) {
				throw new Exception("Lozinke se ne podudaraju!");
			}
			if (eMail.equals("")) {
				throw new Exception("E-mail ne sme biti prazan!");
			}
			if (!pol.equals("muški") && !pol.equals("ženski")) {
				throw new Exception("Morate odabrati pol!");
			}

			
			Korisnik korisnik = new Korisnik(korisnickoIme, lozinka, eMail, pol);
			korisnikService.save(korisnik);

			return "redirect:/prijava.html";
		} catch (Exception ex) {
			
			String poruka = ex.getMessage();
			if (poruka == "") {
				poruka = "Neuspešna registracija!";
			}

			// popunjavanje modela
			model.addAttribute("poruka", poruka);

			return "registracija"; 
		}
	}
	
	@PostMapping(value="/Login")
	@ResponseBody
	public Map<String, Object> postLogin(@RequestParam String korisnickoIme, @RequestParam String lozinka, 
			HttpSession session) throws IOException {
		try {
			
			Korisnik korisnik = korisnikService.findOne(korisnickoIme, lozinka);
			if (korisnik == null) {
				throw new Exception("Neispravno korisničko ime ili lozinka!");
			}			

			
			session.setAttribute(KorisnikController.KORISNIK_KEY, korisnik);
			
			Map<String, Object> odgovor = new LinkedHashMap<>();
			odgovor.put("status", "ok");
			return odgovor;
		} catch (Exception ex) {
			
			String poruka = ex.getMessage();
			if (poruka == "") {
				poruka = "Neuspešna prijava!";
			}
			
			Map<String, Object> odgovor = new LinkedHashMap<>();
			odgovor.put("status", "greska");
			odgovor.put("poruka", poruka);
			return odgovor;
		}
	}

	@GetMapping(value="/Logout")
	@ResponseBody
	public Map<String, Object> logout(HttpSession session) throws IOException {
			
		session.invalidate();
		
		Map<String, Object> odgovor = new LinkedHashMap<>();
		odgovor.put("status", "ok");	
		return odgovor;
	}
	
}
