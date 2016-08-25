package jsf.start.mb;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import com.sun.faces.mgbean.ManagedBeanCreationException;

import jsf.start.model.*;
import jsf.start.model.data.Languages;
import jsf.start.model.data.Plans;

/* Abstracted client's login and register info */
public abstract class ClientSession implements Serializable {

    private static final long serialVersionUID = 2L;
    protected static final byte minNameLength = 6;
    protected static final byte maxNameLength = 50;
    protected static final byte minPassLength = 6;

    private String id;
    private String firstName;
    private String lastName;
    private String plan;
    transient private String password;
    transient private Client client;

    /* HashMap instead of database of clients */
    @ManagedProperty("#{virtualDataBase}")
    transient private ClientLookupService service;
    /* Localized resources */
    @ManagedProperty("#{languages}")
    transient private Languages languages;

    // Object for password hashing.
    // It's not thread safe - so instance-per-session chosen
    transient private MessageDigest md;
    // These options are allowed to be changed from child object
    transient protected String hashLibrary = "MD5";
    transient protected String wordEncoding = "UTF-8";

    public String getId() {
        return client == null ? id :
               client.getId();
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

    public Languages getLanguages() {
        return languages;
    }

    public void setLanguages(Languages languages) {
        this.languages = languages;
    }

    public String getFirstName() {
        return client == null ? firstName :
               client.getFirstname();
    }

    public void setFirstName(String fname) {
        firstName = fname;
    }

    public String getLastName() {
        return client == null ? lastName :
               client.getLastname();
    }

    public void setLastName(String lname) {
        lastName = lname;
    }

    public String getPlan() {
        return client == null ? Plans.PLAN500.getPlanName() :
               client.getPlan().getPlanName();
    }

    public Set<String> getPlans() {
        return Plans.getPlans().keySet();
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getDeposit() {
        return client == null ? null :
               client.getDeposit().toPlainString();
    }

    public void setService(ClientLookupService service) {
        this.service = service;
    }

    protected Client getClient() {
        return getClient(id);
    }

    protected Client getClient(String id) {
        if (client == null) {
            // that's unexpected behavior (but it's pretty common in Eclipse)
            // so it's just eases debugging for me
            if (service == null)
                throw new ManagedBeanCreationException(
                        "ClientLookupService was not injected into Client Bean");
            // could be null if client doesn't exist
            else client = service.findClientById(id);
        }
        return client;
    }

    protected void addNewClient() {
        Plan userPlan = Plans.findPlanByName(plan);
        client = new Client(id, password, firstName, lastName, userPlan);
        service.createNewClient(client);
    }

    protected boolean isLoggedIn()
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return getClient() == null ? false :
               client.getPassword().equals(hashPassword(password));
    }

    private String hashPassword(String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (md == null) md = MessageDigest.getInstance(hashLibrary);
        return Base64.getEncoder().encodeToString(md.digest(password.getBytes(wordEncoding)));
    }

    protected void throwErrorMessage(String message, Object ... params) {
        if (languages == null)
            throw new ManagedBeanCreationException(
                    "Languages library was not injected into Client Bean");

        String localMessage = MessageFormat.format(languages.getLocalized(message), params);
        throw new ValidatorException(getFacesMessage(localMessage));
    }

    private FacesMessage getFacesMessage(String message) {
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
    }

    protected void setFacesMessage(String message, Object ... params) {
        String localMessage = MessageFormat.format(languages.getLocalized(message), params);
        FacesContext.getCurrentInstance()
                    .addMessage(null, getFacesMessage(localMessage));
    }

    public abstract String login();
    public abstract String logout();
    public abstract String register();
    public abstract void validateId(FacesContext context, UIComponent component,
                                    Object value) throws ValidatorException;
    public abstract void validatePassword(FacesContext context, UIComponent component,
                                          Object value) throws ValidatorException;
}
