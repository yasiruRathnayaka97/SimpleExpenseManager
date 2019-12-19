package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.MyDBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class persistentAccountDAO  implements AccountDAO {
    private MyDBHandler mdh;
    public persistentAccountDAO( MyDBHandler mdh) {
        this.mdh=mdh;
    }

    @Override
    public List<Account> getAccountsList() {
        return mdh.getAccountsList();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return mdh.getAccount(accountNo);
    }

    @Override
    public void addAccount(Account account) {
        mdh.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        mdh.updateBalance(accountNo,expenseType,amount);
    }

    @Override
    public List<String> getAccountNumbersList() {
       return mdh.getAccountNumbersList();
    }
}
