// globalne promenljive
// keširanje referenci na elemente
let forma = document.querySelector("form");
let pasusGreska = document.querySelector("p.greska");

let nazivInput = forma.querySelector("input[name=naziv]");
let zanrIdCelija = forma.querySelector("input[name=zanrId]").parentElement;
let trajanjeInput = forma.querySelector("input[name=trajanje]");

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
			while (zanrIdCelija.children.length > 0) { // ukloni sve checkbox-ove
				zanrIdCelija.children[0].remove();
			}
			let zanrovi = odgovor.zanrovi;
			for (let itZanr of zanrovi) { // za svaki žanr
				// formiraj HTML kod za checkbox za tekući žanr
				let zanrHTML = '<input type="checkbox" name="zanrId" value="' + itZanr.id + '"/><span>' + itZanr.naziv + '</span><br>';
				zanrIdCelija.insertAdjacentHTML("beforeend", zanrHTML); // dodaj checkbox u celiju na kraj
			}
		}
	}
	zahtev.send();
	console.log("GET: " + "Zanrovi");
}

function dodaj() {
	// čitanje vrednosti iz forme za dodavanje
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
		"naziv=" + naziv + "&" + 
		"zanrId=" +  zanrId.toString() + "&" +  // vrlo speceifčan slučaj u kome niz neće automatski da se pretvori u odgovarajuću string-ovnu reprezentaciju, kako bi ga Spring prihvatio kao parametar
		"trajanje=" +  trajanje;
	console.log(params);
	let zahtev = new XMLHttpRequest();
	zahtev.open("POST", "Filmovi/Create");
	zahtev.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	zahtev.responseType = "json";
	zahtev.onload = function() {
		// nakon što stigne odgovor...
		let odgovor = zahtev.response;
		console.log(odgovor);
						
		if (odgovor.status == "ok" || odgovor.status == "odbijen") {
			window.location.replace("filmovi.html"); // client-side redirekcija na filmovi.html
		} else if (odgovor.status == "greska") {
			pasusGreska.textContent = odgovor.poruka; // ispis poruke o greški
		}
	}
	zahtev.send(params);
	console.log("POST: " + "Filmovi/Create")
}

// registracija handler-a
forma.onsubmit = function() { // za dodavanje
	dodaj();
	return false; // sprečiti da submit forme promeni stranicu
};

// po učitavanju
popuniZanrove();