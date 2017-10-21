package br.com.mobiclub.quantoprima.database.table;

import br.com.mobiclub.quantoprima.database.common.DBColumn;
import br.com.mobiclub.quantoprima.database.common.DBTable;

public class MobiTable extends DBTable {

	private static final String TABLE_NAME = "Mobi";

    public static final int STATUS_NOT_SYNC_TYPE = 0;
    public static final int STATUS_SYNCING_TYPE = 1;
    public static final int STATUS_SYNCED_TYPE = 2;
    public static final int STATUS_SYNCED_ERROR_TYPE = 3;
    public static final int STATUS_SYNCED_ERROR_TYPE_EXPIRED = 4;
    public static final int STATUS_SYNCED_ERROR_TYPE_NO_CONNECTION = 5;
	
	//Columns Index
	public static final int ID_COL_INDEX = 0;
	public static final int USER_ID_COL_INDEX = 1;
	public static final int CODE_COL_INDEX = 2;
	public static final int TIME_MINUTE_COL_INDEX = 3;
    public static final int STATUS_COL_INDEX = 4;
    public static final int LOCATION_COL_INDEX = 5;
    public static final int LOGO_COL_INDEX = 6;
    public static final int TIME_UPDATE_COL_INDEX = 7;
	
	//Columns Names
	public static final String ID_COL = "Id";
	public static final String USER_ID_COL = "UserId";
	public static final String CODE_COL = "Code";
	public static final String TIME_MINUTE_COL = "TimeMinute";
    public static final String STATUS_COL = "Status";
    public static final String LOCATION_COL = "Location";
    public static final String LOGO_COL = "Logo";
    public static final String TIME_UPDATE_COL = "Time";
	
	private static final DBColumn[] DB_COLUMNS = new DBColumn[]  {
				new DBColumn(ID_COL, "INTEGER PRIMARY KEY AUTOINCREMENT"),
				new DBColumn(USER_ID_COL, "INTEGER"),
				new DBColumn(CODE_COL, "TEXT"),
				new DBColumn(TIME_MINUTE_COL, "INTEGER"),
                new DBColumn(STATUS_COL, "INTEGER"),
                new DBColumn(LOCATION_COL, "STRING"),
                new DBColumn(LOGO_COL, "STRING"),
                new DBColumn(TIME_UPDATE_COL, "STRING")
	};

    public MobiTable() {
		super(TABLE_NAME, DB_COLUMNS);		
	}
}
