package blog.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import blog.BlogApplication;
import blog.entity.Resource;
import blog.entity.User;
import blog.service.ResourceManagement;

@RestController
public class ResourceController {
	private static final Log log = LogFactory.getLog(ResourceController.class);
	@Autowired
	private ResourceManagement resource_management;

	@RequestMapping("/picSave")
	public Map<String,String> savePicture(@RequestParam("image") MultipartFile pic){
		log.info("pic name is: "+pic.getOriginalFilename());
		String picName = this.resource_management.getPicDir(pic.getOriginalFilename());
		log.info("图片的新的文件名："+picName);
		File newPic = new File(BlogApplication.project_path+BlogApplication.pic_dir+"/"+picName);
		TreeMap<String,String> result = new TreeMap<>();
		try {
			newPic.createNewFile();
			FileOutputStream os = new FileOutputStream(newPic,false);
			os.write(pic.getBytes());
			os.flush();
			os.close();
			result.put("url", "/"+picName);
			return result;
		}
		catch(IOException e) {
			result.put("url", "");
			return result;
		}
	}
	//获取文件列表的接口
	@RequestMapping("/resourceList")
	public Map<String,Object> sendResourceList(@RequestParam("uid")int uid){
		Map<String,Object> result = new TreeMap<>();
		ArrayList<Map<String,Object>>result_list = new ArrayList<>();
		List<Resource>records = this.resource_management.getUserFileAll(uid);
		int i,len=records.size();
		for(i=0;i<len;i++) {
			result_list.add(this.resource_management.getUserFileItem(records.get(i).getRid()));
		}
		result.put("list", result_list);
		return result;
	}
	//上传文件的接口
	@RequestMapping("resourceCenter")
	public Map<String,Object> recieveFile(@RequestParam("uid")int uid,@RequestParam("fileInput") MultipartFile file){
		String new_file_name = this.resource_management.getUserFileName(uid, file.getOriginalFilename());//获得新文件的名字
		String new_file_dir = BlogApplication.project_path+BlogApplication.file_dir+"/"+uid+"/"+new_file_name;
		File new_file = new File(new_file_dir);
		Map<String,Object> result = new TreeMap<String,Object>();
		try {
			new_file.createNewFile();
			FileOutputStream fos = new FileOutputStream(new_file);
			fos.write(file.getBytes());
			fos.flush();
			fos.close();
			int rid = this.resource_management.addResource(uid, new_file_name);
			result = this.resource_management.getUserFileItem(rid);
			return result;
		}
		catch(IOException e) {
			
		}
		return new TreeMap<String,Object>();
	}
	//下载文件的接口
	@RequestMapping("/resourceDownloader")
	public void sendUserFile(@RequestParam("RID")int rid,HttpServletResponse response) {
		Resource r = this.resource_management.getUserFile(rid);//待下载的文件
		log.info("Downloader reached!");
		log.info("downloading: " + r.getFileName());
		User u = r.getOwner();
		String FileDir = BlogApplication.project_path+BlogApplication.file_dir+"/"+u.getUid()+"/"+r.getFileName();
		File target_file = new File(FileDir);
		response.setContentType("application/force-download");
		response.addHeader("Content-disposition", "attachment;fileName=" +r.getFileName());
		try {
			OutputStream os = response.getOutputStream();
			byte[] buf = new byte[1024];
			FileInputStream fis = new FileInputStream(target_file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			int i = bis.read(buf);
			while(i!=-1) {
				os.write(buf);
				i = bis.read(buf);
			}
			fis.close();
			bis.close();
		}catch(Exception e){
			
		}
	}
	//删除文件的接口
	@RequestMapping("/deleteResource")
	public Map<String,Object> deleteFile(@RequestParam("RID")int rid){
		Resource r = this.resource_management.getUserFile(rid);
		User u = r.getOwner();
		String target_file_dir = BlogApplication.project_path+BlogApplication.file_dir+"/"+u.getUid()+"/"+r.getFileName();
		File target_file = new File(target_file_dir);
		target_file.delete();
		this.resource_management.delResource(r);
		Map<String,Object> result = new TreeMap<String,Object>();
		result.put("Status", Boolean.TRUE);
		return result;
	}
}
