package com.block.project.springboot.web;

import com.block.project.springboot.config.auth.CustomOauth2Provider;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class IndexController {

    @GetMapping("/")
    public String Home(OAuth2AuthenticationToken authentication,
                       Model model,
                       @RequestHeader Map<String,String> headers,
                       @RequestParam Map<String,String> params,
                       @RequestBody (required=false) String body){

        if( authentication != null ) {

            String registrationId = authentication.getAuthorizedClientRegistrationId().toUpperCase();

            if(registrationId.equals(CustomOauth2Provider.KAKAO.name())){

                LinkedHashMap<String, String > properties = authentication.getPrincipal().getAttribute("properties");
                LinkedHashMap<String, String > kakaoAccount = authentication.getPrincipal().getAttribute("kakao_account");
                model.addAttribute("name",properties.get("nickname"));
                model.addAttribute("email",kakaoAccount.get("email"));
                return "kakao";
            }
        }
        return "index";
    }

    @GetMapping("/callback")
    public String callback(OAuth2AuthenticationToken authentication,
                           Model model,
                           @RequestHeader Map<String,String> headers,
                           @RequestParam Map<String,String> params,
                           @RequestBody (required=false) String body){
        return "index";
    }
}