package blog.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.entity.Article;
import blog.entity.Tag;
import blog.service_frame.SearchService;
import blog.util.NLPUtil;

//与搜索相关的服务的实现
@Service
public class SearchManagement implements SearchService{
	private static final Log log = LogFactory.getLog(SearchManagement.class);
	
	@Autowired
	private TagManagement tag_management;
	@Autowired
	private ArticleManagement article_management;
	@Autowired
	private KeywordManagement keyword_management;
	
	@Override
	public List<Article> searchByTags(String input) {
		try {
			List<String> interests = NLPUtil.cutString(input);
			log.info("cut result = "+interests.toString());
			HashMap<Article,Integer> stat = new HashMap<>();//记录每一篇文章有几个标签重合
			List<Tag> found_tags = this.tag_management.getTagsForSearch(interests);//全部感兴趣的标签，并且每一个标签下一定有文章
			if(found_tags.size()==0) {//没有找到相应的标签，返回全部文章，按照阅读量
				log.info("not found!");
				return this.article_management.getAllArticle();
			}
			else {
				for(Tag t:found_tags) {
					List<Article> temp_list =  t.getArticles();
					log.info("articles containing tag "+t.getContent()+" are "+temp_list.toString());
					for(Article a:temp_list) {
						log.info("Article "+a+" found containning the tag"+t.getContent());
						if(stat.containsKey(a)) {
							stat.replace(a, stat.get(a)+1);
						}
						else {
							stat.put(a, 1);
						}
					}
				}
			}
			Set<Article> raw = stat.keySet();//全部包含感兴趣标签的文章
			TreeMap<Integer,ArrayList<Article>> article_set = new TreeMap<Integer,ArrayList<Article>>();//左边是包含的标签数目，右边是文章
			ArrayList<Article> result_reversed = new ArrayList<Article>();//最后返回的结果的倒序版
			for(Article a:raw) {//构建article_set
				int view = stat.get(a);
				log.info("Article:" +a+"found containning "+view+" tags");
				if(article_set.containsKey(view)) {
					article_set.get(view).add(a);
				}
				else {
					ArrayList<Article> temp = new ArrayList<>();
					temp.add(a);
					article_set.put(view, temp);
				}
			}
			Set<Integer> numbers = article_set.keySet();//出现过的包含标签数目
			for(Integer num: numbers) {//根据出现过的包含个数来遍历文章，result_reversed里面是从小到大排序的文章
				ArrayList<Article> temp = article_set.get(num);
				temp.sort(new Comparator<Article>() {

					@Override
					public int compare(Article o1,Article o2) {
						if(o1.getNumRead()!=o2.getNumRead()) {
							return o1.getNumRead()>o2.getNumRead()?-1:1;
						}
						else {
							return o1.getDatetime().compareTo(o2.getDatetime())>0?-1:1;
						}
					}
					
				});
				log.info("enumerating articles containning "+num+" tags");
				for(Article a:temp) {
					result_reversed.add(a);
				}
			}
			ArrayList<Article> result = new ArrayList<>();
			int i, len = result_reversed.size();
			log.info("size of result_reversed:  "+len);
			for(i=len-1;i>=0;i--) {
				result.add(result_reversed.get(i));
			}
			return result;
		}
		catch(Exception e) {
			
		}
		return null;
	}

	@Override
	public List<Article> searchArticle(String target) {
		// TODO Auto-generated method stub
		return this.keyword_management.getNeededArticles(target);
	}

}
