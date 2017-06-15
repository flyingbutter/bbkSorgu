/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bbksorgu;

/**
 *
 * @author muhammed
 */



import java.io.File;
import java.io.IOException;
import java.net.URL;

 
public final class driver_extract {
    
  public driver_extract() throws IOException{
     asd();  
  }
public void asd() throws IOException{
   ClassLoader classLoader = getClass().getClassLoader();
                URL resource = classLoader.getResource("resources/drivers/chromedriver.exe");
                File f = new File("Driver");
                if (!f.exists()) {
                    f.mkdirs();
                }
                File chromeDriver = new File("Driver" + File.separator + "chromedriver.exe");
                if (!chromeDriver.exists()) {
                    chromeDriver.createNewFile();
                    org.apache.commons.io.FileUtils.copyURLToFile(resource, chromeDriver);
                }
                 System.setProperty("webdriver.chrome.driver", chromeDriver.getAbsolutePath());
       // ChromeDriver driver = new ChromeDriver();
                
   }

   
}

