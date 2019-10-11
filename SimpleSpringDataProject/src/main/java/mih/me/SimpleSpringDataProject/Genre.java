package mih.me.SimpleSpringDataProject;

public class Genre {
	private int id;
	private String genreName;
	
	public Genre() {
	}
	public Genre(String genreName) {
		this.genreName = genreName.toLowerCase();
	}
	@Override
	public String toString() {
		return genreName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGenreName() {
		return genreName;
	}
	public void setGenreName(String genreName) {
		this.genreName = genreName.toLowerCase();
	}
}