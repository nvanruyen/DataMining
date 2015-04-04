package ti2736c;

public class Similarity {

	int similar;
	User u;
	
	public Similarity(int s, User us){
		this.similar = s;
		this.u = us;
	}
	
	public int getSim(){
		return similar;
	}
	
	public User getUser(){
		return u;
	}
	
}
