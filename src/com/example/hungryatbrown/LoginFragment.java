package com.example.hungryatbrown;

import com.facebook.widget.LoginButton;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginFragment extends Fragment{
	
	public interface SkipLoginCallback {
        void onSkipLogin();
    }

	private LoginButton mLoginButton;
	private SkipLoginCallback mSkipLoginCallback;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.login, container, false);
		mLoginButton = (LoginButton) view.findViewById(R.id.authButton);
		mLoginButton.setReadPermissions("user_friends");
		return view;
	}
	
	public void setSkipLogin(SkipLoginCallback callback){
		mSkipLoginCallback = callback;
	}
	 
}
