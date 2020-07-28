package blog.service_frame;

import java.util.List;
import java.util.Set;

import blog.entity.Article;
import blog.entity.Tag;

public interface TagService {
	//根据列表中的字符串来获得相应的标签，如果没有就新建
	public Set<Tag> getTags(List<String> contents);
	//更新标签信息
	public void updateTag(Tag t);
	//根据列表中的字符串来寻找相关的标签，如果没有，就不新建
	public List<Tag> getTagsForSearch(List<String> contents);
}
