package blog.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.BlogApplication;
import blog.dao.ResourceRepository;
import blog.entity.Resource;
import blog.service_frame.ResourceService;

@Service
public class ResourceManagement implements ResourceService{
	@Autowired
	private ResourceRepository resource_repo;
	@Autowired
	private UserManagement user_management;
	//根据输入的图片的名字，经过去重之后，再返回新的名字
	@Override
	public String getPicDir(String filename) {
		File pic_folder = new File(BlogApplication.project_path+BlogApplication.pic_dir);
		String[] name_pics = pic_folder.list(); //全部图片的名字
		String[] name_parts = filename.split("\\.");
		String srcName="",extName="";//本名，扩展名
		int i, len;
		int num = 0;
		if(name_parts.length<=1) {//没有扩展名
			srcName = filename;
			if(name_pics!=null&&name_pics.length!=0) {
				len = name_pics.length;
				for(i=0;i<len;i++) {
					if(name_pics[i].startsWith(srcName))num++;
				}
        			if(num!=0) {
        				srcName = srcName + String.format("(%d)", num);
        			}
			}
		}
		else {
			int num_parts = name_parts.length;
			for(i=0;i<num_parts-1;i++) {//扩展名之前的全部都合起来，成为本名
				srcName+=name_parts[i];
			}
			extName = name_parts[num_parts-1];//最后一个是扩展名
			if(name_pics!=null&&name_pics.length!=0) {
				len = name_pics.length;
				for(i=0;i<len;i++) {//看一看有没有重名的文件
					if(name_pics[i].startsWith(srcName))num++;
				}
				if(num!=0)srcName+=String.format("(%d)", num);
			}
		}
		return srcName+"."+extName;
	}

	@Override
	public List<Resource> getUserFileAll(int uid) {
		return this.resource_repo.findByOwner_uidOrderByDatetimeDesc(uid);
	}

	@Override
	public Map<String, Object> getUserFileItem(int rid) {
		Map<String,Object > result = new TreeMap<>();
		Resource r = this.resource_repo.getOne(rid);
		result.put("fileName", r.getFileName());
		result.put("deleteUrl", "/deleteResource?RID="+r.getRid());
		result.put("RID", r.getRid());
		result.put("url", "resourceDownloader?RID="+r.getRid());
		return result;
	}

	@Override
	public String getUserFileName(int uid, String filename) {
		File user_folder = new File(BlogApplication.project_path+BlogApplication.file_dir+"/"+uid); //用户文件夹
		if(!user_folder.exists())user_folder.mkdir();
		String srcName="",extName=""; //本名，扩展名
		String[] file_names = user_folder.list(); //用户文件夹下的文件名
		String[] file_name_parts = filename.split("\\."); //尝试分解文件名，最后一个点后面是扩展名
		int i, len = file_name_parts.length;
		if(len<=1) {//没有扩展名
			srcName = filename;
			int len_files;
			int num = 0;
			if(file_names!=null&&file_names.length!=0) {
				len_files = file_names.length;
				for(i=0;i<len_files;i++) {
					if(file_names[i].startsWith(srcName))num++;
				}
				if(num!=0)srcName+=String.format("(%d)", num);
			}
			
		}
		else {
			for(i=0;i<len-1;i++) {//除了扩展名之外的，全部都是本名
				srcName += file_name_parts[i];
			}
			extName = file_name_parts[len-1];
			int len_files;
			int num = 0;
			if(file_names!=null&&file_names.length!=0) {
				len_files = file_names.length;
				for(i=0;i<len_files;i++) {
					if(file_names[i].startsWith(srcName))num++;
				}
				if(num!=0)srcName+=String.format("(%d)",num);
			}
			
		}
		return srcName+"."+extName;//前端
	}

	@Override
	public int addResource(int uid, String filename) {
		Resource r = new Resource();
		r.setFileName(filename);
		r.setOwner(this.user_management.getUser(uid));
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String cur_date = formatter.format(date);
		r.setDatetime(cur_date);
		Resource saved = this.resource_repo.save(r);
		return saved.getRid();
	}

	@Override
	public Resource getUserFile(int rid) {
		return this.resource_repo.getOne(rid);
	}

	@Override
	public void delResource(Resource r) {
		this.resource_repo.delete(r);
	}
	
}
