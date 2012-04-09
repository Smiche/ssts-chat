package com.ssts.chat.server;
import it.sauronsoftware.base64.Base64;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientListener extends Thread {
	static ArrayList<NickName> nicks = new ArrayList<NickName>();
	String nickName;
	
	private ServerDispatcher mServerDispatcher;
	private ClientInfo mClientInfo;
	private BufferedReader mIn;
	static ArrayList<String> logMsg = new ArrayList<String>();
	
	public ClientListener(ClientInfo aClientInfo,
			ServerDispatcher aServerDispatcher) throws IOException {
		mClientInfo = aClientInfo;
		mServerDispatcher = aServerDispatcher;
		Socket socket = aClientInfo.mSocket;
		mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	/**
	 * Until interrupted, reads messages from the client socket, forwards them
	 * to the server dispatcher's queue and notifies the server dispatcher.
	 */
	public void run() {
		try {
			
			Socket sockete = mClientInfo.mSocket;
			String senderIP = sockete.getInetAddress().getHostAddress();
			String senderPort = "" + sockete.getPort();
			String auth = senderIP + senderPort;
			PrintWriter sOut = new PrintWriter(new OutputStreamWriter(sockete.getOutputStream()));
			
			while (!isInterrupted()) {
				String message = mIn.readLine();
				if (message == null)
					break;
				try {
				message = Base64.decode(message);
				} catch (RuntimeException e){
					
				}
				if (message.startsWith("!")) {
					if(message.startsWith("!help")){
						sOut.println("Валидни команди са:!help , !kick <ник> , !mute <ник> , !unmute <ник> , !password <парола> ");
						sOut.flush();
					}
					if(message.startsWith("!n"))
					handleNicks(message,auth,sOut);
					
					handleCmds(message,auth);
					if(message.startsWith("!password")){
						if(message.length() > 11 && message.substring(10,message.length()).equals("passXadmin2")){
							for(int i  =0 ;i<nicks.size();i++){
								if(nicks.get(i).getIP().equals(senderIP + senderPort)){
									nicks.get(i).setAdmin();
									logMsg.add("*"+nicks.get(i).getNick()+" получи админски права!\n");
									sendNicks();
								}
							}
						}
					}
				}  else {
					
					if (message.length() != 0) {
						for (int k = 0; k < nicks.size(); k++) {
							if (nicks.get(k).getIP().equals(auth) && !nicks.get(k).isMuted()) {
								message = ClientListener.nicks.get(k).getNick()+ " : " + message;
								mServerDispatcher.dispatchMessage(mClientInfo,
										message);
								logMsg.add(message+"\n");
							}
						}
					}
				}
			}
		} catch (IOException ioex) {
			// when the connection is lost inbetween
			Socket sockete = mClientInfo.mSocket;
			String senderIP = sockete.getInetAddress().getHostAddress();
			String senderPort = "" + sockete.getPort();
			for (int i = 0; i < nicks.size(); i++) {
				if (nicks.get(i).getIP().equals(senderIP + senderPort)) {
					String message = "*" + nicks.get(i).getNick() + " напусна.";
					mServerDispatcher.dispatchMessage(mClientInfo, message);
					logMsg.add(message+"\n");
					nicks.remove(i);
					sendNicks();
				}

			}
		}

		// Communication is broken. Interrupt both listener and sender threads
		mClientInfo.mClientSender.interrupt();
		mServerDispatcher.deleteClient(mClientInfo);
	}
	private void handleNicks(String message2,String authx , PrintWriter toOut){
		nickName = message2.substring(3,message2.length());
		if(nickName.length() < 10 && !nickName.contains("<") && !nickName.contains(">") ){
		boolean found = false;
		boolean taken = false;	
		if(!found){
		for(int i = 0;i<nicks.size();i++){
			if(nicks.get(i).getNick().equals(nickName)){
				taken = true;
			}
		}
		}
		for(int i =0;i<nicks.size();i++){
			if(nicks.get(i).getIP().equals(authx) && !taken){
				mServerDispatcher.dispatchMessage(mClientInfo,"*" + nicks.get(i).getNick() + " си смени ника на :" + nickName);
				logMsg.add("*" + nicks.get(i).getNick() + " си смени ника на :" + nickName+"\n");
				nicks.get(i).setNick(nickName);
				found =true;
			}
		}

		if(!found && !taken){
			nicks.add(new NickName(nickName,authx));
			mServerDispatcher.dispatchMessage(mClientInfo,"*" + nickName + " се присъедини към чата!");
			logMsg.add("*" + nickName + " се присъедини към чата!\n");
	}
		sendNicks();
		}
	}
	public static void writeLog(){
		for(int i =0;i<logMsg.size();i++){
			try {
				ChatServer.fileWrite.write(logMsg.get(i));
				ChatServer.fileWrite.flush();
			} catch (IOException e) {
				
			}
		}
		try {
			ChatServer.fileWrite.close();
		} catch (IOException e) {
		}
	}
	private void handleCmds(String msg,String authx){
		for(int i = 0;i<nicks.size();i++){
		if(msg.startsWith("!kick") && nicks.get(i).getIP().equals(authx) && nicks.get(i).privilegies == 1){
			kickClient(msg,authx);
		} else if(msg.startsWith("!mute") &&nicks.get(i).getIP().equals(authx) && nicks.get(i).privilegies ==1){
			talkClient(msg,authx,false);
			
		} else if(msg.startsWith("!unmute")&&nicks.get(i).getIP().equals(authx) && nicks.get(i).privilegies ==1){
			talkClient(msg,authx,true);
		}
		}
	}
	private void kickClient(String messg,String authc){
		String toKick = messg.substring(6,messg.length());
		for(int i =0;i<nicks.size();i++){
			if(nicks.get(i).getNick().equals(toKick)){
				mServerDispatcher.dispatchMessage(mClientInfo,"*" + nicks.get(i).getNick() + " е премахнат от чата!");
				logMsg.add("*" + nicks.get(i).getNick() + " е премахнат от чата!"+"\n");
				nicks.remove(i);
			}
		}
		sendNicks();
	}
	private void talkClient(String messg,String authc,boolean type){
		String toMute = messg.substring(6,messg.length());
		String toUnMute = messg.substring(8,messg.length());
		for(int i =0;i<nicks.size();i++){
			if(!type){
			if(nicks.get(i).getNick().equals(toMute)){
				nicks.get(i).mute();
				mServerDispatcher.dispatchMessage(mClientInfo,"*" + nicks.get(i).getNick() + " загуби правото си да пише!");
				logMsg.add("*" + nicks.get(i).getNick() + " загуби правото си да пише!"+"\n");
			}
			} else {
			if(nicks.get(i).getNick().equals(toUnMute)){
				nicks.get(i).unmute();
				mServerDispatcher.dispatchMessage(mClientInfo,"*" + nicks.get(i).getNick() + " може да пише отново!");
				logMsg.add("*" + nicks.get(i).getNick() + " може да пише отново!"+"\n");
			}
			}
		}
		sendNicks();
	}
	private void sendNicks(){
		String listOfNicks = "";
		if(nicks.size() == 1 ){
			if(nicks.get(0).privilegies == 0){
			listOfNicks = "l<" + nicks.get(0).getNick();
			} else if(nicks.get(0).isMuted()){
				listOfNicks = "l<"+"<m>" + nicks.get(0).getNick();
			}else {
				listOfNicks = "l<"+"<a>" + nicks.get(0).getNick();
			}
		} else {
			for(int i = 0;i<nicks.size();i++){
				if(nicks.get(i).privilegies == 0 && !nicks.get(i).isMuted()){
				listOfNicks += "<" + nicks.get(i).getNick() + ">";
				} else if(nicks.get(i).isMuted()){
					listOfNicks += "<<m>" + nicks.get(i).getNick() + ">";
				} else {
					listOfNicks += "<<a>" + nicks.get(i).getNick() + ">";
				}
			}
			
		}
		mServerDispatcher.dispatchMessage(mClientInfo, listOfNicks);
	}
}
