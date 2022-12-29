// globalne promenljive
let paramZanrId = new URLSearchParams(window.location.search).get("zanrId"); // čitanje vrednosti zanrId parametra

// keširanje referenci na elemente
let tabela = document.querySelector("table.tabela"); // radi isključivo za elemente koji su postojali po učitavanju HTML dokumenta
let nazivInput = document.querySelector("input[name=naziv]"); // radi isključivo za elemente koji su postojali po učitavanju HTML dokumenta
let zanrIdSelect = document.querySelector("select[name=zanrId]"); // radi isključivo za elemente koji su postojali po učitavanju HTML dokumenta
let trajanjeOdInput = document.querySelector("input[name=trajanjeOd]"); // radi isključivo za elemente koji su postojali po učitavanju HTML dokumenta
let trajanjeDoInput = document.querySelector("input[name=trajanjeDo]"); // radi isključivo za elemente koji su postojali po učitavanju HTML dokumenta

// funkcije
function popuniZanrove() {
	// traži od server-a žanrove
	let zahtev = new XMLHttpRequest();
	zahtev.open("GET", "Zanrovi");
	zahtev.responseType = "json";
	zahtev.onload = function() {
		// nakon što stigne odgovor...
		let odgovor = zahtev.response;
		console.log(odgovor);

		if (odgovor.status == "ok") {
			// ukloni sve opcije iza prve (iza opcije "odaberi")
			while (zanrIdSelect.options.length > 2) {
				zanrIdSelect.options[2].remove();
			}
			// dodaj po jednu opciju za svaki žanr
			let zanrovi = odgovor.zanrovi;
			for (let itZanr of zanrovi) {
				// formiraj HTML kod za opciju za tekući žanr
				let zanrHTML = '<option value="' + itZanr.id + '" ' + (itZanr.id == paramZanrId? 'selected': '') + '>' + itZanr.naziv + '</option>';
				zanrIdSelect.insertAdjacentHTML("beforeend", zanrHTML); // dodaj opciju u select na kraj
			}
		}
	};
	zahtev.send();
	console.log("GET: " + "Zanrovi");
}


function popuniFilmove() {
	// čitanje vrednosti forme za pretragu
	let naziv = nazivInput.value;
	let zanrId = (paramZanrId != null)? paramZanrId: zanrIdSelect.value; // ako URL parametar ne postoji, čitaj vrednost iz select-a
	let trajanjeOd = trajanjeOdInput.value;
	let trajanjeDo = trajanjeDoInput.value;

	// parametri zahteva
	let params = "?" + 
		"naziv=" + naziv + "&" + 
		"zanrId=" + zanrId + "&" + 
		"trajanjeOd=" +  trajanjeOd + "&" + 
		"trajanjeDo=" +  trajanjeDo;
	console.log(params)
	// traži od server-a filmove
	let zahtev = new XMLHttpRequest();
	zahtev.open("GET", "Filmovi" + params);
	zahtev.responseType = "json";
	zahtev.onload = function() {
		// nakon što stigne odgovor...
		let odgovor = zahtev.response;
		console.log(odgovor);
			
		if (odgovor.status == "ok") {
			// ukloni sve redove u tabeli iza 2. (iza reda sa formom za pretragu)
			while (tabela.rows.length > 2) {
				tabela.rows[2].remove();
			}
			// dodaj po jedan red za svaki film
			let filmovi = odgovor.filmovi;
			for (let it in filmovi) {
				// formiraj HTML kod za listu žanrova za tekući film
				let listaZanrovaHTML = '<ul>';
				for (let itZanr of filmovi[it].zanrovi) {
					listaZanrovaHTML += '<li><a href="zanr.html?id=' + itZanr.id + '">' + itZanr.naziv + '</a></li>';
				}
				listaZanrovaHTML += '</ul>';
				// formiraj HTML kod za red u tabeli za tekući film
				let filmHTML =
					'<td class="broj">' + (parseInt(it) + 1) + '</td>' + 
					'<td><a href="film.html?id=' + filmovi[it].id + '">' + filmovi[it].naziv + '</a></td>' + 
					'<td>' + listaZanrovaHTML + '</td>' + 
					'<td class="broj">' + filmovi[it].trajanje + '</td>' + 
					'<td>' + 
						'<a href="projekcije.html?filmId=' + filmovi[it].id + '">projekcije</a>' + 
					'</td>';
				let red = tabela.insertRow(-1);	// kreiraj red na kraju tabele
				red.insertAdjacentHTML("beforeend", filmHTML); // popuni red
			}
		}
	}
	zahtev.send();
	console.log("GET: " + "Filmovi" + params);
}



function popuniStatistikuFilmova() {
	// traži od server-a statistiku filmova
	let zahtev = new XMLHttpRequest();
	zahtev.open("GET", "Filmovi/StatistikaFilmova");
	zahtev.responseType = "json";
	zahtev.onload = function() {
		// nakon što stigne odgovor...
		let odgovor = zahtev.response;
		console.log(odgovor);

		if (odgovor.status == "ok") {
			let tabela = document.querySelectorAll("table.horizontalni-meni")[0]; // radi isključivo za elemente koji su postojali po učitavanju HTML dokumenta

			let filmovi = odgovor.statistikaFilmova.filmovi;
			if (filmovi.length > 0) {
				// ima popularnih filmova
				let lista = tabela.querySelector("ul"); // radi isključivo za elemente koji su postojali po učitavanju HTML dokumenta
				// ukloni sve stavke iz liste
				while (lista.childNodes.length > 0) {
					lista.childNodes[0].remove();
				}
				// dodaj po jednu stavku liste za svaki brojač filma
				for (let itBrojac of filmovi) {
					// formiraj HTML kod za stavku liste za tekući brojač filma
					let brojacHTML = 
						'<li>' + 
							'<a href="film.html?id=' + itBrojac.film.id + '">' + itBrojac.film.naziv + '</a>' + 
							'<progress value="' + itBrojac.brojac + '" max="' + odgovor.statistikaFilmova.max + '"></progress>' + 
							'<span>' + itBrojac.brojac + '</span>' + 
						'</li>';
					lista.insertAdjacentHTML("beforeend", brojacHTML); // dodaj stavku u listu na kraj
				}
			} else {
				tabela.style.display = "none"; // sakrij tabelu ako nema popularnih filmova
			}
		}
	};
	zahtev.send();
	console.log("GET: " + "Filmovi/StatistikaFilmova");
}


function popuniPoseceneFilmove() {
	// traži od server-a posećene filmove
	let zahtev = new XMLHttpRequest();
	zahtev.open("GET", "Filmovi/PoseceniFilmovi");
	zahtev.responseType = "json";
	zahtev.onload = function() {
		// nakon što stigne odgovor...
		let odgovor = zahtev.response;
		console.log(odgovor);

		if (odgovor.status == "ok") {
			let tabela = document.querySelectorAll("table.horizontalni-meni")[1]; // radi isključivo za elemente koji su postojali po učitavanju HTML dokumenta
	
			let filmovi = odgovor.poseceniFilmovi;
			if (filmovi.length > 0) {
				// ima posećenih filmova
				let lista = tabela.querySelector("ul"); // radi isključivo za elemente koji su postojali po učitavanju HTML dokumenta
				// ukloni sve stavke iz liste
				while (lista.children.length > 0) {
					lista.children[0].remove();
				}
				// dodaj po jednu stavku liste za svaki film
				for (let itFilm of filmovi) {
					// formiraj HTML kod za stavku liste za tekući film
					let filmHTML = '<li><a href="film.html?id=' + itFilm.id + '">' + itFilm.naziv + '</a></li>';
					lista.insertAdjacentHTML("beforeend", filmHTML); // dodaj stavku u listu na kraj
				}
			} else {
				tabela.style.display = "none"; // sakrij tabelu ako nema posećenih filmova
			}
		}
	};
	zahtev.send();
	console.log("GET: " + "Filmovi/PoseceniFilmovi");
}


// registracija handler-a
document.querySelector("form").onsubmit = function() { // za pretragu
	popuniFilmove();
	return false; // sprečiti da submit forme promeni stranicu
};

// po učitavanju
popuniZanrove();
popuniFilmove();
popuniStatistikuFilmova();
popuniPoseceneFilmove();