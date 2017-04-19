package com.fiveware.controller;

import br.com.icaptor.santander.captador.rcb.Lamina;
import br.com.icaptor.santander.captador.rcb.online.CaptadorRCBOnline;
import br.com.icaptor.santander.captador.rcb.online.DadosConsultaLamina;
import br.com.serasa.captacao.framework.exception.HarvestException;
import com.fiveware.registroOnline.model.Response;
import com.icaptor.online.santander.rcb.LaminaRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsultaLamina {

    static Logger logger = LoggerFactory.getLogger(ConsultaLamina.class);

    CaptadorRCBOnline captador = new CaptadorRCBOnline();

    public Response<Lamina> handleRequest(LaminaRequest request) {

        captador.setLoginInfo(request.getCnpj(), request.getUsuario(), request.getSenha());


        Response<Lamina> response = new Response<Lamina>();

        try {

            DadosConsultaLamina dadosConsultaLamina = new DadosConsultaLamina();
            dadosConsultaLamina.setContrato(request.getContrato());
            dadosConsultaLamina.setNumeroAcordo(request.getNumeroAcordo());
            dadosConsultaLamina.setVencimentoAcordo(request.getVencimentoAcordo());

            Lamina lamina = captador.consultaLamina(dadosConsultaLamina);

            response.setDados(lamina);

        } catch (HarvestException e) {
            logger.error("Erro Consulta Lamina:{}", e);
            response.setCodigoRetorno(1);
            response.setMsgErro(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro Consulta Lamina:{}", e);
            response.setCodigoRetorno(2);
            response.setMsgErro(e.getMessage());
        }

        return response;

    }


}
