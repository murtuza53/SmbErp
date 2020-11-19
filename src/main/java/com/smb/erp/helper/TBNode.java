/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.helper;

import java.util.List;

/**
 *
 * @author Burhani152
 */
public class TBNode {
        private Double debit = 0.0;
        private Double credit = 0.0;
        private String title = "";
        
        public TBNode() {
        }
        
        public TBNode(Double debit, Double credit){
            this.debit = debit;
            this.credit = credit;
        }
        
        public TBNode(String title, Double balance){
            this.title = title;
            setBalance(balance);
        }

        public TBNode(List<Double> trans){
            this(trans.get(0), trans.get(1));
        }
        
        public void setBalance(List<Double> trans){
            setDebit(trans.get(0));
            setCredit(trans.get(1));
        }
        
        public void setBalance(Double balance){
            if(balance==null || balance==0){
                setDebit(0.0);
                setCredit(0.0);
            }else if(balance>0){
                setDebit(balance);
                setCredit(0.0);
            } else {
                setCredit(balance*-1);
                setDebit(0.0);
            }
        }
        
        public Double getBalance(){
            return debit-credit;
        }
        
        /**
         * @return the debit
         */
        public Double getDebit() {
            return debit;
        }

        /**
         * @param debit the debit to set
         */
        public void setDebit(Double debit) {
            this.debit = debit;
        }

        /**
         * @return the credit
         */
        public Double getCredit() {
            return credit;
        }

        /**
         * @param credit the credit to set
         */
        public void setCredit(Double credit) {
            this.credit = credit;
        }

        /**
         * @return the title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @param title the title to set
         */
        public void setTitle(String title) {
            this.title = title;
        }
        
    }
