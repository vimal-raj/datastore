package xyz.navira.fworks;

//import java.util.Random;

public class DataStoreUtil {
	
	public static final String FILE_NAME_ERROR = "Invalid name [name should be alphanumeric and length between 1 to 10] name: ";
	public static final String FILE_PATH_ERROR = "Invalid path [check path exists and writable] path: ";
	public static final String FILE_EXISTS = "Key already exists!";
	public static final String FILE_NOT_EXISTS = "Key not exists!";
	public static final String FILE_NAME_LIMIT = "Key should not exceed 36 characters";
	public static final String JSON_LIMIT = "JSON OBject exceeds 16KB";
	public static final String INFO_FILE = "info.dat";
	public static final String NAME_REGEX = "^[a-z0-9_]*{1,10}$";
	
	static String getRandomString(int targetStringLength) {
		return "_datastore_";
	    /*int leftLimit = 97; // 'a'
	    int rightLimit = 122; // 'z'
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    return buffer.toString();*/
	}
	
	static String getDefaultPath() {
		return System.getProperty("java.io.tmpdir");
	}
}
