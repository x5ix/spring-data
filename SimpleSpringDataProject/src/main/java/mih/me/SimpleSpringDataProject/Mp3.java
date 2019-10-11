package mih.me.SimpleSpringDataProject;

import org.springframework.stereotype.Component;

@Component
public class Mp3 {
	
	private int id;
	private String author;
	private String song;
	private Genre genre;
	
	public Mp3() {
	}
	public Mp3(String author, String song, Genre genre) {
		super();
		this.author = author;
		this.song = song;
		this.genre = genre;
	}
	@Override
	public String toString() {
		return "Mp3 [id=" + id + ", author=" + author + ", song=" + song + ", " + genre + "]";
	}
	public String string() {
		return id+" author: "+author+" song: "+song;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSong() {
		return song;
	}
	public void setSong(String song) {
		this.song = song;
	}
	public Genre getGenre() {
		return genre;
	}
	public void setGenre(Genre genre) {
		this.genre = genre;
	}
}
