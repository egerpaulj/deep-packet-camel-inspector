package general;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

public class JavaSytaxTest {
	@Test
	public void TestDictionaryAlternative() {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		
		hashMap.put("test1", "I am test1");
		hashMap.put("test2", "I am test2");
		hashMap.put("test3", "I am test3");
		
		
		String result = hashMap.get("test1");
		
		Assert.assertEquals("I am test1", result);
	}

}
