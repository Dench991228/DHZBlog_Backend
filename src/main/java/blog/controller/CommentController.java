package blog.controller;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import blog.service.CommentManagement;

@RestController
public class CommentController {
	@Autowired
	CommentManagement comment_management;
	
	//获取评论列表
	@RequestMapping("/CommentList")
	public Map<String,Object> sendCommentList(@RequestParam("AID") int aid){
		Map<String,Object> result = new TreeMap<>();
		result.put("comments", this.comment_management.getAllComment(aid));
		return result;
	}
	
	//新增评论（不是回复评论）
	@RequestMapping("/NewComment")
	public Map<String,Object> recieveComment(
			@RequestParam("content") String content,
			@RequestParam("UID") int uid,
			@RequestParam("AID") int aid){
		int new_cid = this.comment_management.addComment(uid, aid, content);
		Map<String,Object> result = new TreeMap<>();
		result.put("CID", new_cid);
		return result;
	}
	
	//新增回复评论
	@RequestMapping("/ReplyComment")
	public Map<String,Object> replyComment(
			@RequestParam("comment[UID]")int  uid,
			@RequestParam("comment[AID]")int aid,
			@RequestParam("comment[content]")String content,
			@RequestParam("RPID") int rpid
			){
		int new_cid = this.comment_management.addReply(uid, aid, rpid, content);
		Map<String,Object> result = new TreeMap<>();
		result.put("CID", new_cid);
		return result;
	}
	
	//删除评论
	@RequestMapping("/DeleteComment")
	public Map<String,Object> delComment(@RequestParam("CID") int cid){
		this.comment_management.delComment(cid);
		Map<String,Object> result = new TreeMap<>();
		result.put("Status", Boolean.TRUE);
		return result;
	}
}
