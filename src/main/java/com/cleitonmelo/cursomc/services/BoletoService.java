package com.cleitonmelo.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.cleitonmelo.cursomc.domain.PagamentoComBoleto;

@Service
public class BoletoService {
	
	public void preencherVencimento(PagamentoComBoleto pagamento, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 7);
		pagamento.setDataVencimento(calendar.getTime());
	}

}
