package com.fiveware.controller;

import br.com.icaptor.santander.captador.rcb.Acordo;
import br.com.icaptor.santander.captador.rcb.TipoContrato;
import br.com.icaptor.santander.captador.rcb.online.CaptadorRCBOnline;
import br.com.icaptor.santander.captador.rcb.online.DadosConsultaContrato;
import br.com.serasa.captacao.framework.exception.HarvestException;
import com.fiveware.registroOnline.model.Response;
import com.icaptor.online.santander.rcb.SituacaoContratoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsultaSituacaoContrato {

    static Logger logger = LoggerFactory.getLogger(ConsultaSituacaoContrato.class);

    CaptadorRCBOnline captador = new CaptadorRCBOnline();

    public Response<Acordo> handleRequest(SituacaoContratoRequest request) {

        captador.setLoginInfo(request.getCnpj(), request.getUsuario(), request.getSenha());

        Response<Acordo> response = new Response<Acordo>();

        try {

            DadosConsultaContrato dadosConsultaContrato = new DadosConsultaContrato();
            dadosConsultaContrato.setContrato(request.getContrato());
            dadosConsultaContrato.setTipo(TipoContrato.get(request.getTipoContrato()));

            Acordo acordo = captador.consultaSituacaoContrato(dadosConsultaContrato);
            response.setDados(acordo);

        } catch (HarvestException e) {
            logger.error("Erro ConsultaSituacaoContrato {}", e);
            response.setCodigoRetorno(1);
            response.setMsgErro(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ConsultaSituacaoContrato {}", e);
            response.setCodigoRetorno(2);
            response.setMsgErro(e.getMessage());
        }

        return response;

    }


}
