/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.entity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author FatemaLaptop
 */
public enum BusDocTransactionType {

    NON_INVENTORY("Non_Inventory"),                 //transactiontype
    INVENTORY("Inventory"),                    //transactiontype
    ACCOUNTS_NONE("Accounts_None"),                 //accounttype
    ACCOUNTS_DEFAULT("Accounts_Default"),                 //accounttype
    ACCOUNTS_VOUCHER("Accounts_Voucher"),                 //accounttype
    ACCOUNTS_PAYABLE("Accounts_Payable"),           //accounttype
    ACCOUNTS_RECEIVABLE("Accounts_Receivable");     //accounttype

    public static List<String> BUSDOC_INVENTORY_TYPES = Arrays.asList(new String[]{NON_INVENTORY.getValue(), INVENTORY.getValue()});

    public static List<String> BUSDOC_ACCOUNT_TYPES = Arrays.asList(new String[]{ACCOUNTS_NONE.getValue(), ACCOUNTS_DEFAULT.getValue(),
        ACCOUNTS_VOUCHER.getValue(), ACCOUNTS_PAYABLE.getValue(), ACCOUNTS_RECEIVABLE.getValue()});

    private String value;

    BusDocTransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
