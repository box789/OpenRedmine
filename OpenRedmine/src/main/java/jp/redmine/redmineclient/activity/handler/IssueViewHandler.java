package jp.redmine.redmineclient.activity.handler;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;

import jp.redmine.redmineclient.IssueActivity;
import jp.redmine.redmineclient.IssueFilterActivity;
import jp.redmine.redmineclient.ProjectActivity;
import jp.redmine.redmineclient.R;
import jp.redmine.redmineclient.WikiViewActivity;
import jp.redmine.redmineclient.fragment.Empty;
import jp.redmine.redmineclient.fragment.IssueEdit;
import jp.redmine.redmineclient.param.FilterArgument;
import jp.redmine.redmineclient.param.IssueArgument;
import jp.redmine.redmineclient.param.ProjectArgument;
import jp.redmine.redmineclient.param.WikiArgument;

public class IssueViewHandler extends Core
	implements IssueActionInterface, WebviewActionInterface {

	public IssueViewHandler(ActivityRegistry manager) {
		super(manager);
	}

	@Override
	public void onIssueFilterList(final int connectionid, final int filterid) {
		kickActivity(IssueFilterActivity.class, new IntentFactory() {
			@Override
			public void generateIntent(Intent intent) {
				FilterArgument arg = new FilterArgument();
				arg.setIntent(intent);
				arg.setConnectionId(connectionid);
				arg.setFilterId(filterid);
			}
		});
	}

	@Override
	public void onIssueList(final int connectionid, final long projectid) {
		kickActivity(ProjectActivity.class, new IntentFactory() {
			@Override
			public void generateIntent(Intent intent) {
				ProjectArgument arg = new ProjectArgument();
				arg.setIntent(intent);
				arg.setConnectionId(connectionid);
				arg.setProjectId(projectid);
			}
		});
	}

	@Override
	public void onIssueSelected(final int connectionid, final int issueid) {
		kickActivity(IssueActivity.class, new IntentFactory() {
			@Override
			public void generateIntent(Intent intent) {
				IssueArgument arg = new IssueArgument();
				arg.setIntent(intent);
				arg.setConnectionId(connectionid);
				arg.setIssueId(issueid);
			}
		});
	}

	@Override
	public void onIssueEdit(int connectionid, int issueid) {
		final IssueArgument arg = new IssueArgument();
		arg.setArgument();
		arg.setConnectionId(connectionid);
		arg.setIssueId(issueid);

		runTransaction(new TransitFragment() {
			@Override
			public void action(FragmentTransaction tran) {
				tran.replace(R.id.fragmentOne, IssueEdit.newInstance(arg));
				tran.replace(R.id.fragmentOneFooter, Empty.newInstance());
			}
		}, null);
	}

	@Override
	public void onIssueAdd(int connectionid, long projectId) {
		final IssueArgument arg = new IssueArgument();
		arg.setArgument();
		arg.setConnectionId(connectionid);
		arg.setProjectId(projectId);
		arg.setIssueId(-1);

		runTransaction(new TransitFragment() {
			@Override
			public void action(FragmentTransaction tran) {
				tran.replace(R.id.fragmentOne, IssueEdit.newInstance(arg));
			}
		}, null);
	}

	@Override
	public void onIssueRefreshed(int connectionid, int issueid) {
		//TODO
	}

	@Override
	public void issue(int connection, int issueid) {
		onIssueSelected(connection, issueid);
	}

	@Override
	public boolean url(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		manager.kickActivity(intent);
		return true;
	}

	@Override
	public void wiki(final int connection, final long projectid, final String title) {
		kickActivity(WikiViewActivity.class, new IntentFactory() {
			@Override
			public void generateIntent(Intent intent) {
				WikiArgument arg = new WikiArgument();
				arg.setIntent(intent);
				arg.setConnectionId(connection);
				arg.setProjectId(projectid);
				arg.setWikiTitle(title);
			}
		});
	}

}
