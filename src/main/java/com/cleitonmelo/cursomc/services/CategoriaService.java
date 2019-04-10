package com.cleitonmelo.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.cleitonmelo.cursomc.domain.Categoria;
import com.cleitonmelo.cursomc.repositories.CategoriaRepository;
import com.cleitonmelo.cursomc.services.exceptions.DataIntegrityException;
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

	/**
	 * Deletando o objeto com base no ID da Categoria
	 * @param id
	 */
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);			
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui filhos");
		}

	}

}
