package blog.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.entity.Article;

public interface ArticleRepository extends JpaRepository<Article,Integer>{
	
	//获得某个作者的全部文章
	public List<Article> findByAuthor_uid(int uid);
	
	//根据aid来获取文章
	public Article findByAid(int aid);
	
}
