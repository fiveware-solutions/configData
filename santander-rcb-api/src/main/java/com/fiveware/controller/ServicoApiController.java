package com.fiveware.controller;

import br.com.icaptor.santander.captador.rcb.Acordo;
import br.com.icaptor.santander.captador.rcb.DadosAcordo;
import br.com.icaptor.santander.captador.rcb.Lamina;
import com.fiveware.registroOnline.model.Response;
import com.icaptor.online.santander.rcb.InclusaoAcordoRequest;
import com.icaptor.online.santander.rcb.LaminaRequest;
import com.icaptor.online.santander.rcb.SituacaoAcordoRequest;
import com.icaptor.online.santander.rcb.SituacaoContratoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by valdisnei on 29/03/17.
 */
@RestController
@RequestMapping("/v2/api")
public class ServicoApiController {

    Logger logger = LoggerFactory.getLogger(ServicoApiController.class);


    @PostMapping(value = "/consultasituacaocontrato")
    public Response<Acordo> consultaSituacaoContrato(@RequestBody SituacaoContratoRequest situacaoContratoRequest,
                                           @RequestHeader HttpHeaders headers){
        ConsultaSituacaoContrato consultaSituacaoContrato = new ConsultaSituacaoContrato();

        logger.info("Consulta consultasituacaocontrato {} ",situacaoContratoRequest);

        return consultaSituacaoContrato.handleRequest(situacaoContratoRequest);
    }

    @PostMapping(value = "/consultasituacaoacordo")
    public Response<DadosAcordo> consultasituacaoacordo(@RequestBody SituacaoAcordoRequest situacaoAcordoRequest,
                                                        @RequestHeader HttpHeaders headers){
        ConsultaSituacaoAcordo consultaSituacaoAcordo = new ConsultaSituacaoAcordo();

        logger.info("Consulta consultasituacaoacordo {} ",situacaoAcordoRequest);

        return consultaSituacaoAcordo.handleRequest(situacaoAcordoRequest);
    }


    @PostMapping(value = "/incluiracordo")
    public Response<Acordo> incluirAcordo(@RequestBody InclusaoAcordoRequest inclusaoAcordoRequest,
                                           @RequestHeader HttpHeaders headers){
        IncluiAcordo incluiAcordo = new IncluiAcordo();

        logger.info("Incluir acordo {} ",inclusaoAcordoRequest);

        return incluiAcordo.handleRequest(inclusaoAcordoRequest);
    }


    @PostMapping(value = "/consultalamina")
    public Response<Lamina> consultaLamina(@RequestBody LaminaRequest laminaRequest,
                                           @RequestHeader HttpHeaders headers){
        ConsultaLamina consultaLamina = new ConsultaLamina();

        logger.info("Consulta Lamina {} ",laminaRequest);

        return consultaLamina.handleRequest(laminaRequest);
    }


    @Value("${words}")
    private String profileTeste;

    @GetMapping(value = "/teste")
    public String teste(HttpServletRequest context) throws InterruptedException {
        logger.info("perfil {}", profileTeste);
        return profileTeste;
    }

    @GetMapping(value = "/health")
    public String health(HttpServletRequest context){
        return "[ IP: "+context.getRemoteAddr()+"============>clear PORT: "+context.getRemotePort()+" ]";
    }

    @GetMapping(value = "/metadata")
    public String metadata(HttpServletRequest context,@RequestHeader HttpHeaders headers){
        RestTemplate restTemplate = new RestTemplate();
        String instanceId = restTemplate.getForObject("http://169.254.169.254/latest/meta-data/instance-id", String.class);
        String instanceHostname = restTemplate.getForObject("http://169.254.169.254/latest/meta-data/public-hostname", String.class);

        return "instanceID: "+instanceId+" ===> "+instanceHostname;
    }



}
