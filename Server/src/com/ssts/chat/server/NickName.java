package com.ssts.chat.server;

public class NickName {
	String nick = null;
	String auth = null;
	boolean sent = false;
	int privilegies = 0;
	boolean muted = false;
	public NickName(String name,String IPport){		
		nick = name; 
		auth = IPport;
	}
	public String getNick(){
			return nick;
	}
	public void setNick(String name){
		nick = name;
	}
	public String getIP(){
		return auth;
		
	}
	public void Sent(){
		sent = true;
	}
	public void setAdmin(){
		privilegies = 1;
	}
	public void mute(){
		muted = true;
	}
	public void unmute(){
		muted = false;
	}
	public boolean isMuted(){
		return muted;
	}
	
}
