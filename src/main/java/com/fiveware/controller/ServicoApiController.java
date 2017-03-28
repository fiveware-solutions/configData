package com.fiveware.controller;

import br.com.fiveware.captador.online.santander.portalnegocios.*;
import br.com.fiveware.captador.online.santander.portalnegocios.exception.ConsultaDividaException;
import br.com.fiveware.captador.online.santander.portalnegocios.exception.ParametroErradoException;
import br.com.fiveware.captador.santander.portalnegocios.CaptadorPortalNegocios;
import br.com.fiveware.captador.santander.portalnegocios.dados.OpcaoPropostaSimulacao;
import br.com.fiveware.captador.santander.portalnegocios.dados.SimulacaoPropostaRenegociacao;
import br.com.fiveware.commons.util.FormatacaoUtils;
import com.fiveware.registroOnline.model.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by valdisnei on 21/03/17.
 */
@RestController
public class ServicoApiController {

    private static Logger logger = LoggerFactory.getLogger(ServicoApiController.class);

    @PostMapping(value = "/santantderoyconsultadivida", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Divida>> consultaContrato(@RequestBody ConsultaDividaRequest request,
                                                             @Context HttpServletRequest context,
                                                             @RequestHeader HttpHeaders headers) throws ParametroErradoException {

        CaptadorPortalNegociosOnline captador = new CaptadorPortalNegociosOnline();
        Response<Divida> response = new Response<Divida>();
        try {
            Divida divida = captador.consultaDividaCliente(request.getUsuario(), request.getSenha(), request.getCpfCnpj());

            logger.debug("Divida: {} ", divida.getContratos());


            response.setDados(divida);

        } catch (ConsultaDividaException e) {
            logger.error("Erro na consultar de divida com os parametros {} ", request);
            response.setCodigoRetorno(1);
            response.setMsgErro(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro na consultar de divida com os parametros {} ", request);
            response.setCodigoRetorno(1);
            response.setMsgErro(e.getMessage());
        } finally {

            return ResponseEntity.ok(response);
        }

    }


    @PostMapping(value = "/santantderoysimularenegociacaodivida", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<SimulacaoPropostaRenegociacaoResponse>> simulaDivida(@RequestBody SimulacaoPropostaRenegociacaoRequest request,
                                                                                        @Context HttpServletRequest httpServletRequest,
                                                                                        @RequestHeader HttpHeaders headers) throws ParametroErradoException {

        CaptadorPortalNegocios captador = new CaptadorPortalNegocios(request.getUsuario(), request.getSenha());

        logger.debug("Received CaptadorPortalNegociosOnline.SimulaDivida: " + request);

        Response<SimulacaoPropostaRenegociacaoResponse> response = new Response<SimulacaoPropostaRenegociacaoResponse>();

        try {

            SimulacaoPropostaRenegociacao resposta = captador.simulaPropostaRenegociacao(request.getCpfCnpj(),
                    Arrays.asList(request.getContratosSimulacao()),
                    request.getDataEntrada(),
                    request.getDataVencPrimeiraParc(),
                    request.getPercDesconto(),
                    request.getValorEntrada(),
                    request.getNumParcela(),
                    request.getOpcaoParcelamento(),
                    request.getFormaPgto());

            SimulacaoPropostaRenegociacaoResponse simulacaoOnLine = new SimulacaoPropostaRenegociacaoResponse();

            List<OpcaoPropostaRenegociacao> propostasTraduzidas = new ArrayList<OpcaoPropostaRenegociacao>();
            for (OpcaoPropostaSimulacao proposta : resposta.getPropostasSimulacao()) {
                OpcaoPropostaRenegociacao traduzido = new OpcaoPropostaRenegociacao();
                traduzido.setQtdeParcela(proposta.getQTPARC());
                traduzido.setPercentualTaxa(corrigeFormatoPercentual(proposta.getPCTXJUR()));
                traduzido.setPercentualRenda(corrigeFormatoPercentual(proposta.getPCREND()));
                traduzido.setValorParcela(proposta.getVLPARC());
                traduzido.setValorParcelaComDesconto(proposta.getVLPARCD());
                traduzido.setPercentualTaxaNF(corrigeFormatoPercentual(proposta.getPCTXJURNF()));
                propostasTraduzidas.add(traduzido);
            }

            simulacaoOnLine.setCodigoPessoa(resposta.getCodigoPessoa());
            simulacaoOnLine.setCodigoRenegociacao(resposta.getCodigoRenegociacao());
            simulacaoOnLine.setCodProduto(resposta.getCodProduto());
            simulacaoOnLine.setCodSubProduto(resposta.getCodSubProduto());
            simulacaoOnLine.setDataEntrada(resposta.getDataEntrada());
            simulacaoOnLine.setDataVencPrimeiraParc(resposta.getDataVencPrimeiraParc());
            simulacaoOnLine.setDocumento(resposta.getDocumento());
            simulacaoOnLine.setFormaPgto(resposta.getFormaPgto().getId());
            simulacaoOnLine.setNumParcela(resposta.getNumParcela());
            simulacaoOnLine.setOpcaoParcelamento(resposta.getOpcaoParcelamento().getId());
            simulacaoOnLine.setTotalDividaRenegociavel(resposta.getTotalDividaRenegociavel());
            simulacaoOnLine.setValorDesconto(resposta.getValorDesconto());
            simulacaoOnLine.setValorEntrada(resposta.getValorEntrada());
            simulacaoOnLine.setPropostasSimulacao(propostasTraduzidas);

            response.setDados(simulacaoOnLine);

        } catch (Throwable e) {
            logger.error("Error {}", e);
            response.setCodigoRetorno(1);
            response.setMsgErro(e.getMessage());

        } finally {
            return ResponseEntity.ok(response);
        }

    }


//    @PostMapping(value = "/santanderoyincluipropostarenegociacaodivida", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<br.com.fiveware.captador.online.santander.portalnegocios.services.Response<InclusaoPropostaRenegociacaoResponse>> santanderoyincluipropostarenegociacaodivida
//            (@RequestBody InclusaoPropostaRenegociacaoRequest request,
//             @Context HttpServletRequest context,
//             @RequestHeader HttpHeaders headers) throws ParametroErradoException {
//
//        InclusaoPropostaRenegociacaoServices inclusao = new InclusaoPropostaRenegociacaoServices();
//
//        Response<InclusaoPropostaRenegociacaoResponse> response = inclusao.handleRequest(request, null);
//
//        return ResponseEntity.ok(response);
//    }


    private String corrigeFormatoPercentual(String valor) {
        valor = StringUtils.trimToEmpty(valor);
        valor = StringUtils.remove(valor, "+");
        valor = StringUtils.remove(valor, "-");

        try {
            Double valorDouble = null;
            if (valor.length() == 9) {
                valorDouble = (Double.parseDouble(valor) / 1000000.0);
            }
            if (String.valueOf(Integer.parseInt(valor)).length() <= 3) {
                valorDouble = Double.parseDouble(valor);
            }
            if (valorDouble == null) {
                return valor;
            }

            return FormatacaoUtils.formataNumeroPadraoBrasil(valorDouble, 6);
        } catch (Exception e) {
            return valor;
        }
    }
}