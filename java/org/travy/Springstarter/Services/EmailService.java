package org.travy.Springstarter.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.travy.Springstarter.Util.email.Emdetails;

@Service
public class EmailService {

@Autowired
private JavaMailSender javamailsender;

@Value("${spring.mail.username}")
private String sender;


public Boolean sendSimpleEmail(Emdetails emailDetails){

    try {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom((sender));
        mailMessage.setTo(emailDetails.getRecipient());
        mailMessage.setText(emailDetails.getMsgbody());
        mailMessage.setSubject(emailDetails.getSubject());
        javamailsender.send(mailMessage);
        return true;
        
    } catch (Exception e) {
        return false;
    }



}
}
