package com.soulcode.goserviceapp.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ErrorPersController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("error");

        switch(response.getStatus()){
            case 400:
                mv.addObject("errorMsg", "BAD REQUEST");
            break;
            case 401:
                mv.addObject("errorMsg", "UNAUTHORIZED");
                break;
            case 403:
                mv.addObject("errorMsg", "FORBIDDEN");
                break;
            case 404:
                mv.addObject("errorMsg", "NOT FOUND");
                break;
            case 500:
            default:
                mv.addObject("errorMsg", "INTERNAL SERVER ERROR");
                break;
        }
        mv.addObject("errorCode", response.getStatus());
        return mv;
    }

}
