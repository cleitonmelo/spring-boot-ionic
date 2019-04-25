package com.cleitonmelo.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo " + Pedido.class.getName()));
	}

	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);

		this.setFormaPagamento(obj);

		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());

		this.setItensPedido(obj);
		itemPedidoRepository.saveAll(obj.getItens());

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
			ip.setPreco(produtoService.find(ip.getProduto().getId()).getPreco());
			ip.setPedido(obj);
		}

	}
}
