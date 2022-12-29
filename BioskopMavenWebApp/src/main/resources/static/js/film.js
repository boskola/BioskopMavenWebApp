// globalne promenljive
let paramId = new URLSearchParams(window.location.search).get("id"); // čitanje vrednosti id parametra

// keširanje referenci na elemente
let forme = document.querySelectorAll("form");
let formaIzmena = forme[0];
let formaBrisanje = forme[1];
let tabelaZaObicanPrikaz = document.querySelectorAll("table.forma")[2];

let nazivInput = formaIzmena.querySelector("input[name=naziv]");
let zanrIdCelija = formaIzmena.querySelector("input[name=zanrId]").parentElement;
let trajanjeInput = formaIzmena.querySelector("input[name=trajanje]");
					
let pasusGreska = document.querySelector("p.greska");

// funkcije
function popuniFilm() {
	// parametri zahteva
	let params = "?id=" + paramId;
	console.log(params);
	// traži od server-a film
	let zahtev = new XMLHttpRequest();
	zahtev.open("GET", "Filmovi/Details" + params);
	zahtev.responseType = "json";
	zahtev.onload = function() {
		// nakon što stigne odgovor...
		let odgovor = zahtev.response;
		console.log(odgovor);
	
		if (odgovor.status == "ok") {			
			let film = odgovor.film;
			let zanroviFilma = film.zanrovi;
			if (prijavljeniKorisnik != null && prijavljeniKorisnik.administrator) { // prijavljeniKorisnik je definisan u skripti zajednicki.js
				// popuni formu za izmenu
				nazivInput.value = film.naziv;
				for (let itZanr of zanroviFilma) { // za svaki žanr filma
					for (let itElement of zanrIdCelija.children) { // pronađi i čekiraj odgovarajući checkbox
						if (itElement.tagName == "INPUT" && itElement.value == itZanr.id) {
							itElement.checked = true;
						}
					}
				}
				trajanjeInput.value = film.trajanje;

				tabelaZaObicanPrikaz.rows[2].querySelector("td").textContent = film.trajanje;

				let linkKaProjekcijama = formaIzmena.querySelector("a[href='projekcije.html?filmId=']");
				let href = linkKaProjekcijama.getAttribute("href") + film.id; // dodavanje id-a filma na href link-a do filma
				linkKaProjekcijama.setAttribute("href", href);

				tabelaZaObicanPrikaz.style.display = "none"; // sakrij tabelu za običan prikaz
			} else {
				// popuni tabelu za običan prikaz
				tabelaZaObicanPrikaz.rows[0].querySelector("td").textContent = film.naziv;
	
				let lista = tabelaZaObicanPrikaz.rows[1].querySelector("ul");
				while (lista.children.length > 0) { // isprazni listu link-ova do žanrova
					lista.children[0].remove();
				}
				for (let itZanr of zanroviFilma) { // za svaki žanr
					// formiraj HTML kod za stavku liste za tekući žanr
					let zanrHTML = '<li><a href="zanr.html?id=' + itZanr.id + '">' + itZanr.naziv + '</a></li>';
					lista.insertAdjacentHTML("beforeend", zanrHTML); // dodaj stavku u listu na kraj
				}

				tabelaZaObicanPrikaz.rows[2].querySelector("td").textContent = film.trajanje;

				let linkKaProjekcijama = tabelaZaObicanPrikaz.rows[3].querySelector("a");
				let href = linkKaProjekcijama.getAttribute("href") + film.id; // dodavanje id-a filma na href link-a do filma
				linkKaProjekcijama.setAttribute("href", href);

				formaIzmena.style.display = "none"; // sakrij formu za izmenu
				formaBrisanje.style.display = "none"; // sakrij formu za brisanje
			}
		}
	};
	zahtev.send();
	console.log("GET: " + "Filmovi/Details" + params);
}

function popuniZanrovePaFilm() {
	// traži od server-a žanrove
	let zahtev = new XMLHttpRequest();
	zahtev.open("GET", "Zanrovi");
	zahtev.responseType = "json";
	zahtev.onload = function() {
		// nakon što stigne odgovor...
		let odgovor = zahtev.response;
		console.log(odgovor);
		
		if (odgovor.status == "ok") {
			while (zanrIdCelija.children.length > 0) { // ukloni sve checkbox-ove
				zanrIdCelija.children[0].remove();
			}
			let zanrovi = odgovor.zanrovi;
			for (let itZanr of zanrovi) { // za svaki žanr
				// formiraj HTML kod za checkbox za tekući žanr
				let zanrHTML = '<input type="checkbox" name="zanrId" value="' + itZanr.id + '"/><span>' + itZanr.naziv + '</span><br>';
				zanrIdCelija.insertAdjacentHTML("beforeend", zanrHTML); // dodaj checkbox u celiju na kraj
			}
	
			// prvo se moraju popuniti žanrovi pa onda film (da bi se mogle check-irati ponuđene opcije)
			popuniFilm();
		}
	}
	zahtev.send();
	console.log("GET: " + "Zanrovi");
}


function izmeni() {
	// čitanje vrednosti iz forme za izmenu
	let naziv = nazivInput.value;
							
	let zanrId = []; // lista id-eva žanrova
	for (let itElement of zanrIdCelija.children) { // za svaki čekirani žanr
		if (itElement.tagName == "INPUT" && itElement.checked == true) {
			zanrId.push(itElement.value); // dodaj id žanra u listu
		}
	}
	let trajanje = trajanjeInput.value;
						
	// parametri zahteva
	let params =
		"id=" + paramId + "&" + 
		"naziv=" + naziv + "&" + 
		"zanrId=" +  zanrId.toString() + "&" +  // vrlo speceifčan slučaj u kome niz neće automatski da se pretvori u odgovarajuću string-ovnu reprezentaciju, kako bi ga Spring prihvatio kao parametar
		"trajanje=" +  trajanje;
	console.log(params);
	let zahtev = new XMLHttpRequest();
	zahtev.open("POST", "Filmovi/Edit");
	zahtev.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	zahtev.responseType = "json";
	zahtev.onload = function() {
		// nakon što stigne odgovor...
		let odgovor = zahtev.response;
		console.log(odgovor);
						
		if (odgovor.status == "ok" || odgovor.status == "odbijen") {
			window.location.replace("filmovi.html"); // client-side redirekcija na filmovi.html
		} else if (odgovor.status == "greska") {
			pasusGreska.textContent = odgovor.poruka;  // ispis poruke o greški
		}
	}
	zahtev.send(params);
	console.log("POST: " + "Filmovi/Edit")
}

function obrisi() {
	// parametri zahteva
	let params = "id=" + paramId;
	console.log(params);
	let zahtev = new XMLHttpRequest();
	zahtev.open("POST", "Filmovi/Delete");
	zahtev.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	zahtev.responseType = "json";
	zahtev.onload = function() {
		// nakon što stigne odgovor...
		let odgovor = zahtev.response;
		console.log(odgovor);
						
		if (odgovor.status == "ok" || odgovor.status == "odbijen") {
			window.location.replace("filmovi.html"); // client-side redirekcija na filmovi.html
		}
	}
	zahtev.send(params);
	console.log("POST: " + "Filmovi/Delete");
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
popuniZanrovePaFilm();