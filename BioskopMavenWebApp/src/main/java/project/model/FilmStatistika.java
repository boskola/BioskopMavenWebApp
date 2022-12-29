package project.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmStatistika {

	private Map<Long, FilmBrojac> popularniFilmovi;

	public FilmStatistika() {
		popularniFilmovi = new HashMap<>();
	}

	public void incrementBrojac(Film film) {
		FilmBrojac brojac = popularniFilmovi.get(film.getId());
		if (brojac == null) {
			brojac = new FilmBrojac(film);
			popularniFilmovi.put(film.getId(), brojac);
		}
		brojac.incrementBrojac();
	}

	public List<FilmBrojac> getFilmovi() {
		List<FilmBrojac> sorted = new ArrayList<FilmBrojac>(popularniFilmovi.values());
		Collections.sort(sorted, new SorterFilmova());

		return sorted;
	}

	public boolean isEmpty() {
		return popularniFilmovi.isEmpty();
	}
	
	public int getMax() {
		if (getFilmovi().isEmpty()) {
			return 0;
		}
		return getFilmovi().get(0).getBrojac();
	}

	private class SorterFilmova implements Comparator<FilmBrojac> {

		int direction = -1;
		
		@Override
		public int compare(FilmBrojac brojacA, FilmBrojac brojacB) {
			if (brojacA.getBrojac() == brojacB.getBrojac())
				return brojacA.getFilm().getNaziv().compareTo(brojacB.getFilm().getNaziv());
			
			return Integer.compare(brojacA.getBrojac(), brojacB.getBrojac())*direction;
		}

	}

}
