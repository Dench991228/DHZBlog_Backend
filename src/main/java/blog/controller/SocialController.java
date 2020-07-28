package blog.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import blog.entity.User;
import blog.service.ArticleManagement;
import blog.service.SocialManagement;
import blog.service.UserManagement;

@RestController
public class SocialController {
	@Autowired
	SocialManagement social_management;
	@Autowired
	ArticleManagement article_management;
	@Autowired
	UserManagement user_management;
	//用来获取一篇文章的点赞，是否点赞，收藏，关注等信息
	@RequestMapping("/ArticleInfo")
	public Map<String,Object> getArticleInfo(@RequestParam("UID") String uid,@RequestParam("AID") int aid){
		TreeMap<String,Object> result = new TreeMap<>();

		try{
			int viewer = Integer.parseInt(uid);
			/*获取文章作者的ID*/
			int tar_uid = this.article_management.getArticle(aid).getAuthor().getUid();
			result.put("IsSubscribed",this.social_management.isSubscribed(viewer, tar_uid));
			result.put("IsLiked", this.social_management.isLiked(viewer, aid));
			result.put("NumLikes", this.social_management.numLikes(aid));
			result.put("IsCollected", this.social_management.isCollected(viewer, aid));
			return result;
		}
		//没有登录
		catch(NumberFormatException e){
			result.put("NumLikes", this.social_management.numLikes(aid));
			return result;
		}
	}
	
	//用来获更改一个用户对一篇文章的点赞状态
	@RequestMapping("/ArticleLike")
	public Map<String,Object> changeLikeStatus(@RequestParam("UID") int uid,@RequestParam("AID") int aid){
		TreeMap<String,Object> result = new TreeMap<>();
		if(this.social_management.isLiked(uid, aid)) {//如果已经点过赞了，那就取消点赞
			this.social_management.cancelLike(uid, aid);
		}
		else {//如果没有点过赞，那就点上
			this.social_management.like(uid, aid);
		}
		result.put("Status", Boolean.TRUE);
		return result;
	}
	
	//用来改变一个用户对一篇文章的收藏状态
	@RequestMapping("/ArticleCollect")
	public Map<String,Object> changeCollectStatus(@RequestParam("UID") int uid,@RequestParam("AID") int aid){
		TreeMap<String,Object> result = new TreeMap<>();
		if(this.social_management.isCollected(uid, aid)) {//如果收藏过了，就取消收藏
			this.social_management.cancelCollect(uid, aid);
		}
		else {//如果没收藏过，就收藏
			this.social_management.collect(uid,aid);
		}
		result.put("Status", Boolean.TRUE);
		return result;
	}
	
	//用来改变一个用户对一篇文章作者的关注状态
	@RequestMapping("/AuthorFollowed")
	public Map<String,Object> changeSubscribeStatus(@RequestParam("UID") int uid,@RequestParam("AID") int aid){
		TreeMap<String,Object> result = new TreeMap<>();
		int tar_uid = this.article_management.getArticle(aid).getAuthor().getUid();
		if(this.social_management.isSubscribed(uid, tar_uid)) {//如果当前用户已经关注过作者了
			this.social_management.cancelSubscribe(uid, tar_uid);
		}
		else {//当前用户没有关注过作者
			this.social_management.subscribe(uid, tar_uid);
		}
		result.put("Status", Boolean.TRUE);
		return result;
	}
	
	//用来获得一个用户的社交信息，包括文章数量，粉丝数量，收藏数量
	@RequestMapping("/personInfo")
	public Map<String,Object> getPersonInfo(@RequestParam("UID") int uid){
		TreeMap<String,Object> result = new TreeMap<>();
		User u = this.user_management.getUser(uid);
		result.put("Username", u.getUsername());
		result.put("NumSubscribed", u.getIdols().size());
		result.put("NumFans", u.getFans().size());
		result.put("NumArticle", u.getArticles().size());
		return result;
	}
	
	//获取动态列表
	@RequestMapping("/MovementList")
	public Map<String,Object> getMovementList(@RequestParam("UID")int uid){
		List<Map<String,Object>> list = this.social_management.getMovementList(uid);
		Map<String,Object> result = new TreeMap<>();
		ArrayList<Object> UID = new ArrayList<>();
		ArrayList<Object> Time = new ArrayList<>();
		ArrayList<Object> AID = new ArrayList<>();
		ArrayList<Object> Title = new ArrayList<>();
		ArrayList<Object> Username = new ArrayList<>();
		ArrayList<Object> Type = new ArrayList<>();
		for(Map<String,Object> item:list) {
			UID.add(item.get("UID"));
			Time.add(item.get("Time"));
			AID.add(item.get("AID"));
			Title.add(item.get("TargetTitle"));
			Username.add(item.get("Username"));
			Type.add(item.get("Type"));
		}
		result.put("UID", UID);
		result.put("AID",AID);
		result.put("Time", Time);
		result.put("Username", Username);
		result.put("Type", Type);
		result.put("TargetTitle", Title);
		return result;
	}
}
