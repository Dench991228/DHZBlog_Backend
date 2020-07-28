package blog.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.dao.CommentRepository;
import blog.entity.Comment;
import blog.entity.User;
import blog.service_frame.CommentService;

@Service
public class CommentManagement implements CommentService{
	@Autowired
	private CommentRepository comment_repo;
	@Autowired
	private UserManagement user_management;
	@Autowired
	private ArticleManagement article_management;
	//获取一条评论，格式已经按照要求组装好
	private Map<String,Object> getComment(Comment c){
		TreeMap<String,Object> result = new TreeMap<>();
		User writer = c.getOwner();
		List<Map<String,Object>> replies = new ArrayList<>();
		//获取评论下全部回复，并且按照时间降序
		List<Comment> reply_comments = this.comment_repo.findByIsMotherFalseAndTargetComment_cidOrderByDatetimeDesc(c.getCid());
		result.put("cid", c.getCid());
		result.put("content", c.getContent());
		result.put("uid", writer.getUid());
		result.put("name", writer.getUsername());
		result.put("email", writer.getEmail());
		result.put("time", c.getDatetime());
		result.put("inputShow", Boolean.FALSE);
		for(Comment com:reply_comments) {
			replies.add(this.getReplyComment(com));
		}
		result.put("reply", replies);
		return result;
	}
	
	//获取一条回复评论，格式已经按照要求组装好
	private Map<String,Object> getReplyComment(Comment c){
		TreeMap<String,Object> result = new TreeMap<>();
		User writer = c.getOwner();//写下回复的用户
		User replied = c.getTargetComment().getOwner();
		result.put("cid",c.getCid());
		result.put("to",replied.getUsername());
		result.put("uid", writer.getUid());
		result.put("name", writer.getUsername());
		result.put("email", writer.getEmail());
		result.put("comment", c.getContent());
		result.put("time", c.getDatetime());
		result.put("inputShow", Boolean.FALSE);
		return result;
	}
	
	//获取一篇文章下评论数量，回复评论也算
	@Override
	public int numComment(int aid) {
		return this.comment_repo.findByTargetArticle_aid(aid).size();
	}
	
	//获取一篇文章下全部评论，及其回复评论，已经组装好了
	@Override
	public ArrayList<Map<String,Object>> getAllComment(int aid){
		ArrayList<Map<String,Object>> result = new ArrayList<>();
		//获得全部母评论，按照时间降序
		List<Comment> comments = this.comment_repo.findByTargetArticle_aidAndIsMotherTrueOrderByDatetimeDesc(aid);
		for(Comment c:comments) {
			result.add(this.getComment(c));
		}
		return result;
	}
	
	//添加一条评论（回复评论不算）
	@Override
	public int addComment(int uid,int aid, String content) {
		Comment c = new Comment();
		c.setContent(content);
		c.setMother(true);
		c.setOwner(this.user_management.getUser(uid));
		c.setTargetArticle(this.article_management.getArticle(aid));
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date date = new Date(System.currentTimeMillis());
	        String cur_date = formatter.format(date);
		c.setDatetime(cur_date);
		Comment saved=this.comment_repo.save(c);
		this.comment_repo.flush();
		return saved.getCid();
	}
	
	//添加一条回复
	@Override
	public int addReply(int uid,int aid,int cid,String content) {
		Comment c = new Comment();
		c.setContent(content);
		c.setMother(false);//这是一条回复评论
		c.setOwner(this.user_management.getUser(uid));
		c.setTargetArticle(this.article_management.getArticle(aid));
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date date = new Date(System.currentTimeMillis());
	        String cur_date = formatter.format(date);
		c.setDatetime(cur_date);
		c.setTargetComment(this.comment_repo.getOne(cid));
		Comment saved = this.comment_repo.save(c);
		this.comment_repo.flush();
		return saved.getCid();
	}
	
	//删除一条评论
	@Override
	public void delComment(int cid) {
		Comment c = this.comment_repo.getOne(cid);
		this.comment_repo.delete(c);
		this.comment_repo.flush();
	}

	@Override
	public Comment getComment(int cid) {
		return this.comment_repo.getOne(cid);
	}
}
