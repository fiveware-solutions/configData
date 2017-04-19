package com.fiveware.controller;

import br.com.icaptor.santander.captador.rcb.*;
import br.com.icaptor.santander.captador.rcb.online.CaptadorRCBOnline;
import br.com.icaptor.santander.captador.rcb.online.DadosInclusaoAcordo;
import br.com.serasa.captacao.framework.exception.HarvestException;
import com.amazonaws.services.lambda.runtime.Context;
import com.fiveware.registroOnline.model.Response;
import com.icaptor.online.santander.rcb.InclusaoAcordoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncluiAcordo {

	static Logger logger = LoggerFactory.getLogger(IncluiAcordo.class);
	CaptadorRCBOnline captador = new CaptadorRCBOnline();

	public Response<Acordo> handleRequest(InclusaoAcordoRequest request) {

		captador.setLoginInfo(request.getCnpj(), request.getUsuario(), request.getSenha());	

		Response<Acordo> response = new Response<Acordo>();
		
		try {

			incluir(request, response);


		} catch (HarvestException e) {
			logger.error("error IncluiAcordo {}",e);
			response.setCodigoRetorno(1);
			response.setMsgErro(e.getMessage());
		} catch (Exception e) {
			logger.error("error IncluiAcordo {}",e);
			response.setCodigoRetorno(2);
			response.setMsgErro(e.getMessage());
		}

		return response;

	}

	private void incluir(InclusaoAcordoRequest request, Response<Acordo> response) throws HarvestException {
		DadosInclusaoAcordo dadosInclusaoAcordo = new DadosInclusaoAcordo(TipoContrato.get(request.getTipoContrato()));
		dadosInclusaoAcordo.setDataVencimento(request.getDataVencimento());
		dadosInclusaoAcordo.setExcecao(request.isExcecao());
		dadosInclusaoAcordo.setMotivo(request.getMotivo());
		dadosInclusaoAcordo.setNumeroContrato(request.getContrato());
		dadosInclusaoAcordo.setNumeroParcelasNegociar(request.getNumeroParcelasNegociar());
		dadosInclusaoAcordo.setOrigemAcordo(OrigemAcordo.get(request.getOrigemAcordo()));
		dadosInclusaoAcordo.setTabelaMotivo(request.getTabelaMotivo());
		dadosInclusaoAcordo.setTipoAcordo(TipoAcordo.get(request.getTipoAcordo()));
		dadosInclusaoAcordo.setTipoCarteira(TipoCarteira.get(request.getTipoCarteira()));
		dadosInclusaoAcordo.setTipoInclusao(TipoContrato.get(request.getTipoContrato()));
		// dadosInclusaoAcordo.setUsarExcecaoUltimaTentativa(isUsarExcecaoUltimaTentativa);??
		dadosInclusaoAcordo.setValorComissao(request.getValorComissao());
		dadosInclusaoAcordo.setValorTotalAcordo(request.getValorTotalAcordo());

		Acordo acordo = captador.incluirAcordo(dadosInclusaoAcordo);
		response.setDados(acordo);
	}

}
