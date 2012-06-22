package com.flattr.android.intentintegration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public final class FlattrIntentIntegrator {

  private static final String TAG = FlattrIntentIntegrator.class.getSimpleName();

  public static final String DEFAULT_TITLE = "Install Flattr?";
  public static final String DEFAULT_MESSAGE =
      "This application requires Flattr. Would you like to install it?";
  public static final String DEFAULT_YES = "Yes";
  public static final String DEFAULT_NO = "No";

  private static final String PACKAGE = "com.flattr.android";

  public static final Method PACKAGE_SETTER;
  static {
    Method temp;
    try {
      temp = Intent.class.getMethod("setPackage", new Class[] {String.class});
    } catch (NoSuchMethodException nsme) {
      temp = null;
    }
    PACKAGE_SETTER = temp;
  }
  
  public FlattrIntentIntegrator() {
  }
  
  public static AlertDialog showThing(Activity activity, String thing) {
	  return showThing(activity, thing, false);
  }

  public static AlertDialog showThing(Activity activity, String thing, boolean flattr) {
	  if (isURL(thing.toString())) {
		  return showThing(activity, DEFAULT_TITLE, DEFAULT_MESSAGE, DEFAULT_YES, DEFAULT_NO, null, thing, flattr);
	  }
	  return showThing(activity, DEFAULT_TITLE, DEFAULT_MESSAGE, DEFAULT_YES, DEFAULT_NO, thing, null, flattr);
  }
  
  public static AlertDialog flattrThing(Activity activity, String thing, String title) {
	  return flattrThing(activity, DEFAULT_TITLE, DEFAULT_MESSAGE, DEFAULT_YES, DEFAULT_NO, thing, title);
  }
  
  public static AlertDialog showThing(Activity activity,
                                         String stringTitle,
                                         String stringMessage,
                                         String stringButtonYes,
                                         String stringButtonNo,
                                         String thingid,
                                         String thingUrl,
                                         boolean autoFlattr) {
    Intent intentThing = new Intent(PACKAGE + ".THING");
    setPackage(intentThing);
    intentThing.addCategory(Intent.CATEGORY_DEFAULT);

    if (thingid != null) {
        intentThing.putExtra("id", thingid);
    }
    if (thingUrl != null) {
        intentThing.putExtra("url", thingUrl);
    }
    intentThing.putExtra("flattr", autoFlattr);

    try {
      activity.startActivity(intentThing);
      return null;
    } catch (ActivityNotFoundException e) {
      return showDownloadDialog(activity, stringTitle, stringMessage, stringButtonYes, stringButtonNo);
    }
  }
  
	  public static AlertDialog flattrThing(Activity activity,
								          String stringTitle,
								          String stringMessage,
								          String stringButtonYes,
								          String stringButtonNo,
								          String thingid,
								          String title) {
		  
			Intent intentThing = new Intent(PACKAGE + ".FLATTR");
			setPackage(intentThing);
			intentThing.addCategory(Intent.CATEGORY_DEFAULT);
			intentThing.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			
			if (thingid != null) {
				intentThing.putExtra("id", thingid);
			}
			if (title != null && !title.trim().equals("")) {
				intentThing.putExtra("title", title);
			}
			
			try {
				activity.startActivity(intentThing);
				return null;
			} catch (ActivityNotFoundException e) {
				return showDownloadDialog(activity, stringTitle, stringMessage, stringButtonYes, stringButtonNo);
			}
	}

  private static AlertDialog showDownloadDialog(final Activity activity,
		  String stringTitle,
		  String stringMessage,
		  String stringButtonYes,
		  String stringButtonNo) {
    AlertDialog.Builder downloadDialog = new AlertDialog.Builder(activity);
    downloadDialog.setTitle(stringTitle);
    downloadDialog.setMessage(stringMessage);
    downloadDialog.setPositiveButton(stringButtonYes, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialogInterface, int i) {
        Uri uri = Uri.parse("market://search?q=pname:" + PACKAGE);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
      }
    });
    downloadDialog.setNegativeButton(stringButtonNo, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialogInterface, int i) {}
    });
    return downloadDialog.show();
  }

  private static void setPackage(Intent intent) {
    if (PACKAGE_SETTER != null) {
      try {
        PACKAGE_SETTER.invoke(intent, PACKAGE);
      } catch (InvocationTargetException ite) {
        Log.w(TAG, ite.getTargetException());
      } catch (IllegalAccessException iae) {
        Log.w(TAG, iae);
      }
    }
  }
  
  private static boolean isURL(String s) {
	  if (s.toLowerCase().startsWith("http://") || s.toLowerCase().startsWith("https://")) {
		  return true;
	  }
	  return false;
  }

}
