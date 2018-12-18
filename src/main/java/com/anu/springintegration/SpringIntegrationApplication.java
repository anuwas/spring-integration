package com.anu.springintegration;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.integration.transformer.HeaderEnricher;
import org.springframework.integration.transformer.support.HeaderValueMessageProcessor;
import org.springframework.integration.transformer.support.StaticHeaderValueMessageProcessor;
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
			sender.send(msgsend);
		}
		scanner.close();
	}
	
	@Bean
	public MessageChannel messagechannel() {
		return new DirectChannel();
	}
	
	@Transformer(inputChannel="messagechannel",outputChannel="oputputchannel")
	@Bean
	public HeaderEnricher header() {
		Map<String, HeaderValueMessageProcessor<?>> mp = new HashMap<>(); 
		mp.put("key1", new StaticHeaderValueMessageProcessor<String>("saving"));
		mp.put("key2", new StaticHeaderValueMessageProcessor<String>("current"));
		HeaderEnricher headerEnricher = new HeaderEnricher(mp);
		return headerEnricher;
	}
	
	@Router(inputChannel="oputputchannel")
	@Bean
	public HeaderValueRouter headervaluerouter() {
		HeaderValueRouter headerrouter = new HeaderValueRouter("key1");
		headerrouter.setChannelMapping("saving", "savingchannel");
		headerrouter.setDefaultOutputChannel(deaultchannel());
		return headerrouter;
	}
	
	
	@Bean
	public MessageChannel oputputchannel() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel savingchannel() {
		return new DirectChannel();
	}
	
	//any customization needs to do inside the service activator
	@Bean
	@ServiceActivator(inputChannel="savingchannel")
	public MessageHandler savinghandle() {
		return  (Message<?> message)->System.out.print("saving channel "+message);
	}
	
	@Bean
	public MessageChannel deaultchannel() {
		return new DirectChannel();
	}
	
	//any customization needs to do inside the service activator
	@Bean
	@ServiceActivator(inputChannel="deaultchannel")
	public MessageHandler defaultmessageHandler() {
		return  (Message<?> message)->System.out.print("default channel "+message);
	}

}

