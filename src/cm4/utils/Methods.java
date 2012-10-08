package cm4.utils;


import cm4.items.AI;
import cm4.listeners.CustomOnItemLongClickListener;
import cm4.listeners.DialogButtonOnClickListener;
import cm4.listeners.DialogButtonOnTouchListener;
import cm4.listeners.DialogListener;
import cm4.listeners.DialogOnItemClickListener;
import cm4.listeners.DialogOnItemLongClickListener;
import cm4.main.MainActv;
import cm4.main.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import android.os.AsyncTask;

// Apache
import org.apache.commons.lang.StringUtils;


// REF=> http://commons.apache.org/net/download_net.cgi
//REF=> http://www.searchman.info/tips/2640.html

//import org.apache.commons.net.ftp.FTPReply;

public class Methods {

	static int counter;		// Used => sortFileList()
	
	
	/****************************************
	 * Enums
	 ****************************************/
	public static enum DialogTags {
		// Generics
		dlg_generic_dismiss, dlg_generic_dismiss_second_dialog, dlg_generic_dismiss_third_dialog,
		
			
		// dlg_create_folder.xml
		dlg_create_folder_ok, dlg_create_folder_cancel,

		// dlg_input_empty.xml
		dlg_input_empty_reenter, dlg_input_empty_cancel,
		
		// dlg_confirm_create_folder.xml
		dlg_confirm_create_folder_ok, dlg_confirm_create_folder_cancel,

		// dlg_confirm_remove_folder.xml
		dlg_confirm_remove_folder_ok, dlg_confirm_remove_folder_cancel,

		// dlg_drop_table.xml
		dlg_drop_table_btn_cancel, dlg_drop_table,
		
		// dlg_confirm_drop.xml
		dlg_confirm_drop_table_btn_ok, dlg_confirm_drop_table_btn_cancel,
		
		// dlg_add_memos.xml
		dlg_add_memos_bt_add, dlg_add_memos_bt_cancel, dlg_add_memos_bt_patterns,
		dlg_add_memos_gv,

		// dlg_move_files.xml
		dlg_move_files_move, dlg_move_files,
		
		// dlg_confirm_move_files.xml	=> ok, cancel, dlg tag
		dlg_confirm_move_files_ok, dlg_confirm_move_files_cancel, dlg_confirm_move_files,

		// dlg_item_menu.xml
		dlg_item_menu_bt_cancel, dlg_item_menu,

		// dlg_create_table.xml
		dlg_create_table_bt_create,

		// dlg_memo_patterns.xml
		dlg_memo_patterns,
		
		// dlg_register_patterns.xml
		dlg_register_patterns_register,

		// dlg_search.xml
		dlg_search, dlg_search_ok,

		// dlg_admin_patterns.xml

		// dlg_confirm_delete_patterns.xml
		dlg_confirm_delete_patterns_ok,
		
	}//public static enum DialogTags
	
	public static enum DialogItemTags {
		// dlg_moveFiles(Activity actv)
		dlg_move_files,
		
		// dlg_add_memos.xml
		dlg_add_memos_gv,

		// dlg_db_admin.xml
		dlg_db_admin_lv,

		// dlg_admin_patterns.xml
		dlg_admin_patterns_lv,

		// dlg_delete_patterns.xml
		dlg_delete_patterns_gv,
		
	}//public static enum DialogItemTags
	
	
	public static enum ButtonTags {
		// MainActivity.java
		ib_up,
		
		// DBAdminActivity.java
		db_manager_activity_create_table, db_manager_activity_drop_table, 
		db_manager_activity_register_patterns,
		
		// thumb_activity.xml
		thumb_activity_ib_back, thumb_activity_ib_bottom, thumb_activity_ib_top,
		
		// image_activity.xml
		image_activity_back, image_activity_prev, image_activity_next,
		
		// TIListAdapter.java
		tilist_cb,
		
	}//public static enum ButtonTags
	
	public static enum ItemTags {
		
		// MainActivity.java
		dir_list,
		
		// ThumbnailActivity.java
		dir_list_thumb_actv,
		
		// Methods.java
		dir_list_move_files,
		
		// TIListAdapter.java
		tilist_checkbox,
		
		
	}//public static enum ItemTags

	public static enum MoveMode {
		// TIListAdapter.java
		ON, OFF,
		
	}//public static enum MoveMode

	public static enum PrefenceLabels {
		
		CURRENT_PATH,
		
		thumb_actv,
		
		chosen_list_item,
		
	}//public static enum PrefenceLabels

	public static enum ListTags {
		// MainActivity.java
		actv_main_lv,
		
	}//public static enum ListTags

	
	/****************************************
	 * Vars
	 ****************************************/
	public static final int vibLength_click = 35;

	static int tempRecordNum = 20;

	/****************************************
	 * Methods
	 ****************************************/
	public static void sortFileList(File[] files) {
		// REF=> http://android-coding.blogspot.jp/2011/10/sort-file-list-in-order-by-implementing.html
		/*----------------------------
		 * 1. Prep => Comparator
		 * 2. Sort
			----------------------------*/
		/*----------------------------
		 * 1. Prep => Comparator
			----------------------------*/
		Comparator<? super File> filecomparator = new Comparator<File>(){
			
			public int compare(File file1, File file2) {
				/*----------------------------
				 * 1. Prep => Directory
				 * 2. Calculate
				 * 3. Return
					----------------------------*/
				
				int pad1=0;
				int pad2=0;
				
				if(file1.isDirectory())pad1=-65536;
				if(file2.isDirectory())pad2=-65536;
				
				int res = pad2-pad1+file1.getName().compareToIgnoreCase(file2.getName());
				
				return res;
			} 
		 };//Comparator<? super File> filecomparator = new Comparator<File>()
		 
		/*----------------------------
		 * 2. Sort
			----------------------------*/
		Arrays.sort(files, filecomparator);

	}//public static void sortFileList(File[] files)

	public static void confirm_quit(Activity actv, int keyCode) {
		
		// TODO 自動生成されたメソッド・スタブ
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			
			AlertDialog.Builder dialog=new AlertDialog.Builder(actv);
			
	        dialog.setTitle("アプリの終了");
	        dialog.setMessage("アプリを終了しますか？");
	        
	        dialog.setPositiveButton("終了",new DialogListener(actv, dialog, 0));
	        dialog.setNegativeButton("キャンセル",new DialogListener(actv, dialog, 1));
	        
	        dialog.create();
	        dialog.show();
			
		}//if (keyCode==KeyEvent.KEYCODE_BACK)
		
	}//public static void confirm_quit(Activity actv, int keyCode)

	public static boolean set_pref(Activity actv, String pref_name, String value) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/*----------------------------
		 * 2. Get editor
			----------------------------*/
		SharedPreferences.Editor editor = prefs.edit();

		/*----------------------------
		 * 3. Set value
			----------------------------*/
		editor.putString(pref_name, value);
		
		try {
			editor.commit();
			
			return true;
			
		} catch (Exception e) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Excption: " + e.toString());
			
			return false;
		}

	}//public static boolean set_pref(String pref_name, String value)

	public static String get_pref(Activity actv, String pref_name, String defValue) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/*----------------------------
		 * Return
			----------------------------*/
		return prefs.getString(pref_name, defValue);

	}//public static boolean set_pref(String pref_name, String value)

	public static boolean set_pref(Activity actv, String pref_name, int value) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/*----------------------------
		 * 2. Get editor
			----------------------------*/
		SharedPreferences.Editor editor = prefs.edit();

		/*----------------------------
		 * 3. Set value
			----------------------------*/
		editor.putInt(pref_name, value);
		
		try {
			editor.commit();
			
			return true;
			
		} catch (Exception e) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Excption: " + e.toString());
			
			return false;
		}

	}//public static boolean set_pref(String pref_name, String value)

	public static boolean set_pref(Activity actv, String pref_name, String pref_key, int value) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/*----------------------------
		 * 2. Get editor
			----------------------------*/
		SharedPreferences.Editor editor = prefs.edit();

		/*----------------------------
		 * 3. Set value
			----------------------------*/
		editor.putInt(pref_key, value);
		
		try {
			editor.commit();
			
			return true;
			
		} catch (Exception e) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Excption: " + e.toString());
			
			return false;
		}

	}//public static boolean set_pref(String pref_name, String value)

	public static int get_pref(Activity actv, String pref_name, int defValue) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/*----------------------------
		 * Return
			----------------------------*/
		return prefs.getInt(pref_name, defValue);

	}//public static boolean set_pref(String pref_name, String value)

	public static int get_pref(Activity actv, String pref_name, String pref_key, int defValue) {
		SharedPreferences prefs = 
				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);

		/*----------------------------
		 * Return
			----------------------------*/
		return prefs.getInt(pref_key, defValue);

	}//public static boolean set_pref(String pref_name, String value)


	public static Dialog dlg_template_cancel(Activity actv, int layoutId, int titleStringId,
			int cancelButtonId, DialogTags cancelTag) {
		/*----------------------------
		* Steps
		* 1. Set up
		* 2. Add listeners => OnTouch
		* 3. Add listeners => OnClick
		----------------------------*/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(layoutId);
		
		// Title
		dlg.setTitle(titleStringId);
		
		/*----------------------------
		* 2. Add listeners => OnTouch
		----------------------------*/
		//
		Button btn_cancel = (Button) dlg.findViewById(cancelButtonId);
		
		//
		btn_cancel.setTag(cancelTag);
		
		//
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/*----------------------------
		* 3. Add listeners => OnClick
		----------------------------*/
		//
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		
		//
		//dlg.show();
		
		return dlg;
	
	}//public static Dialog dlg_template_okCancel()

	public static Dialog dlg_template_okCancel(Activity actv, int layoutId, int titleStringId,
			int okButtonId, int cancelButtonId, DialogTags okTag, DialogTags cancelTag) {
		/*----------------------------
		* Steps
		* 1. Set up
		* 2. Add listeners => OnTouch
		* 3. Add listeners => OnClick
		----------------------------*/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(layoutId);
		
		// Title
		dlg.setTitle(titleStringId);
		
		/*----------------------------
		* 2. Add listeners => OnTouch
		----------------------------*/
		//
		Button btn_ok = (Button) dlg.findViewById(okButtonId);
		Button btn_cancel = (Button) dlg.findViewById(cancelButtonId);
		
		//
		btn_ok.setTag(okTag);
		btn_cancel.setTag(cancelTag);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/*----------------------------
		* 3. Add listeners => OnClick
		----------------------------*/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		
		//
		//dlg.show();
		
		return dlg;
	
	}//public static Dialog dlg_template_okCancel()

	public static void dlg_db_activity(Activity actv) {
		/*----------------------------
		 * 1. Dialog
		 * 2. Prep => List
		 * 3. Adapter
		 * 4. Set adapter
		 * 
		 * 5. Set listener to list
		 * 5-2. Set listener => Cancel button
		 * 
		 * 6. Show dialog
			----------------------------*/
		Dialog dlg = Methods.dlg_template_cancel(
									actv, R.layout.dlg_db_admin, 
									R.string.dlg_db_admin_title, 
									R.id.dlg_db_admin_bt_cancel, 
									Methods.DialogTags.dlg_generic_dismiss);
		
		/*----------------------------
		 * 2. Prep => List
			----------------------------*/
		String[] choices = {
										actv.getString(R.string.dlg_db_admin_item_backup_db),
										actv.getString(R.string.dlg_db_admin_item_refresh_db)
										};
		
		List<String> list = new ArrayList<String>();
		
		for (String item : choices) {
			
			list.add(item);
			
		}
		
		/*----------------------------
		 * 3. Adapter
			----------------------------*/
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				actv,
//				R.layout.dlg_db_admin,
				android.R.layout.simple_list_item_1,
				list
				);

		/*----------------------------
		 * 4. Set adapter
			----------------------------*/
		ListView lv = (ListView) dlg.findViewById(R.id.dlg_db_admin_lv);
		
		lv.setAdapter(adapter);
		
		/*----------------------------
		 * 5. Set listener to list
			----------------------------*/
		lv.setTag(Methods.DialogItemTags.dlg_db_admin_lv);
		
		lv.setOnItemClickListener(new DialogOnItemClickListener(actv, dlg));
		
		/*********************************
		 * 5-2. Set listener => Cancel button
		 *********************************/
//		Button bt_cancel = (Button) dlg.findViewById(R.id.dlg_db_admin_bt_cancel);
//		
//		bt_cancel.setOnClickListener(l)
		
		/*----------------------------
		 * 6. Show dialog
			----------------------------*/
		dlg.show();
		
		
	}//public static void dlg_db_activity(Activity actv)

	public static long getMillSeconds(int year, int month, int date) {
		// Calendar
		Calendar cal = Calendar.getInstance();
		
		// Set time
		cal.set(year, month, date);
		
		// Date
		Date d = cal.getTime();
		
		return d.getTime();
		
	}//private long getMillSeconds(int year, int month, int date)

	/****************************************
	 *	getMillSeconds_now()
	 * 
	 * <Caller> 
	 * 1. ButtonOnClickListener # case main_bt_start
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static long getMillSeconds_now() {
		
		Calendar cal = Calendar.getInstance();
		
		return cal.getTime().getTime();
		
	}//private long getMillSeconds_now(int year, int month, int date)

	public static String get_TimeLabel(long millSec) {
		
		 SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
		 
		return sdf1.format(new Date(millSec));
		
	}//public static String get_TimeLabel(long millSec)

	public static boolean restore_db(Activity actv, String dbName,
				String src, String dst) {
		/*********************************
		 * 1. Setup db
		 * 2. Setup: File paths
		 * 3. Setup: File objects
		 * 4. Copy file
		 * 
		 *********************************/
    	// Setup db
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		wdb.close();

		/*********************************
		 * 2. Setup: File paths
		 *********************************/
//    	String src = 
//    			"/mnt/sdcard-ext/ShoppingList_backup/shoppinglist_backup_20120906_201402.bk";
//    			"/mnt/sdcard-ext/CR4_backup/cr4_backup_20120907_184555.bk";

    	
//    	String dst =
////    			"/data/data/test.main/databases/shoppinglist.db";
//    			"/data/data/cr4.main/databases/cr4.db";

    	/*********************************
		 * 3. Setup: File objects
		 *********************************/
		File f_src = new File(src);
		File f_dst = new File(dst);

		/*********************************
		 * 4. Copy file
		 *********************************/
		try {
			FileChannel iChannel = new FileInputStream(src).getChannel();
			FileChannel oChannel = new FileOutputStream(dst).getChannel();
			iChannel.transferTo(0, iChannel.size(), oChannel);
			iChannel.close();
			oChannel.close();
			
			// Log
			Log.d("ThumbnailActivity.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "File copied: " + src);
			
			// debug
			Toast.makeText(actv, "DB restoration => Done", 3000).show();
			
			return true;

		} catch (FileNotFoundException e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
			return false;
			
		} catch (IOException e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
			return false;
			
		}//try
		
	}//private boolean restore_db()

	public static String[] get_column_list(Activity actv, String dbName, String tableName) {
		/*********************************
		 * 1. Set up db
		 * 2. Cursor null?
		 * 3. Get names
		 * 
		 * 4. Close db
		 * 5. Return
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		//=> source: http://stackoverflow.com/questions/4681744/android-get-list-of-tables : "Just had to do the same. This seems to work:"
		String q = "SELECT * FROM " + tableName;
		
		/*********************************
		 * 2. Cursor null?
		 *********************************/
		Cursor c = null;
		
		try {
			c = rdb.rawQuery(q, null);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount(): " + c.getCount());

		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return null;
		}
		
		/*********************************
		 * 3. Get names
		 *********************************/
		String[] column_names = c.getColumnNames();
		
		/*********************************
		 * 4. Close db
		 *********************************/
		rdb.close();
		
		/*********************************
		 * 5. Return
		 *********************************/
		return column_names;
		
//		return null;
	}//public static String[] get_column_list(Activity actv, String tableName)

    public static void drop_table(Activity actv, String dbName, String tableName) {
    	// Setup db
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		boolean res = 
				dbu.dropTable(actv, wdb, tableName);
		
		if (res == true) {
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table dropped: " + tableName);
		} else {//if (res == true)

			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Drop table => Failed: " + tableName);
			
		}//if (res == true)
		

		wdb.close();
		
		
	}//private void drop_table(String tableName)

	public static boolean add_column_to_table(Activity actv, String dbName,
			String tableName, String column_name, String data_type) {
		/*********************************
		 * 1. Column already exists?
		 * 2. db setup
		 * 
		 * 3. Build sql
		 * 4. Exec sql
		 * 
		 * 5. Close db
		 *********************************/
		/*********************************
		 * 1. Column already exists?
		 *********************************/
		String[] cols = Methods.get_column_list(actv, dbName, tableName);
		
		//debug
		for (String col_name : cols) {

			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "col: " + col_name);
			
		}//for (String col_name : cols)

		
		for (String col_name : cols) {
			
			if (col_name.equals(column_name)) {
				
				// debug
				Toast.makeText(actv, "Column exists: " + column_name, Toast.LENGTH_SHORT).show();
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Column exists: " + column_name);
				
				return false;
				
			}
			
		}//for (String col_name : cols)
		
		// debug
		Toast.makeText(actv, "Column doesn't exist: " + column_name, Toast.LENGTH_SHORT).show();
		
		/*********************************
		 * 2. db setup
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*********************************
		 * 3. Build sql
		 *********************************/
		// REF[20121001_140817] => http://stackoverflow.com/questions/8291673/how-to-add-new-column-to-android-sqlite-database
		
		String sql = "ALTER TABLE " + tableName + 
					" ADD COLUMN " + column_name + 
					" " + data_type;
		
		/*********************************
		 * 4. Exec sql
		 *********************************/
		try {
//			db.execSQL(sql);
			wdb.execSQL(sql);
			
			// Log
			Log.d(actv.getClass().getName() + 
					"["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Column added => " + column_name);

			/*********************************
			 * 5. Close db
			 *********************************/
			wdb.close();
			
			return true;
			
		} catch (SQLException e) {
			// Log
			Log.d(actv.getClass().getName() + 
					"[" + Thread.currentThread().getStackTrace()[2].getLineNumber() + "]", 
					"Exception => " + e.toString());
			
			/*********************************
			 * 5. Close db
			 *********************************/
			wdb.close();

			return false;
		}//try

		/*********************************
		 * 5. Close db
		 *********************************/


		
	}//public static boolean add_column_to_table()

	public static List<String> get_table_list(Activity actv, String dbName, String key_word) {
//		/*********************************
//		 * 1. Set up db
//		 * 2. Query
//		 * 
//		 * 3. Build list
//		 *********************************/
//		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
//		
//		SQLiteDatabase rdb = dbu.getReadableDatabase();
//
//		/*********************************
//		 * 2. Query
//		 *********************************/
//		//=> source: http://stackoverflow.com/questions/4681744/android-get-list-of-tables : "Just had to do the same. This seems to work:"
//		String q = "SELECT name FROM " + "sqlite_master"+
//						" WHERE type = 'table' ORDER BY name";
//		
//		Cursor c = null;
//		try {
//			c = rdb.rawQuery(q, null);
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "c.getCount(): " + c.getCount());
//
//		} catch (Exception e) {
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Exception => " + e.toString());
//		}
//		
//		// Table names list
//		List<String> tableList = new ArrayList<String>();
//		
//		/*********************************
//		 * 3. Build list
//		 *********************************/
//		// Log
//		if (c != null) {
//			c.moveToFirst();
//			
////			String t_name = c.getString(0);
//			
//			String reg = "IFM9.*";
//			
//			Pattern p = Pattern.compile(reg);
//			Matcher m;// = p.matcher(t_name);
//
//			
//			for (int i = 0; i < c.getCount(); i++) {
//				//
//				String t_name = c.getString(0);
//				
//				m = p.matcher(t_name);
//				
//				if (m.find()) {
//					
//					tableList.add(c.getString(0));
//					
//				}//if (variable == condition)
////				tableList.add(c.getString(0));
//				
////				// Log
////				Log.d("Methods.java"
////						+ "["
////						+ Thread.currentThread().getStackTrace()[2]
////								.getLineNumber() + "]", "c.getString(0): " + c.getString(0));
//				
//				
//				// Next
//				c.moveToNext();
//				
//			}//for (int i = 0; i < c.getCount(); i++)
//
//		} else {//if (c != null)
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "c => null");
//		}//if (c != null)
//
////		// Log
////		Log.d("Methods.java" + "["
////				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////				+ "]", "c.getCount(): " + c.getCount());
////		
//		rdb.close();
//		
//		return tableList;
		
		return null;
	}//public static List<String> get_table_list(Activity actv, String key_word)

	public static List<String> get_table_list(Activity actv, String dbName) {
		/*********************************
		 * 1. Set up db
		 * 2. Query
		 * 
		 * 3. Build list
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		/*********************************
		 * 2. Query
		 *********************************/
		//=> source: http://stackoverflow.com/questions/4681744/android-get-list-of-tables : "Just had to do the same. This seems to work:"
		String q = "SELECT name FROM " + "sqlite_master"+
						" WHERE type = 'table' ORDER BY name";
		
		Cursor c = null;
		try {
			c = rdb.rawQuery(q, null);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount(): " + c.getCount());

		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
		}
		
		// Table names list
		List<String> tableList = new ArrayList<String>();
		
		/*********************************
		 * 3. Build list
		 *********************************/
		// Log
		if (c != null) {
			c.moveToFirst();
			
//			String t_name = c.getString(0);
			
			String reg = "IFM9.*";
			
			Pattern p = Pattern.compile(reg);
			Matcher m;// = p.matcher(t_name);

			
			for (int i = 0; i < c.getCount(); i++) {
				//
				String t_name = c.getString(0);
				
				m = p.matcher(t_name);
				
				if (m.find()) {
					
					tableList.add(c.getString(0));
					
				}//if (variable == condition)
//				tableList.add(c.getString(0));
				
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "c.getString(0): " + c.getString(0));
				
				
				// Next
				c.moveToNext();
				
			}//for (int i = 0; i < c.getCount(); i++)

		} else {//if (c != null)
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c => null");
		}//if (c != null)

//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount(): " + c.getCount());
//		
		rdb.close();
		
		return tableList;
		
//		return null;
	}//public static List<String> get_table_list(Activity actv, String key_word)

	public static void create_table(Activity actv,
					String dbName, String tname,
					String[] cols, String[] col_types) {
		/*********************************
		 * memo
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		boolean res = dbu.createTable(wdb, tname, cols, col_types);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "res=" + res);
		
		wdb.close();
		
		
	}//public static void create_table()

	/*********************************
	 * <Return>
	 * -1	=> Can't create table
	 * -2	=> No file in the base directory
	 * -3	=> No new file
	 *********************************/
//	public static boolean refreshMainDB(Activity actv, Dialog dlg) {
	public static int refresh_main_db(Activity actv) {
		/*----------------------------
		 * Steps
		 * 1. Set up DB(writable)
		 * 2. Table exists?
		 * 2-1. If no, then create one
		 * 
		 * 3. Execute query for audio files

		 * 4. Insert data into db
		 * 5. Update table "refresh_log"
		 * 
		 * 9. Close db
		 * 10. Return
			----------------------------*/
		/*----------------------------
		 * 1. Set up DB(writable)
			----------------------------*/
		//
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/*----------------------------
		 * 2. Table exists?
		 * 2-1. If no, then create one
		 * 		1. baseDirName
		 * 		2. backupTableName
			----------------------------*/
		boolean res = Methods.refreshMainDB_1_set_up_table(wdb, dbu, MainActv.tname_item);

		if (res == false) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "res == false");
			
			wdb.close();
			
			return -1;
			
		}//if (res == false)
		
		/*----------------------------
		 * 3. Execute query for audio files
		 * 	1. Get value => Last refreshed date
			----------------------------*/
//		Cursor c = Methods.refreshMainDB_2_exec_query(actv, wdb, dbu);
		/*********************************
		 * 3.1. Get value => Last refreshed date
		 *********************************/
		long last_refreshed_date = Methods.get_last_refreshed_date(wdb, dbu);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "last_refreshed_date=" + last_refreshed_date);
		
		/*********************************
		 * memo
		 *********************************/
		String path = StringUtils.join(
					(new String[]{
						MainActv.dname_storage_internal,
						MainActv.dname_tt_internal}),
						File.separator);
						
    	File base_dir = new File(path);

		File[] file_list = base_dir.listFiles();
		
		List<AI> ai_list = new ArrayList<AI>();
		
		if (file_list.length < 1) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "file_list.length < 1");
			
			wdb.close();
			
			return -2;
			
		}//if (file_list.length == condition)
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "file_list.length=" + file_list.length);
		
		/*********************************
		 * If new files, then build a list
		 *********************************/
		int num_of_items = 0;
		
		for (File f : file_list) {
			
			if (f.lastModified() > last_refreshed_date) {
//				String file_name, String title, String memo,
//				long last_played_at,
//				String table_name
				
				ai_list.add(new AI(
						f.getName(),
				    	StringUtils.join(
	    				new String[]{
	    						MainActv.dname_storage_internal,
//	    						MainActv.dname_source_folder_tt},
	    						MainActv.dname_tt_internal},
	    						File.separator),

						"",
						"",
						0,
						MainActv.tname_main,
						f.lastModified()
						));
				
				num_of_items += 1;
				
			}//if (f.lastModified() == condition)
			
		}//for (File f : file_list)
		
		if (ai_list.size() < 1) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "ai_list.size() < 1");
			
			wdb.close();
			
			return -3;
			
		}//if (ai_list.size() == condition)
		
		/*********************************
		 * Insert data => AI, history
		 *********************************/
		int i_res = DBUtils.insert_all_data_ai(wdb, dbu, ai_list);
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "i_res=" + i_res);
		
		/*----------------------------
		 * 4. Insert data into db
			----------------------------*/
//		int numOfItemsAdded;
//		
//		if (c.getCount() < 1) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Query result: 0");
//			
//			numOfItemsAdded = 0;
//			
//			// debug
//			Toast.makeText(actv, "新規のファイルはありません", 2000).show();
//			
//		} else {//if (c.getCount() < 1)
//			
//			numOfItemsAdded = refreshMainDB_3_insert_data(actv, wdb, dbu, c);
//			
//		}//if (c.getCount() < 1)
		
		/*----------------------------
		 * 9. Close db
			----------------------------*/
		wdb.close();
		
		/*----------------------------
		 * 10. Return
			----------------------------*/
		return i_res;
		
	}//public static boolean refresh_main_db(Activity actv)

	/*********************************
	 * <Return>
	 * -1	=> Query failed
	 * -2	=> c.getCount() < 1
	 * 
	 *********************************/
	
	private static long get_last_refreshed_date(SQLiteDatabase wdb, DBUtils dbu) {
		/*********************************
		 * memo
		 *********************************/
		String sql = "SELECT * FROM " + MainActv.tname_refresh_history
					+ " ORDER BY " + android.provider.BaseColumns._ID
					+ " DESC";
		
		Cursor c = null;
		
		try {
			
			c = wdb.rawQuery(sql, null);
			
		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
//			rdb.close();
			
			return -1;
		}
		
		if (c.getCount() < 1) {
			
			return -2;
			
		} else {//if (condition)
			
			c.moveToFirst();
			
			return c.getLong(3);
			
		}//if (condition)
		
		
//		return 0;
	}//private static long get_last_refreshed_date(SQLiteDatabase wdb, DBUtils dbu)

	private static int refreshMainDB_3_insert_data(Activity actv, SQLiteDatabase wdb, DBUtils dbu, Cursor c) {
//		/*----------------------------
//		 * 4. Insert data into db
//			----------------------------*/
//		int numOfItemsAdded = insertDataIntoDB(actv, MainActv.dirName_base, c);
//			
////		int numOfItemsAdded = -1;
//		
//		/*----------------------------
//		 * 5. Update table "refresh_log"
//			----------------------------*/
//		c.moveToPrevious();
//		
//		long lastItemDate = c.getLong(3);
//		
//		updateRefreshLog(actv, wdb, dbu, lastItemDate, numOfItemsAdded);
//		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getLong(3) => " + c.getLong(3));
//		
//
//		return numOfItemsAdded;

		return 0;
		
	}//private static int refreshMainDB_3_insert_data(Cursor c)

	private static Cursor refreshMainDB_2_exec_query(Activity actv, SQLiteDatabase wdb, DBUtils dbu) {
//		/*----------------------------
//		 * 3. Execute query for image files
//		 * 		1. ContentResolver
//		 * 		2. Uri
//		 * 		3. proj
//		 * 		4. Last refreshed date
//		 * 		5. Execute query
//			----------------------------*/
//		/*----------------------------
//		 * 3.1. ContentResolver, Uri, proj
//			----------------------------*/
//		ContentResolver cr = actv.getContentResolver();
//		
//        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        
//		String[] proj = DBUtils.proj;
//
//		/*----------------------------
//		 * 3.4. Last refreshed date
//			----------------------------*/
//		long lastRefreshedDate = 0;		// Initial value => 0
//
//		boolean result = dbu.tableExists(wdb, MainActv.tableName_refreshLog);
//		
//		if (result != false) {
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Table exists: " + MainActv.tableName_refreshLog);
//			
//			
//			// REF=> http://www.accessclub.jp/sql/10.html
//			String sql = "SELECT * FROM refresh_log ORDER BY " + android.provider.BaseColumns._ID + " DESC";
//			
//			Cursor tempC = wdb.rawQuery(sql, null);
//			
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "tempC.getCount() => " + tempC.getCount());
//	
//			if (tempC.getCount() > 0) {
//				
//				tempC.moveToFirst();
//				
//				lastRefreshedDate = tempC.getLong(1);
//				
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", 
//						"lastRefreshedDate => " + String.valueOf(lastRefreshedDate) +
//						" (I will refresh db based on this date!)");
//				
//			}//if (tempC.getCount() > 0)
//		} else {//if (result != false)
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Table doesn't exist: " + MainActv.tableName_refreshLog);
//			
//			// Create one
//			result = dbu.createTable(
//											wdb, 
//											MainActv.tableName_refreshLog, 
//											DBUtils.cols_refresh_log, 
//											DBUtils.col_types_refresh_log);
//			
//			if (result == true) {
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Table created: " + MainActv.tableName_refreshLog);
//				
//			} else {//if (result == true)
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Create table failed: " + MainActv.tableName_refreshLog);
//				
//			}//if (result == true)
//			
//		}//if (result != false)
//		
//		/*----------------------------
//		 * 3.5. Execute query
//			----------------------------*/
//		// REF=> http://blog.csdn.net/uoyevoli/article/details/4970860
//		Cursor c = actv.managedQuery(
//											uri, 
//											proj,
//											MediaStore.Images.Media.DATE_ADDED + " > ?",
//											new String[] {String.valueOf(lastRefreshedDate)},
//											null);
//		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Last refreshed (in sec): " + String.valueOf(lastRefreshedDate / 1000));
//
//        actv.startManagingCursor(c);
//        
//        // Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount() => " + c.getCount());
//
//		return c;

		return null;
		
	}//private static Cursor refreshMainDB_2_exec_query()

	/****************************************
	 *	refreshMainDB_1_set_up_table(SQLiteDatabase wdb, DBUtils dbu)
	 * 
	 * <Caller> 1. <Desc> 1. <Params> 1.
	 * 
	 * <Return>
	 *  false		=> Can't create table
	 * 	true		=> Either (1) New table created, or, (2) Table exists
	 * 
	 * <Steps> 1.
	 * @param tname 
	 ****************************************/
	private static boolean refreshMainDB_1_set_up_table(
						SQLiteDatabase wdb, DBUtils dbu, String tname) {
		/*----------------------------
		 * 2-1.1. baseDirName
			----------------------------*/
		
		boolean result = dbu.tableExists(wdb, tname);
		
		// If the table doesn't exist, create one
		if (result == false) {

			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tname);
			
			result = 
					dbu.createTable(wdb, tname, DBUtils.cols, DBUtils.col_types);
			
			if (result == false) {

				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Can't create a table: "+ tname);
				
				return false;
				
			} else {//if (result == false)
				
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: "+ tname);
				
				return true;
				
			}//if (result == false)

		} else {//if (result == false)
			
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: "+ tname);

			return true;
			
		}//if (result == false)
		
	}//private static boolean refreshMainDB_1_set_up_table(SQLiteDatabase wdb, DBUtils dbu)

	public static void save_refresh_history(Activity actv,
						String dbName, String tname,
						long refreshed_date, int num_of_new_items) {
		
		/*********************************
		 * memo
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		boolean res = DBUtils.insert_data_refresh_history(wdb, refreshed_date, num_of_new_items);
		
		if (res == true) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Refresh history => Saved");
			
		} else {//if (res == true)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Save refresh history => Failed");
			
		}//if (res == true)
		
	}//public static void save_refresh_history(Activity actv)


//	public static void create_table(Activity actv, String dbName, String tname) {
//		/*********************************
//		 * memo
//		 *********************************/
//		DBUtils dbu = new DBUtils(actv, dbName);
//		
//		SQLiteDatabase wdb = dbu.getWritableDatabase();
//		
//		boolean res = dbu.createTable(wdb, tname, columns, types)
//		
//	}//public static void create_table(String dbName, String tname)

}//public class Methods
