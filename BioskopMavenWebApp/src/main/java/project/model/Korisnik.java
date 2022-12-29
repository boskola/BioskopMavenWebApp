package project.model;

public class Korisnik {
	
	private String korisnickoIme="", lozinka="", eMail="", pol="muški";
	private boolean administrator = false;
	
	public Korisnik() {}
	
	public Korisnik(String korisnickoIme, String lozinka, String email, String pol, boolean administrator) {
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.eMail = email;
		this.pol = pol;
		this.administrator = administrator;
	}
	
	public Korisnik(String korisnickoIme, String lozinka, String email, String pol) {
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.eMail = email;
		this.pol = pol;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime*result + ((korisnickoIme == null) ? 0 : korisnickoIme.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Korisnik other = (Korisnik) obj;
		if (korisnickoIme == null) {
			if (other.korisnickoIme != null)
				return false;
		} else if (!korisnickoIme.equals(other.korisnickoIme))
			return false;
		return true;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}
	
	public String getEMail() {
		return eMail;
	}

	public void setEMail(String eMail) {
		this.eMail = eMail;
	}

	public String getPol() {
		return pol;
	}

	public void setPol(String pol) {
		this.pol = pol;
	}

	public boolean isAdministrator() {
		return administrator;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}

	@Override
	public String toString() {
		return "Korisnik [korisnickoIme=" + korisnickoIme + ", lozinka=" + lozinka + ", eMail=" + eMail + ", pol=" + pol
				+ ", administrator=" + administrator + "]";
	}

}
