package mih.me.SimpleSpringDataProject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class SqliteMp3Dao implements Mp3Dao {
	
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate jdbcTemplateNP;
	private SimpleJdbcInsert insertGenre;
	
	@Override
	public int insertList(List<Mp3> listMp3) {
//		String sql = "insert into mp3 (author, song) values (:author, :song) ";
//		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(listMp3);
//		int[] updateCounts = jdbcTemplateNP.batchUpdate(sql, params);
//		return updateCounts.length;
		int i = 0;
		for (Mp3 mp3 : listMp3) {
			insertWithKeyHolder(mp3);
			i++;			
		}
		return i;
	}
	@Override
	public int simpleJdbcInsert(Genre genre) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("genre", genre.getGenreName());
		return insertGenre.execute(params);
	}
	@Override
	@Transactional
	public int insertWithKeyHolder(Mp3 mp3) {
		
		String sqlInsertGenre = "insert into genre (genre) values (:genreName)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("genreName", mp3.getGenre().getGenreName());
		jdbcTemplateNP.update(sqlInsertGenre, params, keyHolder);
		int genreId = keyHolder.getKey().intValue();
		
		System.out.println(TransactionSynchronizationManager.isActualTransactionActive());
		
		String sql = "insert into mp3 (author, song, genreId) values (:author, :song, :genreId)";
		params.addValue("author", mp3.getAuthor());
		params.addValue("song", mp3.getSong());
		params.addValue("genreId", genreId);
		jdbcTemplateNP.update(sql, params, keyHolder);
		return keyHolder.getKey().intValue();
	}
	@Override
	public Map<Genre, Integer> getStat() {
		String sql = "select genreName, count(*) as count from mp3View group by genreName";
		return jdbcTemplateNP.query(sql, new Mp3ResultSetExtractor());
	}
	@Override
	public List<Mp3> getMp3ByGenre(String genre) {
		String sql = "select * from mp3View where genreName = :genreName";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("genreName", genre.toLowerCase());
		return jdbcTemplateNP.query(sql, params, new Mp3RowMapper());
	}
	@Override
	public List<Mp3> getMp3ByAuthor(String author) {
		String sql = "select * from mp3View where authorMp3 like :authorMp3 ";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("authorMp3", "%" + author.toLowerCase() + "%");
		return jdbcTemplateNP.query(sql, params, new Mp3RowMapper());
	}
	@Override
	public Mp3 getMp3ById(int idMp3) {
		String sql = "select * from mp3View where idMp3 = :id";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", idMp3);
		return jdbcTemplateNP.queryForObject(sql, params, new Mp3RowMapper());
	}
	@Override
	public void delete(int id) {
		String sql = "delete from mp3 where id = ?";
		jdbcTemplate.update(sql, id);
	}
//	@Override
//	public void insert(Mp3 mp3) {
//		String sql = "insert into mp3 (author, song) values (?, ?)";
//		jdbcTemplate.update(sql, new Object[] {mp3.getAuthor(), mp3.getSong()});
//	}
	
	
	@Autowired
	public void init(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.jdbcTemplateNP = new NamedParameterJdbcTemplate(dataSource);
		this.insertGenre = new SimpleJdbcInsert(dataSource).withTableName("genre");
	}
	
	private static final class Mp3RowMapper implements RowMapper<Mp3> {

		@Override
		public Mp3 mapRow(ResultSet rs, int rowNum) throws SQLException {
			Genre genre = new Genre();
			genre.setId(rs.getInt("genreId"));
			genre.setGenreName(rs.getString("genreName"));
			Mp3 mp3 = new Mp3();
			mp3.setId(rs.getInt("idMp3"));
			mp3.setSong(rs.getString("songMp3"));
			mp3.setAuthor(rs.getString("authorMp3"));
			mp3.setGenre(genre);
			return mp3;
		}
	}
	
	private static final class Mp3ResultSetExtractor implements ResultSetExtractor<Map<Genre, Integer>>{

		@Override
		public Map<Genre, Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Genre, Integer> mapMp3Stat = new HashMap<Genre, Integer>();
			while(rs.next()) {
				mapMp3Stat.put(new Genre(rs.getString("genreName")), rs.getInt("count"));
			}
			return mapMp3Stat;
		}
	}
}
