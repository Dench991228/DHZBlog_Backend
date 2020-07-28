package blog.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.entity.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword,Integer>{
	
	//根据aid来获得全部的关键词
	public List<Keyword> findByFromArticle_aid(int aid);
	
	//根据内容，返回全部的关键词
	public List<Keyword> findByContent(String content);
}
