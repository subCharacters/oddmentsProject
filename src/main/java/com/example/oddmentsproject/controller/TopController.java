package com.example.oddmentsproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TopController {

    @RequestMapping(path = {"/","/top"})
    public String top() {
        return "top";
    }

    @RequestMapping(path = {"/db"})
    public String db() {
        return "dbtime";
    }
}
