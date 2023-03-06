package com.robertg.linkedin.posts.controller;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/problem-details")
public class ProblemDetailsController {

    @GetMapping
    public ProblemDetail problemDetail(){
        return ProblemDetail.forStatus(429);
    }

    @GetMapping("/names/{id}")
    public String name(@PathVariable int id){
        if(id > 100){
            throw new NameNotFoundException();
        }

        return "Jack";
    }
}

class NameNotFoundException extends RuntimeException{}

@RestControllerAdvice
class ErrorHandler {

    @ExceptionHandler(value = NameNotFoundException.class)
    public ProblemDetail nameNotFoundException(){
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), "The requested name doesn't exist.");
    }
}