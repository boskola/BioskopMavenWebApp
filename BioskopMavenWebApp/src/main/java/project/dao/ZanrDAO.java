package project.dao;

import java.util.List;

import project.model.Zanr;

public interface ZanrDAO {

	public Zanr findOne(Long id);
	public List<Zanr> findAll();
	public List<Zanr> find(String naziv);
	public void save(Zanr zanr);
	public void update(Zanr zanr);
	public void delete(Long id);

}
