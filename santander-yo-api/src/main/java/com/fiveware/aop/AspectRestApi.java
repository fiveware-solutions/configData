package com.fiveware.aop;

/**
 * Created by valdisnei on 24/03/17.
 */
//@Aspect
//@EnableAspectJAutoProxy
//@Component
public class AspectRestApi {
//
//
////    @AfterReturning(value = "execution(public * com.fiveware.controller.ServicoApiController.*(..)) ", returning = "returnValue")
//    private void consultaContrato(JoinPoint joinPoint, ResponseEntity returnValue) throws JsonProcessingException {
//
//        Object bodyResonse = returnValue.getBody();
//        Object requestPayLoad = joinPoint.getArgs()[0];
//
//
//        HttpServletRequest context = (HttpServletRequest) joinPoint.getArgs()[1];
//        List<String> apiKey = getApiKeyFromHeader(joinPoint.getArgs()[2]);
//
//        System.out.println("IP " + context.getRemoteAddr());
//
//        System.out.println(" path " + context.getRequestURI());
//
//        System.out.println("apiKey = " + apiKey.get(0));
//
////        RegistroAcessoOnlineService.INSTANCE.gravarAcessoCaptadorOnline(apiKey.get(0),context.getRequestURI(),
////                                                                        context.getRemoteAddr(),
////                requestPayLoad.toString(),bodyResonse.toString());
//    }
//
//
//
//
//    private List<String> getApiKeyFromHeader(Object o) throws RuntimeException {
//        HttpHeaders headers = (HttpHeaders) o;
//
//        if(Objects.isNull(headers.get("x-api-key")))
//            throw new RuntimeException(String.valueOf(ResponseEntity.badRequest().build()));
//
//        return MoreObjects.firstNonNull(headers.get("x-api-key"), Collections.emptyList());
//    }
}

