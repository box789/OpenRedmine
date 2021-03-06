package jp.redmine.redmineclient.form;

import com.andreabaccega.widget.FormEditText;

import jp.redmine.redmineclient.R;
import jp.redmine.redmineclient.entity.RedmineConnection;
import jp.redmine.redmineclient.form.helper.FormHelper;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

public class RedmineConnectionActivityForm extends FormHelper {
	public FormEditText editName;
	public FormEditText editUrl;
	public FormEditText editToken;
	public EditText editAuthID;
	public EditText editAuthPasswd;
	public Button buttonSave;
	public CheckBox checkHttpAuth;
	public LinearLayout formHttpAuth;
	public LinearLayout formPermitUnsafe;
	public CheckBox checkUnsafeConnection;
	public EditText editCertKey;
	public Button buttonAccess;
	public Button buttonUrl1;
	public Button buttonUrl2;
	public Button buttonUrl3;
	public Button buttonUrl4;
	public RedmineConnectionActivityForm(View activity){
		this.setup(activity);
		this.setupDefaults();
	}


	public void setup(View activity){
		editName = (FormEditText)activity.findViewById(R.id.editName);
		editUrl = (FormEditText)activity.findViewById(R.id.editURL);
		editToken = (FormEditText)activity.findViewById(R.id.editToken);
		editAuthID = (EditText)activity.findViewById(R.id.editAuthID);
		editAuthPasswd = (EditText)activity.findViewById(R.id.editAuthPasswd);
		buttonSave = (Button)activity.findViewById(R.id.buttonSave);
		buttonAccess = (Button)activity.findViewById(R.id.buttonAccess);
		formHttpAuth = (LinearLayout)activity.findViewById(R.id.formHttpAuth);
		checkHttpAuth = (CheckBox)activity.findViewById(R.id.checkHttpAuth);
		formPermitUnsafe = (LinearLayout)activity.findViewById(R.id.formPermitUnsafe);
		checkUnsafeConnection = (CheckBox)activity.findViewById(R.id.checkPermitUnsafe);
		editCertKey = (EditText)activity.findViewById(R.id.editCertKey);

		buttonUrl1 = (Button)activity.findViewById(R.id.buttonUrl1);
		buttonUrl2 = (Button)activity.findViewById(R.id.buttonUrl2);
		buttonUrl3 = (Button)activity.findViewById(R.id.buttonUrl3);
		buttonUrl4 = (Button)activity.findViewById(R.id.buttonUrl4);
	}

	public void setupEvents(){
		checkHttpAuth.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton compoundbutton, boolean flag) {
				performSetEnabled(formHttpAuth,flag);
			}
		});
		checkUnsafeConnection.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton compoundbutton, boolean flag) {
				performSetEnabled(formPermitUnsafe,flag);
			}
		});
		OnClickListener onClick1 = new OnClickListener(){
			@Override
			public void onClick(View v) {
				Button btn = (Button)v;
				String regex = (String)btn.getTag();
				String text = editUrl.getText().toString();
				text = TextUtils.isEmpty(text) ? "" : text;
				text = text.replaceAll(regex, "");
				if(regex.startsWith("^")){
					text = btn.getText().toString().concat(text);
				} else if(regex.endsWith("$") && !TextUtils.isEmpty(text)) {
					text = text.concat(btn.getText().toString());
				}
				if(!TextUtils.isEmpty(text)){
					editUrl.setText(text);
					editUrl.setSelection(text.length());
				}
				editUrl.requestFocusFromTouch();
			}
		};
		buttonUrl1.setOnClickListener(onClick1);
		buttonUrl2.setOnClickListener(onClick1);
		buttonUrl3.setOnClickListener(onClick1);
		buttonUrl4.setOnClickListener(onClick1);
	}

	public void setupDefaults(){
		performSetEnabled(formHttpAuth,checkHttpAuth.isChecked());
		performSetEnabled(formPermitUnsafe,checkUnsafeConnection.isChecked());
	}

	@Override
	public boolean Validate(){
		return ValidateForms(
				editName
				,editUrl
				,editToken
				);
	}

	public String getUrl(){
		if(ValidateForm(editUrl)){
			return editUrl.getText().toString();
		} else {
			return "";
		}
	}
	public String getAuthID(){
		return checkHttpAuth.isChecked() ? editAuthID.getText().toString() : "";
	}
	public String getAuthPassword(){
		return checkHttpAuth.isChecked() ? editAuthPasswd.getText().toString() : "";
	}
	public void setUnsafeConnection(boolean flag){
		checkUnsafeConnection.setChecked(flag);
		performSetEnabled(formPermitUnsafe,flag);
	}
	public boolean isUnsafeConnection(){
		return checkUnsafeConnection.isChecked();
	}
	public void setAuthentication(boolean flag){
		checkHttpAuth.setChecked(flag);
		performSetEnabled(formHttpAuth,flag);
	}
	public void setAuthentication(String id, String passwd){
		boolean flag = !("".equals(id) && "".equals(passwd));
		setAuthentication(flag);
		editAuthID.setText(id);
		editAuthPasswd.setText(passwd);
	}
	public void setToken(String token){
		editToken.setText(token);
	}
	public String getToken(){
		return editToken.getText().toString();
	}

	public void setValue(RedmineConnection rd){

		editName.setText(rd.getName());
		editUrl.setText(rd.getUrl());
		editToken.setText(rd.getToken());
		checkHttpAuth.setChecked(rd.isAuth());
		editAuthID.setText(rd.getAuthId());
		editAuthPasswd.setText(rd.getAuthPasswd());
		checkUnsafeConnection.setChecked(rd.isPermitUnsafe());
		editCertKey.setText(rd.getCertKey());
		setupDefaults();
	}
	public void getValue(RedmineConnection rd){

		rd.setName(editName.getText().toString());
		rd.setUrl(editUrl.getText().toString());
		rd.setToken(editToken.getText().toString());
		rd.setAuth(checkHttpAuth.isChecked());
		rd.setAuthId(editAuthID.getText().toString());
		rd.setAuthPasswd(editAuthPasswd.getText().toString());
		rd.setPermitUnsafe(checkUnsafeConnection.isChecked());
		rd.setCertKey(editCertKey.getText().toString());
	}
}

