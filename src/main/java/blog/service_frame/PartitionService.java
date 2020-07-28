package blog.service_frame;

import java.util.List;
import java.util.Set;

import blog.entity.Partition;

public interface PartitionService {
	//获取全部分区
	public List<Partition> getAllPartition();
	//根据pid获取分区
	public Partition getPartition(int pid);
	//根据若干分区pid获得分区的集合
	public Set<Partition> getPartitions(List<Integer> pids); 
}
