package blog.service_frame;

import java.util.List;

import blog.entity.Article;
import blog.entity.Tag;

public interface SearchService {
	
	//根据标签来进行搜索
	//输入：感兴趣的标签的列表
	public List<Article> searchByTags(String input);
	
	//根据全文来进行搜索
	//输入：待匹配的内容
	public List<Article> searchArticle(String target);
}
