package org.travy.Springstarter.Config;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.travy.Springstarter.Services.AccountService;
import org.travy.Springstarter.Services.AuthorityService;
import org.travy.Springstarter.Services.PostService;
import org.travy.Springstarter.Util.Constants.Priviledges;
import org.travy.Springstarter.Util.Constants.Roles;
import org.travy.Springstarter.models.Account;
import org.travy.Springstarter.models.Authority;
import org.travy.Springstarter.models.Post;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private PostService postService;


    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private AccountService accountService;

    @Override
    public void run(String... args) throws Exception {
        for(Priviledges _auth : Priviledges.values()){
        Authority newaut = new Authority();
        newaut.setId(_auth.getid());
        newaut.setName(_auth.getpriviledge());
        authorityService.save(newaut);
        }
       

        Account account01 = new Account();
        account01.setEmail("user@user.com");
        account01.setPassword("pass987");
        account01.setFirstname("userp");
        account01.setLastname("lastname");
        account01.setAge(19);
        account01.setDate_of_birth(LocalDate.parse("1990-01-01"));
        account01.setGender("female");
        

        Account account02 = new Account();
        account02.setEmail("johntomiwa71@gmail.com");
        account02.setPassword("pass987");
        account02.setFirstname("admin");
        account02.setLastname("lastname");
        account02.setRoles(Roles.ADMIN.getRole());
        account02.setAge(30);
        account02.setDate_of_birth(LocalDate.parse("1990-04-01"));
        account02.setGender("Male");
        
        Account account03 = new Account();
        account03.setEmail("editor@editor.com");
        account03.setPassword("pass987");
        account03.setFirstname("editor");
        account03.setLastname("lastname");
        account03.setRoles(Roles.EDITOR.getRole());
        account03.setAge(70);
        account03.setDate_of_birth(LocalDate.parse("1990-01-07"));
        account03.setGender("Male");

        
        Account account04 = new Account();
        account04.setEmail("super_editor@editor.com");
        account04.setPassword("pass987");
        account04.setFirstname("super_editor");
        account04.setLastname("lastname");
        account04.setRoles(Roles.EDITOR.getRole());
        account04.setAge(66);
        account04.setDate_of_birth(LocalDate.parse("1990-01-09"));
        account04.setGender("Male");
        Set<Authority>authorities = new HashSet<>();
        authorityService.findById(Priviledges.ACCESS_ADMIN_PANEL.getid()).ifPresent(authorities :: add);
        authorityService.findById(Priviledges.RESET_ANY_USER_PASSWORD.getid()).ifPresent(authorities:: add);
        account04.setAuthorities(authorities);
     
        accountService.save(account01);
        accountService.save(account02);
        accountService.save(account03);
        accountService.save(account04);
      
        
        List<Post> posts = postService.findAll();

        if(posts.size() == 0){

            Post post01 = new Post();

            post01.setTitle( "Input Title");

            post01.setBody( "Write Here");

            post01.setAccount(account01);

            postService.save(post01);


            Post post02 = new Post();

            post02.setTitle( "Input Title");

            post02.setBody( "Write Here");

            post02.setAccount(account02);

            postService.save(post02);



        }

       

    }
    
}
