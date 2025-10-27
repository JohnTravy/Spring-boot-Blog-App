package org.travy.Springstarter.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.travy.Springstarter.Util.Constants.Roles;
import org.travy.Springstarter.models.Account;
import org.travy.Springstarter.models.Authority;
import org.travy.Springstarter.respositories.AccountRepository;

@Service
public class AccountService implements UserDetailsService{

    @Autowired
    private AccountRepository accountRepository;
    
    @Value("${spring.mvc.static-path-pattern}")
    private String photo_prefix;
   
    
    @Autowired
    private PasswordEncoder passwordEncoder;


    public Account save(Account account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if(account.getRoles() == null){
            account.setRoles(Roles.USER.getRole());
        }
        if(account.getPhoto()==null){
            
            String path = photo_prefix.replace("**", "images/person.png");
            account.setPhoto(path);
        }
       
       
        return accountRepository.save(account);
        
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);

        if(optionalAccount.isEmpty()){

            throw new UsernameNotFoundException("Account Not Found");

        }

        Account account = optionalAccount.get();

        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(new SimpleGrantedAuthority(account.getRoles()));

        for(Authority _auth : account.getAuthorities()){
         
            grantedAuthority.add(new SimpleGrantedAuthority(_auth.getName()));
            
        }

        return new User(account.getEmail(), account.getPassword(), grantedAuthority);
        

    }


   public Optional<Account> findOneByEmail(String email){

       return accountRepository.findByEmail(email);

   }

   public Optional<Account>findbyid(Long id){

    return accountRepository.findById(id);

   }


   public Optional<Account>findbyToken(String token){

    return accountRepository.findByToken(token);

   }
  

}
