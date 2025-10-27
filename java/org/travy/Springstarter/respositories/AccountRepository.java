package org.travy.Springstarter.respositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.travy.Springstarter.models.Account;


public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);
    
    Optional<Account> findByToken(String token);
    
}
