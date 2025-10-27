package org.travy.Springstarter.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.travy.Springstarter.Services.PostService;
import org.travy.Springstarter.models.Post;



@Controller
public class HomeController {
    
  @Autowired
  private PostService postService;
  
   @GetMapping("/")
  public String home(Model model){

    List<Post>posts = postService.findAll();
    model.addAttribute("posts", posts);

    return "home_views/home";

  }

 

  

}
