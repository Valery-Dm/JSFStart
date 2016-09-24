package jsf.start.model;

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

    public Integer getInitialDeposit() {
        return initialDeposit;
    }

    @Override
    public String toString() {
        return planName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Plan)) return false;
        Plan other = (Plan) obj;
        return getPlanName().equals(other.getPlanName()) &&
               getInitialDeposit().equals(other.getInitialDeposit());
    }
}
