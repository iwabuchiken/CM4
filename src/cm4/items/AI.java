package cm4.items;

public class AI {
	/*----------------------------
	 * Class fields
		----------------------------*/
	long db_id;
	long created_at;
	long modified_at;
	
	String file_name;
	String file_path;
	
	String title;
	String memo;
	
	long last_played_at;
	
	String table_name;
	
	/*----------------------------
	 * Constructor
		----------------------------*/
	public AI(
			String file_name, String file_path,
			String title, String memo,
			
			long last_played_at,
			
			String table_name, long created_at) {

		this.file_name = file_name;
		this.file_path = file_path;
		
		this.title = title;
		this.memo = memo;
		
		this.last_played_at = last_played_at;
		
		this.table_name = table_name;
		
		this.created_at = created_at;
		
	}//public ThumbnailItem(long fileId, String file_path, long date_added, long date_modified)

	public AI(
			String file_name, String file_path,
			String title, String memo,
			
			long last_played_at,
			
			String table_name,
			
			long db_id, long created_at, long modified_at) {

		this.file_name = file_name;
		this.file_path = file_path;
		
		this.title = title;
		this.memo = memo;
		
		this.last_played_at = last_played_at;
		
		this.table_name = table_name;
		
		this.db_id = db_id;
		this.created_at = created_at;
		this.modified_at = modified_at;
		
	}//public ThumbnailItem(long fileId, String file_path, long date_added, long date_modified)

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public long getLast_played_at() {
		return last_played_at;
	}

	public void setLast_played_at(long last_played_at) {
		this.last_played_at = last_played_at;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public long getDb_id() {
		return db_id;
	}

	public void setDb_id(long db_id) {
		this.db_id = db_id;
	}

	public long getModified_at() {
		return modified_at;
	}

	public void setModified_at(long modified_at) {
		this.modified_at = modified_at;
	}

	public long getCreated_at() {
		return created_at;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	
	/*----------------------------
	 * Methods
		----------------------------*/
	
}//public class ThumbnailItem
