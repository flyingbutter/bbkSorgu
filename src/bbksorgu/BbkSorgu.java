/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bbksorgu;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author muhammed
 */
public class BbkSorgu {

public static String csv_file;
static WebElement asd;    
public static boolean yalın_run=false;
public static boolean xdsl_run=false;
public static String driverpath;
public static String file;
public static String fileName;
static WebElement  check;
static WebDriver driver1;
static String check_text;
static String siparis_no;
public static String order_no;
public static int index=0;
public static int maxval=0;
static String xdslpast="";
static String xdslstr="";        
static XSSFWorkbook workbook_xlsx = null;
static XSSFSheet sheet1_xlsx = null;
static HSSFWorkbook workbook_xls = null;
static HSSFSheet sheet1_xls = null;
static XSSFWorkbook wrkbook= new XSSFWorkbook();
static XSSFSheet writablesheet1=wrkbook.createSheet();  
static FileOutputStream fileOut=null;
static PrintWriter out;
/**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

      
prepare();

getfile();
int pastprog=0;              
progressbar bar=new progressbar(maxval+1);
pastprog=file_check(writablesheet1);
for(int k=pastprog;k<=maxval;k++){
    
String[] bbk = null;


            if (sheet1_xlsx!=null)
            {
                          bbk=read(sheet1_xlsx,k);
                          
            }
            else if(sheet1_xls!=null)
            {
                          bbk=read(sheet1_xls,k);
            }
            System.out.println(bbk[0]);
            System.out.println(bbk[1]);
            String[][] result = null;
            if(yalın_run==true){
          result=sorgula(bbk);
            }
            else if(xdsl_run==true){
            result=sorgula_xdsl(bbk);
            }

            write(result,k,bbk);
            
           bar.myprogressBar.setString((k+1)+"/"+(maxval+1));
                 bar.myprogressBar.setValue(k+1);

}

               wrkbook.write(fileOut);
                  wrkbook.close();
                  out.close();
                  bar.myframe.dispose();
                  driver1.quit();
                  File file = new File(csv_file);
        try{file.delete();}catch(Exception e){return;}
 
        Desktop dt = Desktop.getDesktop();
       try{ dt.open(new File(fileName));}
       catch(java.io.IOException e){
       System.exit(1);
       }
    
         

    }
    
   static void write(String[][] result,int k,String[] bbk){
       String csv_data=bbk[0];
       if(yalın_run==true){
   if(k==0)
   {   
       Row row =  CellUtil.getRow(k, writablesheet1);
        Cell my_cell =  row.createCell(0, CellType.STRING);         

        for(int y=0;y<6;y++){
        my_cell =  row.createCell(y+1, CellType.STRING);   
       csv_data+="--"+result[y][0];
       my_cell.setCellValue(result[y][0]);
       
        }
   
   }
   
   Row row =  CellUtil.getRow(k+1, writablesheet1);
  
   Cell my_cell =  row.createCell(0, CellType.STRING);         
       my_cell.setCellValue(bbk[0]);
        for(int y=0;y<6;y++){
        my_cell =  row.createCell(y+1, CellType.STRING);         
       my_cell.setCellValue(result[y][1]);
       csv_data+="--"+result[y][1];
       
        }
   
       }
       else if(xdsl_run==true){
   
   Row row =  CellUtil.getRow(k+1, writablesheet1);
  
   Cell my_cell =  row.createCell(0, CellType.STRING);         
       my_cell.setCellValue(bbk[0]);

        for(int y=0;y<6;y++){
            if(result[y][1]!=null){
        my_cell =  row.createCell(y+1, CellType.STRING);         
       my_cell.setCellValue(result[y][0]+":"+result[y][1]);
       csv_data+="--"+result[y][0]+":"+result[y][1];
            }
        }
   
   
       }
       System.out.println(csv_data);
        out.println(csv_data);
        out.flush();
    
   } 
    
   static void prepare() throws IOException{
    driver_extract mydriver=new driver_extract();


// Create a new instance of the chrome driver
        //Launch the  Website
	driver1 = new ChromeDriver();		
	
        driver1.get("http://crm.sep.ttnet.com.tr");
        
       buttons  mybutton = new buttons();      
       mybutton.showButtonDemo();
         
  
       driver1.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
       driver1.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

    WebDriverWait wait2 = new WebDriverWait(driver1, 20); 

while(yalın_run==false && xdsl_run==false){
   sleep(1);
}

if(yalın_run==true)
{
   WebElement menu1=getElementByLocator(By.xpath("//*[contains(text(), 'Sipariş Yönetimi')]"));
   asd= wait2.until( ExpectedConditions.elementToBeClickable(menu1));
   
   menu1.click();
     sleep(3);
   
     WebElement menu2=getElementByLocator(By.xpath("//*[contains(text(), 'Altyapı Kontrol')]"));

asd= wait2.until( ExpectedConditions.elementToBeClickable(menu2));

menu2.click();
     sleep(2);
     
     
    
     
     for(int i=0;i<6;i++){
     int[] myarray = {1,2,3,13,14,15};    
     WebElement menuAdsl=getElementByLocator(By.xpath("//*[@id=\"pt1:r1:1:sml1::content\"]/li[3]/ul/li["+myarray[i]+"]/label"));
     asd=wait2.until(ExpectedConditions.elementToBeClickable(menuAdsl));
     menuAdsl.click();
     sleep(0.1);
     }
     
   }
else if(xdsl_run==true)
{
   WebElement menu1=getElementByLocator(By.xpath("//*[contains(text(), 'Son Müşterilerim')]"));
   asd= wait2.until( ExpectedConditions.elementToBeClickable(menu1));
   menu1.click();
sleep(1);
   
   WebElement menu2=getElementByLocator(By.xpath("//*[@id=\"pt1:pt_t42::db\"]/table/tbody/tr[1]"));
    asd= wait2.until( ExpectedConditions.elementToBeClickable(menu2));
   menu2.click();
sleep(1);
   
   
      WebElement menu3=getElementByLocator(By.xpath("//*[contains(text(), 'ISS Transfer')]"));
    asd= wait2.until( ExpectedConditions.elementToBeClickable(menu3));
   menu3.click();
   sleep(1);
   
           WebDriver frame = wait2.until( ExpectedConditions.frameToBeAvailableAndSwitchToIt(getElementByLocator(By.xpath("//*[@title='İçerik']"))));
            sleep(1);
}
   }
   
   static String[] read(HSSFSheet sheet,int k){
       String[] cellVal=new String[2];
       String pstn="";
    Row row = sheet.getRow(k);
          Cell cell = row.getCell(0);
          cell.setCellType(CellType.STRING);
          cellVal[0]=cell.getStringCellValue();//xdsl
          
            cell = row.getCell(1);
          cell.setCellType(CellType.STRING);
         char[] isgsm=cell.getStringCellValue().toCharArray();
         if(isgsm[isgsm.length-10]=='2' || isgsm[isgsm.length-10]=='3'|| isgsm[isgsm.length-10]=='4'){
             for(int a=(isgsm.length-10);a<isgsm.length;a++){
                 pstn+=isgsm[a];
             }
          cellVal[1]=pstn;//pstn
         }
         else{
             cellVal[1]=cellVal[0];        
         }
       return cellVal;
   }
   
   static String[] read(XSSFSheet sheet,int k){
        String[] cellVal=new String[2];
       String pstn="";
    Row row = sheet.getRow(k);
          Cell cell = row.getCell(0);
          cell.setCellType(CellType.STRING);
          cellVal[0]=cell.getStringCellValue();//xdsl
          
          cell = row.getCell(1);try{
          cell.setCellType(CellType.STRING);}
          catch(java.lang.NullPointerException e){
          return cellVal;
          }
         char[] isgsm=cell.getStringCellValue().toCharArray();
         if(isgsm[isgsm.length-10]=='2' || isgsm[isgsm.length-10]=='3'|| isgsm[isgsm.length-10]=='4'){
             for(int a=(isgsm.length-10);a<isgsm.length;a++){
                 pstn+=isgsm[a];
             }
          cellVal[1]=pstn;//pstn
         }
         else{
             cellVal[1]=cellVal[0];        
         }
       return cellVal;
   }
   
   public static WebElement getElementByLocator( final By locator ) {
 // LOGGER.info( "Get element by locator: " + locator.toString() );  
  final long startTime = System.currentTimeMillis();
  Wait<WebDriver> wait = new FluentWait<WebDriver>( driver1 )
    .withTimeout(30, TimeUnit.SECONDS)
    .pollingEvery(5, TimeUnit.SECONDS)
    .ignoring( StaleElementReferenceException.class ,NoSuchElementException.class) ;
  int tries = 0;
  boolean found = false;
  WebElement we = null;
  while ( (System.currentTimeMillis() - startTime) < 91000 ) {
      tries++;
   //LOGGER.info( "Searching for element. Try number " + tries ); 
   try {
    we = wait.until( ExpectedConditions.presenceOfElementLocated( locator ) );
    found = true;
    break;
   } catch ( StaleElementReferenceException e ) {      
   // LOGGER.info( "Stale element: \n" + e.getMessage() + "\n");
   } catch(NoSuchElementException e){
      // LOGGER.info( "no such element: \n" + e.getMessage() + "\n");

   }
   
  }
  long endTime = System.currentTimeMillis();
  long totalTime = endTime - startTime;

 
  return we;
}
   
   public static void getfile(){
   
                File inp = new File(file);
         String fileNameWithOutExt = FilenameUtils.removeExtension(inp.getName());
         String my_path=fileName;

         fileName = fileName+"\\"+fileNameWithOutExt+"-sorgu sonucu.xlsx";  
         csv_file="Driver\\"+fileNameWithOutExt+"_temp.txt";
    try {
         out = new PrintWriter(new BufferedWriter(new OutputStreamWriter( new FileOutputStream(csv_file,true), "UTF-8")));
    } catch (UnsupportedEncodingException ex) {
        Logger.getLogger(BbkSorgu.class.getName()).log(Level.SEVERE, null, ex);
    } catch (FileNotFoundException ex) {
        Logger.getLogger(BbkSorgu.class.getName()).log(Level.SEVERE, null, ex);
    }

    try {
         fileOut = new FileOutputStream(fileName);
    } catch (FileNotFoundException ex) {
        Logger.getLogger(BbkSorgu.class.getName()).log(Level.SEVERE, null, ex);
    }

         String ext1 = FilenameUtils.getExtension(file); // returns "txt"
        
         if(ext1.equals("xlsx")){   
         
                OPCPackage pkg = null;
             try {
                 pkg = OPCPackage.open(new File(file));
             } catch (InvalidFormatException ex) {
                 Logger.getLogger(BbkSorgu.class.getName()).log(Level.SEVERE, null, ex);
             }

                    try {
                        //Get the workbook instance for XLS file
                        workbook_xlsx = new XSSFWorkbook(pkg);
                    } catch (IOException ex) {
                        Logger.getLogger(BbkSorgu.class.getName()).log(Level.SEVERE, null, ex);
                    }
//Get first sheet from the workbook
                 sheet1_xlsx = workbook_xlsx.getSheetAt(0);
                 maxval=sheet1_xlsx.getLastRowNum();
                  // System.out.println(maxval);
              
         }
         
         else if(ext1.equals("xls")){   
         
                NPOIFSFileSystem fs = null;
                    try {
                        fs = new NPOIFSFileSystem(new File(file));
                    } catch (IOException ex) {
                        Logger.getLogger(BbkSorgu.class.getName()).log(Level.SEVERE, null, ex);
                    }
           
                    try {
                        workbook_xls = new HSSFWorkbook(fs.getRoot(), true);
                    } catch (IOException ex) {
                        Logger.getLogger(BbkSorgu.class.getName()).log(Level.SEVERE, null, ex);
                    }

            sheet1_xls = workbook_xls.getSheetAt(0);
            maxval=sheet1_xls.getLastRowNum();
                  // System.out.println(maxval);
         }
         
   }
  
   static boolean isElementClickable(By by){     
try
{
   WebDriverWait wait = new WebDriverWait(driver1, 1);
   wait.until(ExpectedConditions.elementToBeClickable(by));
   return true;
}
catch (Exception e)
{
  return false;
}
  
  
  }
  
  static boolean isElementPresent(By by)  
 {  
               driver1.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);  
               try  
               {  
                    driver1.findElement(by);  
                   return true;  
               }  
               catch(NoSuchElementException e)  
               {  
                   return false;  
               }  
    }
  
  public static WebElement getElementByLocator( final By locator ,int time) {
 // LOGGER.info( "Get element by locator: " + locator.toString() );  
  final long startTime = System.currentTimeMillis();
  Wait<WebDriver> wait = new FluentWait<WebDriver>( driver1 )
    .withTimeout(time, TimeUnit.SECONDS)
    .pollingEvery(1, TimeUnit.SECONDS)
    .ignoring( StaleElementReferenceException.class ,NoSuchElementException.class) ;
  int tries = 0;
  boolean found = false;
  WebElement we = null;
  while ( (System.currentTimeMillis() - startTime) < 91000 ) {
      tries++;
   //LOGGER.info( "Searching for element. Try number " + tries ); 
   try {
    we = wait.until( ExpectedConditions.presenceOfElementLocated( locator ) );
    found = true;
    break;
   } catch ( StaleElementReferenceException e ) {      
   // LOGGER.info( "Stale element: \n" + e.getMessage() + "\n");
   } catch(NoSuchElementException e){
      // LOGGER.info( "no such element: \n" + e.getMessage() + "\n");

   }
   
  }
  long endTime = System.currentTimeMillis();
  long totalTime = endTime - startTime;

 
  return we;
}
  
  public static String[][] sorgula_xdsl(String[] bbk){
   String[][] text = new String[6][2];
   WebDriverWait wait3 = new WebDriverWait(driver1, 20); 
   
   
             WebElement xdsltxt=getElementByLocator(By.xpath("//*[@id=\"r1:0:r1:0:it2::content\"]"));
            asd=wait3.until(ExpectedConditions.elementToBeClickable(xdsltxt));
            xdsltxt.clear();
            xdsltxt.sendKeys(bbk[0]);
            
            WebElement pstntxt=getElementByLocator(By.xpath("//*[@id=\"r1:0:r1:0:it1::content\"]"));
            asd=wait3.until(ExpectedConditions.elementToBeClickable(pstntxt));
            pstntxt.clear();
            pstntxt.sendKeys(bbk[1]);
            

            sleep(0.5);
   
   WebElement kontrolButon=getElementByLocator(By.xpath("//*[@id=\"r1:0:r1:0:cb1\"]"));
            asd=wait3.until(ExpectedConditions.elementToBeClickable(kontrolButon));
            
            kontrolButon.click();
            sleep(3);
         
            WebElement satır01 = null;
             long totalExecutionTime = 6000;
             long startTime = System.currentTimeMillis();
     while(!isElementClickable(By.xpath("//*[@id=\"d1::msgDlg::close\"]"))&&!isElementPresent(By.xpath("//*[@id=\"r1:0:r1:0:ol12\"]/td[2]"))&&(System.currentTimeMillis() - startTime < totalExecutionTime))
     {
     sleep(0.5);
         System.out.print("-");
     }
         WebDriverWait wait = new WebDriverWait(driver1, 2); 

           if(isElementClickable(By.xpath("//*[@id=\"d1::msgDlg::close\"]")))
           {  
              WebElement tamambuton=getElementByLocator(By.xpath("//*[@id=\"d1::msgDlg::close\"]"));
            tamambuton.click();
            sleep(1);
               text[0][1]="Belirtilen ADSL no'ya ait herhangi bir kayıt bulunamadı";
               xdslstr="";
               System.out.println("uyarı bulundu");
               return text;
               }
           WebElement xdslno;
         try{  satır01=getElementByLocator(By.xpath("//*[@id=\"r1:0:r1:0:ol12\"]/td[2]"),3);
                xdslno=getElementByLocator(By.xpath("//*[@id=\"r1:0:r1:0:plam2\"]/td[2]"),3);
                
           }
           catch(org.openqa.selenium.TimeoutException e){
               text[0][1]="hata";
               xdslstr="";
               System.out.println("timeout");
               return text;
               
           }            
           
       try{ xdslno=getElementByLocator(By.xpath("//*[@id=\"r1:0:r1:0:plam2\"]/td[2]"),3);
                   }catch(org.openqa.selenium.TimeoutException e){
                    text[0][1]="hata";
               xdslstr="";
               System.out.println("timeout");
               return text;
                   }
                   
                   
          
                   xdslstr=xdslno.getAttribute("innerText");
            System.out.println(xdslstr+":"+xdslpast);
           if(!(xdslpast.equals("") && xdslstr.equals("")))
              
               while(xdslpast.equals(xdslstr))
               {System.out.print(".");
                   sleep(1);
          try{ xdslno=getElementByLocator(By.xpath("//*[@id=\"r1:0:r1:0:plam2\"]/td[2]"),3);
           xdslstr=xdslno.getAttribute("innerText");}
          catch(org.openqa.selenium.TimeoutException e){
           text[0][1]="hata";
               xdslstr="";
               System.out.println("timeout");
               return text;
          }
               
               }
           
           satır01=getElementByLocator(By.xpath("//*[@id=\"r1:0:r1:0:ol12\"]/td[2]"));
                  WebElement satır02=getElementByLocator(By.xpath("//*[@id=\"r1:0:r1:0:plam1\"]/td[2]"));
                 String result1=satır01.getAttribute("innerText");
                 String result2=satır02.getAttribute("innerText");
 
                  text[0][0]=result1;
                  text[0][1]=result2;
             System.out.println(result1);
             System.out.println(result2);
             boolean found=true;
             for(int p=1;p<6;p++){
                 if(found!=true)
                     continue;
                 result1="";
                 result2="";
               WebElement satır1,satır2;
               if(!isElementPresent(By.xpath("//*[@id=\"r1:0:r1:0:ol12j_id_"+p+"\"]/td[2]")))
               continue;
               
               //*[@id="r1:0:r1:0:plam2"]/td[2]
                
               satır1=driver1.findElement(By.xpath("//*[@id=\"r1:0:r1:0:ol12j_id_"+p+"\"]/td[2]"));
                  
                  //*[@id="r1:0:r1:0:ol12j_id_1"]/td[2]
                  //*[@id="r1:0:r1:0:ol12j_id_2"]/td[2]
                  
                  
                 satır2=driver1.findElement(By.xpath("//*[@id=\"r1:0:r1:0:plam1j_id_"+p+"\"]/td[2]"));

                 //*[@id="r1:0:r1:0:plam1j_id_1"]/td[2]/span
                  //*[@id="r1:0:r1:0:plam1j_id_2"]/td[2]/span
                  result1=satır1.getAttribute("innerText");                  
                
                  
                  
                  result2=satır2.getAttribute("innerText");            
                             
            
                text[p][0]=result1;
                text[p][1]=result2;
                     
   }
            
            xdslpast=xdslstr;
            System.out.println(xdslpast);
       return text;
   }
   
   
   public static String[][] sorgula(String[] bbk){
   String[][] text=new String[6][2];
       WebDriverWait wait3 = new WebDriverWait(driver1, 10); 
    By blockingpane=By.xpath("/html/body/div[3]");

       if(isElementClickable(By.xpath("/html/body/div[3]")))
{

    wait3.until( ExpectedConditions.invisibilityOfElementLocated(blockingpane));

}
wait3.until( ExpectedConditions.invisibilityOfElementLocated(blockingpane));

          WebElement adresButon=getElementByLocator(By.xpath("//*[@id=\"pt1:r1:1:cmdAddress\"]"));
            asd=wait3.until(ExpectedConditions.elementToBeClickable(adresButon));
try{           
    adresButon.click();}
catch(Exception e){
 sleep(4);
            wait3.until( ExpectedConditions.invisibilityOfElementLocated(blockingpane));
            adresButon=getElementByLocator(By.xpath("//*[@id=\"pt1:r1:1:cmdAddress\"]")); 
            asd=wait3.until(ExpectedConditions.elementToBeClickable(adresButon));
                adresButon.click();
}
            sleep(1);
   
            WebDriver frame = wait3.until( ExpectedConditions.frameToBeAvailableAndSwitchToIt(getElementByLocator(By.xpath("//*[@title='İçerik']"))));
            sleep(1);
            
            
            WebElement bbkfield=getElementByLocator(By.xpath("//*[@id=\"condoUnitSearchUserInput::content\"]"));
            asd=wait3.until(ExpectedConditions.elementToBeClickable(bbkfield));
            bbkfield.sendKeys(bbk[0]);

            
            
            WebElement aramabuton=getElementByLocator(By.xpath("//*[@id=\"cmdFindByCondoUnitId\"]"));
            asd=wait3.until(ExpectedConditions.elementToBeClickable(aramabuton));
            aramabuton.click();
            sleep(1);
               WebElement check=getElementByLocator(By.xpath("//*[@id=\"cmdFindByCondoUnitId\"]"));
                      asd=wait3.until(ExpectedConditions.elementToBeClickable(check));

          if( !getElementByLocator(By.xpath("//*[@id=\"soc2::content\"]"),2).isSelected())
           {
              try{        WebDriverWait waity = new WebDriverWait(driver1, 1); 

               System.out.println("hata");
                text[0][1]="Belirtilen bbk ya ait herhangi bir adres bulunamadı";
WebElement hata=getElementByLocator(By.xpath("//*[@id=\"d1::msgDlg::close\"]"));
            asd=waity.until(ExpectedConditions.elementToBeClickable(hata));
            hata.click();
            
            WebElement close=getElementByLocator(By.xpath("//*[@id=\"commandButton1\"]"));
            asd=waity.until(ExpectedConditions.elementToBeClickable(close));
            close.click();
            
            return text;}
              catch(Exception e){}
           }
               
            Select kullanım=new Select(getElementByLocator(By.xpath("//*[@id=\"soc6::content\"]")));
            kullanım.selectByVisibleText("Ev");
            
            
             
             
            WebElement kaydetbuton=getElementByLocator(By.xpath("//*[@id=\"cbManageAddres_SaveAddres\"]"));
            asd=wait3.until(ExpectedConditions.elementToBeClickable(kaydetbuton));
            kaydetbuton.click();
            
                    driver1.switchTo().defaultContent();
                    sleep(1);
             
   
wait3.until( ExpectedConditions.invisibilityOfElementLocated(blockingpane));

WebElement adreskaldır=getElementByLocator(By.xpath("//*[@id=\"pt1:r1:1:pgl5\"]")); 
 asd=wait3.until(ExpectedConditions.elementToBeClickable(adreskaldır));
 
 
 wait3.until( ExpectedConditions.invisibilityOfElementLocated(blockingpane));
            
 WebElement kontrolbuton=getElementByLocator(By.xpath("//*[@id=\"pt1:r1:1:cb1\"]"));  
              asd=wait3.until(ExpectedConditions.elementToBeClickable(kontrolbuton));
           try{
              kontrolbuton.click();}
           catch(Exception e){
           sleep(4);
            wait3.until( ExpectedConditions.invisibilityOfElementLocated(blockingpane));
            kontrolbuton=getElementByLocator(By.xpath("//*[@id=\"pt1:r1:1:cb1\"]"));  
              asd=wait3.until(ExpectedConditions.elementToBeClickable(kontrolbuton));
                kontrolbuton.click();
           }
           
           
               sleep(1);
             String result1="";
             String result2="";
             //*[@id="pt1:r1:1:plam2"]/td[2]/span
              
             WebElement bbkno=getElementByLocator(By.xpath("//*[@id=\"pt1:r1:1:plam2\"]/td[2]"));
                String strbbk=bbkno.getAttribute("innerText");
                System.out.println(strbbk);
                System.out.println(bbk[0]);
                while(!strbbk.contains(bbk[0])){
                sleep(1);
                    System.out.println("-");
                    try{ 
                        bbkno=getElementByLocator(By.xpath("//*[@id=\"pt1:r1:1:plam2\"]/td[2]"));
                 strbbk=bbkno.getAttribute("innerText");
                    }catch(Exception e){
                    sleep(5);
                    bbkno=getElementByLocator(By.xpath("//*[@id=\"pt1:r1:1:plam2\"]/td[2]"));
                 strbbk=bbkno.getAttribute("innerText");
                    }
                }

                  WebElement satır01=getElementByLocator(By.xpath("//*[@id=\"pt1:r1:1:ol12\"]/td[2]"));
                  WebElement satır02=getElementByLocator(By.xpath("//*[@id=\"pt1:r1:1:plam1\"]/td[2]"));
                  result1=satır01.getAttribute("innerText");
                  result2=satır02.getAttribute("innerText");
 
                  text[0][0]=result1;
                  text[0][1]=result2;
             System.out.println(result1);
             System.out.println(result2);
             for(int p=1;p<6;p++){
                 result1="";
                 result2="";
               
                  WebElement satır1=getElementByLocator(By.xpath("//*[@id=\"pt1:r1:1:ol12j_id_"+p+"\"]/td[2]"));
                  WebElement satır2=getElementByLocator(By.xpath("//*[@id=\"pt1:r1:1:plam1j_id_"+p+"\"]/td[2]"));
                  
                  
                  result1=satır1.getAttribute("innerText");                  
                
                  
                  
                  result2=satır2.getAttribute("innerText");            
                             
            
                text[p][0]=result1;
                text[p][1]=result2;
                     
   }
                    
                    
       return text;
   
   }
   
   public static void sleep(double timesec){
     try {
        Thread.sleep((long) (timesec*1000));
    } catch (InterruptedException ex) {
        Logger.getLogger(BbkSorgu.class.getName()).log(Level.SEVERE, null, ex);
    }
   }
   
   public static int file_check( XSSFSheet writablesheet1){
int s=0;
try  {
    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csv_file), "UTF8"));
    String line;
    int r=1;
    while ((line = br.readLine()) != null) {
    String[] line_parts = line.split("--");
    int c=0;  
    Row rows = CellUtil.getRow(r, writablesheet1);   
    for (int h=0;h<line_parts.length;h++) {
          
        Cell my_cell =  rows.createCell( c , CellType.STRING);
        my_cell.setCellValue(line_parts[h]);
    c++;   
    }
    r++;
    s++;
    }
}catch(IOException e){
return 0;
}
return s;
}

}
