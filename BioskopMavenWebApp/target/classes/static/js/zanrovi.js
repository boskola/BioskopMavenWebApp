// globalne promenljive
// keširanje referenci na elemente
let tabela = document.querySelector("table.tabela");
let forma = document.querySelector("form");
let nazivInput = forma.querySelector("input[name=naziv]");

// funkcije
function popuniZanrove() {
	// čitanje vrednosti forme za pretragu
	let params = "?naziv=" + nazivInput.value;
	console.log(params);
	// traži od server-a žanrove
	let zahtev = new XMLHttpRequest();
	zahtev.open("GET", "Zanrovi" + params);
	zahtev.responseType = "json";
	zahtev.onload = function() {
		// nakon što stigne odgovor...
		let odgovor = zahtev.response;
		console.log(odgovor);
		
		if (odgovor.status == "ok") {
			let zanrovi = odgovor.zanrovi;

			// ukloni sve redove u tabeli iza 2. (iza reda sa formom za pretragu)	
			while (tabela.rows.length > 2) {
				tabela.rows[2].remove();
			}
			// dodaj po jedan red za svaki žanr
			for (let it in zanrovi) {
				// formiraj HTML kod za red u tabeli za tekuć žanr
				let zanrHTML = 
					'<td class="broj">' + (parseInt(it) + 1) + '</td>' + 
					'<td><a href="zanr.html?id=' + zanrovi[it].id + '">' + zanrovi[it].naziv + '</a></td>' + 
					'<td><a href="filmovi.html?zanrId=' + zanrovi[it].id + '">filmovi</a></td>';
				let red = tabela.insertRow(-1);	// kreiraj red na kraju tabele
				red.insertAdjacentHTML("beforeend", zanrHTML); // popuni red
			}
		}
	};
	zahtev.send();
	console.log("GET: Zanrovi" + params);
}

// registracija handler-a
forma.onsubmit = function() {
	popuniZanrove();
	return false;
};
nazivInput.onkeyup = function() {
	popuniZanrove();
};

// po učitavanju
popuniZanrove();