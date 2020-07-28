package blog.service_frame;

import java.util.List;
import java.util.Map;

import blog.entity.Article;

public interface KeywordService {
	
	//根据一篇文章的内容，更新/创建它的关键词信息
	//输入：一篇已经设置好内容，题目，分区，标签信息的文章
	//输出：无
	public void updateKeywordWeight(Article a);
	
	//根据搜索框输入结果，输出含有搜索结果的文章，并且按照权重大小排序
	//输入：复杂搜索内容（需要分词）
	//输出：包含搜索内容的文章列表，如果没有的话，就按照先阅读量，后发最后编辑时间来降序输出
	public List<Article> getNeededArticles(String input);
}
