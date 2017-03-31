package ir.tahasystem.music.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

	public static final String PREFS_NAME = "PREFS";

	public static void write(Activity context, String key, String value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void write(Context context, String key, String value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void writeBool(Context context, String key, boolean value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void write(Context context, String key, int value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putInt(key, value);
		editor.commit();
	}


	public static String read(Activity context, String key) {

		SharedPreferences preferences = context.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE);
		return preferences.getString(key, "");

	}

	public static String read(Context context, String key) {

		SharedPreferences preferences = context.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE);
		return preferences.getString(key, "");

	}

	public static String readNull(Context context, String key) {

		SharedPreferences preferences = context.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE);
		return preferences.getString(key, null);

	}


	public static int readInt(Context context, String key) {

		SharedPreferences preferences = context.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE);
		return preferences.getInt(key, 0);

	}

	public static int readIntOne(Context context, String key) {

		SharedPreferences preferences = context.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE);
		return preferences.getInt(key, 1);

	}

	public static int readIntNotify(Context context, String key) {

		SharedPreferences preferences = context.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE);
		return preferences.getInt(key, 15);

	}

	public static boolean readBool(Context context, String key) {

		SharedPreferences preferences = context.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE);
		return preferences.getBoolean(key, false);

	}

	public static boolean readBoolTrue(Context context, String key) {

		SharedPreferences preferences = context.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE);
		return preferences.getBoolean(key, true);

	}

}
