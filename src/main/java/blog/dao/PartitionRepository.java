package blog.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.entity.Partition;

//用来管理各种各样的和分区的数据访问相关
public interface PartitionRepository extends JpaRepository<Partition,Integer>{
	
}
