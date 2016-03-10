package com.lonewolfgames.framework;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jhyde on 7/7/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class Utilities {

    public static SimpleDateFormat SERVER_DATE_FORMAT 	= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

    private static final String DIGITS_PATTERN =
            "[^\\\\d]";

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_PATTERN_HARD =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final String PASSWORD_PATTERN_EASY =
            "^.{6,}$";
    private static final String WEBSITE_PATTERN =
            "^[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";
    private static final String PHONE_PATTERN =
            "^1?\\W*([2-9][0-8][0-9])\\W*([2-9][0-9]{2})\\W*([0-9]{4})(\\se?x?t?(\\d*))?";
    private static final String PHONE_PATTERN2 =
            "^1?\\s*\\W?\\s*([2-9][0-8][0-9])\\s*\\W?\\s*([2-9][0-9]{2})\\s*\\W?\\s*([0-9]{4})(\\e?x?t?(\\d*))?";

//    private static final String ISBN_10_PATTERN =
//            "^(?:ISBN(?:-10)?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[-\\ ]){3})" +
//                    "[-\\ 0-9X]{13}$)[0-9]{1,5}[-\\ ]?[0-9]+[-\\ ]?[0-9]+[-\\ ]?[0-9X]$";
//
//    private static final String ISBN_13_PATTERN =
//            "^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[-\\ ]){4})[-\\ 0-9]{17}$)" +
//                    "97[89][-\\ ]?[0-9]{1,5}[-\\ ]?[0-9]+[-\\ ]?[0-9]+[-\\ ]?[0-9]$";
//
//    private static final String ISBN_10_OR_13_PATTERN =
//            "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[-\\ ]){3})" +
//                    "[-\\ 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[-\\ ]){4})[-\\ 0-9]{17}$)" +
//                    "(?:97[89][-\\ ]?)?[0-9]{1,5}[-\\ ]?[0-9]+[-\\ ]?[0-9]+[-\\ ]?[0-9X]$";

	public static boolean isNetworkAvailable(Context context) {
		if(context == null) {
			return false;
		}
		
		boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    
	    return haveConnectedWifi || haveConnectedMobile;
	}

    public final static void displayToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public final static void displayToastShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public final static AlertDialog.Builder displayAlert(
            Context context,
            String title,
            String message,
            DialogInterface.OnClickListener positiveListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", positiveListener)
                .setIcon(android.R.drawable.ic_dialog_alert);
    }

    public final static AlertDialog.Builder displayAlert(
            Context context,
            String title,
            String message,
            String positive,
            String negative,
            DialogInterface.OnClickListener positiveListener,
            DialogInterface.OnClickListener negativeListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positive, positiveListener)
                .setNegativeButton(negative, negativeListener)
                .setIcon(android.R.drawable.ic_dialog_alert);
    }

    public static void hideSoftkeyboard(Activity activity, View view) {// Close the soft keyboard
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
	}

	public static String dateNow() {
		return SERVER_DATE_FORMAT.format(new Date());
	}

    public static Date parseDate(SimpleDateFormat format, String dateString) {
        Date date = new Date();

        try {
            date = format.parse(dateString);
        } catch(ParseException ex) {

        }

        return date;
    }

	public static float parseFloat(String floatString) {
		float number = 0;

		try {
			number = Float.parseFloat(floatString);
		} catch (NumberFormatException ex) {

		}

		return number;
	}

    public static int parseInt(String integerString) {
        int number = 0;

        try {
            number = Integer.parseInt(integerString);
        } catch (NumberFormatException ex) {

        }

        return number;
    }

    public static long parseLong(String longString) {
        long number = 0;

        // Remove all non-digit characters
        longString = longString.replace(DIGITS_PATTERN, "");

        try {
            number = Long.parseLong(longString);
        } catch (NumberFormatException ex) {

        }

        return number;
    }

    public static boolean parseBoolean(String booleanString) {
        boolean b = false;

        try {
            b = Boolean.parseBoolean(booleanString);
        } catch(Exception ex) {

        }

        return b;
    }

    public static boolean validateEmail(final String email) {
        if(email.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static boolean validatePassword(final String password) {
        if(password.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile(PASSWORD_PATTERN_EASY);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public static boolean validateWebsite(final String website) {
        if(website.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile(WEBSITE_PATTERN);
        Matcher matcher = pattern.matcher(website);

        return matcher.matches();
    }

    public static boolean validatePhone(final String phone) {
        if(phone.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile(PHONE_PATTERN2);
        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();
    }

//    public static boolean validateISBN10(final String isbn) {
//        if(isbn.isEmpty()) {
//            return false;
//        }
//
//        Pattern pattern = Pattern.compile(ISBN_10_PATTERN);
//        Matcher matcher = pattern.matcher(isbn);
//
//        return matcher.matches();
//    }
//
//    public static boolean validateISBN13(final String isbn) {
//        if(isbn.isEmpty()) {
//            return false;
//        }
//
//        Pattern pattern = Pattern.compile(ISBN_13_PATTERN);
//        Matcher matcher = pattern.matcher(isbn);
//
//        return matcher.matches();
//    }
//
//    public static boolean validateISBN10OR13(final String isbn) {
//        if(isbn.isEmpty()) {
//            return false;
//        }
//
//        Pattern pattern = Pattern.compile(ISBN_10_OR_13_PATTERN);
//        Matcher matcher = pattern.matcher(isbn);
//
//        return matcher.matches();
//    }

    public static void setSelectableItemBackground(Context context, View view) {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        view.setBackgroundResource(backgroundResource);
        typedArray.recycle();
    }

    public static boolean validateISBN10(String isbn) {
        if(isbn == null || isbn.isEmpty()) {
            return false;
        }

        // remove any hyphens
        isbn = isbn.replaceAll("-", "");

        // must be a 10 digit ISBN
        if(isbn.length() != 10) {
            return false;
        }

        try {
            int total = 0;
            for(int x = 0; x < 9; x++) {
                int digit = Integer.parseInt(isbn.substring(x, x + 1));
                total += ((10 - x) * digit);
            }

            String checksum = Integer.toString((11 - (total % 11)) % 11);
            if("10".equals(checksum)) {
                checksum = "X";
            }

            return checksum.equals(isbn.substring(9));
        } catch(NumberFormatException nfe) {
            //to catch invalid ISBNs that have non-numeric characters in them
            return false;
        }
    }

    public static boolean validateISBN13(String isbn) {
        if(isbn == null || isbn.isEmpty()) {
            return false;
        }

        //remove any hyphens
        isbn = isbn.replaceAll("-", "");

        //must be a 13 digit ISBN
        if(isbn.length() != 13) {
            return false;
        }

        try {
            int tot = 0;
            for(int i = 0; i < 12; i++) {
                int digit = Integer.parseInt(isbn.substring(i, i + 1));
                tot += (i % 2 == 0) ? digit * 1 : digit * 3;
            }

            //checksum must be 0-9. If calculated as 10 then = 0
            int checksum = 10 - (tot % 10);
            if(checksum == 10) {
                checksum = 0;
            }

            return checksum == Integer.parseInt(isbn.substring(12));
        } catch(NumberFormatException nfe) {
            //to catch invalid ISBNs that have non-numeric characters in them
            return false;
        }
    }
}
