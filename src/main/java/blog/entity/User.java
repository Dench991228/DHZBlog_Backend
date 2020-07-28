package blog.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "Users")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int uid;//用户的编号
	
	@Column(length=120)
	private String username;//用户的用户名
	
	@Column(length=120)
	private String password;//用户的密码
	
	@Column(length=120)
	private String email;//用户的邮箱
	
	private boolean verified; //此用户是否验证过
	
	private String code; //此用户的md5码，用户的新邮箱生成
	
	@OneToMany(mappedBy="author",targetEntity=Article.class,fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	private List<Article> articles;//用户写过的全部文章
	
	@ManyToMany
	@JoinTable(name="Likes",joinColumns=@JoinColumn(name="user_uid"),inverseJoinColumns=@JoinColumn(name="article_aid"))
	private Set<Article> liked;//用户点过赞的文章
	
	@ManyToMany
	@JoinTable(name="Collects",joinColumns=@JoinColumn(name="user_uid"),inverseJoinColumns=@JoinColumn(name="article_aid"))
	private Set<Article> collected;//用户收藏过的文章
	
	@ManyToMany(mappedBy="idols")
	@JsonIgnore
	private Set<User> fans;//关注此用户的用户
	
	@ManyToMany
	@JoinTable(name="fan_idol",joinColumns=@JoinColumn(name="user_fan_uid"),inverseJoinColumns=@JoinColumn(name="user_idol_uid"))
	private Set<User> idols;//用户关注的用户 
	
	@OneToMany(mappedBy="owner",targetEntity=Comment.class,fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	private Set<Comment> writtenComments;//一个用户写过的全部评论
	
	@OneToMany(mappedBy="owner",targetEntity=Resource.class,fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	private Set<Resource> uploadedResources;
	
	public User() {
		super();
	}
	public User(String username,String password,String email,String phone) {
		super();
		this.username=username;
		this.password=password;
		this.email=email;
	}
	
	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<Article> getArticles() {
		return articles;
	}
	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
	
	public Set<Article> getLiked() {
		return liked;
	}
	public void setLiked(Set<Article> liked) {
		this.liked = liked;
	}
	public Set<Article> getCollected() {
		return collected;
	}
	public void setCollected(Set<Article> collected) {
		this.collected = collected;
	}
	public Set<User> getFans() {
		return fans;
	}
	public void setFans(Set<User> fans) {
		this.fans = fans;
	}
	public Set<User> getIdols() {
		return idols;
	}
	public void setIdols(Set<User> idols) {
		this.idols = idols;
	}
	
	public Set<Comment> getWrittenComments() {
		return writtenComments;
	}
	public void setWrittenComments(Set<Comment> writtenComments) {
		this.writtenComments = writtenComments;
	}
	
	
	public boolean isVerified() {
		return verified;
	}
	
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public Set<Resource> getUploadedResources() {
		return uploadedResources;
	}
	
	public void setUploadedResources(Set<Resource> uploadedResources) {
		this.uploadedResources = uploadedResources;
	}
	
	@Override
	public String toString() {
		String result="";
		result += String.format("UID: %d|", this.uid);
		result += String.format("Username: %s|", this.username);
		result += String.format("Email: %s|", this.email);
		result += String.format("Password: %s;", this.password);
		return result;
	}
}
