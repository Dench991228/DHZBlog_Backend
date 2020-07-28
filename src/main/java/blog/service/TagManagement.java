package blog.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.dao.TagRepository;
import blog.entity.Tag;
import blog.service_frame.TagService;

@Service
public class TagManagement  implements TagService{

	@Autowired
	TagRepository repo;
	
	//根据内容查找相关的标签
	//如果没有这个内容对应的标签，那就新建一个
	private Tag getTag(String content) {
		Tag temp = repo.findByContent(content);
		if(temp==null) {//如果不存在这个标签，就生成一个新的
			Tag t = new Tag();
			t.setContent(content);
			t.setNumArticle(0);
			t=repo.save(t);
			return t;
		}
		else {
			return temp;
		}
	}
	
	//根据标签的内容列表生成标签的集合
	@Override
	public Set<Tag> getTags(List<String> contents){
		Set<Tag> result = new HashSet<>();
		int i,len=contents.size();
		for(i=0;i<len;i++) {
			Tag t = this.getTag(contents.get(i));
			result.add(t);
		}
		return result;
	}
	
	//更新标签的信息
	@Override
	public void updateTag(Tag t) {
		this.repo.save(t);
	}
	

	//获取相关的标签，如果没有也不新建
	@Override
	public List<Tag> getTagsForSearch(List<String> contents){
		List<Tag> result = new ArrayList<>();
		for(String s:contents) {
			Tag t = this.repo.findByContent(s);
			if(t!=null&&t.getNumArticle()!=0)result.add(t);
		}
		return result;
	}
}
