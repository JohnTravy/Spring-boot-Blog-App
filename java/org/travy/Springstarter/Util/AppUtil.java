package org.travy.Springstarter.Util;

import java.io.File;

public class AppUtil {
    
    public static String get_upload_path(String Filename){

        return new File("C:\\Users\\gworl\\Documents\\SpringProjects\\Springstartermvc\\src\\main\\resources\\static\\images") + "\\" +  Filename;

    }
    
}
