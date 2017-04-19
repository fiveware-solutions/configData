package com.fiveware.controller;

import br.com.icaptor.santander.captador.rcb.DadosAcordo;
import br.com.icaptor.santander.captador.rcb.TipoContrato;
import br.com.icaptor.santander.captador.rcb.online.CaptadorRCBOnline;
import br.com.icaptor.santander.captador.rcb.online.DadosConsultaAcordo;
import br.com.serasa.captacao.framework.exception.HarvestException;
import com.fiveware.registroOnline.model.Response;
import com.icaptor.online.santander.rcb.SituacaoAcordoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsultaSituacaoAcordo {

    static Logger logger = LoggerFactory.getLogger(ConsultaSituacaoAcordo.class);
    CaptadorRCBOnline captador = new CaptadorRCBOnline();

    public Response<DadosAcordo> handleRequest(SituacaoAcordoRequest request) {

        captador.setLoginInfo(request.getCnpj(), request.getUsuario(), request.getSenha());

        Response<DadosAcordo> response = new Response<DadosAcordo>();

        try {

            DadosConsultaAcordo dadosConsultaAcordo = new DadosConsultaAcordo();
            dadosConsultaAcordo.setContrato(request.getContrato());
            dadosConsultaAcordo.setNumeroAcordo(request.getNumeroAcordo());
            dadosConsultaAcordo.setTipo(TipoContrato.get(request.getTipoContrato()));
            dadosConsultaAcordo.setNumeroAcordo(request.getNumeroAcordo());

            DadosAcordo dadosAcordo = captador.consultarSituacaoAcordo(dadosConsultaAcordo);
            response.setDados(dadosAcordo);

        } catch (HarvestException e) {
            logger.error("error ConsultaSituacaoAcordo {}", e);
            response.setCodigoRetorno(1);
            response.setMsgErro(e.getMessage());
        } catch (Exception e) {
            logger.error("error ConsultaSituacaoAcordo {}", e);
            response.setCodigoRetorno(2);
            response.setMsgErro(e.getMessage());
        }

        return response;

    }
}
