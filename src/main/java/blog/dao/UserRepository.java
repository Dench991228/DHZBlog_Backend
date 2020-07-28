package blog.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import blog.entity.User;

public interface UserRepository extends JpaRepository<User,Integer>{
	
	//根据用户UID来获取此人的信息
	public User findByUid(int uid);
	
	//根据用户名来查看有没有这个用户，用来进行重复判断
	public User findByUsername(String new_username);
	
	//根据email来获得用户
	public User findByEmail(String email);
}

