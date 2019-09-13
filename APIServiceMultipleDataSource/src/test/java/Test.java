import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.intuit.ipp.exception.FMSException;
import com.webservicemaster.iirs.utility.AES;
import com.webservicemaster.iirs.utility.Utility;

public class Test {
	
	@Autowired
	private static Utility util;
	
	@Autowired
	private static AES aes;
	
	@Value("${aes.key}")
	private static String aesKey;

//	public static void main(String[] args) throws Exception {
//		// TODO Auto-generated method stub
//		
//		/*util.DownloadImage("https://scontent.xx.fbcdn.net/v/t1.0-1/p50x50/15442272_10210182020060663_5394080671666138107_n.jpg?oh=d6d181c039326a23bad1cef87a3ec81a&oe=5A97E9B6", 
//						   "D:\\image.jpg");*/
//		
//		/*util.sendOAuth1("rPd6nBNqMAQkuxeOFPpTWBH6e", 
//				 		"8yYPyafPEHMGWZFvPgiGlTARWECyFjkVb0jV1npgSWykTGqOMn", 
//				 		"354616491-KQqyYPmk2MQcwPDYEP0C1LQbFayoZ9ITaIuLGEBe", 
//				 		"kAKxkY53yI4j8r0d5HMQve48sr1Eg3RXCg9S3dDDzT3TX", 
//				 		"https://api.twitter.com/1.1/account/verify_credentials.json?include_email=true&include_entities=true");*/
//
//
//	}

}
