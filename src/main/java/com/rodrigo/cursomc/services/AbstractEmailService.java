package com.rodrigo.cursomc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.rodrigo.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService{

	@Value("${default.sender}")
	private String sender; 
	
	@Autowired
	private TemplateEngine templateEngine; 
	
	@Autowired
	private JavaMailSender javaMailSender; 
	
	@Override
	public void sendOrderConfirmationEmail(Pedido obj) {
		SimpleMailMessage sm = prepareSimpleMailMessageFromPedido(obj);
		sendEmail(sm);
	}

	//Método responsavel por criar o obj/estrutura/corpo que será encaminhado por email.
	protected  SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {
		SimpleMailMessage sm = new SimpleMailMessage(); 
		sm.setTo(obj.getCliente().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Pedido confirmado! Código: "+obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(obj.toString());
		return sm; 
	}
	
	@ResponseBody
	protected String htmlFromTemplatePedido(Pedido obj) {
		Context context = new Context(); 
		//Envia o obj para o template de Thymeleaf. 
		context.setVariable("pedido", obj);
		
		//Diretório do template
		return templateEngine.process("email/confirmacaoPedido", context); 
	}
	
	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido obj) {
		try {
			MimeMessage mm = prepareMimeMessageFromPedido(obj);
			sendHtmlEmail(mm);
		}
		catch(MessagingException ex) {
			sendOrderConfirmationEmail(obj); 
		}
	}

	//Cria o obj que será enviado por email em html. 
	protected MimeMessage prepareMimeMessageFromPedido(Pedido obj) throws MessagingException {
		MimeMessage  mimeMessage = javaMailSender.createMimeMessage(); 
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true); 
		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Pedido confirmado! Código: "+obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplatePedido(obj),true);
		return mimeMessage;
	}
}
