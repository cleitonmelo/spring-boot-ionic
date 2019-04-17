package com.cleitonmelo.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cleitonmelo.cursomc.domain.Cliente;
import com.cleitonmelo.cursomc.dto.ClienteDTO;
import com.cleitonmelo.cursomc.repositories.ClienteRepository;
import com.cleitonmelo.cursomc.services.exceptions.DataIntegrityException;
import com.cleitonmelo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo " + Cliente.class.getName()));
	}
	
	/**
	 * Inserir um objeto do tipo categoria
	 * @param categoria
	 * @return Salva objeto do tipo categoria
	 */
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	/**
	 * Atualizando um objeto do tipo categoria
	 * @param obj
	 * @return Salva um objeto do tipo categoria
	 */
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	/**
	 * Deletando o objeto com base no ID da Cliente
	 * @param id
	 */
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);			
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um cliente que possua entidades relacionadas");
		}

	}

	/**
	 * Lista todas as categorias
	 * @return
	 */
	public List<Cliente> findAll() {
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
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String direction, String orderBy){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	/**
	 * 
	 * @param objDto
	 * @return
	 */
	public Cliente fromDto(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome() , objDto.getEmail(), null, null);
	}
	
	/**
	 * Atualiza os dados
	 * @param newObj
	 * @param obj
	 */
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
			
	}


}
