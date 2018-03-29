package cases;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blc.qa.dataprovider.TestBase;
import com.blc.qa.util.HttpUtil;


public class LoginTest extends TestBase{

	@Test(dataProvider = "xml")
	public  void  loginScm(String fsShopName,String fsUserId,String fsPwd){
		
		Map<String, String> map=new HashMap();
		map.put("fsShopName", fsShopName);
		map.put("fsUserId", fsUserId);
		map.put("fsPwd", fsPwd);
		map.put("redirectURL", "localhost");
		String url="http://scm.qa.51eparty.com/scm/user/loginJsonp";
		String result=HttpUtil.sendGet(url,map);
		System.out.println("------------"+result);
		JSONObject obj=JSON.parseObject(result);
		
	}
	
	
	
}
