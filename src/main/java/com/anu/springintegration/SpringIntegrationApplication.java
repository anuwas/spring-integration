package com.anu.springintegration;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.integration.splitter.MethodInvokingSplitter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;



@SpringBootApplication
public class SpringIntegrationApplication {

	public static void main(String[] args) {
		//SpringApplication.run(SpringIntegrationApplication.class, args);
		ConfigurableApplicationContext ctx = SpringApplication.run(SpringIntegrationApplication.class, args);
		Sender sender = ctx.getBean(Sender.class);
		Scanner scanner = new Scanner(System.in);
		while(scanner.hasNext()) {
			String msgsend = scanner.next();
			Account ac = new Account();
			ac.setAccountNo("123456");
			ac.setAccountType(msgsend);
			AccountHolder ach = new AccountHolder();
			ach.setName("anu");
			ach.setAddress("USA");
			AccountHolder ach2 = new AccountHolder();
			ach2.setName("was");
			ach2.setAddress("india");
			List<AccountHolder> achList = new ArrayList<>();
			achList.add(ach);
			achList.add(ach2);
			ac.setAccountHolder(achList);
			sender.send(ac);
		}
		scanner.close();
	}
	
	@Bean
	public MessageChannel messagechannel() {
		return new DirectChannel();
	}
	
	@Splitter(inputChannel="messagechannel")
	@Bean
	public AbstractMessageSplitter messageSpilitter() {
		MethodInvokingSplitter misp = new MethodInvokingSplitter(messageModifier(),"callbySplitter");
		misp.setOutputChannel(outputChannel());
		return misp;
		
	}
	
	@Bean
	public MessageModifier messageModifier() {
		return new MessageModifier();
	}
	
	@Bean
	public MessageChannel outputChannel() {
		return new DirectChannel();
	}
	//any customization needs to do inside the service activator
	@Bean
	@ServiceActivator(inputChannel="outputChannel")
	public MessageHandler savingChannelHandler() {
		return  (Message<?> message)->System.out.println("Message after splitter "+message);
	}
	


}

