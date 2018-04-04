package item;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Recipe implements Comparable<Recipe>{

	public boolean researched = false;
	public List<Component> products = new ArrayList<Component>();
	public List<Component> educts = new ArrayList<Component>();
	
	public Recipe(){
		
	}
	
	public Recipe addP(int ID, int n){
		products.add(new Component(ID,n));
		sortComponents(products);
		return this;
	}
	public Recipe addE(int ID, int n){
		educts.add(new Component(ID,n));
		sortComponents(educts);
		return this;
	}
	
	private void sortComponents(List<Component> list) {
		list.sort(new Comparator<Component>(){
			public int compare(Component c1, Component c2) {
				return c1.compareTo(c2);
			}
		});
	}
	
	public int compareTo(Recipe r) {
		if(r.products.size() == this.products.size()) {
			for (int i = 0; i < this.products.size(); i++) {
				if(!r.products.get(i).equals(this.products.get(i)))return this.products.get(i).compareTo(r.products.get(i));
			}
		}else return this.products.size() - r.products.size();
		if(r.educts.size() == this.educts.size()) {
			for (int i = 0; i < this.educts.size(); i++) {
				if(!r.educts.get(i).equals(this.educts.get(i)))return this.educts.get(i).compareTo(r.educts.get(i));
			}
		}else return this.educts.size() - r.educts.size();
		return 0;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Recipe) {
			Recipe recipe = (Recipe)obj;
			if(recipe.products.size() == this.products.size()) {
				for (int i = 0; i < this.products.size(); i++) {
					if(!recipe.products.get(i).equals(this.products.get(i)))return false;
				}
			}else return false;
			if(recipe.educts.size() == this.educts.size()) {
				for (int i = 0; i < this.educts.size(); i++) {
					if(!recipe.educts.get(i).equals(this.educts.get(i)))return false;
				}
			}else return false;
		}else return false;
		return true;
	}
	
	public static class Component implements Comparable<Component>{
		public int ID,n;
		public Component(int ID, int n){
			this.ID = ID;
			this.n = n;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Component) {
				Component c = (Component)obj;
				return (ID == c.ID && n == c.n);
			}else return false;
		}

		public int compareTo(Component c) {
			return ID != c.ID ? ID-c.ID : (n != c.n ? n-c.n : 0);
		}
	}
}
