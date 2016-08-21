package jsf.start.model.impl;

import java.io.Serializable;

public class Plan implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String planName;
    private Integer initialDeposit;
    
    public Plan(String planName, Integer initialDeposit) {
        this.planName = planName;
        this.initialDeposit = initialDeposit;
    }

    public String getPlanName() {
        return planName;
    }
    
    public void setPlanName(String planName) {
        this.planName = planName;
    }
    
    public Integer getInitialDeposit() {
        return initialDeposit;
    }
    
    public void setInitialDeposit(Integer initialDeposit) {
        this.initialDeposit = initialDeposit;
    }
    
    @Override
    public String toString() {
        return planName;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Plan)) return false;
        return getPlanName().equals(((Plan) obj).getPlanName());
    }

    @Override
    public int hashCode() {
        return planName.hashCode();
    }
}
