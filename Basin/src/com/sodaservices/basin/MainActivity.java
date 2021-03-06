package com.sodaservices.basin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

public class MainActivity extends FragmentActivity {

	private static final int SPLASH = 0;
	private static final int FRAGMENT_COUNT = 1;

	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
	
	private boolean isResumed = false;
	
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = 
	    new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	    	onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	 // Add code to print out the key hash
//	    try {
//	        PackageInfo info = getPackageManager().getPackageInfo(
//	                "com.sodaservices.basin", 
//	                PackageManager.GET_SIGNATURES);
//	        for (Signature signature : info.signatures) {
//	            MessageDigest md = MessageDigest.getInstance("SHA1");
//	            md.update(signature.toByteArray());
//	            showToast("KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
//	            }
//	    } catch (NameNotFoundException e) {
//
//	    } catch (NoSuchAlgorithmException e) {

//	    }
	    
	    setContentView(R.layout.activity_main);
	    
	    uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);

	    FragmentManager fm = getSupportFragmentManager();
	    fragments[SPLASH] = fm.findFragmentById(R.id.splashFragment);
	    
	    FragmentTransaction transaction = fm.beginTransaction();
	    for(int i = 0; i < fragments.length; i++) {
	        transaction.hide(fragments[i]);
	    }
	    transaction.commit();
	}
	
	private void showFragment(int fragmentIndex, boolean addToBackStack) {
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
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	    isResumed = true;
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	    isResumed = false;
	}
	
	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
		//showToast("State has changed.");
	    // Only make changes if the activity is visible
	    if (isResumed) {
	    	//showToast("Activity is visible.");
	        FragmentManager manager = getSupportFragmentManager();
	        // Get the number of entries in the back stack
	        int backStackSize = manager.getBackStackEntryCount();
	        // Clear the back stack
	        for (int i = 0; i < backStackSize; i++) {
	            manager.popBackStack();
	        }
	        if (state.isOpened()) {
	        	Intent i = new Intent(this, com.sodaservices.basin.Product.class);
	        	startActivityForResult(i, 1);
	        	//showToast("State is open.");
	            // If the session state is open:
	            // Show the authenticated fragment
//	        	Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
//
//	        		  // callback after Graph API response with user object
//	        		  @Override
//	        		  public void onCompleted(GraphUser user, Response response) {
//	        			  
//	        			  TextView tv_welcome = (TextView) findViewById(R.id.welcome);
//	        			  tv_welcome.setText("Welcome " + user.getName() + "!");
//	        			  showFragment(SELECTION, false);
//	        		  }
//	        		});
	            //showFragment(SELECTION, false);
	        } else if (state.isClosed()) {
	        	//showToast("State is closed.");
	            // If the session state is closed:
	            // Show the login fragment
	            showFragment(SPLASH, false);
	        }
	    }
	}
	
	@Override
	protected void onResumeFragments() {
	    super.onResumeFragments();
	    Session session = Session.getActiveSession();

	    if (session != null && session.isOpened()) {
	    	Intent i = new Intent(this, com.sodaservices.basin.Product.class);
	    	startActivityForResult(i, 1);
	    	//showToast("session open and not null");
	        // if the session is already open,
	        // try to show the selection fragment
//	    	Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
//
//      		  // callback after Graph API response with user object
//      		  @Override
//      		  public void onCompleted(GraphUser user, Response response) {
//      			  
//      			  TextView tv_welcome = (TextView) findViewById(R.id.welcome);
//      			  tv_welcome.setText("Welcome " + user.getName());
//      			  showFragment(SELECTION, false);
//      		  }
//      		});
	        //showFragment(SELECTION, false);
	    } else {
	    	//showToast("session is closed");
	        // otherwise present the splash screen
	        // and ask the person to login.
	        showFragment(SPLASH, false);
	    }
	}
	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		//showToast("count is " + count);
//	    super.onActivityResult(requestCode, resultCode, data);
//	    //Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
//	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//showToast("onSaveInstanceState");
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	public void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}

}
