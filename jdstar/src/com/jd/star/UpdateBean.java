package com.jd.star;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class UpdateBean {
	private  HashMap<String,String>openidSet;
	private ConcurrentHashMap<String, ArrayList<String>> openidMsgSet;
	public HashMap<String, String> getOpenidSet() {
		return openidSet;
	}
	public void setOpenidSet(HashMap<String, String> openidSet) {
		this.openidSet = openidSet;
	}
	public ConcurrentHashMap<String, ArrayList<String>> getOpenidMsgSet() {
		return openidMsgSet;
	}
	public void setOpenidMsgSet(ConcurrentHashMap<String, ArrayList<String>> openidMsgSet) {
		this.openidMsgSet = openidMsgSet;
	}
	
}
