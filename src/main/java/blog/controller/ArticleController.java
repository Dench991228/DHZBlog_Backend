package blog.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import blog.entity.Article;
import blog.entity.Partition;
import blog.entity.Tag;
import blog.entity.User;
import blog.service.ArticleManagement;
import blog.service.CommentManagement;
import blog.service.KeywordManagement;
import blog.service.PartitionManagement;
import blog.service.SocialManagement;
import blog.service.TagManagement;
import blog.service.UserManagement;

@RestController
public class ArticleController {
	private static final Log log = LogFactory.getLog(ArticleController.class);
	@Autowired
	private ArticleManagement article_management;
	@Autowired
	private TagManagement tag_management;
	@Autowired
	private PartitionManagement partition_management;
	@Autowired
	private UserManagement user_management;
	@Autowired
	private SocialManagement social_management;
	@Autowired
	private CommentManagement comment_management;
	@Autowired
	private KeywordManagement keyword_management;

	@RequestMapping("/ArticleSubmit")
	public Map<String,Object> articleSubmit(
			@RequestParam("Content")String content,
			@RequestParam("UID") int uid, 
			@RequestParam("AID") int aid,
			@RequestParam("Title") String title,
			@RequestParam("Partitions") List<Integer>pids,
			@RequestParam("Tags") List<String> tag_contents
			){
		Map<String,Object> result = new TreeMap<>();
		log.info(pids.toString());
		log.info(tag_contents.toString());
		if(aid==-1) {//新创建的文章，目前先考虑这个
			Set<Tag> tags = this.tag_management.getTags(tag_contents);
			Set<Partition> partitions = this.partition_management.getPartitions(pids);
			this.article_management.addArticle(uid, title, content, tags, partitions);
		}
		else {
			Article a = this.article_management.getArticle(aid);//当前正在编辑的文章
			a.getPartitions().clear();
			for(Tag t:a.getTags()) {
				t.setNumArticle(t.getNumArticle()-1);
				this.tag_management.updateTag(t);
			}
			a.setContent(content);
			a.setTitle(title);
			Set<Tag> tags = this.tag_management.getTags(tag_contents);
			Set<Partition> partitions = this.partition_management.getPartitions(pids);
			a.setPartitions(partitions);
			a.setTags(tags);
			for(Tag t:tags) {
				t.setNumArticle(t.getNumArticle()+1);
				this.tag_management.updateTag(t);
			}
			this.keyword_management.updateKeywordWeight(a);
			this.article_management.updateArticle(a);
		}
		result.put("Status",Boolean.TRUE);
		return result;
	}
	
	@RequestMapping("/getArticle")
	public Map<String,Object> sendArticles(@RequestParam("Type") String type,@RequestParam("Value") int value){
		Map<String,Object> result = new TreeMap<>();
		
		ArrayList<Integer> writer_ids = new ArrayList<>();
		ArrayList<String> writer_names = new ArrayList<>();
		ArrayList<String> titles = new ArrayList<>();
		ArrayList<Integer> article_ids = new ArrayList<>();
		ArrayList<Integer> num_comments = new ArrayList<>();
		ArrayList<String> times = new ArrayList<>();
		ArrayList<Integer> num_likes = new ArrayList<>();
		
		List<Article> articles;
		if(type.compareTo("Sector")==0) {//按照分区来获取
			if(value==-1) {//获取全部文章
				articles = this.article_management.getAllArticle();
			}
			else {//根据value获得文章
				articles = this.article_management.getArticleByPID(value);
			}
		}
		else {//按照uid来获取
			articles = this.article_management.getArticleByUID(value);
		}
		int i,len = articles.size();
		for(i=0;i<len;i++) {
			User author = articles.get(i).getAuthor();
			Article article = articles.get(i);
			writer_ids.add(author.getUid());
			writer_names.add(author.getUsername());
			titles.add(article.getTitle());
			article_ids.add(article.getAid());
			num_comments.add(this.comment_management.numComment(article.getAid()));
			num_likes.add(this.social_management.numLikes(article.getAid()));
			times.add(article.getDatetime());
		}
		result.put("WriterIDs", writer_ids);
		result.put("WriterNames", writer_names);
		result.put("Titles",titles);
		result.put("AIDs", article_ids);
		result.put("Likes",num_likes);
		result.put("NumComments",num_comments);
		result.put("Times",times);
		return result;
	}
	
	//用来根据AID向浏览界面发送文章
	//cur_uid没有实际作用
	@RequestMapping("/ArticleGet")
	public Map<String,Object> sendArticleByAID(@RequestParam("UID") String cur_uid,@RequestParam("AID") int aid){
		TreeMap<String,Object> result = new TreeMap<>();
		Article a = this.article_management.getArticle(aid);
		log.info("Article get: "+a);
		a.setNumRead(a.getNumRead()+1);
		this.article_management.addNumRead(a);
		User author = a.getAuthor();
		result.put("Title", a.getTitle());
		result.put("AuthorName", author.getUsername());
		result.put("AID", a.getAid());
		result.put("Content", a.getContent());
		
		//获取pid列表
		Set<Partition> partitions = a.getPartitions();
		ArrayList<Integer> pids = new ArrayList<>();
		for(Partition p:partitions) {
			pids.add(p.getPid());
		}
		
		//获取Tag的content列表
		Set<Tag> tags = a.getTags();
		ArrayList<String> contents = new ArrayList<>();
		for(Tag t:tags) {
			contents.add(t.getContent());
		}
		
		result.put("Partitions", pids);
		result.put("Tags", contents);
		return result;
	}

	//用来删除文章的接口
	@RequestMapping("/DelArticle")
	public Map<Object,Object> deleteArticle(@RequestParam("AID") int aid){
		Article a = this.article_management.getArticle(aid);
		Map<Object,Object> result = new TreeMap<>();
		if(a!=null){
			this.article_management.delArticle(aid);
			result.put("Status",Boolean.TRUE);
		}
		else{
			result.put("Status",Boolean.FALSE);
		}
		return result;
	}

	//用来获得一个人的全部收藏的文章
	@RequestMapping("/UserCollection")
	public Map<String,Object> getCollection(@RequestParam("UID") int uid){
		List<Article> collection = this.article_management.getCollectedArticlesByUID(uid);
		collection.sort((o1, o2) -> {
			if(o1.getNumRead()!=o2.getNumRead())return o1.getNumRead()>o2.getNumRead()?-1:1;
			else return o1.getDatetime().compareTo(o2.getDatetime())>0?-1:1;
		});
		return this.article_management.modifyArticleList(collection);
	}
}
