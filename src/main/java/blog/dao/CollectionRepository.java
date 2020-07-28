package blog.dao;

import blog.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection,Integer> {

    //根据一个用户的id找他的全部收藏
    List<Collection> findByCollector_uid(int uid);

    //根据一个用户id和文章id找它有没有收藏这篇文章
    Collection findByTarget_aidAndCollector_uid(int aid,int uid);


}
