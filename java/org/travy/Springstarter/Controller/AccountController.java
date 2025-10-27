package org.travy.Springstarter.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.travy.Springstarter.Services.AccountService;
import org.travy.Springstarter.Services.EmailService;
import org.travy.Springstarter.Util.AppUtil;
import org.travy.Springstarter.Util.email.Emdetails;
import org.travy.Springstarter.models.Account;

import jakarta.validation.Valid;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

    @Value("${spring.mvc.static-path-pattern}")
    private String photo_prefix;

    @Value("${password.token.reset.timeout.minutes}")
    private int password_token_timeout;

    @Value("${site.domain}")
    private String site_domain;

    @GetMapping("/register")
    public String register(Model model){

        Account account = new Account();
        model.addAttribute("account", account);
        return "account_views/register";
        
    }

    @PostMapping("/register")
    public String register_user(@Valid @ModelAttribute Account account, BindingResult result){

        if(result.hasErrors()){

            return "account_views/register";

        }
        accountService.save(account);
        return "redirect:/";


    }

    
    @GetMapping("/login")
    public String login(Model model){

        return "account_views/login";
        
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model, Principal principal){
        String autuser = "email";

        if(principal != null){

            autuser = principal.getName();
            
        }
        Optional<Account> optionalaccount = accountService.findOneByEmail(autuser);

        if(optionalaccount.isPresent()){

            Account account = optionalaccount.get();
            model.addAttribute("account", account);
            model.addAttribute("photo", account.getPhoto());
            return "account_views/profile";

        }
      
      return "account_views/profile";
  
    }

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
     public String post_profile(@Valid @ModelAttribute Account account, BindingResult bindingResult, Principal principal ){

        if(bindingResult.hasErrors()){

            return "account_views/profile";

        }
        String authuser = "email";
        if(principal != null){

            authuser = principal.getName();

        }

        Optional<Account> optionalAccount = accountService.findOneByEmail(authuser);

        if(optionalAccount.isPresent()){
    
                Account account_by_id = accountService.findbyid(account.getId()).get();
                account_by_id.setAge(account.getAge());
                account_by_id.setDate_of_birth(account.getDate_of_birth());
                account_by_id.setFirstname(account.getFirstname());
                account_by_id.setGender(account.getGender());
                account_by_id.setLastname(account.getLastname());
                account_by_id.setPassword(account.getPassword());
                accountService.save(account_by_id);
                SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false); 
                
                
               
    
    
           
                 return "redirect:/";
            
        }else{

            return "redirect:/?error";

        }

     }

     @PostMapping("/update_photo")
     @PreAuthorize("isAuthenticated()")
     public String update_photo(@RequestParam("file") MultipartFile file , RedirectAttributes attributes, Principal principal ){

        if(file.isEmpty()){ 

            attributes.addFlashAttribute("error", "No File Added");
            return "redirect:/profile";


        }else{

            String filename = file.getOriginalFilename();

            try {
                int length = 10;
                boolean useNumbers = true;
                boolean useLetters = true;
                String generatedString = RandomStringUtils.secure().next(length, useLetters, useNumbers);
                String final_photo_name = generatedString + filename;
                String absolute_filelocation = AppUtil.get_upload_path(final_photo_name);
                Path path = Paths.get(absolute_filelocation);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                attributes.addFlashAttribute("message", "Successfully added");

                String authuser = "email";
                if(principal != null){
               
                    authuser = principal.getName();

                }

            
                Optional<Account> optional_account = accountService.findOneByEmail(authuser);

                if(optional_account.isPresent()){
                    Account account = optional_account.get();
                    Account account_by_id = accountService.findbyid(account.getId()).get();
                    String relative_filelocation = photo_prefix.replace("**", "images/" + final_photo_name);
                    account_by_id.setPhoto(relative_filelocation);
                    accountService.save(account_by_id);
                }

                try{
                      TimeUnit.SECONDS.sleep(1);

                }catch(InterruptedException ie){
                    Thread.currentThread().interrupt();
                }
                return "redirect:/profile";

            } catch (Exception e) {
               
            }

        }
        
          
        return "redirect:/profile?error";

     }

     @GetMapping("/forgot-password")
     @PreAuthorize("isAuthenticated()")
     public String forgot_password(Model model){

        return "account_views/forgot-password";

     }

     @PostMapping("/reset-password")
     public String reset_password(@RequestParam("email") String _email, RedirectAttributes attributes, Model model){

        Optional<Account> optionalaccount = accountService.findOneByEmail(_email);
        if(optionalaccount.isPresent()){
            Account account = accountService.findbyid(optionalaccount.get().getId()).get();
            String reset_token = UUID.randomUUID().toString();
            account.setToken(reset_token);
            account.setPassword_reset_token_expiry(LocalDateTime.now().plusMinutes(password_token_timeout));
            accountService.save(account);
            String reset_message = "This is the reset password link: " + site_domain + "change-password?token="+reset_token;
            Emdetails emaildetails = new Emdetails(account.getEmail(), reset_message, "reset password Tom Tom Post demo");

            if(emailService.sendSimpleEmail(emaildetails) == false){
                
                attributes.addFlashAttribute("error", "Error while sending email, CONTACT ADMIN");
                return "redirect:/forgot-password";

            }
              attributes.addFlashAttribute("message", "password reset email sent");
              return "redirect:/login";

        }else{
            attributes.addFlashAttribute("error", "No user Found with the email supplied");
            return "redirect:/forgot-password";
        }

     }

     @GetMapping("/change-password")

     public String  change_password(Model model, @RequestParam("token") String token, RedirectAttributes attributes){

        if(token.equals("")){
            attributes.addFlashAttribute("error", "invalid token");
            return "redirect:/forgot-password";
        }
        Optional<Account>optionalAccount = accountService.findbyToken(token);

        if(optionalAccount.isPresent()){

            Account account = accountService.findbyid(optionalAccount.get().getId()).get();
            LocalDateTime now = LocalDateTime.now();
            if(now.isAfter(optionalAccount.get().getPassword_reset_token_expiry())){

                attributes.addFlashAttribute("error", "Token Expired");
                return "redirect:/forgot-password";
            }
            model.addAttribute("account", account);
            return "account_views/change-password";


        }
        attributes.addFlashAttribute("error", "Invalid Token");
        return "redirect:/forgot-password";


     }

     @PostMapping("/change-password")
     public String post_change_password(@ModelAttribute Account account, RedirectAttributes attributes){

        Account account_by_id = accountService.findbyid(account.getId()).get();
        account_by_id.setPassword(account.getPassword());
        account_by_id.setToken("");
        accountService.save(account_by_id);
        attributes.addFlashAttribute("message", "Password updated");
        return "redirect:/login";

     }


    }


 

