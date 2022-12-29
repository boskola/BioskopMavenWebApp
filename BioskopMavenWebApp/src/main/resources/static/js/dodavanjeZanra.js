// globalne promenljive
// keširanje referenci na elemente
let forma = document.querySelector("form");
let nazivInput = forma.querySelector("input[name=naziv]");
let pasusGreska = document.querySelector("p.greska");

// funkcije
function dodaj() {
	// čitanje vrednosti iz forme za dodavanje
	let params = "naziv=" + nazivInput.value;
	console.log(params);
	
	let zahtev = new XMLHttpRequest();
	zahtev.open("POST", "Zanrovi/Create");
	zahtev.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	zahtev.responseType = "json";
	zahtev.onload = function() {
		// nakon što stigne odgovor...
		let odgovor = zahtev.response;
		console.log(odgovor);
		
		if (odgovor.status == "ok" || odgovor.status == "odbijen") {
			window.location.replace("zanrovi.html"); // client-side redirekcija na zanrovi.html
		} else if (odgovor.status == "greska") {
			pasusGreska.textContent = odgovor.poruka; // ispis poruke o greški
		} 
	};
	zahtev.send(params);
	console.log("POST: Zanrovi");
}

// registracija handler-a
forma.onsubmit = function() {
	dodaj();
	return false;
};
