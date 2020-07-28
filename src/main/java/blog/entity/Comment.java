package blog.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="Comments")
public class Comment implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//评论ID，唯一确定一条评论
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int cid;
	
	//评论内容
	@Lob
	@Column(columnDefinition="text")
	private String content;
	
	//发布时间
	@Column(length=120)
	private String datetime;
	
	//是不是母评论
	private boolean isMother;
	
	//发布这条评论的用户，需要调试cascade
	@ManyToOne(optional=false,fetch=FetchType.LAZY,targetEntity=User.class)
	@JoinColumn(name="ownerId", referencedColumnName="uid")
	@JsonIgnore
	private User owner;
	
	//这条评论在哪篇文章下，需要调试cascade
	@ManyToOne(optional=false,fetch=FetchType.LAZY,targetEntity=Article.class)
	@JoinColumn(name="targetArticleId", referencedColumnName="aid")
	@JsonIgnore
	private Article targetArticle;
	
	//这条评论回复的对象，需要调试cascade
	@ManyToOne(optional=true,fetch=FetchType.LAZY,targetEntity=Comment.class)
	@JoinColumn(name="targetCommentId",referencedColumnName="cid",nullable=true)
	@JsonIgnore
	private Comment targetComment;
	
	//这条评论的回复
	@OneToMany(mappedBy="targetComment",targetEntity=Comment.class,fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	private Set<Comment> replys;
	
	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public boolean isMother() {
		return isMother;
	}

	public void setMother(boolean isMother) {
		this.isMother = isMother;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Article getTargetArticle() {
		return targetArticle;
	}

	public void setTargetArticle(Article targetArticle) {
		this.targetArticle = targetArticle;
	}

	public Comment getTargetComment() {
		return targetComment;
	}

	public void setTargetComment(Comment targetComment) {
		this.targetComment = targetComment;
	}

	public Set<Comment> getReplys() {
		return replys;
	}

	public void setReplys(Set<Comment> replys) {
		this.replys = replys;
	}

	@Override
	public String toString() {
		return "Comment [cid=" + cid + ", content=" + content
				+ ", datetime=" + datetime + "]";
	}
	
	
}
