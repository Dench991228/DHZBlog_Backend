package blog.util;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

//与自然语言处理有关的各种函数
@Service
public class NLPUtil {
	public static List<String> cutString(String input) throws IOException{
		StringReader sr=new StringReader(input);
	        IKSegmenter ik=new IKSegmenter(sr, true);
	        Lexeme lex=null;
	        List<String> list=new ArrayList<>();
	        while((lex=ik.next())!=null){
	            list.add(lex.getLexemeText());
	        }
	        return list;
	}
}
