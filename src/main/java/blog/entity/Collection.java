package blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="collection")
public class Collection implements Serializable {

    //收藏记录的ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int clid;

    //加入收藏的时间
    @Column(length=120)
    String collectionTime;

    //收藏这篇文章的人
    @ManyToOne(optional = false,fetch = FetchType.LAZY,targetEntity = User.class)
    @JoinColumn(name="collectorId",referencedColumnName = "uid")
    User collector;

    //目标文章
    @ManyToOne(optional = false, fetch = FetchType.LAZY, targetEntity = Article.class)
    @JoinColumn(name="articleId", referencedColumnName = "aid")
    @JsonIgnore
    Article target;

    public int getClid() {
        return clid;
    }

    public String getCollectionTime() {
        return collectionTime;
    }

    public User getOwner() {
        return collector;
    }

    public Article getTarget() {
        return target;
    }

    public void setClid(int clid) {
        this.clid = clid;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }

    public void setOwner(User owner) {
        this.collector = owner;
    }

    public void setTarget(Article target) {
        this.target = target;
    }
}
