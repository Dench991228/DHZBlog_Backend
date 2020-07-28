package blog.service_frame;

import java.util.ArrayList;
import java.util.Map;

import blog.entity.Comment;

public interface CommentService {
	//根据uid，aid和评论内容生成一条评论，返回cid
	public int addComment(int uid,int aid,String content);
	//根据uid，aid，cid生成一条回复评论，返回回复评论的cid
	public int addReply(int uid,int aid,int cid,String content);
	//根据cid删除一条评论
	public void delComment(int cid);
	//根据aid，生成这篇文章下全部评论，已经组装完成
	public ArrayList<Map<String,Object>> getAllComment(int aid);
	//根据aid获得这篇文章下评论的数量，回复评论也算
	public int numComment(int aid);
	//根据cid获得评论
	public Comment getComment(int cid);
}
