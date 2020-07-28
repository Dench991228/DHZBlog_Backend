package blog.controller;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import blog.entity.User;
import blog.service.ArticleManagement;
import blog.service.UserManagement;

@RestController
public class UserController {
	private static final Log log = LogFactory.getLog(UserController.class);
	private final UserManagement user_management;
	private final ArticleManagement article_management;

	@Autowired
	public UserController(UserManagement user_management, ArticleManagement article_management) {
		this.user_management = user_management;
		this.article_management = article_management;
	}

	//获得用户信息（个人信息修改界面）
	@RequestMapping("/profile")
	public Map<String,String> sendProfile(@RequestParam("UID") int uid){
		User u = this.user_management.getUser(uid);
		TreeMap<String,String> result = new TreeMap<>();
		result.put("username", u.getUsername());
		result.put("email", u.getEmail());
		return result;
	}
	//检查这个邮箱是不是已经被用过了
	@RequestMapping("/checkemail")
	public Map<String,Boolean> containEmail(@RequestParam("email") String email) {
		log.info("recieved:email="+email);
		Map<String,Boolean> result = new TreeMap<>();
		if(email.compareTo("")!=0&&user_management.isEmailExisted(email)) {
			result.put("email",Boolean.TRUE);
		}
		else {
			result.put("email", Boolean.FALSE);
		}
		return result;
	}
	
	//检查这个用户名是不是被用过了
	@RequestMapping("/checkname")
	public Map<String,Object> containName(@RequestParam("name")String name){
		TreeMap<String,Object> result = new TreeMap<>();
		User u = this.user_management.getUserByUsername(name);
		if(u==null) {
			result.put("name", Boolean.FALSE);
			return result;
		}
		else {
			result.put("name", Boolean.TRUE);
			return result;
		}
	}
	
	//提交注册表单
	@RequestMapping("/register")
	public Map<String,Object> register(@RequestParam("email") String email,@RequestParam("password") String password) {
		Map<String,Object>result = new TreeMap<>();
		User u = new User();
		//没有邮箱，就用电话作为默认的用户名
		String username=email;
		u.setUsername(username);
		u.setEmail(email);
		u.setPassword(password);
		u.setVerified(false);
		String code = this.user_management.getMD5Code(email);
		u.setCode(code);
		int new_uid = user_management.addUser(u);
		if(email.compareTo("")!=0){
			this.user_management.sendVerifyEmail(email, new_uid,code);
		}
		result.put("uid", new_uid);
		result.put("registered",true);
		result.put("username",username);
		return result;
	}
	
	//提交登录表单
	@RequestMapping("/login")
	public Map<String,Object> login(@RequestParam("name") String name,@RequestParam("password") String password){
		Map<String,Object> result = new TreeMap<>();
		User u = this.user_management.getUserByEmail(name);
		if(u==null) {
			result.put("validation",Boolean.FALSE);
			return result;
		}
		if(u.getPassword().compareTo(password)==0&&u.isVerified()) {//密码正确，并且已经验证过
			result.put("validation",Boolean.TRUE);
			result.put("UID", u.getUid());
			result.put("name",u.getUsername());
			return result;
		}
		result.put("validation", Boolean.FALSE);//密码错误
		return result;
	}
	
	//提交更改个人信息表单
	@RequestMapping("/changeinfo")
	public Map<String,Object> changeInfo(
			@RequestParam("UID") int uid,
			@RequestParam("email") String email,
			@RequestParam("username") String username,
			@RequestParam("password_old") String password_old,
			@RequestParam("password_new") String password_new
			){
		Map<String,Object> result = new TreeMap<>();
		User u = this.user_management.getUser(uid);
		if(password_old.compareTo(u.getPassword())!=0) {//密码错误，返回修改失败以及原来的数据
			result.put("validation", Boolean.FALSE);
			result.put("email_new", u.getEmail());
			result.put("username_new", u.getUsername());
			return result;
		}
		email = email.compareTo("")==0?u.getEmail():email;
		username = username.compareTo("")==0?u.getUsername():username;
		password_new = password_new.compareTo("")==0? u.getPassword():password_new;
		u.setEmail(email);
		u.setUsername(username);
		u.setPassword(password_new);
		this.user_management.addUser(u);
		result.put("validation", Boolean.TRUE);
		result.put("email_new", u.getEmail());
		result.put("username_new", u.getUsername());
		return result;
	}
	@GetMapping("/userVerify")
	public ModelAndView verifyUser(@RequestParam("UID")int uid, @RequestParam("Code")String code) {
		User u = this.user_management.getUser(uid);
		if(u.getCode().compareTo(code)==0) {
			u.setVerified(true);
			this.user_management.updateUser(u);
		}
		else {
			
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:");
		return mv;
	}
}
