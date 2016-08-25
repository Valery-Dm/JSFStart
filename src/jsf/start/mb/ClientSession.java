package jsf.start.mb;

import java.awt.IllegalComponentStateException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import jsf.start.model.ClientLookupService;
import jsf.start.model.data.Languages;
import jsf.start.model.data.Plans;
import jsf.start.model.impl.Client;

/**
 * Abstracted client's login info.
 * 
 * @author user
 *
 */
public abstract class ClientSession implements Serializable {

    private static final long serialVersionUID = 2L;
    protected static final byte minNameLength = 6;
    protected static final byte maxNameLength = 50;
    protected static final byte minPassLength = 6;
    private String id;
    private String errorMessage;
    transient private String password;
    transient private Client client;

    /* HashMap instead of database of clients */
    @ManagedProperty("#{virtualDataBase}")
    transient private ClientLookupService service;
    /* Localized resources */
    @ManagedProperty("#{languages}")
    transient private Languages languages;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public Languages getLanguages() {
        return languages;
    }

    public void setLanguages(Languages languages) {
        this.languages = languages;
    }

    public String getFirstName() {
        return client == null ? null :
               client.getFirstname();
    }

    public String getLastName() {
        return client == null ? null :
               client.getLastname();
    }

    public String getPlan() {
        return client == null ? Plans.PLAN500.getPlanName() :
               client.getPlan().getPlanName();
    }

    public String getDeposit() {
        return client == null ? null :
               client.getDeposit().toPlainString();
    }
    
    
    public void setService(ClientLookupService service) {
        this.service = service;
    }

    protected Client getClient(String id) {
        if (client == null) {
            // that's unexpected behavior (but it's pretty common in Eclipse)
            // so it's just eases debugging for me
            if (service == null)
                throw new IllegalComponentStateException(
                        "ClientLookupService was not injected into Client Bean");
            // could be null if client doesn't exist
            else client = service.findClientById(id);
        }
        return client;
    }
    
    protected boolean isPasswordCorrect(String password) {
        getClient(id);
        return client == null ? false : 
               client.getPassword().equals(password);
    }
    
    protected void throwErrorMessage(String msg) {
        if (languages == null)
            throw new IllegalComponentStateException(
                    "Languages library was not injected into Client Bean");
        errorMessage = languages.getMessage(msg);
        throw new ValidatorException(getFacesMessage(errorMessage));
    }

    protected FacesMessage getFacesMessage(String msg) {
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
    }
    
    protected void setFacesMessage() {
        FacesContext.getCurrentInstance()
                    .addMessage(null, getFacesMessage(errorMessage));
    }
    
    public abstract String login();
    public abstract void validateIdLength(FacesContext context, UIComponent component,
                                          Object value) throws ValidatorException;
    public abstract void validatePassword(FacesContext context, UIComponent component,
                                          Object value) throws ValidatorException;
}
