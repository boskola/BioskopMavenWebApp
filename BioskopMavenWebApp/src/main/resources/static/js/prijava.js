// globalne promenljive
// keširanje referenci na elemente
let forma = document.querySelector("form");
let korisnickoImeInput = forma.querySelector("input[name=korisnickoIme]");
let lozinkaInput = forma.querySelector("input[name=lozinka]");

let pasusGreska = document.querySelector("p.greska");

// funkcije
function prijava() {
	// čitanje vrednosti iz forme za prijavu
	let korisnickoIme = korisnickoImeInput.value;
	let lozinka = lozinkaInput.value;

	// parametri zahteva
	let params =
		"korisnickoIme=" + korisnickoIme + "&" + 
		"lozinka=" + lozinka;
	console.log(params);
	
	let zahtev = new XMLHttpRequest();
	zahtev.open("POST", "Korisnici/Login");
	zahtev.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	zahtev.responseType = "json";
	zahtev.onload = function() {
		let odgovor = zahtev.response;
		console.log(odgovor);
	
		if (odgovor.status == "ok") {
			// prijava uspela
			window.location.replace(baseURL); // client-side redirekcija na početnu stranicu
		} else if (odgovor.status == "greska") {
			pasusGreska.textContent = odgovor.poruka; // ispis poruke o greški
		}
	}
	zahtev.send(params);
	console.log("POST: " + "Korisnici/Login")
}

// registracija handler-a
forma.onsubmit = function() { // za prijavu
	prijava();	
	return false; // sprečiti da submit forme promeni stranicu
};
