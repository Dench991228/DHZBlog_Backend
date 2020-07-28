package blog.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import blog.entity.Article;
import blog.service.ArticleManagement;
import blog.service.KeywordManagement;
import blog.service.SearchManagement;

@RestController
public class SearchController {
	private static final Log log = LogFactory.getLog(SearchController.class);
	@Autowired
	private SearchManagement search_management;
	@Autowired
	private ArticleManagement article_management;
	@Autowired
	private KeywordManagement keyword_management;
	//根据标签搜索的地址
	@RequestMapping("/Search")
	public Map<String,Object> getSearchResult(@RequestParam("Type") String type,@RequestParam("Value") String input){
		if(type.compareTo("All")!=0) {//标签搜索
			return this.article_management.modifyArticleList(this.search_management.searchByTags(input));
		}
		else {//全局搜索
			return this.article_management.modifyArticleList(this.search_management.searchArticle(input));
		}
	}
}
