package kr.co.lotte.controller;


import ch.qos.logback.classic.Logger;
import kr.co.lotte.dto.ProductsDTO;
import kr.co.lotte.service.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MarketController {

    private final MarketService marketService;


    @GetMapping("/product/list")
    public String list(){

        return "/product/list";
    }

    @GetMapping("/product/view")
    public String view(Model model,int prodno){

        log.info("prodno : "+prodno);

        ProductsDTO productsDTO = marketService.selectProduct(prodno);

        log.info("productsDTO : "+productsDTO);

        model.addAttribute("product", productsDTO);//제품정보와 이미지정보도 같이 담은 productsDTO

        return "/product/view";
    }

    @GetMapping("/product/cart")
    public String cart(){
        return "/product/cart";

    }


    @GetMapping("/product/order")
    public String order(){

        return "/product/order";
    }


    @GetMapping("/product/search")
    public String search(){

        return "/product/search";
    }

    @GetMapping("/product/complete")
    public String complete(){

        return "/product/complete";
    }


}
