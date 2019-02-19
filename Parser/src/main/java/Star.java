
public class Star {

	private String name;
	private int birth;
	public Star() {}
	public Star(String name,int birth) {
		
		this.name = name;
		this.birth = birth;
	}
	public String getName() {
		return this.name;
	}
    public void setName(String nname){
        this.name = nname;
        return;
    }
	public int getBirth() {
		return this.birth;
	}
    public void setBirth(int nbirth){
        this.birth = nbirth;
        return;
    }
	
	
	public String toString() {
        String s = "Star Name: "+this.getName()+" Birth Year: "+ this.getBirth();
        return s;
		
	}
	
}
