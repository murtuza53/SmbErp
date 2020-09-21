package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the prodtransexecution database table.
 *
 */
@Entity
@Table(name = "prodtransexecution")
public class ProductTransactionExecution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer executionid = (int)new Date().getTime();

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdon;

    private double executionqty;

    @JoinColumn(name = "fromprodtransid", referencedColumnName = "prodtransid")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ProductTransaction fromprodtransid;

    //bi-directional many-to-one association to ProductTransaction
    @JoinColumn(name = "toprodtransid", referencedColumnName = "prodtransid")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ProductTransaction toprodtransid;

    public ProductTransactionExecution() {
    }

    public Date getCreatedon() {
        return this.createdon;
    }

    public void setCreatedon(Date createdon) {
        this.createdon = createdon;
    }

    public ProductTransaction getFromprodtransid() {
        return this.fromprodtransid;
    }

    public void setFromprodtransid(ProductTransaction fromprodtransid) {
        this.fromprodtransid = fromprodtransid;
    }

    /**
     * @return the toprodtransid
     */
    public ProductTransaction getToprodtransid() {
        return toprodtransid;
    }

    /**
     * @param toprodtransid the toprodtransid to set
     */
    public void setToprodtransid(ProductTransaction toprodtransid) {
        this.toprodtransid = toprodtransid;
    }

    /**
     * @return the executionid
     */
    public Integer getExecutionid() {
        return executionid;
    }

    /**
     * @param executionid the executionid to set
     */
    public void setExecutionid(Integer executionid) {
        this.executionid = executionid;
    }

    /**
     * @return the executionqty
     */
    public double getExecutionqty() {
        return executionqty;
    }

    /**
     * @param executionqty the executionqty to set
     */
    public void setExecutionqty(double executionqty) {
        this.executionqty = executionqty;
    }

    @Override
    public String toString(){
        StringBuilder buf = new StringBuilder();
        buf.append("From_PT:" + fromprodtransid.getProdtransid() + ", To_PT:" + toprodtransid.getProdtransid() +", Exe_Qty:" + getExecutionqty());
        return buf.toString();
    }
}
