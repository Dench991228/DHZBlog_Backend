package blog.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import blog.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.dao.ArticleRepository;
import blog.dao.TagRepository;
import blog.service_frame.ArticleService;

@Service
public class ArticleManagement implements ArticleService {
	@Autowired
	private ArticleRepository repo;
	@Autowired
	private UserManagement user_management;
	@Autowired
	private PartitionManagement partition_management;
	@Autowired
	private KeywordManagement keyword_management;
	@Autowired
	private TagRepository tag_repo;
	
	@Override
	public Article addArticle(int uid,String title,String content,Set<Tag> tags, Set<Partition> partitions) {
		Article a = new Article();
		User u = this.user_management.getUser(uid);
		a.setAuthor(u);
		a.setContent(content);
		a.setTitle(title);
		a.setNumRead(0);
		//获取当前时间
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String cur_date = formatter.format(date);
		a.setDatetime(cur_date);
		a.setPartitions(partitions);
		a.setTags(tags);
		Article added = repo.save(a);
		for(Tag t:tags) {
			t.setNumArticle(t.getNumArticle()+1);
			this.tag_repo.save(t);
		}
		this.keyword_management.updateKeywordWeight(added);
		return added;
	}
	
	@Override
	public void delArticle(int aid) {
		Article a = repo.getOne(aid);
		if(a!=null) {
			this.repo.delete(a);
			this.repo.flush();
		}
	}
	
	@Override
	public Article getArticle(int aid) {
		return this.repo.getOne(aid);
	}
	
	@Override
	public List<Article> getArticleByPID(int pid){
		Set<Article> article_set =this.partition_management.getPartition(pid).getContainedArticles();
		ArrayList<Article> result = new ArrayList<>();
		for(Article a:article_set) {
			result.add(a);
		}
		result.sort(new Comparator<Article>(){
			@Override
			public int compare(Article o1, Article o2) {
				if(o1.getNumRead()!=o2.getNumRead()) {
					return o1.getNumRead()>o2.getNumRead()?-1:1;
				}
				else {
					return o1.getDatetime().compareTo(o2.getDatetime())>0?-1:1;
				}
			}
		});
		return result;
	}
	
	@Override
	public List<Article> getAllArticle(){
		ArrayList<Article> result = (ArrayList<Article>)this.repo.findAll();
		result.sort(new Comparator<Article>() {

			@Override
			public int compare(Article o1, Article o2) {
				if(o1.getNumRead()!=o2.getNumRead()) {
					return o1.getNumRead()>o2.getNumRead()?-1:1;
				}
				else {
					return o1.getDatetime().compareTo(o2.getDatetime())>0?-1:1;
				}
			}
			
		});
		return result;
	}
	
	@Override
	public List<Article> getArticleByUID(int uid){
		List<Article> result = (this.user_management.getUser(uid).getArticles());
		result.sort(new Comparator<Article>(){
			@Override
			public int compare(Article o1, Article o2) {
				if(o1.getNumRead()!=o2.getNumRead()) {
					return o1.getNumRead()>o2.getNumRead()?-1:1;
				}
				else {
					return o1.getDatetime().compareTo(o2.getDatetime())>0?-1:1;
				}
			}
		});
		return result;
	}
	
	@Override
	public void updateArticle(Article a) {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String cur_date = formatter.format(date);
		a.setDatetime(cur_date);
		this.repo.saveAndFlush(a);
	}
	
	@Override
	public Map<String,Object> modifyArticleList(List<Article> articles){
		ArrayList<Integer> Likes = new ArrayList<>();
		ArrayList<Integer> AIDs = new ArrayList<>();
		ArrayList<String> Titles = new ArrayList<>();
		ArrayList<String> Times = new ArrayList<>();
		ArrayList<Integer> WriterIDs = new ArrayList<>();
		ArrayList<String> WriterNames = new ArrayList<>();
		ArrayList<Integer> NumComments = new ArrayList<>();
		ArrayList<Integer> NumReads = new ArrayList<>();
		for(Article a:articles) {
			User u = a.getAuthor();
			Likes.add(a.getLikes().size());
			AIDs.add(a.getAid());
			Titles.add(a.getTitle());
			Times.add(a.getDatetime());
			WriterIDs.add(u.getUid());
			WriterNames.add(u.getUsername());
			NumComments.add(a.getArticleComments().size());
			NumReads.add(a.getNumRead());
		}
		Map<String,Object> result = new TreeMap<>();
		result.put("AIDs", AIDs);
		result.put("Likes",Likes);
		result.put("Titles", Titles);
		result.put("Times",Times);
		result.put("WriterIDs", WriterIDs);
		result.put("WriterNames",WriterNames);
		result.put("NumComments",NumComments);
		result.put("NumRead",NumReads);
		return result;
	}
	
	@Override
	public void addNumRead(Article a) {
		a.setNumRead(a.getNumRead()+1);
		this.repo.save(a);
	}

	@Override
	public List<Article> getCollectedArticlesByUID(int uid) {
		User u = this.user_management.getUser(uid);
		List<Collection> collect_record = u.getMyCollection();
		List<Article> collected_articles = new ArrayList<>();
		for(Collection r:collect_record){
			collected_articles.add(r.getTarget());
		}
		return collected_articles;
	}
}
