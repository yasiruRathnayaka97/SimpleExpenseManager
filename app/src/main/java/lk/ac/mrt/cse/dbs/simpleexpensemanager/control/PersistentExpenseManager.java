package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;


import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistentTransactionDAO;


public class PersistentExpenseManager extends ExpenseManager {
     Context context;
    public PersistentExpenseManager(Context context){
        this.context=context;
            setup();
    }
    @Override
    public void setup() {
        MyDBHandler mdh=new MyDBHandler(context);
        AccountDAO persistentAccountDAO = new persistentAccountDAO(mdh);
        setAccountsDAO(persistentAccountDAO);
        TransactionDAO persistentTransactionDAO = new persistentTransactionDAO(mdh);
        setTransactionsDAO(persistentTransactionDAO);

//        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
//        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
//        getAccountsDAO().addAccount(dummyAcct1);
//        getAccountsDAO().addAccount(dummyAcct2);
        //ADD THIS CAN COASUSE REPETTITION OF ENTITIES.
    }
}
