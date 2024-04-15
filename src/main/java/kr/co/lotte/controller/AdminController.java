package kr.co.lotte.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/admin/index")
    public String adminIndex(){
        return "/admin/index";
    }

    //config
    @GetMapping("/admin/config/banner")
    public String banner(){
        return "/admin/config/banner";
    }

    @GetMapping("/admin/config/info")
    public String info(){
        return "/admin/config/info";
    }

    //product
    @GetMapping("/admin/product/list")
    public String list(){
        return "/admin/product/list";
    }

    @GetMapping("/admin/product/register")
    public String register(){
        return "/admin/product/register";
    }

}
