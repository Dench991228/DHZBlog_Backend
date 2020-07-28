package blog.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.dao.KeywordRepository;
import blog.entity.Article;
import blog.entity.Keyword;
import blog.entity.Partition;
import blog.entity.Tag;
import blog.service_frame.KeywordService;
import blog.util.NLPUtil;

@Service
public class KeywordManagement implements KeywordService{
	private static final Log log = LogFactory.getLog(KeywordManagement.class);
	@Autowired
	private ArticleManagement article_management;
	
	@Autowired
	private KeywordRepository keyword_repo;
	
	@Autowired
	private TagManagement tag_management;
	
	@Override
	public void updateKeywordWeight(Article a) {
		log.info("start updating keywords!");
		Set<Tag> tags = a.getTags();
		Set<Partition> partitions = a.getPartitions();
		String title = a.getTitle();
		String content = a.getContent();
		if(a.getKeywords()!=null&&a.getKeywords().size()!=0) {
			log.info("这是一篇有内容的文章，文章aid是"+a.getAid());
			this.keyword_repo.deleteAll(a.getKeywords());
			a.getKeywords().clear();
			this.keyword_repo.flush();
			log.info("original keywords deleted finished");
		}
		TreeMap<String,Integer> keyword_frequency = new TreeMap<String,Integer>();
		try {
			List<String>content_words = NLPUtil.cutString(content);
			log.info("content contains : "+content_words.toString());
			List<String> title_words = NLPUtil.cutString(title);
			log.info("title contains : "+title_words.toString());
			for(String word:content_words) {//把正文的词权重加进去，每一个词权重是1
				if(keyword_frequency.containsKey(word)) {
					keyword_frequency.replace(word, keyword_frequency.get(word)+1);
				}
				else {
					keyword_frequency.put(word, 1);
				}
			}
			for(String word:title_words) {//把题目的词权重加进去，每一个词权重是5
				if(keyword_frequency.containsKey(word)) {
					keyword_frequency.replace(word, keyword_frequency.get(word)+5);
				}
				else {
					keyword_frequency.put(word, 5);
				}
			}
			for(Tag t:tags) {//考虑每一个标签，每一个标签的权重是10
				String word = t.getContent();
				if(keyword_frequency.containsKey(word)) {
					keyword_frequency.replace(word, keyword_frequency.get(word)+10);
				}
				else {
					keyword_frequency.put(word, 10);
				}
			}
			for(Partition p:partitions) {//考虑每一个分区，每一个分区的权重是20
				String word = p.getName();
				if(keyword_frequency.containsKey(word)) {
					keyword_frequency.replace(word, keyword_frequency.get(word)+20);
				}
				else {
					keyword_frequency.put(word, 20);
				}
			}
			Set<String> keywords = keyword_frequency.keySet();
			for(String word:keywords) {
				log.info("statistic finished!");
				Keyword kw = new Keyword();
				kw.setContent(word);
				kw.setFromArticle(a);
				kw.setWeight(keyword_frequency.get(word));
				Keyword k = this.keyword_repo.save(kw);
			}
		}
		catch(Exception e) {
			
		}
	}
	
	//根据输入的内容返回相关的文章，及其对于此关键词的权重
	//输入：一个关键词的内容
	//输出：文章列表/null（压根没有这个关键词记录）
	private HashMap<Article, Integer> getRelatedArticles(String content) {
		List<Keyword> keywords = this.keyword_repo.findByContent(content);
		if(keywords.size()==0||keywords==null) {//压根没有这个关键词记录
			return new HashMap<Article,Integer>();
		}
		else {
			HashMap<Article,Integer> result = new HashMap<Article,Integer>();
			for(Keyword kw:keywords) {
				Article a = kw.getFromArticle();
				result.put(a, kw.getWeight());
			}
			return result;
		}
	}
	
	//把hm2内容合并到hm1去
	private void MergeHashMap(HashMap<Article, Integer> hm1,HashMap<Article,Integer> hm2) {
		Set<Article> articles = hm2.keySet();
		for(Article a:articles) {
			if(hm1.containsKey(a)) {
				hm1.replace(a, hm1.get(a)+hm2.get(a));
			}
			else {
				hm1.put(a, hm2.get(a));
			}
		}
	}
	@Override
	public List<Article> getNeededArticles(String input) {
		HashMap<Article,Integer> article_weight = new HashMap<>();
		try {
			List<String> interests = NLPUtil.cutString(input);
			log.info("搜索内容分解结果"+interests.toString());
			for(String word:interests) {
				this.MergeHashMap(article_weight, this.getRelatedArticles(word));
			}
			if(article_weight.size()==0) {//如果没有相关的文章，那就返回全部文章
				return this.article_management.getAllArticle();
			}
			TreeMap<Integer,ArrayList<Article>> weight_article = new TreeMap<>();
			Set<Article> articles = article_weight.keySet();
			for(Article a:articles) {
				int weight = article_weight.get(a);
				if(weight_article.containsKey(weight)) {
					weight_article.get(weight).add(a);
				}
				else {
					ArrayList<Article> temp = new ArrayList<>();
					temp.add(a);
					weight_article.put(weight, temp);
				}
			}
			List<Article> result_reversed = new ArrayList<>();
			Set<Integer> weights = weight_article.keySet();
			for(Integer i:weights) {
				ArrayList<Article> temp = weight_article.get(i);
				temp.sort(new Comparator<Article>() {

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
				for(Article a:temp) {
					result_reversed.add(a);
				}
			}
			ArrayList<Article> result = new ArrayList<>();
			int i,len=result_reversed.size();
			for(i=len-1;i>=0;i--) {
				result.add(result_reversed.get(i));
			}
			return result;
		}
		catch(Exception e) {//如果输入的内容太简单，可能会出现这个问题
			log.info("挂掉了");
			return this.article_management.getAllArticle();
		}
	}
	
	
}
