package jsf.start.mb;

import static jsf.start.model.data.Pages.HOME_PAGE;
import static jsf.start.model.data.Pages.INDEX_PAGE;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

@ManagedBean(name="clientSession")
@SessionScoped
public class ClientBean extends ClientSession {

    private static final long serialVersionUID = 2L;
    
    // It's not thread safe - so instance-per-session chosen
    transient private MessageDigest md;

    @Override
    public String login() {
        assert ajaxLoaderIsShown();
        setErrorMessage(null);
        
        if (getId() != null || getPassword() != null) {
            try {
                if (isPasswordCorrect(hashPassword(getPassword()))) return HOME_PAGE;
                else setErrorMessage(getLanguages().getMessage("cantLogin"));
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                setErrorMessage(getLanguages().getMessage("cantLogin"));
                e.printStackTrace();
            }
        } 
        // Not likely to be thrown (fields are required), added for consistency
        else setErrorMessage(getLanguages().getMessage("emptyInput"));

        setFacesMessage();
        return INDEX_PAGE;
    }

    @Override
    public void validateIdLength(FacesContext context, UIComponent component,
                                 Object value) throws ValidatorException {
        // if id is empty (i.e. null)
        if (!(value instanceof String))
            throwErrorMessage("loginerrmsgname");

        // or has incorrect length
        String userId = (String) value;
        if (userId.length() < minNameLength || userId.length() > maxNameLength)
            throwErrorMessage("loginNameValMsg");
        
        //setErrorMessage(null);
    }

    @Override
    public void validatePassword(FacesContext context, UIComponent component,
                                 Object value) throws ValidatorException {
        if (!(value instanceof String) || !isPasswordValid((String) value))
            throwErrorMessage("invalidPassword");
        //setErrorMessage(null);
    }
    
    private boolean ajaxLoaderIsShown() {
        // "Please wait" message tester, it
        // pauses page loading for visual control
        try { Thread.sleep(2000); } 
        catch (InterruptedException e) { e.printStackTrace(); }
        return true;
    }

    private String hashPassword(String password) 
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (md == null) md = MessageDigest.getInstance("MD5");
        return Base64.getEncoder().encodeToString(md.digest(password.getBytes("UTF-8")));
    }

    private boolean isPasswordValid(String value) {
        // Password should be at least 6 characters long 
        // and should contain at least 1 number
        if (value.length() < minPassLength) return false;
        for (char ch : value.toCharArray())
            if (Character.isDigit(ch)) return true;
        return false;
    }

}
