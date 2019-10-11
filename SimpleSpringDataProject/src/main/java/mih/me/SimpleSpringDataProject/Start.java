package mih.me.SimpleSpringDataProject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {

	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
		Mp3Dao mp3Dao = (Mp3Dao)context.getBean("sqliteMp3Dao");
		Genre genre = new Genre("country");
		Mp3 mp3 = new Mp3("AuthorX", "Song2", genre);
		List<Mp3> listMp3 = new ArrayList<Mp3>();
		listMp3.add(mp3);
		listMp3.add(new Mp3("AuthorX", "Song3", new Genre("hip-hop")));
//		mp3Dao.insert(mp3);
		mp3Dao.delete(2);
		System.out.println(mp3Dao.getMp3ByGenre("PoP").toString());
		System.out.println(mp3Dao.getMp3ByAuthor("AutHorX").toString());
		System.out.println(mp3Dao.getMp3ById(9));
		System.out.println(mp3Dao.getStat());
		System.out.println(mp3Dao.insertWithKeyHolder(mp3));
//		System.out.println(mp3Dao.simpleJdbcInsert(genre));
//		System.out.println(mp3Dao.insertList(listMp3));
		((ConfigurableApplicationContext)context).close();
	}
}