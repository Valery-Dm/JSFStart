package jsf.start.model;

import java.math.BigDecimal;

public class Client {

    private String id;
    private String password;
    private BigDecimal deposit;

    private String firstname;
    private String lastname;
    private Plan plan;

    public Client(String id, String password, String firstname, String lastname, Plan plan) {
        if (id == null || password == null)
            throw new IllegalArgumentException(
                  "null id and/or password are not allowed in constructor");
        this.id = id;
        this.password = password;
        deposit = plan != null ?
                BigDecimal.valueOf(plan.getInitialDeposit()) : BigDecimal.valueOf(0);
        this.firstname = firstname;
        this.lastname = lastname;
        this.plan = plan;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Plan getPlan() {
        return plan;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

//    public void setId(String id) {
//        this.id = id;
//    }

    public void setPassword(String password) {
        if (password != null)
            this.password = password;
    }

    public void setDeposit(BigDecimal deposit) {
        if (deposit != null)
            this.deposit = deposit;
    }

    public void setFirstname(String firstname) {
        if (firstname != null)
            this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        if (lastname != null)
            this.lastname = lastname;
    }

    public void setPlan(Plan plan) {
        if (plan != null)
            this.plan = plan;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Client))
            return false;
        Client other = (Client) obj;
        return id.equals(other.id) &&
               password.equals(other.password) &&
               deposit.equals(other.deposit) &&
               firstname.equals(other.firstname) &&
               lastname.equals(other.lastname) &&
               plan.equals(other.plan);
    }
}
