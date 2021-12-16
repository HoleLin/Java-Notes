package com.holelin.sundry.test;

import cn.hutool.core.net.NetUtil;
import com.holelin.sundry.utils.security.GoogleAuthenticator;
import org.junit.Test;

/* 
 * Not really a unit test- but it shows usage 
 */  
public class GoogleAuthTest {  
      
    @Test  
    public void genSecretTest() {  
        String secret = GoogleAuthenticator.generateSecretKey();
        String url = GoogleAuthenticator.getQRBarcodeURL("testuser", "testhost", secret);  
        System.out.println("Please register " + url);  
        System.out.println("Secret key is " + secret);  
    }  
      
    // Change this to the saved secret from the running the above test.   
    static String savedSecret = "F6EUJJMYK7GDC4KI";  
      
    @Test  
    public void authTest() {  
        // enter the code shown on device. Edit this and run it fast before the code expires!  
        long code = 349394;  
        long t = System.currentTimeMillis();  
        GoogleAuthenticator ga = new GoogleAuthenticator();  
        ga.setWindowSize(5); //should give 5 * 30 seconds of grace...  
        boolean r = ga.check_code(savedSecret, code, t);  
        System.out.println("Check code = " + r);  
    }

    @Test
    public void authTest2() {
        System.out.println(NetUtil.localIpv4s());
    }
}  