/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bbksorgu;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author muhammed
 */
public class buttons {
 
     private JFrame mainFrame;
   private JLabel headerLabel;
   private JLabel statusLabel;
   private JPanel controlPanel;
     JFileChooser chooser;
   String choosertitle;
   
  
   public buttons(){
      prepareGUI();
      
   }


   public void prepareGUI(){
       
       
       
      mainFrame = new JFrame("Sorgulamak istediğiniz dosyayı seçin.");
      mainFrame.setSize(550,300);

      mainFrame.setLayout(new GridLayout(3, 1));
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });    
      headerLabel = new JLabel("", JLabel.CENTER);        
      statusLabel = new JLabel("",JLabel.CENTER);    

      statusLabel.setSize(450,100);
      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());

      mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(statusLabel);
      mainFrame.setVisible(true);  
   }
    
   void showButton(){
        headerLabel.setText("devam ettir"); 

       JButton button3=new JButton("devam et");
    button3.addActionListener(new ActionListener() {
          
          public void actionPerformed(ActionEvent e) {


          }
      });
   }
   void showButtonDemo(){

      headerLabel.setText("dosya seçin"); 

      //resources folder should be inside SWING folder.

      
      JButton mybutton = new JButton("Yalın altyapı sorgusunu başlat"); 
      JButton xdslbutton = new JButton("Xdsl ile altyapı sorgusunu baslat");
      JButton button2 =new JButton("dosya seç");
      
      
      
/*
      mybutton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            statusLabel.setText("Ok Button clicked.");
         }          
      });
*/
     mybutton.addActionListener(new ActionListener() {
          
          public void actionPerformed(ActionEvent e) {
              BbkSorgu.yalın_run=true;
               mainFrame.dispose();

          }
      });
     
      xdslbutton.addActionListener(new ActionListener() {
          
          public void actionPerformed(ActionEvent e) {
              BbkSorgu.xdsl_run=true;
               mainFrame.dispose();

          }
      });
     button2.addActionListener(new ActionListener() {
          
               public void actionPerformed(ActionEvent e) {
    int result;
                   try {
                       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                   } catch (ClassNotFoundException ex) {
                       Logger.getLogger(buttons.class.getName()).log(Level.SEVERE, null, ex);
                   } catch (InstantiationException ex) {
                       Logger.getLogger(buttons.class.getName()).log(Level.SEVERE, null, ex);
                   } catch (IllegalAccessException ex) {
                       Logger.getLogger(buttons.class.getName()).log(Level.SEVERE, null, ex);
                   } catch (UnsupportedLookAndFeelException ex) {
                       Logger.getLogger(buttons.class.getName()).log(Level.SEVERE, null, ex);
                   }

    chooser = new JFileChooser(); 
FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", "xls", "xlsx");
 chooser.setFileFilter(filter);

//chooser.addChoosableFileFilter(new FileNameExtensionFilter("XLS files", "xls"));
  //  chooser.addChoosableFileFilter(new FileNameExtensionFilter("XLSX files", "xlsx"));


    chooser.setFileFilter(filter);
    chooser.setCurrentDirectory(new java.io.File("."));
    chooser.setDialogTitle(choosertitle);
    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    //
    // disable the "All files" option.
    //
    chooser.setAcceptAllFileFilterUsed(false);
    //    
    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
      System.out.println("getCurrentDirectory(): " 
         +  chooser.getCurrentDirectory());
      System.out.println("getSelectedFile() : " 
         +  chooser.getSelectedFile());
      }
    else {
      System.out.println("No Selection ");
      }
    BbkSorgu.file=chooser.getSelectedFile().toString();
    BbkSorgu.fileName=chooser.getCurrentDirectory().toString();
     
               }

      });
     

      controlPanel.add(mybutton);
      controlPanel.add(button2);
      controlPanel.add(xdslbutton);
      mainFrame.setVisible(true);  
   }
  
}
