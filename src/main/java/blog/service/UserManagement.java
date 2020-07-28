package blog.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import blog.dao.UserRepository;
import blog.entity.User;
import blog.service_frame.UserService;
import blog.util.MailUtil;

@Service
public class UserManagement implements UserService{
	private static final Logger log = LoggerFactory.getLogger(UserManagement.class);
	//仓库
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private MailUtil mail_management;
	
	//根据uid获得用户
	@Override
	public User getUser(int uid) {
		return repo.findByUid(uid);
	}
	
	//判断邮箱是否被占用
	@Override
	public boolean isEmailExisted(String email) {
		return repo.findByEmail(email)!=null;
	}
	
	//判断用户名是否被占用
	@Override
	public boolean isUsernameExisted(String username) {
		return repo.findByUsername(username)!=null;
	}

	
	//存入新用户，返回新用户ID
	@Override
	public int addUser(User u) {
		log.info("trying to save!");
		User new_user=repo.saveAndFlush(u);
		return new_user.getUid();
	}
	
	//根据用户的邮箱来获取用户
	@Override
	public User getUserByEmail(String email) {
		return repo.findByEmail(email);
	}
	
	//根据用户名来获取用户
	@Override
	public User getUserByUsername(String name) {
		return repo.findByUsername(name);
	}
	
	@Override
	public void sendVerifyEmail(String user_email, int uid, String code)throws MailException{
		String link=String.format("http://localhost:8080/userVerify?UID=%d&Code=%s",uid, code);
		String email_text=String.format("欢迎来到01咖啡厅，这是您的用户验证连接，点击之后即可验证：%s",link );
		this.mail_management.sendSimpleEmail("2559440494@qq.com", user_email, "Greetings from 01 Cafe", email_text);
	}
	
	@Override
	public void updateUser(User u) {
		this.repo.save(u);
	}
	
	@Override
	public String getMD5Code(String user_email) {
		Random rd = new Random();
		int rand = rd.nextInt();
		String md5_rand = DigestUtils.md5DigestAsHex(String.valueOf(rand).getBytes());
		String md5_email = DigestUtils.md5DigestAsHex(user_email.getBytes());
		StringBuilder result = DigestUtils.appendMd5DigestAsHex(md5_rand.getBytes(), new StringBuilder(md5_email));
		return result.toString();
	}
}
