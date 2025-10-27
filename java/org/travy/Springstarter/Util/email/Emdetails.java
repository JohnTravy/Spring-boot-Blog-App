package org.travy.Springstarter.Util.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emdetails {

     private String recipient;
     private String msgbody;
     private String subject;


}
