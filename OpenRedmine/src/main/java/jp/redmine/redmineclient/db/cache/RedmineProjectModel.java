package jp.redmine.redmineclient.db.cache;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import jp.redmine.redmineclient.entity.RedmineConnection;
import jp.redmine.redmineclient.entity.RedmineIssue;
import jp.redmine.redmineclient.entity.RedmineProject;
import android.util.Log;


public class RedmineProjectModel  implements IMasterModel<RedmineProject>{
	protected Dao<RedmineProject, Long> dao;
	public RedmineProjectModel(DatabaseCacheHelper helper) {
		try {
			dao = helper.getDao(RedmineProject.class);
		} catch (SQLException e) {
			Log.e("RedmineProjectModel","getDao",e);
		}
	}

	public List<RedmineProject> fetchAll() throws SQLException{
		return dao.queryForAll();
	}

	public List<RedmineProject> fetchAll(int connection) throws SQLException{
		List<RedmineProject> item;
		item = dao.queryForEq(RedmineProject.CONNECTION, connection);
		if(item == null){
			item = new ArrayList<RedmineProject>();
		}
		return item;
	}

	public RedmineProject fetchById(int connection, int projectId) throws SQLException{
		PreparedQuery<RedmineProject> query = dao.queryBuilder().where()
		.eq(RedmineProject.CONNECTION, connection)
		.and()
		.eq(RedmineProject.PROJECT_ID, projectId)
		.prepare();
		Log.d("RedmineProject",query.getStatement());
		RedmineProject item = dao.queryForFirst(query);
		if(item == null)
			item = new RedmineProject();
		return item;
	}

	public RedmineProject fetchById(long id) throws SQLException{
		RedmineProject item;
		item = dao.queryForId(id);
		if(item == null)
			item = new RedmineProject();
		return item;
	}

	public int insert(RedmineProject item) throws SQLException{
		int count = dao.create(item);
		return count;
	}

	public int update(RedmineProject item) throws SQLException{
		int count = dao.update(item);
		return count;
	}
	public int delete(RedmineProject item) throws SQLException{
		int count = dao.delete(item);
		return count;
	}
	public int delete(long id) throws SQLException{
		int count = dao.deleteById(id);
		return count;
	}
	public void refreshItem(RedmineConnection info,RedmineIssue data) throws SQLException{
		data.setProject(refreshItem(info,data.getProject()));
	}

	public RedmineProject refreshItem(RedmineConnection info,RedmineProject data) throws SQLException{

		RedmineProject project = this.fetchById(info.getId(), data.getProjectId());
		data.setRedmineConnection(info);
		if(project.getId() == null){
			this.insert(data);
			data = fetchById(info.getId(), data.getProjectId());
		} else {
			data.setId(project.getId());
			if(data.getModified() != null && project.getModified().after(data.getModified())){
				this.update(data);
			}
		}
		return data;
	}

	@Override
	public long countByProject(int connection_id, long project_id) throws SQLException {
		QueryBuilder<RedmineProject, ?> builder = dao.queryBuilder();
		builder
			.setCountOf(true)
			.where()
				.eq(RedmineProject.CONNECTION, connection_id)
				;
		return dao.countOf(builder.prepare());
	}

	@Override
	public RedmineProject fetchItemByProject(int connection_id,
			long project_id, long offset, long limit) throws SQLException {
		QueryBuilder<RedmineProject, Long> builder = dao.queryBuilder();
		Where<RedmineProject,Long> where = builder.where()
			.eq(RedmineProject.CONNECTION, connection_id)
			;
		setupLimit(builder,offset,limit);
		builder.setWhere(where);
		builder.orderBy(RedmineProject.PROJECT_ID, true);
		RedmineProject item = dao.queryForFirst(builder.prepare());
		if(item == null)
			item = new RedmineProject();
		return item;
	}
	protected void setupLimit(QueryBuilder<?,?> builder,Long startRow, Long maxRows) throws SQLException{
		if(maxRows != null){
			builder.limit(maxRows);
		}
		if(startRow != null && startRow != 0){
			builder.offset(startRow);
		}
	}
}
