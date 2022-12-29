// globalne promenljive (važe i u ostalim skriptama)
let baseURL = "";
let prijavljeniKorisnik = null;

// funkcije
function popuniBaseURL() {
	// traži od servera baseURL
	let zahtev = new XMLHttpRequest();
	zahtev.open("GET", "baseURL");
	zahtev.responseType = "json";
	zahtev.onload = function() {
		// nakon što stigne odgovor...
		let odgovor = zahtev.response;
		console.log(zahtev.response);

		// inicjalizuj globalnu promenljivu baseURL
		baseURL = odgovor.baseURL;
		// postavi href atribut base elementa
		let baseElement = document.querySelector("base"); // radi isključivo za elemente koji su postojali po učitavanju HTML dokumenta
		baseElement.setAttribute("href", odgovor.baseURL);
	};
	zahtev.send();
	console.log("GET: " + "baseURL");
}

function popuniPrijavljenogKorisnika() {
	// traži od servera prijavljenog korisnika
	let zahtev = new XMLHttpRequest();
	zahtev.open("GET", "Korisnici/PrijavljeniKorisnik");
	zahtev.responseType = "json";
	zahtev.onload = function() {
		// nakon što stigne odgovor...
		let odgovor = zahtev.response;
		console.log(zahtev.response);

		if (odgovor.status == "ok") {
			// inicjalizuj globalnu promenljivu prijavljeniKorisnik
			prijavljeniKorisnik = odgovor.prijavljeniKorisnik;

			// popuni i/ili sakrij tabele sa kontrolama za prijavljenog korisnika
			let tabeleKorisnik = document.querySelectorAll("table.korisnik");
			let tabelaPrijavljen = tabeleKorisnik[0];
			let tabelaNeprijavljen = tabeleKorisnik[1]; // može da bude undefined ako nema 2 tabele!
			if (prijavljeniKorisnik != null) {
				// prijavljen		
				let korisnikLink = tabelaPrijavljen.querySelector("a"); // selektuje se prvi element za koga važi selektor

				// popuni link do korisnika	
				let href = korisnikLink.getAttribute("href") + prijavljeniKorisnik.korisnickoIme;  // dodavanje korisničkog imena na href link-a do korisnika
				korisnikLink.setAttribute("href", href);
				korisnikLink.textContent = prijavljeniKorisnik.korisnickoIme;

				if (tabeleKorisnik.length >= 2) {
					tabelaNeprijavljen.style.display = "none"; // sakrij tabelu za neprijavljenog korisnika ako postoje 2 tabele
				}
			} else {
				// neprijavljen
				if (tabeleKorisnik.length >= 2) {
					tabelaPrijavljen.style.display = "none"; // sakrij tabelu za prijavljenog korisnika ako postoje 2 tabele
				}
			}
			// sakrij admnistratorske kontrole
			if (!(prijavljeniKorisnik != null && prijavljeniKorisnik.administrator == true)) {
				// nije (prijavljen i administrator)
				let linkKorisnici = document.querySelector('a[href="korisnici.html"]'); // radi isključivo za elemente koji su postojali po učitavanju HTML dokumenta
				let linkDodavanjeZanra = document.querySelector('a[href="dodavanjeZanra.html"]'); // radi isključivo za elemente koji su postojali po učitavanju HTML dokumenta
				let linkDodavanjeFilma = document.querySelector('a[href="dodavanjeFilma.html"]'); // radi isključivo za elemente koji su postojali po učitavanju HTML dokumenta
				let linkDodavanjeProjekcije = document.querySelector('a[href="dodavanjeProjekcije.html"]'); // radi isključivo za elemente koji su postojali po učitavanju HTML dokumenta
				let linkDodavanjeKorisnika = document.querySelector('a[href="dodavanjeKorisnika.html"]'); // radi isključivo za elemente koji su postojali po učitavanju HTML dokumenta

				if (linkKorisnici != null) { // sakrij list item koji obuhvata link do korisnici.html (ako postoji)
					linkKorisnici.parentElement.style.display = "none";
				}
				if (linkDodavanjeZanra != null) { // sakrij listu koja obuhvata link do dodavanjeZanra.html (ako postoji)
					linkDodavanjeZanra.parentElement.style.display = "none";
				}
				if (linkDodavanjeFilma) { // sakrij listu koja obuhvata link do dodavanjeFilma.html (ako postoji)
					linkDodavanjeFilma.parentElement.style.display = "none";
				}
				if (linkDodavanjeProjekcije) { // sakrij listu koja obuhvata link do dodavanjeProjekcije.html (ako postoji)
					linkDodavanjeProjekcije.parentElement.style.display = "none";
				}
				if (linkDodavanjeKorisnika) { // sakrij listu koja obuhvata link do dodavanjeKorisnika.html (ako postoji)
					linkDodavanjeKorisnika.parentElement.style.display = "none";
				}
			}
		}
	}
	zahtev.send();
	console.log("GET: " + "Korisnici/PrijavljeniKorisnik");
}

function odjava() {
	// pošalji server-u zahtev za odjavu
	let zahtev = new XMLHttpRequest();
	zahtev.open("GET", "Korisnici/Logout");
	zahtev.responseType = "json";
	zahtev.onload = function() {
		// nakon što stigne odgovor...
		let odgovor = zahtev.response;
		console.log(odgovor);

		if (odgovor.status == "ok") {
			// odjava uspela
			window.location.replace(baseURL); // client-side redirekcija na početnu stranicu
		}
	};
	zahtev.send();
	console.log("GET: " + "Korisnici/Logout");
}

// registracija handler-a
let linkZaOdjavu = document.querySelector('a[href="Korisnici/Logout"]'); // radi isključivo za elemente koji su postojali po učitavanju HTML dokumenta
if (linkZaOdjavu != null) { // za odjavu (ako postoji)
	linkZaOdjavu = linkZaOdjavu.onclick = function() {
		odjava();
		return false; // sprečiti da klik na link promeni stranicu
	};
}

// po učitavanju
popuniBaseURL(); // dobavi i ugradi u DOM informaciju o baseURL-u
popuniPrijavljenogKorisnika(); // dobavi i ugradi u DOM informaciju o prijavljenom korisniku