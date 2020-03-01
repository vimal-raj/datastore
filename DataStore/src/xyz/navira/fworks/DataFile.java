package xyz.navira.fworks;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class DataFile extends File{
	
	private static final long serialVersionUID = 1L;
	
	private long ttl = -1;
	private String name;
	
	public long getTtl() {
		return ttl;
	}

	public void setTtl(long ttl) {
		this.ttl = ttl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	DataFile(String pathname) throws IOException {
		super(pathname);
		super.createNewFile();
	}
	
	DataFile(File file, String name, int ttl) throws IOException{
		super(file.getAbsolutePath());
		this.name = name;
		this.ttl = ttl;
	}
	
	public synchronized String getContentAsString() throws IOException {
		return getContent().toJSONString();
	}
	
	public synchronized JSONObject getContent() throws IOException {
		String str = "";
		int ch;
		FileReader reader = new FileReader(this);
		while((ch = reader.read()) != -1)
			str += (char) ch;
		reader.close();
		return (JSONObject) JSONValue.parse(str.trim() == "" ? "{}" : str);
	}
	
	public synchronized void putContent(JSONObject obj) throws IOException {
		String json = obj.toJSONString();
		if(json.getBytes().length > 16000)
			throw new RuntimeException(DataStoreUtil.JSON_LIMIT);
		FileWriter writer = new FileWriter(this);
        writer.write(obj.toJSONString());
        writer.flush();
        writer.close();
	}
	
	@Override
	public String toString() {		
		try {
			return "DataFile [ttl=" + ttl + ", name=" + name + ", content=" + this.getContent().toJSONString() + "]";
		} catch (IOException e) {
			e.printStackTrace();
			return "DataFile [ttl=" + ttl + ", name=" + name + ", content= ]";
		}
	}

}
