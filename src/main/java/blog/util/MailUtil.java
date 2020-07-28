package blog.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailUtil {
	@Autowired
	private JavaMailSender sender;
	public void sendSimpleEmail(String from,String reciever, String subject, String content) throws MailException{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setSubject(subject);
		message.setText(content);
		message.setTo(reciever);
		this.sender.send(message);
	}
}
