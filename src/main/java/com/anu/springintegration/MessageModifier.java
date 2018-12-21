package com.anu.springintegration;

import java.util.ArrayList;
import java.util.Collection;

public class MessageModifier {
	//message will be split here
	public Collection<Object> callbySplitter(Account act){
		Collection<Object> col = new ArrayList<>();
		col.add(act);
		col.addAll(act.getAccountHolder());
		return col;
	}
}
