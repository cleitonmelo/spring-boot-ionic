package com.cleitonmelo.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cleitonmelo.cursomc.domain.Categoria;
import com.cleitonmelo.cursomc.repositories.CategoriaRepository;
import com.cleitonmelo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	/**
	 * Buscar objeto do tipo categoria
	 * @param id
	 * @return objeto do tipo categoria
	 */
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo " + Categoria.class.getName()));
	}
	
	/**
	 * Inserir um objeto do tipo categoria
	 * @param categoria
	 * @return Salva objeto do tipo categoria
	 */
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	/**
	 * Atualizando um objeto do tipo categoria
	 * @param obj
	 * @return Salva um objeto do tipo categoria
	 */
	public Categoria update(Categoria obj) {
		find(obj.getId());
		return repo.save(obj);
	}

}
