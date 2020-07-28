package blog.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.entity.Tag;

public interface TagRepository extends JpaRepository<Tag,Integer>{
	public Tag findByContent(String content);
}
