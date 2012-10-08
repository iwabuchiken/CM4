package cm4.main;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cm4.listeners.ButtonOnClickListener;
import cm4.listeners.ButtonOnTouchListener;
import cm4.listeners.CustomOnItemLongClickListener;
import cm4.listeners.DialogListener;
import cm4.utils.DBUtils;
import cm4.utils.Methods;
import cm4.main.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;


public class MainActv extends ListActivity {
	
	public static Vibrator vib;

	/*********************************
	 * Intent data labels
	 *********************************/

	/*********************************
	 * Prefs
	 *********************************/
	/*********************************
	 * Paths and names
	 *********************************/
	public static String dname_storage_sdcard = "/mnt/sdcard-ext";
	
	public static String dname_storage_internal = "/mnt/sdcard";

	public static String  dirName_base = "CM4";
	
	public static String fname_list = "list.txt";

	
	
	// tapeatalk
	public static String dname_tt_sdcard = "tapeatalk_records";
	
	public static String dname_tt_internal = "tapeatalk_records";

	/*********************************
	 * List-related
	 *********************************/
	public static ArrayAdapter<String> aAdp = null;

	public static List<String> file_list = null;
	
	/*********************************
	 * Others
	 *********************************/
	
	/*********************************
	 * DB
	 *********************************/
	public static String dbName = "CM4.db";
	
	// Table => refresh_history
	public static String tname_refresh_history = "refresh_history";
	
	public static String[] cols_refresh_history = {
		"last_refreshed", "num_of_items_added"
	};
	
	public static String[] col_types_refresh_history = {
		"INTEGER", 			"INTEGER"
	};

	// Table => item
	public static String tname_item = "item";
	
	public static String[] cols_item = 
		{"file_name", 	"file_path",	"title", "memo",
			"last_played_at",	"table_name"};
	
	public static String[] col_types_item =
		{"TEXT", 		"TEXT", 		"TEXT",	"TEXT",
			"INTEGER",			"TEXT"};

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	/*----------------------------
		 * 1. super
		 * 2. Set content
		 * 2-2. Set title
		 * 3. Initialize => vib
		 * 
			----------------------------*/
    	/*********************************
		 * 1. super
		 * 2. Set content
		 * 2-2. Set title
		 * 
		 * 3. Initialize => vib
		 * 
		 * 4. Show list

		 *********************************/
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /*----------------------------
		 * 2-2. Set title
			----------------------------*/
		this.setTitle(this.getClass().getName());
        
        vib = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
      
        /*********************************
		 * 4. Show list
		 *********************************/
        show_list();

        //debug
        show_table_list();
        
    }//public void onCreate(Bundle savedInstanceState)


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		Methods.confirm_quit(this, keyCode);
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.main_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.main_opt_menu_db_activity://---------------------------

			Methods.dlg_db_activity(this);
			
//			//debug
//			Methods.create_table(this,
//					MainActv.dbName, MainActv.tname_refresh_history,
//					MainActv.cols_refresh_history, MainActv.col_types_refresh_history
//					);
//			
//			Methods.create_table(this,
//					MainActv.dbName, MainActv.tname_item,
//					MainActv.cols_item, MainActv.col_types_item
//					);

			break;
			
		}//switch (item.getItemId())
		
		return super.onOptionsItemSelected(item);
		
	}//public boolean onOptionsItemSelected(MenuItem item)

	private void show_list() {
		/*********************************
		 * memo
		 *********************************/
//    	File f = new File(StringUtils.join(
//    				new String[]{
//    						MainActv.dirName_ExternalStorage,
//    						MainActv.dname_source_folder_tt}),
//    						File.separator);
    	
    	File f = new File(MainActv.dname_storage_internal);
    	
    	// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "f.exists()=" + f.exists());
		
		File[] f_list = f.listFiles();
		
		for (File f_item : f_list) {
			
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			  		+ "]", "f_item.getName()=" + f_item.getName());
			
		}//for (File f_item : f_list)
    	
//    	File[] f_list = f.listFiles();
//    	
//    	// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "f_list.length=" + f_list.length);
    	
//    	file_list = new ArrayList<String>();
//    	
//    	for (File item : f_list) {
//			
//    		file_list.add(item.getName());
//    		
//		}//for (File f : f_list)
//    	
//    	// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "file_list.size()=" + file_list.size());
    	
	}//private void show_list()

	private void show_table_list() {
		
		List<String> table_list = Methods.get_table_list(this, MainActv.dbName);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "table_list=" + table_list);
		
	}//private void show_table_list()
	

	private void do_debug() {
		/*********************************
		 * 6. Drop table
		 * 7. Add new col => "last_viewed_at"
		 *********************************/
//		copy_db_file();
//		test_simple_format();
//		restore_db("ifm9_backup_20121001_140224.bk");
//		check_db();
//		show_column_list("IFM9__Android");
//		10-01 15:05:54.408: D/MainActv.java[260](14946): New col added to: IFM9__Android

    	/*********************************
		 * 6. Drop table
		 *********************************/
//    	Methods.drop_table(this, MainActv.dbName, MainActv.tableName_show_history);
    	
    	/*********************************
		 * 7. Add new col => "last_viewed_at"
		 *********************************/
//    	add_new_col_last_viewed_at();
    	
    	
	}//private void do_debug()


}//public class MainActv extends Activity
