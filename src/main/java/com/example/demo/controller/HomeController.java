package com.example.demo.controller;

import com.example.demo.repository.HomeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {

    @Autowired
    HomeMapper homeMapper;

    @GetMapping("/")
    public String index(Model model){
//        int selectId = homeMapper.selectById();
//        System.out.println(selectId);
//        model.addAttribute("message", selectId);
        return "index";
    }
}
