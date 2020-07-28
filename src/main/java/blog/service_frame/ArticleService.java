package blog.service_frame;

import java.util.List;
import java.util.Map;
import java.util.Set;

import blog.entity.Article;
import blog.entity.Partition;
import blog.entity.Tag;

public interface ArticleService {
	
	//添加一篇文章
	public Article addArticle(int uid,String title,String content,Set<Tag> tags, Set<Partition> partitions);
	
	//删除一篇文章
	public void delArticle(int aid);
	
	//添加一篇文章
	public Article getArticle(int aid);
	
	//根据分区pid获取文章列表，按照浏览量排序
	public List<Article> getArticleByPID(int pid);
	
	//获取全部文章，按照浏览量排序
	public List<Article> getAllArticle();
	
	//根据用户uid获取文章列表，按照最后编辑时间排序
	public List<Article> getArticleByUID(int uid);
	
	//更新文章
	public void updateArticle(Article a) ;
	
	//把一个文章的列表组装成返回的数据
	public Map<String,Object> modifyArticleList(List<Article> articles);
	
	//更新一篇文章的阅读量
	public void addNumRead(Article a);

	//找到一个用户的全部收藏的文章
	List<Article> getCollectedArticlesByUID(int uid);
}
