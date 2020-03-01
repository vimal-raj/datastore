package xyz.navira.fworks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public class DataStore implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private String path;
	private int size;
	private File dataStore;
	private List<DataFile> files;
	private File info;
	
	public DataStore() throws Exception {		
		this(DataStoreUtil.getRandomString(6));
	}
	
	DataStore(String name) throws Exception{		
		this(name, DataStoreUtil.getDefaultPath());
	}
	
	DataStore(String name, String path) throws Exception{
		name = name.toLowerCase();
		if(!validateName(name))
			throw new Exception(DataStoreUtil.FILE_NAME_ERROR + name);
		if(!validatePath(path))
			throw new RuntimeException(DataStoreUtil.FILE_NAME_ERROR + path);
		this.name = name;
		this.path= path;
		files = new ArrayList<DataFile>();
		createDataStore();
		updateInfo(false);
	}
	
	public String getName() {
		return name;
	}
	public String getPath() {
		return dataStore.getAbsolutePath();
	}
	public int getSize() {
		return size;
	}
	public List<DataFile> getFiles() {
		return files;
	}
	
	private boolean validateName(String name) {		
		return name.matches(DataStoreUtil.NAME_REGEX);
	}
	private boolean validatePath(String path) {
		File dir = new File(path);
		return dir.isDirectory() && dir.canWrite();
	}	
	private void createDataStore() throws IOException {
		this.dataStore = new File(this.path+File.separator+this.name);
		if(!this.dataStore.exists())
			dataStore.mkdir();	
		updateDataSource();
	}
	
	public DataFile createDataFile(String name) throws IOException {
		return createDataFile(name, -1);
	}
	
	public DataFile createDataFile(String name, int ttl) throws IOException {
		if (name.length() > 36) throw new RuntimeException(DataStoreUtil.FILE_NAME_LIMIT + path);
		for(DataFile file: files) {
			if(name.equals(file.getName())) {
				throw new RuntimeException(DataStoreUtil.FILE_EXISTS + path);
			}
		}
		DataFile dF = new DataFile(this.getPath()+File.separator+name);
		dF.setName(name);
		dF.setTtl(ttl != -1 ?  (System.currentTimeMillis() / 1000L) + ttl : -1);
		dF.putContent(new JSONObject());
		files.add(dF);
		updateInfo(false);
		return dF;
	}
	
	private synchronized void updateDataSource() throws IOException {
		info = new File(this.getPath()+File.separator+DataStoreUtil.INFO_FILE);
		info.createNewFile();
		Map<String, Integer> ttls = getTtlFromInfo();
		files = new ArrayList<DataFile>(); 
		for(File file : this.dataStore.listFiles()) {
			if(!info.getName().equals(file.getName())) {
				int ttl = ttls.get(file.getName());
				if(ttl != -1 && ttl < (System.currentTimeMillis() / 1000L)) {
					file.delete();
					continue;
				}
				files.add(new DataFile(file, file.getName(), ttl));
			}
		}
	}
	
	public synchronized boolean deleteDataFile(String name) throws IOException {
		boolean deleted = false;
		for(DataFile file : files) {
			if(name.equals(file.getName())) {	 			
				deleted = file.delete();
				break;
			}
		}
		return deleted;
	}
	private synchronized void updateInfo(boolean clear) throws IOException {
		FileWriter fw = new FileWriter(this.info, clear);
		String str = "";
		for(DataFile f : files) {
			str += f.getName()+"="+f.getTtl()+",";
		}
		fw.write(str);
		fw.flush();
		fw.close();
	}
	
	private synchronized Map<String, Integer> getTtlFromInfo() throws FileNotFoundException, IOException {
		Map<String, Integer> map = new HashMap<String, Integer>();
		String str = "";
		int ch;
		FileReader f = new FileReader(info);
		while((ch = f.read()) != -1)
			str += (char) ch;
		for(String s : str.split(",")) {
			if(s.trim().length() > 2) {
				String[] nameTtl = s.split("=");
				map.put(nameTtl[0], Integer.parseInt(nameTtl[1]));
			}
		}
		f.close();
		return map;
	}
	
	public DataFile getFile(String name) {
		DataFile f = null;
		for(DataFile file : files) {
			if(name.equals(file.getName())) {	 			
				f = file;
				break;
			}
		}
		if(f == null) throw new RuntimeException(DataStoreUtil.FILE_NOT_EXISTS + path);
		return f;
	}
	
	public int size() {
		return files.size();
	}
	
	public long sizeOnDisk() {
	    long size = 0;
	    for (File file : dataStore.listFiles()) {
	        if (file.isFile()) {
	            size += file.length();
	        }
	        else
	            size += sizeOnDisk();
	    }
	    return size;
	}
	
	public int size(boolean realsize) {
		return dataStore.listFiles().length;
	}
}
