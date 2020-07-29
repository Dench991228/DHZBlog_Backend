package blog.entity;

import javax.persistence.*;

@Entity
@Table(name="likes")
public class Like {

    //点赞记录的key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int lid;

    //点赞的时间
    @Column(length=120)
    String likeTime;

    //点赞的用户
    @ManyToOne(optional = false, fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name="likerId", referencedColumnName = "uid")
    User liker;

    //被点赞的文章
    @ManyToOne(optional = false, fetch = FetchType.LAZY, targetEntity = Article.class)
    @JoinColumn(name="targetId", referencedColumnName = "aid")
    Article liked;

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public String getLikeTime() {
        return likeTime;
    }

    public void setLikeTime(String likeTime) {
        this.likeTime = likeTime;
    }

    public User getLiker() {
        return liker;
    }

    public void setLiker(User liker) {
        this.liker = liker;
    }

    public Article getLiked() {
        return liked;
    }

    public void setLiked(Article liked) {
        this.liked = liked;
    }

    @Override
    public String toString() {
        return "Like{" +
                "lid=" + lid +
                ", likeTime='" + likeTime + '\'' +
                ", liker=" + liker +
                ", liked=" + liked +
                '}';
    }
}
