package blog.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import blog.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment,Integer>{
	//找出一篇文章下全部的母评论，按照时间降序
	public List<Comment> findByTargetArticle_aidAndIsMotherTrueOrderByDatetimeDesc(int aid);
	//找出一条评论的全部回复，按照时间降序
	public List<Comment> findByIsMotherFalseAndTargetComment_cidOrderByDatetimeDesc(int cid);
	//找出一篇文章下全部评论，包括母评论和子评论
	public List<Comment> findByTargetArticle_aid(int aid);
}
