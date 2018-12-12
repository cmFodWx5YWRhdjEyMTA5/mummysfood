package in.ckd.calenderkhanado.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.ckd.calenderkhanado.models.DashBoardModel;
import in.ckd.calenderkhanado.models.HomeFeed;

/**
 * Created by Nilesh Deokar on 7/9/2015.
 */
public class DataBaseHelperNew extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ckd.db";
    public  final String CONTACTS_TABLE_NAME = "Order";
    public  final String CONTACTS_COLUMN_ID = "id";
    public  final String CONTACTS_COLUMN_NAME = "order_name";
    private HashMap hp;


    public static final String TAG = "DataBaseHelperNew";

    public static final String TABLE_ORDER = "Order";

    public static final String TABLE_ADD_TO_CART= "cart";


    private static final int DATABASE_VERSION = 1;


    // Database Name

    //Detail Table Column names

    private static final String KEY_FEED_ID = "feed_id";
    private static final String DATA = "data";



    public DataBaseHelperNew(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table contacts " +
                        "(id integer primary key, name text,phone text,email text, street text,place text)"
        );

        db.execSQL("create table "+ TABLE_ADD_TO_CART + "(model TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertContact (String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.insert("contacts", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public void insertAddToCart(List<HomeFeed.Data> tagsModelList)

    {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();


            Gson gson = new Gson();

            String inputString = gson.toJson(tagsModelList);


            values.put("model", inputString);


            db.insert(TABLE_ADD_TO_CART, null, values);


            db.close();

        } catch (SQLiteException exception) {
            exception.printStackTrace();
        }

    }




    public List<HomeFeed.Data> getAddToCartItem() {

        SQLiteDatabase db = this.getReadableDatabase();


        String value = null;

        String countQuery;



        countQuery = "SELECT  *  FROM  " + TABLE_ADD_TO_CART ;


        Cursor cursor = db.rawQuery(countQuery, null);

        try {


            if (cursor.moveToFirst()) {
                do {
                    value = cursor.getString(cursor.getColumnIndex("model"));

                } while (cursor.moveToNext());
            }


            cursor.close();
            db.close();


        } catch (SQLiteException e) {
            e.printStackTrace();
        }


        Gson gson = new Gson();

        Type type = new TypeToken<List<HomeFeed.Data>>() {}.getType();

        List<HomeFeed.Data> finalOutputString = gson.fromJson(value, type);


        return finalOutputString;

    }
    public boolean deleteTable(String tableName)

    {
        SQLiteDatabase db = this.getWritableDatabase();

        try {


            return   db.delete(tableName,"",null)>0;

        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }

    }


}