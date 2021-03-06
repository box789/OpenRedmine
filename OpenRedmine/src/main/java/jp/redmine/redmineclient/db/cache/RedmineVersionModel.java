package jp.redmine.redmineclient.db.cache;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import jp.redmine.redmineclient.entity.RedmineConnection;
import jp.redmine.redmineclient.entity.RedmineIssue;
import jp.redmine.redmineclient.entity.RedmineProjectVersion;
import jp.redmine.redmineclient.entity.RedmineUser;


public class RedmineVersionModel implements IMasterModel<RedmineProjectVersion> {
	protected Dao<RedmineProjectVersion, Integer> dao;
	public RedmineVersionModel(DatabaseCacheHelper helper) {
		try {
			dao = helper.getDao(RedmineProjectVersion.class);
		} catch (SQLException e) {
			Log.e("RedmineVersionModel","getDao",e);
		}
	}

	public List<RedmineProjectVersion> fetchAll() throws SQLException{
		return dao.queryForAll();
	}

	public List<RedmineProjectVersion> fetchAll(int connection) throws SQLException{
		List<RedmineProjectVersion> item;
		item = dao.queryForEq(RedmineUser.CONNECTION, connection);
		if(item == null){
			item = new ArrayList<RedmineProjectVersion>();
		}
		return item;
	}

	public RedmineProjectVersion fetchById(int connection, int statusId) throws SQLException{
		PreparedQuery<RedmineProjectVersion> query = dao.queryBuilder().where()
		.eq(RedmineProjectVersion.CONNECTION, connection)
		.and()
		.eq(RedmineProjectVersion.VERSION_ID, statusId)
		.prepare();
		RedmineProjectVersion item = dao.queryForFirst(query);
		if(item == null)
			item = new RedmineProjectVersion();
		return item;
	}

	public RedmineProjectVersion fetchById(int id) throws SQLException{
		RedmineProjectVersion item;
		item = dao.queryForId(id);
		if(item == null)
			item = new RedmineProjectVersion();
		return item;
	}


	@Override
	public long countByProject(int connection_id, long project_id) throws SQLException {
		QueryBuilder<RedmineProjectVersion, ?> builder = dao.queryBuilder();
		builder
			.setCountOf(true)
			.where()
				.eq(RedmineProjectVersion.CONNECTION, connection_id)
				.and()
				.eq(RedmineProjectVersion.PROJECT_ID, project_id)
				;
		return dao.countOf(builder.prepare());
	}

	@Override
	public RedmineProjectVersion fetchItemByProject(int connection_id,
			long project_id, long offset, long limit) throws SQLException {
		QueryBuilder<RedmineProjectVersion, ?> builder = dao.queryBuilder();
		builder
			.limit(limit)
			.offset(offset)
			.orderBy(RedmineProjectVersion.DUE_DATE, false)
			.orderBy(RedmineProjectVersion.NAME, true)
			.where()
				.eq(RedmineProjectVersion.CONNECTION, connection_id)
				.and()
				.eq(RedmineProjectVersion.PROJECT_ID, project_id)
				;
		RedmineProjectVersion item = builder.queryForFirst();
		if(item == null)
			item = new RedmineProjectVersion();
		return item;
	}

	public int insert(RedmineProjectVersion item) throws SQLException{
		int count = dao.create(item);
		return count;
	}

	public int update(RedmineProjectVersion item) throws SQLException{
		int count = dao.update(item);
		return count;
	}
	public int delete(RedmineProjectVersion item) throws SQLException{
		int count = dao.delete(item);
		return count;
	}
	public int delete(int id) throws SQLException{
		int count = dao.deleteById(id);
		return count;
	}

	public void refreshItem(RedmineIssue data) throws SQLException{
		RedmineProjectVersion item = refreshItem(data.getConnectionId(),data.getVersion());
		data.setVersion(item);
	}

	public RedmineProjectVersion refreshItem(RedmineConnection info,RedmineProjectVersion data) throws SQLException{
		return refreshItem(info.getId(),data);
	}

	public RedmineProjectVersion refreshItem(int id,RedmineProjectVersion data) throws SQLException{
		if(data == null)
			return null;
		RedmineProjectVersion version = this.fetchById(id, data.getVersionId());
		if(version.getId() == null){
			data.setConnectionId(id);
			this.insert(data);
			version = fetchById(id, data.getVersionId());
		} else {
			if(version.getModified() == null){
				version.setModified(new java.util.Date());
			}
			if(data.getModified() == null){
				data.setModified(new java.util.Date());
			}
			if(version.getModified().after(data.getModified())){
				data.setId(version.getId());
				data.setConnectionId(id);
				this.update(data);
			}
		}
		return version;
	}
}
