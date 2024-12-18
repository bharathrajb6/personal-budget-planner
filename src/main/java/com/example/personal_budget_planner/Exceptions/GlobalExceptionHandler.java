package com.example.personal_budget_planner.Exceptions;

import com.example.personal_budget_planner.DTO.Response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * To handle User Exception
     *
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> handleUserException(UserException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "User Error", exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * To handle Transaction Exception
     *
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<?> handleTransactionException(TransactionException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Transaction Error", exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * To handle Saving Goal Exception
     *
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(GoalException.class)
    public ResponseEntity<?> handleGoalException(TransactionException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Goal Error", exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * To handle redis cache exception
     *
     * @param cacheException
     * @param request
     * @return
     */
    @ExceptionHandler(CacheException.class)
    public ResponseEntity<?> handleCacheException(CacheException cacheException, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Cache Error", cacheException.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
