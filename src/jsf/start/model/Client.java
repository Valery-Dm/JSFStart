package jsf.start.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jsf.start.model.data.ColorSchemas;

public class Client implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String password;
    private BigDecimal deposit;

    private String firstname;
    private String lastname;
    private Plan plan;

    private String colorSchema;

    public Client(String id, String password, String firstname, String lastname, Plan plan) {
        this.id = id;
        this.password = password;
        deposit = plan != null ?
                BigDecimal.valueOf(plan.getInitialDeposit()) : BigDecimal.valueOf(0);
        this.firstname = firstname;
        this.lastname = lastname;
        this.plan = plan;
        setColorSchema(ColorSchemas.getSchema("default"));
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

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getColorSchema() {
        return colorSchema;
    }

    public void setColorSchema(String colorSchema) {
        this.colorSchema = colorSchema;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(obj);
    }
}
