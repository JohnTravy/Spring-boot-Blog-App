package org.travy.Springstarter.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Email(message = "invalid message")
    @NotEmpty(message = "Email missing")
    public String email;
    

    @NotEmpty(message = "password missing") 
    private String password;

    @NotEmpty(message = "firstname missing") 
    private String firstname;

    @NotEmpty(message = "lastname missing") 
    private String lastname;

    
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate date_of_birth;

    private String gender;

    private int age;

    private String photo;

    private String roles;

    @OneToMany(mappedBy = "account")
    private List<Post>posts;

    
    private String token;

    private LocalDateTime password_reset_token_expiry;

  @ManyToMany
  @JoinTable(
    name = "account_authority",
    joinColumns = {@JoinColumn(name="account_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name="authority_id", referencedColumnName = "id")}) 
    private Set<Authority> authorities = new HashSet<>(); 
}
