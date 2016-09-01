package jsf.start.mb;

import static jsf.start.model.data.ColorSchemas.*;

import java.io.*;
import java.security.*;
import java.text.MessageFormat;
import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import com.sun.faces.mgbean.ManagedBeanCreationException;

import jsf.start.model.*;
import jsf.start.model.data.*;

/* Abstracted client's login and register info */
public abstract class ClientSession implements Serializable {

    private static final long serialVersionUID = 2L;
    public static final byte minNameLength = 6;
    public static final byte maxNameLength = 50;
    public static final byte minPassLength = 6;

    private String id;
    private String firstName;
    private String lastName;
    private String plan;
    transient private String password;
    transient private Client client;

    private Date dueDate;

    /* HashMap instead of database of clients */
    @ManagedProperty("#{virtualDataBase}")
    transient private ClientLookupService service;
    /* Localized resources */
    @ManagedProperty("#{languages}")
    transient private Languages languages;
    /* Current context */
    @ManagedProperty("#{facesContext}")
    transient private FacesContext context;

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
        if (id != null) this.id = id.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // spaces are allowed
        this.password = password;
    }

    public Languages getLanguages() {
        return languages;
    }

    public void setLanguages(Languages languages) {
        this.languages = languages;
    }

    public FacesContext getContext() {
        return context;
    }

    public void setContext(FacesContext context) {
        this.context = context;
    }

    public String getFirstName() {
        return client == null ? firstName :
               client.getFirstname();
    }

    public void setFirstName(String fname) {
        if (fname != null) firstName = fname.trim();
    }

    public String getLastName() {
        return client == null ? lastName :
               client.getLastname();
    }

    public void setLastName(String lname) {
        if (lname != null) lastName = lname.trim();
    }

    public String getPlan() {
        return client == null ? Plans.PLAN500.getPlanName() :
               client.getPlan().getPlanName();
    }

    public Set<String> getPlans() {
        return Plans.getPlans().keySet();
    }

    public void setPlan(String plan) {
        if (plan != null) this.plan = plan.trim();
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

    protected Client getClient(String userId) {
        if (client == null) {
            // that's unexpected behavior (but it's pretty common in Eclipse),
            // right message just eases debugging for me
            if (service == null)
                throw new ManagedBeanCreationException(
                        "ClientLookupService was not injected into Client Bean");
            // could be null if client doesn't exist
            return service.findClientById(userId);
        }
        return client;
    }

    public Date getDueDate() {
        return dueDate != null ? dueDate : new Date();
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getColorSchema() {
        if (client == null)
            return ColorSchemas.getSchema("default");
        return client.getColorSchema();
    }

    public void setColorSchema(String colorSchema) {
        if (colorSchema != null && client != null) {
            colorSchema = colorSchema.trim().toLowerCase();
            if (contains(colorSchema))
                client.setColorSchema(getSchema(colorSchema));
        }
    }

    protected void addNewClient() {
        Plan userPlan = Plans.findPlanByName(plan);
        client = new Client(id, password, firstName, lastName, userPlan);
        service.createNewClient(client);
    }

    protected boolean isLoggedIn()
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Client cl = getClient();
        if (cl == null ||
           !cl.getPassword().equals(hashPassword(password)))
            return false;
        client = cl;
        return true;
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

        throw new ValidatorException(getFacesMessage(message, params));
    }

    public FacesMessage getFacesMessage(String message, Object ... params) {
        String localMessage = MessageFormat.format(languages.getLocalized(message), params);
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, localMessage, null);
    }

    protected void setFacesMessage(String message, Object ... params) {
        getContext().addMessage(null, getFacesMessage(message, params));
    }

    public abstract String login();
    public abstract String logout();
    public abstract String register();
    public abstract void validateId(FacesContext context, UIComponent component,
                                    Object value) throws ValidatorException;
    public abstract void validatePassword(FacesContext context, UIComponent component,
                                          Object value) throws ValidatorException;
}
