package blog.dao;

import blog.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like,Integer> {

    //根据uid寻找一个用户点赞过的全部赞
    List<Like> findByLiker_uid(int uid);

    //根据用户id和文章id找点赞记录
    Like findByLiker_uidAndLiked_aid(int uid, int aid);
}
