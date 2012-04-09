package com.ssts.chat.client;
import it.sauronsoftware.base64.Base64;

import java.awt.Color;
import java.awt.Font;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UnsupportedLookAndFeelException;

@SuppressWarnings("serial")
public class ChatClient extends JFrame implements ActionListener{
	 String input ="";
	   public static String SERVER_HOSTNAME = "213.91.131.3";
	   public static  int SERVER_PORT = 11111;
	   public static boolean soundsEnabled = false;
	   public static Socket socket;
	   static BufferedReader ini;
	   static String read;
	public static String gotten = "";
	static boolean toPrint = false;
	public static String nickList = "";
	final static JTextPane textArea = new JTextPane();
	final static JTextPane textArea_1 = new JTextPane();
	final static JTextPane textArea_2 = new JTextPane();
	 static ArrayList<String> nicks = new ArrayList<String>();
	 static ArrayList<String> log = new ArrayList<String>();
	 static int count = 0;
	public static long time = 0L;
	JButton btnIzprati = new JButton("\u0418\u0437\u043F\u0440\u0430\u0442\u0438");
    static BufferedReader in = null;
    static PrintWriter out = null;
    
    static boolean nickSet = false;
	/**
	 * GUI constructor
	 */
	public ChatClient(){		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(250,200,619,428);
		setTitle("SSTS ×àò");
		getContentPane().setLayout(null);
		
		
		
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 611, 396);
		getContentPane().add(tabbedPane); 
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("×àò", panel);
		panel.setLayout(null);
		btnIzprati.setBounds(360, 334, 89, 23);
		panel.add(btnIzprati);
		final JScrollPane scrollPane2 = new JScrollPane(textArea_1);
		scrollPane2.setBounds(10, 317, 340, 40);
		scrollPane2.setWheelScrollingEnabled(true);
		scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	
		textArea_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
			     if (key == KeyEvent.VK_ENTER) {
			    	 e.consume();
			    	 input = textArea_1.getText();
			    	 if(!input.isEmpty()){
			    	 if(nickSet == false){
			    		 textArea.setText("Ñëîæåòå ñè íèê îò íàñòðîéêèòå, çà äà ìîæåòå äà ÷àòèòå!");
			    	 }	
			    	 if(nickSet){
			    		try {
							sendToServer(input);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			    		 }
					textArea_1.setText("");
					
			     }
			     }
			     }
		});
		
		
		
		textArea_1.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		panel.add(scrollPane2);
		
		textArea.setBounds(5, 5, 426, 255);
		textArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		textArea.setEditable(false);
		textArea.setOpaque(true);
		textArea.setContentType("text/html");
		textArea.setText("Ñëîæåòå ñè íèê îò íàñòðîéêèòå, çà äà ìîæåòå äà ÷àòèòå!");
		
		final JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(0, 0, 477, 309);
		panel.add(scrollPane);

		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		btnIzprati.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input = textArea_1.getText();
				if(!input.isEmpty()){
		    	 if(nickSet == false){
		    		 textArea.setText("Ñëîæåòå ñè íèê îò íàñòðîéêèòå, çà äà ìîæåòå äà ÷àòèòå!");
		    	 } else {
		    		 try {
						sendToServer(input);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				textArea_1.setText("");
		    	 }
			}
			}
		});
		textArea_2.setForeground(Color.GREEN);
		textArea_2.setFont(new Font("Monospaced", Font.BOLD, 14));
		 textArea_2.setContentType("text/html");
		textArea_2.setBackground(Color.DARK_GRAY);
		textArea_2.setEditable(false);
		//textArea_2.setWrapStyleWord(true);
		//textArea_2.setLineWrap(true);
		
		textArea_2.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		final JScrollPane scrollPane3 = new JScrollPane(textArea_2);
		scrollPane3.setBounds(487, 0, 119, 368);
		scrollPane3.setWheelScrollingEnabled(true);
		scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel.add(scrollPane3);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Íàñòðîéêè", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u041D\u0438\u043A:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(10, 11, 38, 30);
		panel_1.add(lblNewLabel);
		
		final JTextArea textArea_3 = new JTextArea();
		textArea_3.setFont(new Font("Monospaced", Font.BOLD, 16));
		textArea_3.setWrapStyleWord(true);
		textArea_3.setLineWrap(true);
		textArea_3.setBounds(58, 11, 146, 30);
		panel_1.add(textArea_3);
		
		JLabel label = new JLabel("\u0417\u0432\u0443\u043A");
		label.setFont(new Font("Tahoma", Font.BOLD, 16));
		label.setBounds(10, 64, 42, 30);
		panel_1.add(label);
		
		final JCheckBox checkBox = new JCheckBox("");
		checkBox.setSelected(true);
		checkBox.setBounds(58, 64, 26, 30);
		panel_1.add(checkBox);
		
		JLabel lblIp = new JLabel("IP:");
		lblIp.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblIp.setBounds(10, 105, 26, 30);
		panel_1.add(lblIp);
		
		final JTextArea textArea_4 = new JTextArea();
		textArea_4.setFont(new Font("Monospaced", Font.BOLD, 16));
		textArea_4.setText(SERVER_HOSTNAME);
		textArea_4.setWrapStyleWord(true);
		textArea_4.setLineWrap(true);
		textArea_4.setBounds(46, 105, 181, 30);
		panel_1.add(textArea_4);
		
		JLabel lblPort = new JLabel("PORT:");
		lblPort.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPort.setBounds(10, 146, 58, 30);
		panel_1.add(lblPort);
		
		final JTextArea textArea_5 = new JTextArea();
		textArea_5.setWrapStyleWord(true);
		textArea_5.setLineWrap(true);
		textArea_5.setFont(new Font("Monospaced", Font.BOLD, 16));
		textArea_5.setText(""+SERVER_PORT);
		textArea_5.setBounds(80, 146, 83, 30);
		panel_1.add(textArea_5);
		
		JButton button = new JButton("\u041D\u0430\u0441\u0442\u0440\u043E\u0439");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					setSettings(textArea_3.getText(),checkBox.isSelected(),textArea_4.getText(),textArea_5.getText());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		button.setBounds(10, 334, 89, 23);
		panel_1.add(button);
		
		JLabel label_1 = new JLabel("\u041F\u0440\u0438 \u043F\u0440\u043E\u043C\u044F\u043D\u0430 \u043D\u0430 IP'\u0442\u043E \u0441\u0442\u0430\u0440\u0442\u0438\u0440\u0430\u0439\u0442\u0435 \u043F\u0440\u043E\u0433\u0440\u0430\u043C\u0430\u0442\u0430 \u043D\u0430\u043D\u043E\u0432\u043E.");
		label_1.setBounds(10, 287, 327, 36);
		panel_1.add(label_1);		
	}
 
    public static void connection() throws InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
    {  	    
    	
        try {//connect 2 serv
        	socket = new Socket(SERVER_HOSTNAME,SERVER_PORT);
           in = new BufferedReader(
               new InputStreamReader(socket.getInputStream()));
           out = new PrintWriter(
               new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException ioe) {
           ioe.printStackTrace();
        }
        try {
        	
        	
           // Read messages from the server and print them
            String message;
           while ((message=in.readLine()) != null) {
        	   if (log.size()>120){
        		   log.remove(0);
        	   }
        	   
        	   if(message.startsWith("l<")  || message.startsWith("<")){
        		   handleNicks(message);
        	   } else {
        	   
        	if(log.size()<1){
        	  log.add("<span style=color:white>"+handleSmiles(wrapText(message,true))+"</span>");
        	} else {
        		log.add("<br><span style=color:white>"+handleSmiles(wrapText(message,false))+"</span></br>");
        	}
        	  if(!SysTray.frame.isVisible())
        	  SysTray.icon.displayMessage("Ñúîáùåíèå!", "Êëèêíè òóê :)",
        	            TrayIcon.MessageType.INFO);
        	  printMessages(log);
        	   playSound("bing.wav");
        	 textArea.setCaretPosition(textArea.getDocument().getLength());
           }
           }
        } catch (IOException ioe) {
           ioe.printStackTrace();
        }
 
    }

    static void printMessages(ArrayList<String> text){
	StringBuilder build = new StringBuilder();
    	for(String s : text){
		build.append(s + "\n");
    	}
    	textArea.setText(build.toString());
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	public static void sendToServer(String toOut) throws InterruptedException{
		if(socket!=null)
			if(System.currentTimeMillis() > time + 700){
				time = System.currentTimeMillis();
			if(toOut.length() < 200 && !toOut.contains("<") && !toOut.contains(">")){
			out.println(Base64.encode(toOut));
			out.flush();
			}
			}

	}
	 public static synchronized void playSound(final String url) {
		 if(soundsEnabled)
		    new Thread(new Runnable() {
		      public void run() {
		        try {
		          Clip clip = AudioSystem.getClip();
		          AudioInputStream inputStream = AudioSystem.getAudioInputStream(ChatClient.class.getResourceAsStream("/resources/" + url));
		          clip.open(inputStream);
		          clip.start(); 
		        } catch (Exception e) {
		          System.err.println(e.getMessage());
		        }
		      }
		    }).start();
		  }
	 
	 public static void handleNicks(String msg){
		 if(msg.startsWith("l<")){
			 nicks.add(msg.substring(2));
		 } else {
			 String[] splits = msg.split("><");
			 for(int i = 0;i<splits.length;i++){
				 if(i == 0){
					 nicks.add(splits[i].substring(1)); 
				 }else  if(i == splits.length-1){
					 nicks.add(splits[i].substring(0,splits[i].length()-1));
				 } else {
					 nicks.add(splits[i]);
				 }
			 }
		 }
		 setNicks();
	 }
		 
		public static void setNicks(){
			for(int i =0;i<nicks.size();i++){
				if(i>0){
				if(!nicks.get(i).startsWith("<a>") && !nicks.get(i).startsWith("<m>")){
				nickList+="<br><font size=5 color=green>"+nicks.get(i)+"</font></br>";
				} else if(nicks.get(i).startsWith("<m>")){
					nickList+="<br><img src=\""+ChatClient.class.getResource("resources/muted.png")+ "\"><font size=5 color=black>"+nicks.get(i).substring(3,nicks.get(i).length())+"</font></br>";
				}else{
					nickList+="<br><img src=\""+ChatClient.class.getResource("resources/admin.png")+ "\"><font size=5 color=red>"+nicks.get(i).substring(3,nicks.get(i).length())+"</font></br>";
				}
				} else {
					if(!nicks.get(i).startsWith("<a>") && !nicks.get(i).startsWith("<m>")){
						nickList+="<font size=5 color=green>"+nicks.get(i)+"</font>";
						} else if(nicks.get(i).startsWith("<m>")){
							nickList+="<img src=\""+ChatClient.class.getResource("resources/muted.png")+ "\"><font size=5 color=black>"+nicks.get(i).substring(3,nicks.get(i).length())+"</font>";
						} else {
							nickList+="<img src=\""+ChatClient.class.getResource("resources/admin.png")+ "\"><font size=5 color=red>"+nicks.get(i).substring(3,nicks.get(i).length())+"</font>";
						}
				}
			}
				nicks.clear();
			textArea_2.setText(nickList);
			nickList = "";
		}
		public static void setSettings(String nick,boolean sounds,String IP,String port) throws InterruptedException{
			soundsEnabled = sounds;
			if(IP != null && port !=null){
				SERVER_HOSTNAME = IP;
				SERVER_PORT = Integer.parseInt(port);
	            try{
	            	   BufferedWriter outi = new BufferedWriter(new FileWriter("ip.ini"));
	            	   outi.write(SERVER_HOSTNAME+":" + SERVER_PORT);
	            	   outi.flush();
	               } catch(IOException e2) {
	            	   e2.printStackTrace();
	               }
	    	 if(nick!=null){
	    		 nickSet = true;
	    		 try {
	    			 if(nick.length()>3 && !nick.contains("<"))
					sendToServer("!n "+nick);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    		 }
	    }
		}
			public static void readIni(){
		       try {		    	   
		    	ini = new BufferedReader(new FileReader("ip.ini"));
		    	  read = ini.readLine();
		    	  if(read == null){
	 	               try{
    	            	   BufferedWriter outi = new BufferedWriter(new FileWriter("ip.ini"));
    	            	   outi.write(SERVER_HOSTNAME+":" + SERVER_PORT);
    	            	   outi.flush();
    	            	   readIni();
    	               } catch(IOException e2) {
    	            	   e2.printStackTrace();
    	               }
		    	  }
		    	  String[] ips = read.split(":");
		    	  SERVER_HOSTNAME = ips[0];
		    	  SERVER_PORT = Integer.parseInt(ips[1]);
		    	               }catch(IOException e){
		    	               try{
		    	            	   BufferedWriter outi = new BufferedWriter(new FileWriter("ip.ini"));
		    	            	   outi.write(SERVER_HOSTNAME+":" + SERVER_PORT);
		    	               } catch(IOException e1) {
		    	            	   e1.printStackTrace();
		    	               }
		    	           }
			}
			public static String handleSmiles(String text){
				text = text.replaceAll("\\:\\)","<img src=\""+ChatClient.class.getResource("resources/smiles/icon_smile.gif")+ "\">");
				text = text.replaceAll("\\:\\(","<img src=\""+ChatClient.class.getResource("resources/smiles/icon_sad.gif")+ "\">");
				text = text.replaceAll("\\:\\|","<img src=\""+ChatClient.class.getResource("resources/smiles/icon_neutral.gif")+ "\">");
				text = text.replaceAll("\\:D","<img src=\""+ChatClient.class.getResource("resources/smiles/icon_biggrin.gif")+ "\">");
				text = text.replaceAll("\\:S","<img src=\""+ChatClient.class.getResource("resources/smiles/icon_confused.gif")+ "\">");
				text = text.replaceAll("\\:P","<img src=\""+ChatClient.class.getResource("resources/smiles/icon_razz.gif")+ "\">");
				return text;
				
			}
			public static String wrapText(String text,boolean firstLine){
				if(firstLine){
				if(text.length() > 70){
					text = text.substring(0,70) + "<br>" +text.substring(70) + "</br>";
				}
				} else {
					if(text.length()> 70){
						text = text.substring(0,70)+"</br><br>" + text.substring(70)+ "</br>";
					}
				}
				return text;
			}
}

