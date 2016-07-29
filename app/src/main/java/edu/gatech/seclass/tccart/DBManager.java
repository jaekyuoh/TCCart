package edu.gatech.seclass.tccart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by jaekyuoh on 3/18/16.
 */
public class DBManager extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "User.db";
    public static final String USER_TABLE = "user_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "FIRSTNAME";
    public static final String COL_3 = "LASTNAME";
    public static final String COL_4 = "EMAIL";
    public static final String COL_5 = "VIPSTATUS";
    public static final String COL_6 = "CREDIT";
    public static final String COL_18 = "CUMULATIVECOST";
    public static final String COL_19 = "VIPLASTUPDATE";
    public static final String COL_20 = "CREDITRECEIVEDDATE";



    public static final String PURCHASE_TABLE = "purchase_table";
    public static final String COL_7 = "PURCHASEID";
    public static final String COL_8 = "CUSTOMERID";
    public static final String COL_9 = "DATE";
    public static final String COL_10 = "NUMCOFFEE";
    public static final String COL_11 = "NUMTEA";
    public static final String COL_12 = "BEFORECREDIT";
    public static final String COL_13 = "TOTAL";
    public static final String COL_14 = "CREDIT";
    public static final String COL_15 = "CREDITAMOUNT";
    public static final String COL_16 = "VIP";
    public static final String COL_17 = "VIPAMOUNT";

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, 6);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + USER_TABLE + " (ID TEXT PRIMARY KEY,FIRSTNAME TEXT," +
                "LASTNAME TEXT,EMAIL TEXT, VIPSTATUS INTEGER,CREDIT REAL, CUMULATIVECOST REAL, " +
                "VIPLASTUPDATE TEXT, CREDITRECEIVEDDATE TEXT)");
        db.execSQL("create table " + PURCHASE_TABLE + " (PURCHASEID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "CUSTOMERID TEXT,DATE TEXT, " + "NUMCOFFEE INTEGER,NUMTEA INTEGER,BEFORECREDIT REAL," +
                " TOTAL REAL, CREDIT INTEGER, CREDITAMOUNT REAL, VIP INTEGER, VIPAMOUNT REAL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+PURCHASE_TABLE);
        onCreate(db);
    }

    public boolean insertData(String id,String firstName, String name,String email,int vipStatus,
                              double credit, double cumulativeCost, String vipLastUpdate, String creditReceivedDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,firstName);
        contentValues.put(COL_3,name);
        contentValues.put(COL_4,email);
        contentValues.put(COL_5,vipStatus);
        contentValues.put(COL_6,credit);
        contentValues.put(COL_18,cumulativeCost);
        contentValues.put(COL_19,vipLastUpdate);
        contentValues.put(COL_20,creditReceivedDate);

        long result = db.insert(USER_TABLE,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public boolean insertDataPurchase(String customerId,String date, int numCoffee,int numTea,double beforeCredit, double total,
                                      int credit, double creditAmount, int vip, double vipAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_8,customerId);
        contentValues.put(COL_9,date);
        contentValues.put(COL_10,numCoffee);
        contentValues.put(COL_11,numTea);
        contentValues.put(COL_12,beforeCredit);
        contentValues.put(COL_13,total);

        contentValues.put(COL_14,credit);
        contentValues.put(COL_15,creditAmount);
        contentValues.put(COL_16,vip);
        contentValues.put(COL_17,vipAmount);
        long result = db.insert(PURCHASE_TABLE,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + USER_TABLE, null);
        return cursor;
    }

    public Cursor getAllDataPurchase(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + PURCHASE_TABLE, null);
        return cursor;
    }

    public boolean updateData(String id, String firstName, String name, String email, int vipStatus,
                              double credit, double cumulativeCost, String vipLastUpdate, String creditReceivedDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,firstName);
        contentValues.put(COL_3,name);
        contentValues.put(COL_4,email);
        contentValues.put(COL_5,vipStatus);
        contentValues.put(COL_6,credit);
        contentValues.put(COL_18,cumulativeCost);
        contentValues.put(COL_19,vipLastUpdate);
        contentValues.put(COL_20,creditReceivedDate);

        //update with ID where id equals to id (given as input) such that matches contentValues supplied
        db.update(USER_TABLE,contentValues, "ID=?", new String[] {id});
        return true;
    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(USER_TABLE,"ID=?", new String[] {id});
        return res;
    }

    public Integer deleteDataPurchase(String purchaseId){
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(PURCHASE_TABLE,"PURCHASEID=?", new String[] {purchaseId});
        return res;
    }



    public boolean duplicateID(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + USER_TABLE + " where ID = " + "'" +id+"'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public Customer retrieveUserInfo(String id){
        Customer customer = new Customer();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + USER_TABLE + " where ID = " + "'" +id+"'";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() == 0){
            //No matching user found
            customer.setId("");
            customer.setFirstName("");
            customer.setLastName("");
            customer.setEmail("");
            customer.setVIPstatus(0);
            customer.setCredit(0);
            customer.setCumulativeCost(0);
            customer.setVipLastUpdate("");
            customer.setCreditReceivedDate("");
            return customer;
        }
        else if (cursor.moveToNext()){
            //customer = new Customer(cursor.getString(0), cursor.getString(1),cursor.getString(2));
            customer.setId(cursor.getString(0));
            customer.setFirstName(cursor.getString(1));
            customer.setLastName(cursor.getString(2));
            customer.setEmail(cursor.getString(3));
            customer.setVIPstatus(cursor.getInt(4));
            customer.setCredit(cursor.getDouble(5));
            customer.setCumulativeCost(cursor.getDouble(6));
            customer.setVipLastUpdate(cursor.getString(7));
            customer.setCreditReceivedDate(cursor.getString(8));
            return customer;
        }
        return customer;
    }
    public ArrayList<Integer> retrieveAllUser(){
        ArrayList<Integer> idList = new ArrayList<Integer>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select ID from " + USER_TABLE, null);
        if(cursor.getCount() == 0){
            //No Single User
            return null;
        }
        else{
            while(cursor.moveToNext()) {
                idList.add(cursor.getInt(0));
            }
            return idList;
        }
    }


    public ArrayList<Purchase> retrievePurchaseInfo(String id){
        ArrayList<Purchase> purchaseList = new ArrayList<Purchase>();

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + PURCHASE_TABLE + " where CUSTOMERID = " + "'" +id+"'";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() == 0){
            //No matching user found
            Purchase purchase = new Purchase();
            purchaseList.add(purchase);
            return null;
        }
        else{
            while(cursor.moveToNext()) {
                //customer = new Customer(cursor.getString(0), cursor.getString(1),cursor.getString(2));
                Purchase purchase = new Purchase();
                purchase.setPurchaseId(cursor.getInt(0));
                purchase.setCustomerId(cursor.getString(1));
                purchase.setDate(cursor.getString(2));
                purchase.setCoffeeCount(cursor.getInt(3));
                purchase.setTeaCount(cursor.getInt(4));
                purchase.setBeforeDiscount(cursor.getDouble(5));
                purchase.setTotal(cursor.getDouble(6));
                purchase.setCreditUsed(cursor.getInt(7));
                purchase.setCreditAmount(cursor.getDouble(8));
                purchase.setVip(cursor.getInt(9));
                purchase.setVipAmount(cursor.getDouble(10));

                purchaseList.add(purchase);
            }
            return purchaseList;
        }

    }


}
