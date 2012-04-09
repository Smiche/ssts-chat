package com.ssts.chat.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends Thread{
	public Server(){
	}
	public void run(){
	    ServerSocket serverSocket = null;
        try {
           serverSocket = new ServerSocket(ChatServer.LISTENING_PORT);
           ChatServer.textArea.setText("ChatServer работи с порт " + ChatServer.LISTENING_PORT+"\n"+"Моля затваряйте сървъра през бутона Х"+"\n"+"в горния десен край на прозореца!");
        } catch (IOException se) {
        	ChatServer.textArea.setText("Невалиден порт " + ChatServer.LISTENING_PORT);
        se.printStackTrace();
        System.exit(-1);
        }
 
        // Start ServerDispatcher thread
        ServerDispatcher serverDispatcher = new ServerDispatcher();
        serverDispatcher.start();
 
        // Accept and handle client connections
        while (true) {
           try {
               Socket socket = serverSocket.accept();
               ClientInfo clientInfo = new ClientInfo();
               clientInfo.mSocket = socket;
               ClientListener clientListener =
                   new ClientListener(clientInfo, serverDispatcher);
               ClientSender clientSender =
                   new ClientSender(clientInfo, serverDispatcher);
               clientInfo.mClientListener = clientListener;
               clientInfo.mClientSender = clientSender;
               clientListener.start();
               clientSender.start();
               serverDispatcher.addClient(clientInfo);
           } catch (IOException ioe) {
               ioe.printStackTrace();
           }
        }    	
    }

	}
