package com.fiveware.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiveware.registroOnline.service.RegistroAcessoOnlineService;
import com.google.common.base.MoreObjects;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by valdisnei on 24/03/17.
 */
@Aspect
@EnableAspectJAutoProxy
@Component
public class AspectRestApi {

    @Autowired
    private ObjectMapper objectMapper;

    @AfterReturning(value = "execution(public * com.fiveware.controller.ServicoApiController.*(..)) ", returning = "returnValue")
    private void consultaContrato(JoinPoint joinPoint, ResponseEntity returnValue) throws JsonProcessingException {

        Object bodyResonse = returnValue.getBody();
        Object requestPayLoad = joinPoint.getArgs()[0];


        HttpServletRequest context = (HttpServletRequest) joinPoint.getArgs()[1];
        List<String> apiKey = getApiKeyFromHeader(joinPoint.getArgs()[2]);


        RegistroAcessoOnlineService.INSTANCE.gravarAcessoCaptadorOnline(apiKey.get(0),context.getRequestURI(),
                                                                        context.getRemoteAddr(),
                requestPayLoad.toString(),bodyResonse.toString());
    }




    private List<String> getApiKeyFromHeader(Object o) throws RuntimeException {
        HttpHeaders headers = (HttpHeaders) o;

        if(Objects.isNull(headers.get("x-api-key")))
            throw new RuntimeException(String.valueOf(ResponseEntity.badRequest().build()));

        return MoreObjects.firstNonNull(headers.get("x-api-key"), Collections.emptyList());
    }
}

