package com.softserve.recruitment.application.exception;

import com.softserve.recruitment.infrastructure.burger_images_client.endpoint.BurgerRequestClientException;
import com.softserve.recruitment.infrastructure.burger_images_client.endpoint.BurgerRequestServerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.webflux.advice.general.ProblemAdviceTrait;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class ExceptionHandlerController implements ProblemAdviceTrait {

  @ExceptionHandler(BurgerRequestClientException.class)
  public Mono<ResponseEntity<Problem>> handleRestResponseException(BurgerRequestClientException ex,
      ServerWebExchange request) {
    Problem problem = Problem.builder()
                      .withStatus(Status.NOT_FOUND)
                      .with("message", ex.getMessage())
                      .build();
    return create(ex, problem, request);
  }

  @ExceptionHandler(BurgerRequestServerException.class)
  public Mono<ResponseEntity<Problem>> handleRestResponseException(BurgerRequestServerException ex,
      ServerWebExchange request) {
    Problem problem = Problem.builder()
                             .withStatus(Status.INTERNAL_SERVER_ERROR)
                             .withDetail(ex.getMessage())
                             .build();
    return create(ex, problem, request);
  }
}
