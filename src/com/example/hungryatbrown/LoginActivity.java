package com.example.hungryatbrown;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

public class LoginActivity extends FragmentActivity {

	private final String TAG = "LoginFragment";
	private static final String USER_SKIPPED_LOGIN_KEY = "user_skipped_login";
	
	private boolean isResumed = false;
	private boolean skipLogin = false;
	private static final int LOGIN = 0;
	private Fragment[] fragments = new Fragment[LOGIN+1];
	private UiLifecycleHelper uiHelper;
	
	LoginFragment mLoginFragment;
	private Session.StatusCallback callback = new Session.StatusCallback() {
	        @Override
	        public void call(Session session, SessionState state, Exception exception) {
	            onSessionStateChange(session, state, exception);
	        }
	 };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null){
			skipLogin = savedInstanceState.getBoolean(USER_SKIPPED_LOGIN_KEY);
		}
		uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        
		setContentView(R.layout.activity_login);
		FragmentManager manager = getSupportFragmentManager();
		mLoginFragment = (LoginFragment) manager.findFragmentById(R.id.loginFragment);
		fragments[LOGIN] = mLoginFragment;
		
		FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(mLoginFragment);
        transaction.commit();
        
		mLoginFragment.setSkipLogin(new LoginFragment.SkipLoginCallback() {
			
			@Override
			public void onSkipLogin() {
				// TODO Auto-generated method stub
				skipLogin = true;
				startMainActivity();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showLoginFragment(){
		showFragment(LOGIN, false);
	}
	
	private void showFragment(int fragmentIndex, boolean addToBackStack){
		FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(fragments[i]);
            } else {
                transaction.hide(fragments[i]);
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}
	
	@Override
    public void onResume(){
    	super.onResume();
    	uiHelper.onResume();
    	isResumed = true;
    }
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		uiHelper.onDestroy();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	    outState.putBoolean(USER_SKIPPED_LOGIN_KEY, skipLogin);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
    protected void onResumeFragments() {
		super.onResumeFragments();
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()){
			startMainActivity();
			skipLogin = false;
		} else if (skipLogin){
			startMainActivity();
		} else {
			showFragment(LOGIN, false);
		}
		
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception){
		if (isResumed){
			FragmentManager manager = getSupportFragmentManager();
            int backStackSize = manager.getBackStackEntryCount();
            for (int i = 0; i < backStackSize; i++) {
                manager.popBackStack();
            }
			if (state.equals(SessionState.OPENED)){
				startMainActivity();
			} else if (state.isClosed()){
				showFragment(LOGIN, false);
			}
		}
		
	}
	
	private void startMainActivity(){
    	Intent intent = new Intent();
    	intent.setClass(this, MainActivity.class);
    	startActivity(intent);
    }
}
