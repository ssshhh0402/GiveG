package com.block.project.springboot.web;

import com.block.project.springboot.config.auth.LoginUser;
import com.block.project.springboot.config.auth.SessionUser;
import com.block.project.springboot.domain.user.User;
import com.block.project.springboot.service.PostsService;
import com.block.project.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser User user){

        model.addAttribute("posts",postsService.findAllDesc());

        if(user != null){
            model.addAttribute("profileName", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){

        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post",dto);

        return "posts-update";
    }
}
