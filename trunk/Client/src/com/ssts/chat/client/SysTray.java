package com.ssts.chat.client;
import java.awt.HeadlessException;import java.awt.Image;
 
import java.awt.MenuItem;
 
import java.awt.Panel;
 
import java.awt.PopupMenu;
 
import java.awt.SystemTray;
 
import java.awt.TrayIcon;
 
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
 
import java.awt.event.ActionListener;
 
import java.awt.image.BufferedImage;
import java.io.IOException;
 
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
 
import javax.swing.JOptionPane;
 
import javax.swing.plaf.metal.MetalIconFactory;
 
  
 
public class SysTray {
	static ChatClient frame;
	public static boolean newMessage;
	public static TrayIcon icon;
	
private static Image getIcon() throws HeadlessException {
	Image img = null;
	try {
		img = ImageIO.read(SysTray.class.getResource("/resources/icon.png"));
	} catch (IOException e2) {
		e2.printStackTrace();
	}
	
	return img;
	
	
}
 
private static PopupMenu createPopupMenu() throws
 
                                                 HeadlessException {
 
        PopupMenu menu = new PopupMenu();
 
MenuItem exit = new MenuItem("Изход");
 
        exit.addActionListener(new ActionListener() {
 
           public void actionPerformed(ActionEvent e) {
 
               System.exit(0);
 
           }
 
        });
 
        menu.add(exit);
 
return menu;
 
    }
 
public static void main(String[] args) throws Exception {
	UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
	ChatClient.readIni();
	
	icon = new TrayIcon(getIcon(),
    		 
            "SSTS Чат клиент 1.5", createPopupMenu());

    icon.addActionListener(new ActionListener() {

       public void actionPerformed(ActionEvent e) {
    	   frame.setVisible(true);
       }

    });
    
       SystemTray.getSystemTray().add(icon);
       
	
	frame = new ChatClient();
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.setResizable(false);
	frame.setIconImage(getIcon());
	frame.addWindowListener(new WindowListener() {
        public void windowClosed(WindowEvent arg0) {
        	frame.setVisible(false);
        }
        public void windowActivated(WindowEvent arg0) {
        }
        public void windowClosing(WindowEvent arg0) {
        	frame.setVisible(false);
        }
        public void windowDeactivated(WindowEvent arg0) {
        }
        public void windowDeiconified(WindowEvent arg0) {
        }
        public void windowIconified(WindowEvent arg0) {
        }
        public void windowOpened(WindowEvent arg0) {
        }
    });
	 new Thread(new Runnable() {
	      public void run() {
	      try {
			ChatClient.connection();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      }
	    }).start();     
 
    }
}