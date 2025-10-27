package org.travy.Springstarter.Controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.travy.Springstarter.Services.AccountService;
import org.travy.Springstarter.Services.PostService;
import org.travy.Springstarter.models.Account;
import org.travy.Springstarter.models.Post;

import jakarta.validation.Valid;



@Controller
public class PostController {

@Autowired
private PostService postService;

@Autowired
private AccountService accountService;

@GetMapping("/post/{id}")
public String getPost(@PathVariable Long id, Model model, Principal principal){
   
    Optional<Post> optionalPost = postService.getByid(id);
     String authuser = "email";
    if(optionalPost.isPresent()){
      
        Post post = optionalPost.get();
        model.addAttribute("post", post);

        if(principal != null){

            authuser = principal.getName();

        }
        if(authuser.equals(post.getAccount().getEmail())){
           
            model.addAttribute("isOwner", true);

        }else{

            model.addAttribute("isOwner", false);
        }

        return "post_views/post";
    }
    else{

        return "404";
    }

    

    
}

@GetMapping("/posts/add")
@PreAuthorize("isAuthenticated()")
public String addpost(Model model, Principal principal){

    String authuser = "email";

    if(principal != null){

        authuser = principal.getName();

    }
    Optional<Account>optionalaccount = accountService.findOneByEmail(authuser);
    if(optionalaccount.isPresent()){
        
        Post post = new Post();
        post.setAccount(optionalaccount.get());
        model.addAttribute("post", post);

    }else{
 
        return "redirect:/";

    }
 
    return "post_views/post_add";
}


@PostMapping("/posts/add")
@PreAuthorize("isAuthenticated")
public String addPostHandler(@Valid @ModelAttribute Post post, BindingResult bindingResult, Principal principal){
    
    if(bindingResult.hasErrors()){

        return "post_views/post_add";

    }

    String authuser = "email";

    if(principal != null){

        authuser = principal.getName();

    }
    if(post.getAccount().getEmail().compareToIgnoreCase(authuser) < 0){

       return "redirect:/?error";

    }

    postService.save(post);

    return "redirect:/post/" + post.getId();



}

@GetMapping("/posts/{id}/edit")
@PreAuthorize("isAuthenticated")
public String getPostedit(@PathVariable Long id, Model model){

Optional<Post>optionalpost = postService.getByid(id);

if(optionalpost.isPresent()){

    Post post = optionalpost.get();
    model.addAttribute("post", post);
    return "post_views/post_edit";

}else{

    return "404";

}

}

@PostMapping("/posts/{id}/edit")
@PreAuthorize("isAuthenticated")
public String updatepost(@Valid  @ModelAttribute Post post,  BindingResult bindingResult, @PathVariable Long id){

    if(bindingResult.hasErrors()){

        return "post_views/post_edit";

    }
    Optional<Post>optionalpost = postService.getByid(id);

    if(optionalpost.isPresent()){

        Post existingpost = optionalpost.get();
        existingpost.setTitle(post.getTitle());
        existingpost.setBody(post.getBody());
        postService.save(existingpost);

    }
    return "redirect:/post/" + post.getId();

}


@GetMapping("/posts/{id}/delete")
@PreAuthorize("isAuthenticated")

public String deletepost(@PathVariable Long id){

   
    Optional<Post>optionalpost = postService.getByid(id);

    if(optionalpost.isPresent()){

        Post post = optionalpost.get();
        postService.delete(post);

        return "redirect:/";
    }else{

        return "404";

    }
   
    
    




}

}