package blog.service_frame;

import java.util.List;
import java.util.Map;

public interface SocialService {
	//取消收藏
	public void cancelCollect(int uid,int aid);
	//取消点赞
	public void cancelLike(int uid,int aid);
	//取消关注
	public void cancelSubscribe(int cur_uid, int tar_uid);
	//收藏
	public void collect(int uid, int aid);
	//判断是否收藏
	public boolean isCollected(int uid, int aid);
	//判断是否点赞
	public boolean isLiked(int uid, int aid);
	//判断是否关注
	public boolean isSubscribed(int cur_uid, int tar_uid);
	//点赞
	public void like(int uid, int aid);
	//一篇文章点赞人数
	public int numLikes(int aid);
	//关注一个人
	public void subscribe(int cur_uid, int tar_uid);
	//获得一个人全部关注的用户的动态，主要包括发布文章，发布评论，点赞，收藏
	public List<Map<String,Object>> getMovementList(int uid); 
}
