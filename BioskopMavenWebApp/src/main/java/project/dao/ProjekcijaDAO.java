package project.dao;

import java.time.LocalDateTime;
import java.util.List;

import project.model.Projekcija;

public interface ProjekcijaDAO {

	public Projekcija findOne(Long id);
	public List<Projekcija> findAll();
	public List<Projekcija> find(LocalDateTime datumIVremeOd, LocalDateTime datumIVremeDo, Long filmId, String tip, Integer sala, Double cenaKarteOd, Double cenaKarteDo);
	public void save(Projekcija projekcija);
	public void update(Projekcija projekcija);
	public void delete(Long id);

}
