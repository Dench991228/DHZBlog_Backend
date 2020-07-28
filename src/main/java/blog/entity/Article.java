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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="Articles")
public class Article implements Serializable {
	
	private static final long serialVersionUID = 1L;

	//文章id
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int aid;
	
	//文章题目
	@Column(length=256)
	private String title;
	
	//文章内容
	@Lob
	@Column(columnDefinition="text")
	private String content;
	
	//发布时间
	@Column(length=120)
	private String datetime;
	
	//阅读量
	private int NumRead;
	
	//一篇文章的作者，需要调试cascade
	@ManyToOne(optional=false,fetch=FetchType.LAZY,targetEntity=User.class)
	@JoinColumn(name="authorId", referencedColumnName="uid")
	@JsonIgnore
	private User author;
	
	//一篇文章下的全部评论
	@OneToMany(mappedBy="targetArticle",targetEntity=Comment.class,fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
	private Set<Comment> articleComments;
	
	//与这篇文章相关的分区信息
	@ManyToMany
	@JoinTable(name="article_partition",joinColumns=@JoinColumn(name="article_aid"),inverseJoinColumns=@JoinColumn(name="partition_pid"))
	private Set<Partition> partitions;
	
	//与这篇文章相关的标签
	@ManyToMany
	@JoinTable(name="article_tag",joinColumns=@JoinColumn(name="article_aid"),inverseJoinColumns=@JoinColumn(name="tag_tid"))
	private Set<Tag> tags;
	
	//这篇文章得到的赞
	@OneToMany(mappedBy="liked",cascade = CascadeType.REMOVE)
	private List<Like> likes;

	//这篇文章得到的收藏
	@OneToMany(mappedBy = "target", cascade = CascadeType.REMOVE)
	private List<Collection> collections;

	//关键字
	@OneToMany(targetEntity=Keyword.class,cascade=CascadeType.REMOVE,fetch=FetchType.LAZY,mappedBy="fromArticle")
	private List<Keyword> keywords;
	
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public int getNumRead() {
		return NumRead;
	}
	public void setNumRead(int numRead) {
		NumRead = numRead;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public Set<Partition> getPartitions() {
		return partitions;
	}
	public void setPartitions(Set<Partition> partitions) {
		this.partitions = partitions;
	}
	public Set<Tag> getTags() {
		return tags;
	}
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	public Set<Comment> getArticleComments() {
		return articleComments;
	}
	public void setArticleComments(Set<Comment> articleComments) {
		this.articleComments = articleComments;
	}
	
	@Override
	public String toString() {
		return "Article [aid=" + aid + ", title=" + title + ", NumRead="
				+ NumRead + "]";
	}
	public List<Keyword> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}

	public List<Like> getLikes() {
		return likes;
	}

	public void setLikes(List<Like> likes) {
		this.likes = likes;
	}

	public List<Collection> getCollections() {
		return collections;
	}

	public void setCollections(List<Collection> collections) {
		this.collections = collections;
	}
}
