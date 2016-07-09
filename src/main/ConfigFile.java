package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConfigFile{
		private List<String> data;
		private File file;
		
		private Map<String, Object> configs;
		
		public ConfigFile(String p){
			file = new File(p);
			configs = new TreeMap<>();
		}
		
		public boolean load(){
			try {
				data = Files.readAllLines(Paths.get(file.getAbsolutePath()));
			}catch (IOException e){
				e.printStackTrace();
				try {
					file.delete();
					file.createNewFile();
				}catch (IOException e1){
					e1.printStackTrace();
				}
				return false;
			}
			this.configs = new TreeMap<>();
			try {
				this.data.forEach(s -> configs.put(s.substring(s.indexOf(':')+1, s.indexOf('=')), extractData(s.charAt(0), s.substring(s.indexOf('=')+1, s.length()))));
			}catch (Exception e){
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		private Object extractData(char type, String data) {
			switch (type){
				case 'B':return data.equals("true");
				case 'I':return Integer.parseInt(data);
				case 'D':return Double.parseDouble(data);
				case 'S':return data;
				default : return null;
			}
		}
		
		private String extractType(Object data) {
			if(data instanceof Boolean) {return "B";}
			if(data instanceof Integer) {return "I";}
			if(data instanceof Double) {return "D";}
			if(data instanceof String) {return "S";}
			return null;
		}
		
		public void save(){
			try {
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter out = new BufferedWriter(fw);
				this.configs.forEach((s,o) -> {
					try {
						out.write(extractType(o)+":"+s+"="+(o instanceof String ? o.toString() : String.valueOf(o))+"\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				out.close();
			} catch (IOException e) {e.printStackTrace();}
		}
		
		public void setDefaultValue(String config, Object value) {
			if(this.configs.get(config)==null) {
				this.configs.put(config, value);
			}
		}
		
		public ConfigFile setConfig(String config, Object value) {
			this.configs.put(config, value);
			return this;
		}
		
		@SuppressWarnings("unchecked")
		public <T>T getConfig(String config) throws NoConfigFoundException {
			if (this.configs.containsKey(config)){
				return (T) this.configs.get(config);
			}else {
				throw new NoConfigFoundException();
			}
		}
		
		public static class NoConfigFoundException extends Exception{
			private static final long serialVersionUID = -8859452479777186789L;
		}
	}