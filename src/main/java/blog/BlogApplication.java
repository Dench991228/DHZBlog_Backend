package blog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogApplication {
    private static final Logger log = LoggerFactory.getLogger(BlogApplication.class);
    public static String project_path =System.getProperty("user.dir");
    public static String pic_dir = "/public";
    public static String file_dir = "/user_files";
    public static void main(String[] args) {
        log.info("hahaha!!!");
        log.info(System.getProperty("user.dir"));
        SpringApplication.run(BlogApplication.class, args);
    }
}
