package project.service;

import java.util.List;

import project.model.Zanr;

public interface ZanrService {

	Zanr findOne(Long id);
	List<Zanr> findAll();
	List<Zanr> find(Long[] ids);
	Zanr save(Zanr zanr);
	List<Zanr> save(List<Zanr> zanrovi);
	Zanr update(Zanr zanr);
	List<Zanr> update(List<Zanr> zanrovi);
	Zanr delete(Long id);
	void delete(List<Long> ids);
	List<Zanr> find(String naziv);
	
}
