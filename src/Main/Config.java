package Main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Config{
		public List<String> data;
		public String path;
		public File file;
		public Config(String p){
			path = p;
			file = new File(p);
			load();
		}
		public void load(){
			try {data = Files.readAllLines(Paths.get(file.getAbsolutePath()));} catch (IOException e){}
		}
		public void save(){
			try {
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter out = new BufferedWriter(fw);
				for(String c : data) {
					out.write(c);out.newLine();out.flush();
				}out.close();
			} catch (IOException e) {e.printStackTrace();}
		}
		
		public Config setconfig(String config, Object value) {
			if(value.getClass()!=String.class)value = String.valueOf(value);
			for(int i = 0; i < data.size(); i++) {
				if(data.get(i).contains(config)) {
					String s = data.get(i);int j;
					for(j = 0; j < s.length() && s.charAt(j)!='='; j++);
					data.set(i,s.substring(0, j+1)+value);
				}
			}
			return this;
		}
		
		public Object getconfig(String config) {
			Class<?> c = null;
			String s = null;
			for(int i = 0; i < data.size(); i++) {
				if(data.get(i).contains(config)) {
					s = data.get(i);
					int j;for(j = 0; j < s.length() & s.charAt(j)!=':'; j++);
					switch (s.charAt(j-1)){
					case 'B':c=Boolean.class;break;
					case 'I':c=Integer.class;break;
					case 'D':c=Double.class;break;
					case 'S':c=String.class;break;
					}
					for(j = 0; j < s.length() & s.charAt(j)!='='; j++);
					s = (String)s.subSequence(j+1, s.length());
				}
			}
			Object o = null;
			if(c==Boolean.class)if(s.equals("true"))o=true;else o=false;
			if(c==Integer.class)o=Integer.parseInt(s);
			if(c==Double.class)o=Double.parseDouble(s);
			if(c==String.class)o=s;
			return o;
		}
	}