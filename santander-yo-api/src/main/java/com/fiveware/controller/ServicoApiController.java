package com.fiveware.controller;

import br.com.fiveware.captador.online.santander.portalnegocios.*;
import br.com.fiveware.captador.online.santander.portalnegocios.exception.ConsultaDividaException;
import br.com.fiveware.captador.online.santander.portalnegocios.exception.ParametroErradoException;
import br.com.fiveware.captador.santander.portalnegocios.CaptadorPortalNegocios;
import br.com.fiveware.captador.santander.portalnegocios.dados.OpcaoPropostaSimulacao;
import br.com.fiveware.captador.santander.portalnegocios.dados.SimulacaoPropostaRenegociacao;
import br.com.fiveware.commons.util.FormatacaoUtils;
import com.fiveware.registroOnline.model.Response;
import com.google.common.base.MoreObjects;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by valdisnei on 29/03/17.
 */
@RestController
@RequestMapping("/api")
public class ServicoApiController {

    Logger logger = LoggerFactory.getLogger(ServicoApiController.class);

    @PostMapping("/consultadivida")
    public Response<Divida> consultaContrato(@RequestBody ConsultaDividaRequest request,
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

            return response;
        }
    }


    @PostMapping(value = "/santantderoysimularenegociacaodivida")
    public Response<SimulacaoPropostaRenegociacaoResponse> simulaDivida(@RequestBody SimulacaoPropostaRenegociacaoRequest request,
                                                                        @RequestHeader HttpHeaders headers,
                                                                        @PathVariable("version") String version) throws ParametroErradoException {

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
            return response;
        }

    }

    @Value("${words}")
    private String profileTeste;

    @GetMapping(value = "/teste")
    public String teste(HttpServletRequest context) throws InterruptedException {
        logger.info("perfil {}", profileTeste);
        return profileTeste;
    }


    @GetMapping(value = "/health")
    public String health(HttpServletRequest context) throws InterruptedException {

        Stopwatch stopwatch = Stopwatch.createStarted();

        logger.info(" configurado 60 segundos no codigo");

        Thread.sleep(60000);
        Stopwatch stop = stopwatch.stop();

        long elapsed = stop.elapsed(TimeUnit.SECONDS);


        logger.info(" - tempo estimado: {} segundos ", stop.elapsed(TimeUnit.SECONDS));


        return "Started API >>>>>>>>>>>> OK  >>>>>>>>>>> " + context.getRemoteAddr() + " port:" + context.getRemotePort();
    }


    @GetMapping(value = "/metadata")
    public String metadata(HttpServletRequest context, @RequestHeader HttpHeaders headers) {
        RestTemplate restTemplate = new RestTemplate();
        String instanceId = restTemplate.getForObject("http://169.254.169.254/latest/meta-data/instance-id", String.class);
        String instanceHostname = restTemplate.getForObject("http://169.254.169.254/latest/meta-data/public-hostname", String.class);

        return "instanceID: " + instanceId + " #############==> " + instanceHostname;
    }


    private List<String> getApiKeyFromHeader(HttpHeaders headers) throws RuntimeException {

        if (Objects.isNull(headers.get("x-api-key")))
            throw new RuntimeException(String.valueOf(ResponseEntity.badRequest().build()));

        return MoreObjects.firstNonNull(headers.get("x-api-key"), Collections.emptyList());
    }


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
