package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class MyDBHandler extends SQLiteOpenHelper {

    //information of database
    private static final int DATABASE_VERSION = 30;
    private static final String DATABASE_NAME = "170517G.db";
    public static final String TABLE_NAME1 = "AccountTable";
    public static final String TABLE_NAME2 = "TransactionTable";
    public static final String COLUMN_11 = "accountNo";
    public static final String COLUMN_12= "bankName";
    public static final String COLUMN_13= "accountHolderName";
    public static final String COLUMN_14= "balance";
    public static final String COLUMN_21 = "accountNo";
    public static final String COLUMN_22= "type";
    public static final String COLUMN_23= "ammount";
    public static final String COLUMN_24= "date";
    SimpleDateFormat simpleDateFormat =new SimpleDateFormat();
    //initialize the database
    public MyDBHandler(Context context) {

        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE1 = "CREATE TABLE "+TABLE_NAME1+"("+COLUMN_11 +
                " TEXT , " + COLUMN_12 + " TEXT, "+COLUMN_13+" TEXT," +COLUMN_14+" REAL)";
        db.execSQL(CREATE_TABLE1);
        String CREATE_TABLE2 = "CREATE TABLE "+TABLE_NAME2+"("+COLUMN_21 +
                " TEXT , " + COLUMN_22 + " TEXT, "+COLUMN_23+" REAL," +COLUMN_24+" DATE)";
        db.execSQL(CREATE_TABLE2);
    }


    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);
    }

    public List<Account> getAccountsList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Account> array_list = new ArrayList<Account>();
        Cursor res = db.rawQuery("select accountNo from " + TABLE_NAME1, null);
        res.moveToFirst();
        while (res.isAfterLast() ==false){
            Account account=new Account(res.getString(res.getColumnIndex("accountNo")),res.getString(res.getColumnIndex("bankName")),res.getString(res.getColumnIndex("bankHolderName")),res.getDouble(res.getColumnIndex("balance")));
            array_list.add(account);
            res.moveToNext();
        }
        res.close();
        return array_list;

    }


    public Account getAccount(String accountNo)  {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_NAME1+" WHERE "+COLUMN_11+" = ?",new String[] { accountNo } );
        if( res != null && res.moveToFirst()){
            Account account=new Account(res.getString(res.getColumnIndex(COLUMN_11)), res.getString(res.getColumnIndex(COLUMN_12)), res.getString(res.getColumnIndex(COLUMN_13)), res.getDouble(res.getColumnIndex(COLUMN_14)));
            res.close();
            return  account;
        }
        else {
            return null;
        }
    }

    public void addAccount(Account account) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_11, account.getAccountNo());
        contentValues.put(COLUMN_12, account.getBankName());
        contentValues.put(COLUMN_13, account.getAccountHolderName());
        contentValues.put(COLUMN_14, account.getBalance());
        db.insert(TABLE_NAME1, null, contentValues);


    }

    public void removeAccount(String accountNo) throws InvalidAccountException {


    }


    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = this.getAccount(accountNo);

        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                SQLiteDatabase db1 = getWritableDatabase();
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put(COLUMN_14,account.getBalance());
                String whereClause1 = "accountNo=?";
                String whereArgs1[] = {accountNo};
                db1.update(TABLE_NAME1, contentValues1, whereClause1, whereArgs1);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                SQLiteDatabase db2 = getWritableDatabase();
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put(COLUMN_14,account.getBalance());
                String whereClause2 = "accountNo=?";
                String whereArgs2[] = {accountNo};
                db2.update(TABLE_NAME1, contentValues2, whereClause2, whereArgs2);
                break;
        }

    }




    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME1, null);
        res.moveToFirst();
        while (res.isAfterLast() ==false){

            array_list.add(res.getString(res.getColumnIndex("accountNo")));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_21, accountNo);
        contentValues.put(COLUMN_22, String.valueOf(expenseType));
        contentValues.put(COLUMN_23, amount);
        contentValues.put(COLUMN_24,simpleDateFormat.format(date));
        db.insert(TABLE_NAME2, null, contentValues);

    }


    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Transaction> array_list = new ArrayList<Transaction>();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME2, null);
        res.moveToFirst();
        while (res.isAfterLast() ==false){

            try {
                Date date = new SimpleDateFormat("dd/mm/yyyy").parse(res.getString(res.getColumnIndex("date")));
                double amount=res.getDouble(res.getColumnIndex("amount"));
                ExpenseType expenseType=ExpenseType.INCOME;
                if (res.getString(res.getColumnIndex("expenseType"))=="Expense"){
                    expenseType = ExpenseType.EXPENSE;
                }
                Transaction transaction=new Transaction(date,res.getString(res.getColumnIndex("accountNo")),expenseType,amount);
                array_list.add(transaction);
                res.moveToNext();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        res.close();
        return array_list;

    }


    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Transaction> array_list = new ArrayList<Transaction>();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME2+" order by "+COLUMN_24+" DESC limit "+limit, null);
        res.moveToFirst();
        while (res.isAfterLast() ==false){
            try {
                Date date = new SimpleDateFormat("dd/mm/yyyy").parse(res.getString(res.getColumnIndex("date")));
                double amount=res.getDouble(res.getColumnIndex(COLUMN_23));
                ExpenseType expenseType=ExpenseType.INCOME;
                if (res.getString(res.getColumnIndex(COLUMN_22))=="Expense"){
                    expenseType = ExpenseType.EXPENSE;
                }
                Transaction transaction=new Transaction(date,res.getString(res.getColumnIndex("accountNo")),expenseType,amount);
                array_list.add(transaction);
                res.moveToNext();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        res.close();
        return array_list;



    }
}


