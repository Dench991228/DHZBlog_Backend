package blog.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.dao.PartitionRepository;
import blog.entity.Partition;

@Service
public class PartitionManagement {
	@Autowired
	private PartitionRepository pr;
	
	//获取全部的分区
	public List<Partition> getAllPartition(){
		return pr.findAll();
	}
	
	//根据分区编号获取分区
	public Partition getPartition(int pid) {
		return pr.getOne(pid);
	}
	
	//根据分区编号列表获取分区集合
	public Set<Partition> getPartitions(List<Integer> pids){
		Set<Partition> result = new HashSet<>();
		int len=pids.size();
		int i;
		for(i=0;i<len;i++) {
			result.add(this.getPartition(pids.get(i)));
		}
		return result;
	}
	
	//创建所有的分区，如果有就跳过
	public void initPartitions() {
		Partition java = new Partition("Java","/icons/java.png");
		Partition python = new Partition("Python","/icons/python.png");
		Partition front = new Partition("前端","/icons/front.png");
		Partition db = new Partition("数据库","/icons/db.png");
		if(this.pr.getOne(1)==null) {
			this.pr.save(java);
		}
		if(this.pr.getOne(2)==null) {
			this.pr.save(python);
		}
		if(this.pr.getOne(3)==null) {
			this.pr.save(front);
		}
		if(this.pr.getOne(4)==null) {
			this.pr.save(db);
		}
		this.pr.flush();
	}
}
