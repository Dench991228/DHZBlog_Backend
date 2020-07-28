package blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blog.entity.User;
import blog.service.UserManagement;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class Entrance {
	@GetMapping("/")
	public String getEntrance() {
		return "forward:/index.html";
	}
}
