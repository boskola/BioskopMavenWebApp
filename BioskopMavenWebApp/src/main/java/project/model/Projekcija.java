package project.model;

import java.time.LocalDateTime;

public class Projekcija {

	private Long id;
	private LocalDateTime datumIVreme;
	private Film film;
	private int sala;
	private String tip;
	private double cenaKarte;
	
	public Projekcija() {}

	public Projekcija(LocalDateTime datumIVreme, Film film, int sala, String tip, double cenaKarte) {
		this.datumIVreme = datumIVreme;
		this.film = film;
		this.sala = sala;
		this.tip = tip;
		this.cenaKarte = cenaKarte;
	}
	
	public Projekcija(Long id, LocalDateTime datumIVreme, Film film, int sala, String tip, double cenaKarte) {
		this.id = id;
		this.datumIVreme = datumIVreme;
		this.film = film;
		this.sala = sala;
		this.tip = tip;
		this.cenaKarte = cenaKarte;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime*result + ((id == null) ? 0 : id.hashCode());
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
		Projekcija other = (Projekcija) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDatumIVreme() {
		return datumIVreme;
	}

	public void setDatumIVreme(LocalDateTime datumIVreme) {
		this.datumIVreme = datumIVreme;
	}

	public Film getFilm() {
		return film;
	}

	public void setFilm(Film film) {
		this.film = film;
	}

	public int getSala() {
		return sala;
	}

	public void setSala(int sala) {
		this.sala = sala;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public double getCenaKarte() {
		return cenaKarte;
	}

	public void setCenaKarte(double cenaKarte) {
		this.cenaKarte = cenaKarte;
	}

	@Override
	public String toString() {
		return "Projekcija [id=" + id + ", datumIVreme=" + datumIVreme + ", film=" + film + ", sala=" + sala
				+ ", tip=" + tip + ", cenaKarte=" + cenaKarte + "]";
	}

}
