package com.anu.springintegration;

import java.util.List;

public class Account {
	
	private String accountNo;
	private String accountType;
	private List<AccountHolder> accountHolder;
	
	public List<AccountHolder> getAccountHolder() {
		return accountHolder;
	}
	public void setAccountHolder(List<AccountHolder> accountHolder) {
		this.accountHolder = accountHolder;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	@Override
	public String toString() {
		return "Account [accountNo=" + accountNo + ", accountType=" + accountType + ", accountHolder=" + accountHolder
				+ "]";
	}
	
	

}
