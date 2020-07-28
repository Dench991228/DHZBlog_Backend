package blog.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="Partitions")
public class Partition implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int pid;
	
	@Column(length=120)
	private String name;
	
	@Column(length=120)
	private String iconUrl;
	
	@ManyToMany(mappedBy="partitions")
	@JsonIgnore
	private Set<Article> containedArticles;
	
	public Partition() {
		super();
	}
	public Partition(String name,String url) {
		super();
		this.name=name;
		this.iconUrl=url;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public Set<Article> getContainedArticles() {
		return containedArticles;
	}
	public void setContainedArticles(Set<Article> containedArticles) {
		this.containedArticles = containedArticles;
	}
	
}
