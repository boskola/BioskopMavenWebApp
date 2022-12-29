package project.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.dao.ZanrDAO;
import project.model.Zanr;
import project.service.ZanrService;

@Service
public class DatabaseZanrService implements ZanrService {

	@Autowired
	private ZanrDAO zanrDAO;

	@Override
	public Zanr findOne(Long id) {
		return zanrDAO.findOne(id);
	}

	@Override
	public List<Zanr> findAll() {
		return zanrDAO.findAll();
	}

	@Override
	public List<Zanr> find(Long[] ids) {
		List<Zanr> rezultat = new ArrayList<>();
		for (Long id: ids) {
			Zanr zanr = zanrDAO.findOne(id);
			rezultat.add(zanr);
		}

		return rezultat;
	}

	@Override
	public Zanr save(Zanr zanr) {
		zanrDAO.save(zanr);
		return zanr;
	}

	@Override
	public List<Zanr> save(List<Zanr> zanrovi) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Zanr update(Zanr zanr) {
		zanrDAO.update(zanr);
		return zanr;
	}

	@Override
	public List<Zanr> update(List<Zanr> zanrovi) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Zanr delete(Long id) {
		Zanr zanr = findOne(id);
		if (zanr != null) {
			zanrDAO.delete(id);
		}
		return zanr;
	}

	@Override
	public void delete(List<Long> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Zanr> find(String naziv) {
	
		if (naziv == null) {
			naziv = "";
		}
		return zanrDAO.find(naziv);
	}

}
