package blog.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import blog.entity.Partition;
import blog.service.PartitionManagement;
@RestController
public class PartitionController {
	@Autowired
	private PartitionManagement pm;
	

	@RequestMapping("/getPartition")
	public Map<String,List<Map<String,Object>>> getAllPartitions(){
		Map<String,List<Map<String,Object>>> result = new TreeMap<>();
		List<Partition> p = pm.getAllPartition();
		ArrayList<Map<String,Object>> data = new ArrayList<>();
		int len = p.size();
		int i;
		for(i=0;i<len;i++) {
			TreeMap<String,Object> par = new TreeMap<>();
			par.put("partition_name",p.get(i).getName());
			par.put("partition_pid", p.get(i).getPid());
			par.put("partition_icon_url", p.get(i).getIconUrl());
			data.add(par);
		}
		data.sort(new Comparator<Map<String,Object>>(){

			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				if((int)o1.get("partition_pid")<(int)o2.get("partition_pid")) {
					return -1;
				}
				else {
					return 1;
				}
			}
			
		});
		result.put("partition_data", data);
		return result;
	}
}
