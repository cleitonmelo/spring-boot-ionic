package com.cleitonmelo.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cleitonmelo.cursomc.domain.ItemPedido;
import com.cleitonmelo.cursomc.domain.PagamentoComBoleto;
import com.cleitonmelo.cursomc.domain.Pedido;
import com.cleitonmelo.cursomc.domain.enums.EstadoPagamento;
import com.cleitonmelo.cursomc.repositories.ItemPedidoRepository;
import com.cleitonmelo.cursomc.repositories.PagamentoRepository;
import com.cleitonmelo.cursomc.repositories.PedidoRepository;
import com.cleitonmelo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;

	@Autowired
	private BoletoService boletoService;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private ClienteService clienteService;

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo " + Pedido.class.getName()));
	}

	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);

		this.setFormaPagamento(obj);

		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());

		this.setItensPedido(obj);
		itemPedidoRepository.saveAll(obj.getItens());

		System.out.println(obj);
		return obj;
	}

	private void setFormaPagamento(Pedido obj) {
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagamento = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherVencimento(pagamento, obj.getInstante());
		}
	}

	private void setItensPedido(Pedido obj) {
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}

	}
}
