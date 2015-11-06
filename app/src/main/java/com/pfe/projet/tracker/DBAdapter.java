package dz.finalprojectchp_1.appandroid.location_4;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Azz_B on 03/11/2015.
 */
public class DBAdapter {
    private DBHelper dbHelper = null;
    private SQLiteDatabase database= null;

    public DBAdapter(Context context) {
        if( dbHelper == null)
             dbHelper = new DBHelper(context);

    }


    //----------services Queary function----------------------
        //open readable database method
        public void openReadable(){
            if(database == null)
                database = dbHelper.getReadableDatabase();
        }
        //open writable database method
        public void openWritable(){
            if((database == null)?true : (database.isReadOnly())){
                database = dbHelper.getWritableDatabase();
            }
        }
        //close database method
        public void close(){
            try {
                database.close();
            }catch (SQLException e){

            }

        }
        //insert function
        public long insert( String imei , double latitude , double longitude , String time){
            database = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBHelper.UID , imei);
            values.put(DBHelper.LATITUDE , latitude);
            values.put(DBHelper.LONGITUDE , longitude);
            values.put(DBHelper.TIME , time);

            return database.insert(DBHelper.TABLE_NAME , null , values);
        }

    //------------------------ INNER CLASS DBHelper -----------------------------

    static class DBHelper extends SQLiteOpenHelper{

        private Context context;
        //DB information
            private static final String DB_NAME = "TRAFFIC";
            private static final int DB_VERSION = 2;
        //table information
            private static final String TABLE_NAME = "CORDONNER";
            private static final String UID = "imei";
            private static final String LATITUDE="latutide";
            private static final String LONGITUDE = "longitude";
            private static final String TIME = "time";
        //table query
            private static final String CREATE_TABLE_QUEARY = "CREATE TABLE "+TABLE_NAME+" ("+UID+" VARCHAR(255) , "+LATITUDE+" DOUBLE NOT NULL, "+LONGITUDE+" DOUBLE NOT NULL , "
                                                                +TIME+" VARCHAR(100) NOT NULL )";
            private static final String DROP_TABLE_QUERY="DROP TABLE IF EXISTS "+TABLE_NAME;

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            Msg.toastMsg(context,"Call onCreate");
            try {
                db.execSQL(CREATE_TABLE_QUEARY);

            }catch (SQLException e){
                Msg.toastMsg(context,""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Msg.toastMsg(context,"Call onUpgrade");
            try {
                db.execSQL(DROP_TABLE_QUERY);
                onCreate(db);
            }catch (SQLException e){
                Msg.toastMsg(context,""+e);
            }
        }
    }
}
