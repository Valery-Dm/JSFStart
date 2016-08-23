package jsf.start.mb;

import java.io.Serializable;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import jsf.start.model.ClientLookupService;
import jsf.start.model.data.Languages;
import jsf.start.model.data.Plans;
import jsf.start.model.impl.Client;
import jsf.start.model.impl.Plan;

@ManagedBean(name = "client")
@SessionScoped
public class ClientSessionBean implements Serializable {

    private static final long serialVersionUID = 1L;
    /* Redirect strings */
    private static final String INDEX_PAGE    = "index";
    private static final String REGISTER_PAGE = "register";
    private static final String HOME_PAGE     = "home?faces-redirect=true";

    /* Client's data */
    private String           id;
    transient private String password;
    private String           deposit;
    transient private String errorMessage;
    private String           firstname;
    private String           lastname;
    private String           plan = Plans.PLAN500.getPlanName();
    /* HashMap instead of database of clients */
    @ManagedProperty("#{virtualDataBase}")
    transient private ClientLookupService service;
    /* Localized resources */
    @ManagedProperty("#{languages}")
    transient private Languages msg;
    
    @PostConstruct
    public void init() {
//        System.out.println("Post construct");
//        service.toString();
//        msg.toString();
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        errorMessage = null;
        // immediate ajax validation
        if (service.findClientById(id) != null) 
            errorMessage = getMsg().getMessage("idExist");
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // public void setErrorMessage(String errMessage) {
    // this.errorMessage = errMessage;
    // }

    public ClientLookupService getService() {
        return service;
    }

    public void setService(ClientLookupService service) {
        this.service = service;
    }

    public Languages getMsg() {
        return msg;
    }

    public void setMsg(Languages msg) {
        this.msg = msg;
    }

    public Set<String> getPlans() {
        return Plans.getPlans().keySet();
    }

    public String login() {
        // For testing "Please wait" message
         try {
             Thread.sleep(1000);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
        errorMessage = null;
        if (id != null && password != null) {
            Client client = service.findClientById(id);
            if (client != null && client.getPassword().equals(password)) {
                firstname = client.getFirstname();
                lastname = client.getLastname();
                plan = client.getPlan().getPlanName();
                deposit = client.getDeposit().toPlainString();
                return HOME_PAGE;
            } else errorMessage = getMsg().getMessage("cantLogin");
        } else errorMessage = getMsg().getMessage("emptyInput");

        FacesContext.getCurrentInstance().addMessage(null, setErrorMessage(errorMessage));
        return INDEX_PAGE;
    }

    public String register() {
        errorMessage = null;
        if (id != null && password != null) {
            Client client = service.findClientById(id);
            if (client == null) {
                Plan userPlan = Plans.findPlanByName(plan);
                client = new Client(id, password, firstname, lastname, userPlan);
                deposit = client.getDeposit().toPlainString();
                service.createNewClient(client);
                return HOME_PAGE;
            } else errorMessage = getMsg().getMessage("idExist");
        } else errorMessage = getMsg().getMessage("emptyInput");
        
        FacesContext.getCurrentInstance().addMessage(null, setErrorMessage(errorMessage));
        return REGISTER_PAGE;
    }

    public void validatePassword(FacesContext context, 
                                 UIComponent component, 
                                 Object value) throws ValidatorException {
        errorMessage = null;
        if (!(value instanceof String) || !isPasswordValid((String) value)) {
            errorMessage = getMsg().getMessage("invalidPassword");
            throw new ValidatorException(setErrorMessage(errorMessage));
        }
    }

    private boolean isPasswordValid(String value) {
        //Password should be at least 6 characters long and should contain at least 1 number
        if (value.length() < 6) return false;
        for (char ch : value.toCharArray())
            if (Character.isDigit(ch)) return true;
        return false;
    }
    
    private FacesMessage setErrorMessage(String msg) {
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
    }
}
