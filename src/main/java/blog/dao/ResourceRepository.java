package blog.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import blog.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource,Integer>{
	//获取文件列表，根据上传时间倒序
	public List<Resource> findByOwner_uidOrderByDatetimeDesc(int uid);
}
