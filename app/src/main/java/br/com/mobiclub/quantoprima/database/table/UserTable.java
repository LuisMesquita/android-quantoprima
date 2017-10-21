package br.com.mobiclub.quantoprima.database.table;

import br.com.mobiclub.quantoprima.database.common.DBColumn;
import br.com.mobiclub.quantoprima.database.common.DBTable;

public class UserTable extends DBTable {

    private static final String TABLE_NAME = "User";

    //Columns Index
    public static final int ID_COL_INDEX = 0;
    public static final int NAME_COL_INDEX = 1;
    public static final int EMAIL_COL_INDEX = 2;
    public static final int LAST_NAME_COL_INDEX = 3;
    public static final int BIHTHDAY_COL_INDEX = 4;
    public static final int GENDER_COL_INDEX = 5;
    public static final int FACEBOOK_ID_COL_INDEX = 6;
    public static final int FACEBOOK_ACCESS_EXPIRES_COL_INDEX = 7;
    public static final int FACEBOOK_ACCESS_TOKEN_COL_INDEX = 8;
    public static final int USER_ID_COL_INDEX = 9;
    public static final int PHOTO_COL_INDEX = 10;
    public static final int FACEBOOK_EMAIL_COL_INDEX = 11;
    public static final int CPF_COL_INDEX = 12;

    //Columns Names
    public static final String ID_COL = "Id";
    public static final String NAME_COL = "Name";
    public static final String EMAIL_COL = "Email";
    public static final String LAST_NAME_COL = "LastName";
    public static final String BIHTHDAY_COL = "Birthday";
    public static final String GENDER_COL = "Gender";
    public static final String FACEBOOK_ID_COL = "FacebookId";
    public static final String FACEBOOK_ACCESS_EXPIRES_COL = "FacebookAccessExpires";
    public static final String FACEBOOK_ACCESS_TOKEN_COL = "FacebookAccessToken";
    public static final String USER_ID_COL = "UserId";
    public static final String PHOTO_COL = "Photo";
    public static final String FACEBOOK_EMAIL_COL = "FacebookEmail";
    public static final String CPF_COL = "cpf";

    private static final DBColumn[] DB_COLUMNS = new DBColumn[]
            {
                    new DBColumn(ID_COL, "INTEGER PRIMARY KEY AUTOINCREMENT"),
                    new DBColumn(NAME_COL, "TEXT"),
                    new DBColumn(EMAIL_COL, "TEXT"),
                    new DBColumn(LAST_NAME_COL, "TEXT"),
                    new DBColumn(BIHTHDAY_COL, "TEXT"),
                    new DBColumn(GENDER_COL, "TEXT"),
                    new DBColumn(FACEBOOK_ID_COL, "INTEGER"),
                    new DBColumn(FACEBOOK_ACCESS_EXPIRES_COL, "INTEGER"),
                    new DBColumn(FACEBOOK_ACCESS_TOKEN_COL, "TEXT"),
                    new DBColumn(USER_ID_COL, "INTEGER"),
                    new DBColumn(PHOTO_COL, "TEXT"),
                    new DBColumn(FACEBOOK_EMAIL_COL, "TEXT"),
                    new DBColumn(CPF_COL, "TEXT")
            };

    public UserTable() {
        super(TABLE_NAME, DB_COLUMNS);
    }
}
