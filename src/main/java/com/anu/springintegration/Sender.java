package com.anu.springintegration;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel="messagechannel")
public interface Sender {
	public void send(Account msg);
}
