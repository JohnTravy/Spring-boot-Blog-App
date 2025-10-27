package org.travy.Springstarter.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/")
public class HomeRestController {
    
Logger logger = LoggerFactory.getLogger(HomeRestController.class);

   @GetMapping("/")
   public String home(){

    logger.debug("This is a test error log");
    return "sample response";

   }

  }

 

  


