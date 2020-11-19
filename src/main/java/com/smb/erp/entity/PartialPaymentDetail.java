package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * The persistent class for the ppdetails database table.
 *
 */
@Entity
@Table(name = "ppdetails")
@NamedQuery(name = "PartialPaymentDetail.findAll", query = "SELECT p FROM PartialPaymentDetail p")
public class PartialPaymentDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long pplineno = 0l;

    private Double amount = 0.0;

    private Long position = 0l;

    @Temporal(TemporalType.TIMESTAMP)
    private Date transdate;

    private Long version = 1l;

    //bi-directional many-to-one association to BusDoc
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "docno")
    private BusDoc busdoc;

    //bi-directional many-to-one association to LederLine
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "llno")
    private LedgerLine ledline;

    public PartialPaymentDetail() {
    }

    public Long getPplineno() {
        return this.pplineno;
    }

    public void setPplineno(Long pplineno) {
        this.pplineno = pplineno;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getPosition() {
        return this.position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public Date getTransdate() {
        return this.transdate;
    }

    public void setTransdate(Date transdate) {
        this.transdate = transdate;
    }

    public Long getVersion() {
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "PartialPaymentDetail{" + "pplineno=" + pplineno + ", amount=" + amount + ", transdate=" + transdate + ", busdoc=" + getBusdoc() + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.pplineno);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PartialPaymentDetail other = (PartialPaymentDetail) obj;
        if (!Objects.equals(this.pplineno, other.pplineno)) {
            return false;
        }
        return true;
    }

    /**
     * @return the ledline
     */
    public LedgerLine getLedline() {
        return ledline;
    }

    /**
     * @param ledline the ledline to set
     */
    public void setLedline(LedgerLine ledline) {
        this.ledline = ledline;
    }

    /**
     * @return the busdoc
     */
    public BusDoc getBusdoc() {
        return busdoc;
    }

    /**
     * @param busdoc the busdoc to set
     */
    public void setBusdoc(BusDoc busdoc) {
        this.busdoc = busdoc;
    }

    public Double getBalanceAmount(){
        return busdoc.getTotalPending() - amount;
    }
    
    public Double getPaidAmount(){
        return busdoc.getTotalPaid();
    }
    
    public Double getPendingAmount(){
        if(pplineno>0){
            return busdoc.getTotalPending()+amount;
        }
        return busdoc.getTotalPending();
    }
}
