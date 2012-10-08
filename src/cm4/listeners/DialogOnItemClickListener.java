package cm4.listeners;

import cm4.items.RefreshDBTask;
import cm4.main.R;
import cm4.utils.Methods;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class DialogOnItemClickListener implements OnItemClickListener {

	//
	Activity actv;
	Dialog dlg;
	Dialog dlg2;
	//
	Vibrator vib;
	
	//
//	Methods.DialogTags dlgTag = null;

	public DialogOnItemClickListener(Activity actv, Dialog dlg) {
		// 
		this.actv = actv;
		this.dlg = dlg;
		
		vib = (Vibrator) actv.getSystemService(Context.VIBRATOR_SERVICE);
		
	}//public DialogOnItemClickListener(Activity actv, Dialog dlg)

	public DialogOnItemClickListener(Activity actv, Dialog dlg, Dialog dlg2) {
		// 
		this.actv = actv;
		this.dlg = dlg;
		this.dlg2 = dlg2;
		
		vib = (Vibrator) actv.getSystemService(Context.VIBRATOR_SERVICE);
		
	}//public DialogOnItemClickListener(Activity actv, Dialog dlg)

//	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		/*----------------------------
		 * Steps
		 * 1. Get tag
		 * 2. Vibrate
		 * 3. Switching
			----------------------------*/
		
		Methods.DialogItemTags tag = (Methods.DialogItemTags) parent.getTag();
//		
		vib.vibrate(Methods.vibLength_click);
		
		/*----------------------------
		 * 3. Switching
			----------------------------*/
		switch (tag) {
		
		case dlg_db_admin_lv://-----------------------------
			
			dlg_db_admin_lv(parent, position);
			
			break;// case dlg_db_admin_lv
			
		default:
			break;
		}//switch (tag)
		
	}//public void onItemClick(AdapterView<?> parent, View v, int position, long id)

	private void dlg_db_admin_lv(AdapterView<?> parent, int position) {
		/*----------------------------
		 * 1. Get chosen item name
		 * 2. Switching
			----------------------------*/
		
		String item = (String) parent.getItemAtPosition(position);
		
//		// debug
//		Toast.makeText(actv, item, 2000).show();
		
		/*----------------------------
		 * 2. Switching
			----------------------------*/
		if (item.equals(actv.getString(R.string.dlg_db_admin_item_backup_db))) {
			
//			Methods.db_backup(actv, dlg);
			
		} else if (item.equals(actv.getString(R.string.dlg_db_admin_item_refresh_db))){
			
			RefreshDBTask task_ = new RefreshDBTask(actv, dlg);
			
			// debug
			Toast.makeText(actv, "Starting a task...", 2000)
					.show();
			
			task_.execute("Start");

			dlg.dismiss();
			
//			// Log
//			Log.d("DialogOnItemClickListener.java"
//					+ "["
//					+ Thread.currentThread().getStackTrace()[2]
//							.getLineNumber() + "]", "DB refresh");
			
			
//			Methods.refreshMainDB(actv, dlg);
			
		}
	}//private void dlg_db_admin_lv()
	
}//public class DialogOnItemClickListener implements OnItemClickListener
