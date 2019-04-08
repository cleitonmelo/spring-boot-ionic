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
	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo " + Categoria.class.getName()));
	}
	
	/**
	 * Inserir um objeto do tipo categoria
	 * @param categoria
	 * @return Salva objeto do tipo categoria
	 */
	public Categoria inserir(Categoria categoria) {
		categoria.setId(null);
		return repo.save(categoria);
	}

}
