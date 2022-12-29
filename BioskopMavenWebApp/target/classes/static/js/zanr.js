// globalne promenljive
let paramId = new URLSearchParams(window.location.search).get("id"); 

// keširanje referenci na elemente
let forme = document.querySelectorAll("form");
let formaIzmena = forme[0];
let formaBrisanje = forme[1];
let tabelaZaObicanPrikaz = document.querySelectorAll("table.forma")[2];

let nazivInput = formaIzmena.querySelector("input[name=naziv]");

// funkcije
function popuniZanr() {
	let params = "?id=" + paramId;
	console.log(params);
	// traži od server-a žanr
	let zahtev = new XMLHttpRequest();
	zahtev.open("GET", "Zanrovi/Details" + params);
	zahtev.responseType = "json";
	zahtev.onload = function() {
		// nakon što stigne odgovor...
		let odgovor = zahtev.response;
		console.log(odgovor);
		
		if (odgovor.status == "ok") {
			let zanr = odgovor.zanr;
			if (prijavljeniKorisnik != null && prijavljeniKorisnik.administrator) { // prijavljeniKorisnik je definisan u skripti zajednicki.js
			    // popuni formu za izmenu
				nazivInput.value = zanr.naziv;

				let linkKaFilmovima = formaIzmena.querySelector("a[href='filmovi.html?zanrId=']");
				let href = linkKaFilmovima.getAttribute("href") + paramId; // dodavanje id-a žanra na href link-a do filmova
				linkKaFilmovima.setAttribute("href", href);

				tabelaZaObicanPrikaz.style.display = "none"; // sakrij tabelu za običan prikaz
			} else {
				// popuni tabelu za običan prikaz
				tabelaZaObicanPrikaz.rows[0].querySelector("td").textContent = zanr.naziv;

				let linkKaFilmovima = tabelaZaObicanPrikaz.rows[1].querySelector("a");
				let href = linkKaFilmovima.getAttribute("href") + paramId; // dodavanje id-a žanra na href link-a do filmova
				linkKaFilmovima.setAttribute("href", href);

				formaIzmena.style.display = "none"; // sakrij formu za izmenu
				formaBrisanje.style.display = "none"; // sakrij formu za brisanje
			}
		}
	}
	zahtev.send();
	console.log("GET: Zanrovi/Details" + params);
}

function izmeni() {
	// čitanje vrednosti iz forme za izmenu
	let naziv = nazivInput.value;
						
	// parametri zahteva
	let params =
		"id=" + paramId + "&" + 
		"naziv=" + naziv + "&";
	console.log(params);
	let zahtev = new XMLHttpRequest();
	zahtev.open("POST", "Zanrovi/Edit");
	zahtev.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	zahtev.responseType = "json";
	zahtev.onload = function() {
		// nakon što stigne odgovor...
		let odgovor = zahtev.response;
		console.log(odgovor);
						
		if (odgovor.status == "ok" || odgovor.status == "odbijen") {
			window.location.replace("zanrovi.html"); // client-side redirekcija na zanrovi.html
		} else if (odgovor.status == "greska") {
			pasusGreska.textContent = odgovor.poruka;  // ispis poruke o greški
		}
	}
	zahtev.send(params);
	console.log("POST: " + "Zanrovi/Edit")
}

function obrisi() {
	// parametri zahteva
	let params = "id=" + paramId;
	console.log(params);
	let zahtev = new XMLHttpRequest();
	zahtev.open("POST", "Zanrovi/Delete");
	zahtev.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	zahtev.responseType = "json";
	zahtev.onload = function() {
		// nakon što stigne odgovor...
		let odgovor = zahtev.response;
		console.log(odgovor);
						
		if (odgovor.status == "ok" || odgovor.status == "odbijen") {
			window.location.replace("zanrovi.html"); // client-side redirekcija na zanrovi.html
		}
	}
	zahtev.send(params);
	console.log("POST: " + "Zanrovi/Delete");
}

// registracija handler-a
formaIzmena.onsubmit = function() { // za izmenu
	izmeni();
	return false; // sprečiti da submit forme promeni stranicu
};
formaBrisanje.onsubmit = function() { // za brisanje
	obrisi();
	return false; // sprečiti da submit forme promeni stranicu
};

// po učitavanju
popuniZanr();