package blog.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.entity.Article;
import blog.entity.Comment;
import blog.entity.User;
import blog.service_frame.SocialService;

//这一部分主要是和社交相关，包括点赞、收藏、关注等
//评论并不包含在这里面
@Service
public class SocialManagement implements SocialService{
	private static final Log log = LogFactory.getLog(SocialManagement.class);
	@Autowired
	UserManagement user_management;
	@Autowired
	ArticleManagement article_management;
	@Autowired
	CommentManagement comment_management; 
	//一个用户给一篇文章点赞
	@Override
	public void like(int uid,int aid) {
		User u = this.user_management.getUser(uid);
		u.getLiked().add(this.article_management.getArticle(aid));
		this.user_management.addUser(u);
	}
	//一个用户取消一篇文章的点赞
	@Override
	public void cancelLike(int uid,int aid) {
		User u = this.user_management.getUser(uid);
		u.getLiked().remove(this.article_management.getArticle(aid));
		this.user_management.addUser(u);
	}
	
	//一个用户是否给一篇文章点赞
	@Override
	public boolean isLiked(int uid,int aid) {
		User u = this.user_management.getUser(uid);
		return u.getLiked().contains(this.article_management.getArticle(aid));
	}
	
	//一篇文章有多少人点赞
	@Override
	public int numLikes(int aid) {
		Article a = this.article_management.getArticle(aid);
		return a.getLiker().size();
	}
	
	//一个用户收藏一篇文章
	@Override
	public void collect(int uid,int aid) {
		User u = this.user_management.getUser(uid);
		Article a = this.article_management.getArticle(aid);
		u.getCollected().add(a);
		this.user_management.addUser(u);
	}
	//取消一个用户收藏一篇文章
	@Override
	public void cancelCollect(int uid ,int aid) {
		User u = this.user_management.getUser(uid);
		Article a = this.article_management.getArticle(aid);
		u.getCollected().remove(a);
		this.user_management.addUser(u);
	}
	
	//判断一个用户是否收藏一篇文章
	@Override
	public boolean isCollected(int uid, int aid) {
		User u = this.user_management.getUser(uid);
		Article a = this.article_management.getArticle(aid);
		return u.getCollected().contains(a);
	}
	
	//一个用户关注一个用户
	@Override
	public void subscribe(int cur_uid,int tar_uid) {
		User fan = this.user_management.getUser(cur_uid);
		User idol = this.user_management.getUser(tar_uid);
		fan.getIdols().add(idol);
		this.user_management.addUser(fan);
	}
	
	//取消一个用户关注一个用户
	@Override
	public void cancelSubscribe(int cur_uid,int tar_uid) {
		User fan = this.user_management.getUser(cur_uid);
		User idol = this.user_management.getUser(tar_uid);
		fan.getIdols().remove(idol);
		this.user_management.addUser(fan);
	}
	
	//判断一个用户是否关注另一个用户
	@Override
	public boolean isSubscribed(int cur_uid,int tar_uid) {
		User fan = this.user_management.getUser(cur_uid);
		User idol = this.user_management.getUser(tar_uid);
		return fan.getIdols().contains(idol);
	}
	
	//按照时间排好序
	@Override
	public List<Map<String, Object>> getMovementList(int uid) {
		User cur_user = this.user_management.getUser(uid);
		Set<User> target_users = cur_user.getIdols(); //当前用户关注的人
		List<Map<String,Object>>result = new ArrayList<>();
		for(User u:target_users) {
			log.info("用户"+cur_user+"关注了"+u);
			//此用户发表评论的行为
			Set<Comment> writtenComment = u.getWrittenComments();
			for(Comment c:writtenComment) {
				log.info("用户"+u+"发布了评论："+c);
				result.add(this.getMovementItem("Comment", u.getUid(), c.getCid()));
			}
			//此用户发布文章的行为
			List<Article> writtenArticle = u.getArticles();
			for(Article a:writtenArticle) {
				log.info("用户"+u+"撰写了："+a);
				result.add(this.getMovementItem("Write",u.getUid(),a.getAid()));
			}
		}
		result.sort(new Comparator<Map<String,Object>>(){

			@Override
			public int compare(Map<String, Object> o1,
					Map<String, Object> o2) {
				return o1.get("Time").toString().compareTo(o2.get("Time").toString())>0?-1:1;
			}
			
		});
		log.info("最终的动态列表如下"+result.toString());
		return result;
	}
	private Map<String,Object> getMovementItem(String type, int uid, int id) {
		Map<String,Object> result = new TreeMap<>(); 
		if(type.compareTo("Comment")==0) {//这是一条评论
			Comment c = this.comment_management.getComment(id);
			User sender = c.getOwner();
			Article target = c.getTargetArticle();
			result.put("UID", sender.getUid());
			result.put("AID", target.getAid());
			result.put("Time",target.getDatetime());
			result.put("TargetTitle", target.getTitle());
			result.put("Username",sender.getUsername());
			result.put("Type", "Comment");
			return result;
		}
		else if(type.compareTo("Collect")==0) {//这是一条收藏
			result.put("Type", "Collect");
		}
		else if(type.compareTo("Write")==0) {//发布文章
			result.put("Type", "Write");
		}
		else {//点赞
			result.put("Type", "Write");
		}
		User u = this.user_management.getUser(uid);
		Article a = this.article_management.getArticle(id);
		result.put("UID", u.getUid());
		result.put("AID", a.getAid());
		result.put("Time",a.getDatetime());
		result.put("TargetTitle", a.getTitle());
		result.put("Username",u.getUsername());
		return result;
	}
}
