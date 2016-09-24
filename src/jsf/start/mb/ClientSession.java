package jsf.start.mb;

import java.io.*;
import java.security.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

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

    private LocalDate dueDate;

    /* HashMap instead of database of clients */
    @Inject
    private ClientLookupService service;
    /* Localized resources */
    @Inject
    private Languages languages;

    // Object for password hashing.
    // It's not thread safe - so instance-per-session chosen
    transient private MessageDigest md;
    // These options are allowed to be changed from child object
    protected String hashLibrary = "MD5";
    protected String wordEncoding = "UTF-8";

    @PostConstruct
    public void init() {
        System.out.println("========= ClientSession created =========");
    }

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
        return client == null ? Plans.getDefault().getPlanName() :
               client.getPlan().getPlanName();
    }

    public Set<String> getPlans() {
        return Plans.getPlans().keySet();
    }

    public void setPlan(String plan) {
        if (plan != null) this.plan = plan.trim();
    }

    public Languages getLanguages() {
        return languages;
    }

    public void setLanguages(Languages languages) {
        this.languages = languages;
    }

    public FacesContext getContext() {
        return FacesContext.getCurrentInstance();
    }

    public String getDeposit() {
        return client == null ? null :
               client.getDeposit().toPlainString();
    }

    public void setService(ClientLookupService service) {
        this.service = service;
    }

    public Client getClient() {
        return getClient(id);
    }

    public Client getClient(String userId) {
        if (client == null) {
            // for debugging purposes
            if (service == null)
                throw new ManagedBeanCreationException(
                        "ClientLookupService was not injected into Client Bean");
            // could be null if client doesn't exist
            return service.findClientById(userId);
        }
        return client;
    }

    public LocalDate getDueDate() {
        return dueDate != null ? dueDate : LocalDate.now();
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    protected void addNewClient()
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Plan userPlan = Plans.findPlanByName(plan);
        String hashedPassword = hashPassword(password);
        client = new Client(id, hashedPassword, firstName, lastName, userPlan);
        service.createNewClient(client);
    }

    protected boolean isLoggedIn()
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Client client = getClient();
        if (client == null ||
           !client.getPassword().equals(hashPassword(password)))
            return false;
        this.client = client;
        return true;
    }

    public String hashPassword(String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (md == null) md = MessageDigest.getInstance(hashLibrary);
        return Base64.getEncoder().encodeToString(md.digest(password.getBytes(wordEncoding)));
    }

    protected void throwErrorMessage(String message, Object ... params) {
        throw new ValidatorException(getFacesMessage(message, params));
    }

    protected void setFacesMessage(String message, Object ... params) {
        getContext().addMessage(null, getFacesMessage(message, params));
    }

    public FacesMessage getFacesMessage(String message, Object ... params) {
        if (languages == null)
            throw new ManagedBeanCreationException(
                    "Languages library was not injected into Client Bean");
        String localMessage = MessageFormat.format(languages.getLocalized(message), params);
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, localMessage, null);
    }

    private void writeObject(ObjectOutputStream stream)
                                    throws IOException {
        stream.defaultWriteObject();
        // password field and client object are omitted
        // so not to expose private information
        stream.writeBoolean(client != null);

        System.out.println("============= ClientSession serialized =============");
    }

    private void readObject(ObjectInputStream stream)
            throws ClassNotFoundException, IOException {
        stream.defaultReadObject();
        // I don't know how safe is this approach,
        // I need to initialize 'client' again
        // in order to continue server's session
        // but I can't call login() as password is absent.
        // So it looks fragile:
        // what if someone could forge serialized
        // form with client's 'id' replaced
        if (stream.readBoolean()) client = getClient();

        System.out.println("============= ClientSession restored =============");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        // using getClass method to be 'symmetric'
        if (obj == null || obj.getClass() != getClass())
            return false;
        ClientSession other = (ClientSession) obj;
        if (client != null && !client.equals(other.client))
            return false;
        boolean equals = false;
        if (client == null) {
            equals = ((id == null && other.id == null) ||
                     (id != null && id.equals(other.id))) &&
                     ((firstName == null && other.firstName == null) ||
                     (firstName != null && firstName.equals(other.firstName))) &&
                     ((lastName == null && other.lastName == null) ||
                     (lastName != null && lastName.equals(other.lastName))) &&
                     ((plan == null && other.plan == null) ||
                     (plan != null && plan.equals(other.plan)));
            if (!equals) return false;
        }
        equals = ((password == null && other.password == null) ||
                 (password != null && password.equals(other.password))) &&
                 ((dueDate == null && other.dueDate == null) ||
                 (dueDate != null && dueDate.equals(other.dueDate))) &&
                 service instanceof ClientLookupService &&
                 other.service instanceof ClientLookupService &&
                 service.getClass() == other.service.getClass() &&
                 languages instanceof Languages &&
                 other.languages instanceof Languages &&
                 languages.equals(other.languages) &&
                 hashLibrary.equals(other.hashLibrary) &&
                 wordEncoding.equals(other.wordEncoding);
                // Nothing to check in service.equals() so it's omitted.
                // Since MessageDigest doesn't offer 'deep' equals method
                // its equality check is also excluded
        return equals;
    }

    public abstract String login();
    public abstract String logout();
    public abstract String register();
    public abstract void validateId(FacesContext context, UIComponent component,
                                    Object value) throws ValidatorException;
    public abstract void validatePassword(FacesContext context, UIComponent component,
                                          Object value) throws ValidatorException;
}
