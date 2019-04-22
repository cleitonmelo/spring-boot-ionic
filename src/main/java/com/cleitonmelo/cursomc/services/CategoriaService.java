package com.cleitonmelo.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cleitonmelo.cursomc.domain.Categoria;
import com.cleitonmelo.cursomc.dto.CategoriaDTO;
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
	 * Atualizar os dados tipo de categoria
	 * @param obj
	 * @return
	 */
	public Categoria update(Categoria obj) {
		Categoria newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
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

	/**
	 * Lista todas as categorias
	 * @return
	 */
	public List<Categoria> findAll() {
		return repo.findAll();
	}
	
	/**
	 * Retorna dados de categoria conforme paginação selecionada
	 * @param page
	 * @param linesPerPage
	 * @param direction
	 * @param orderBy
	 * @return
	 */
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String direction, String orderBy){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	/**
	 * 
	 * @param objDto
	 * @return
	 */
	public Categoria fromDto(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}
	
	/**
	 * Atualiza os dados
	 * @param newObj
	 * @param obj
	 */
	private void updateData(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
	}

}
