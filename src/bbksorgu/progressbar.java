/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bbksorgu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

/**
 *
 * @author muhammed
 */
public class progressbar {
      JFrame myframe = new JFrame("SORGU");
      JProgressBar myprogressBar = new JProgressBar();
    public progressbar(int maxval){
showbar(maxval);
}
   void showbar(int maxval){
    myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container content = myframe.getContentPane();   
    myprogressBar.setMaximum(maxval);
    myprogressBar.setValue(0);
    myprogressBar.setStringPainted(true);
    myprogressBar.setForeground(new Color(0,153,0));
    TitledBorder border = BorderFactory.createTitledBorder("sorgulanan");
    myprogressBar.setBorder(border);
    content.add(myprogressBar, BorderLayout.NORTH);
    myframe.setSize(500, 100);
    myframe.setVisible(true);
  
   }
}
