package blog.service_frame;

import java.util.List;
import java.util.Map;

import blog.entity.Resource;

public interface ResourceService {
	//根据输入的文章图片的文件名，获取真正的文件名（用来去重）
	public String getPicDir(String filename);
	//根据输入的用户uid，获取全部资源记录的列表
	public List<Resource> getUserFileAll(int uid);
	//根据用户输入的rid，获取一个Resource的json对象
	public Map<String,Object> getUserFileItem(int rid);
	//根据用户uid和文件原始名，获取新的文件名（用来去重）
	public String getUserFileName(int uid,String filename);
	//根据uid和文件名（已去重），生成新的资源记录，返回rid
	public int addResource(int uid,String filename);
	//根据rid获得一条资源记录
	public Resource getUserFile(int rid);
	//根据资源记录删除文件
	public void delResource(Resource r);
}
