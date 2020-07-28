package blog.service_frame;

import blog.entity.User;

public interface UserService {
	
	//把这个用户加入用户列表，返回uid
	public int addUser(User u);
	
	//根据uid获得用户，可能返回null
	public User getUser(int uid);
	
	//根据邮箱获得用户，可能返回null
	public User getUserByEmail(String email);
	
	//根据用户名返回用户，可能返回null
	public User getUserByUsername(String username);
	
	//更新这个用户的信息
	public void updateUser(User u);
	
	//判断这个邮箱是不是被用过
	public boolean isEmailExisted(String email);

	//判断这个用户名是不是被用过
	public boolean isUsernameExisted(String username);
	
	//发送验证邮件
	public void sendVerifyEmail(String user_email, int uid, String Code);
	
	//根据用户的邮箱和一个随机数生成验证用的md5
	public String getMD5Code(String user_email);
}
