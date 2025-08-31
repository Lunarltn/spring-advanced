package org.example.expert.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Aspect
@Component
@Order(1)
@RequiredArgsConstructor
public class AdminLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(AdminLoggingAspect.class);

    @Pointcut("execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    private void changeUserRole() {

    }

    @Pointcut("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..))")
    private void deleteComment() {

    }

    @Around("changeUserRole()||deleteComment()")
    public Object aroundAdminLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;

        String userId = request.getAttribute("userId").toString();
        LocalDateTime currentTime = LocalDateTime.now();
        String url = request.getRequestURL().toString();
        String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

        log.info(" 요청 사용자 아이디 = {}", userId);
        log.info("요청 시각 = {}", currentTime);
        log.info("요청 URL = {}", url);
        log.info("requestBody = {}", requestBody);
        Object result = joinPoint.proceed();

        String responseBody = getResponseBody(result);

        log.info("responseBody = {}", responseBody);

        return result;
    }

    private static String getResponseBody(Object response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseNode = objectMapper.valueToTree(response);
        JsonNode bodyNode = responseNode.path("body");
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bodyNode);

    }
}
