package Items;

import java.util.ArrayList;

public class Recipe {

	public boolean researched = false;
	public ArrayList<component> products = new ArrayList<component>();
	public ArrayList<component> educts = new ArrayList<component>();
	
	public Recipe(){
		
	}
	
	public Recipe addP(int ID, int n){
		products.add(new component(ID,n));
		return this;
	}
	public Recipe addE(int ID, int n){
		educts.add(new component(ID,n));
		return this;
	}
	
	public class component{
		public int ID,n;
		public component(int ID, int n){
			this.ID = ID;
			this.n = n;
		}
	}
}
