package com.ssts.chat.server;

import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
 
public class ChatServer extends JFrame
{
    public static int LISTENING_PORT = 80;
    public static String fileName = "";
    static JTextArea textArea = new JTextArea();
    static Server serv;
    static BufferedWriter fileWrite;
    static DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    static Date date = new Date();
	public ChatServer() {
		getContentPane().setLayout(null);
		
		textArea.setBounds(10, 81, 287, 55);
		setBounds(290,60,310,197);
		getContentPane().add(textArea);
		setTitle("SSTS Чат Сървър");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final JTextArea textArea_1 = new JTextArea();
		textArea_1.setFont(new Font("Monospaced", Font.PLAIN, 12));
		textArea_1.setText("8000");
		textArea_1.setBounds(10, 44, 169, 26);
		getContentPane().add(textArea_1);
		textArea.setEditable(false);
		
		JButton btnNewButton = new JButton("\u041F\u0443\u0441\u043D\u0438");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LISTENING_PORT = Integer.parseInt(textArea_1.getText());
				serv = new Server();
				serv.start();
				
			}
		});
		btnNewButton.setBounds(189, 47, 89, 23);
		getContentPane().add(btnNewButton);
		
		JLabel label = new JLabel("\u041F\u043E\u0440\u0442");
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
		label.setBounds(10, 19, 123, 26);
		getContentPane().add(label);
	}
 
    public static void main(String[] args)
    {	
    	
    	fileName = dateFormat.format(date);
        try {
        	String curDir = System.getProperty("user.dir");
        	boolean dirMake = (new File(curDir+"\\Logs\\")).mkdir();
        	if(dirMake){
        	}
			fileWrite = new BufferedWriter(new FileWriter(curDir+"\\Logs\\"+fileName+"log.txt"));
			fileWrite.write("Chat log:"+fileName+"\n");
			fileWrite.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	ChatServer frame = new ChatServer();
    	frame.setVisible(true);
    	frame.setResizable(false);
    	frame.addWindowListener(new WindowListener() {
            public void windowClosed(WindowEvent arg0) {
            	ClientListener.writeLog();
            	System.exit(-1);
            }
            public void windowActivated(WindowEvent arg0) {
            }
            public void windowClosing(WindowEvent arg0) {
            	ClientListener.writeLog();
            	System.exit(-1);
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
    }
        
}
 