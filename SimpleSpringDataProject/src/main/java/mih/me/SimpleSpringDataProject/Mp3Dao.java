package mih.me.SimpleSpringDataProject;

import java.util.List;
import java.util.Map;

public interface Mp3Dao {
	
	int insertList(List<Mp3> listMp3);
	
	int simpleJdbcInsert(Genre genre);
	
	int insertWithKeyHolder(Mp3 mp3);
	
	Map<Genre, Integer> getStat();
	
	List<Mp3> getMp3ByGenre(String genre);
	
	List<Mp3> getMp3ByAuthor(String author);
	
	Mp3 getMp3ById(int id);
	
	void delete(int id);
	
//	void insert(Mp3 mp3);
	
}