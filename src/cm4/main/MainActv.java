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
//        File f = new File(StringUtils.join(new String[]{MainActv.dirName_ExternalStorage, MainActv.}))
        
        /*----------------------------
		 * 4. Set list
			----------------------------*/
//        set_initial_dir_list();
        
        /*----------------------------
		 * 5. Set listener => Image buttons
			----------------------------*/
//		set_listeners();
		
		/*----------------------------
		 * 6. Set path label
			----------------------------*/
//		Methods.updatePathLabel(this);
		
		/*********************************
		 * 7. Initialize preferences
		 *********************************/
		
		/*********************************
		 * 8. Refresh DB
		 *********************************/
		
//		int current_history_mode = Methods.get_pref(
//				this, 
//				MainActv.prefName_mainActv, 
//				MainActv.prefName_mainActv_history_mode,
//				-1);
//
//		// Log
//		Log.d("MainActv.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "onCreate: current_history_mode=" + current_history_mode);
		
		//debug
//		do_debug();
//		copy_db_file();
//		test_simple_format();
//		restore_db();
//		check_db();
//		show_column_list();
		
        
    }//public void onCreate(Bundle savedInstanceState)

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		Methods.confirm_quit(this, keyCode);
		
		return super.onKeyDown(keyCode, event);
	}
    
    
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
