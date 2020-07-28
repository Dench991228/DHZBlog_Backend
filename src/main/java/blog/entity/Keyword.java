package blog.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="keywords")
public class Keyword implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//主键
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int akid;
	
	//关键词内容
	@Column(length=32)
	private String content;
	
	//权重
	private int weight;
	
	//属于的文章
	@ManyToOne(optional=false,fetch=FetchType.LAZY,targetEntity=Article.class)
	@JoinColumn(name="articleId", referencedColumnName="aid")
	@JsonIgnore
	private Article fromArticle;

	public int getAkid() {
		return akid;
	}

	public void setAkid(int akid) {
		this.akid = akid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public Article getFromArticle() {
		return fromArticle;
	}

	public void setFromArticle(Article fromArticle) {
		this.fromArticle = fromArticle;
	}

	@Override
	public String toString() {
		return "Keyword [akid=" + akid + ", content=" + content
				+ ", weight=" + weight + "]";
	}
	
	
}
