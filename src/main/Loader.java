package main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import sun.misc.Launcher;

public class Loader {
	private String path;
	public ArrayList<String> names;
	public ArrayList<String> paths;
	public ArrayList<Class<?>> classes;
	
	public Loader(String packagePath){
		path = packagePath;
		names = new ArrayList<String>();
		paths = new ArrayList<String>();
		classes = new ArrayList<Class<?>>();
	}
	
	public void loadPackage(){
		File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
		if(jarFile.isFile()) {  // Run with JAR file
		    JarFile jar;
			try {
				jar = new JarFile(jarFile);
			    final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
			    while(entries.hasMoreElements()) {
			        final String name = entries.nextElement().getName();
			        if (name.startsWith(path + "/")) { //filter according to the path
			            paths.add(name);
			        }
			    }
				jar.close();
			} catch (IOException e) {e.printStackTrace();}
		}else{ // Run with IDE
			final URL url = Launcher.class.getResource("/" + path);
			if (url != null) {
				try {
					final File apps = new File(url.toURI());
					for (File app : apps.listFiles()) {
			            paths.add(app.getName());
					}
				}catch (URISyntaxException ex) {}
		    }
		}
		for(String name : paths){
			if(name.endsWith(".class")){
				for(int i = 0; i < name.length(); i++){
					if(name.regionMatches(i, path, 0, path.length())){
						name = name.substring(i+path.length()+1);
					}
				}
				name = name.substring(0,name.length()-6);
				names.add(name);
			}
		}
	}
	
	public void createClasses(){
		String pathTemp = path.replace('/', '.');
		for(String name : names){
			try {
				classes.add(Class.forName(pathTemp+"."+name));
			} catch (IllegalArgumentException | SecurityException | ClassNotFoundException e){
				e.printStackTrace();
			}
		}
	}
}
