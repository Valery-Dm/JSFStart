package jsf.start.mb;

import static jsf.start.model.data.Pages.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

/* All boilerplate code is abstracted away, so just business logic is in here */
@Named("clientSession")
@FlowScoped("ClientFlow")
public class ClientBean extends ClientSession {

    private static final long serialVersionUID = 2L;


    @Override
    public String login() {
        assert ajaxLoaderIsShown();

        if (getId() != null && getPassword() != null) {
            try {
                if (isLoggedIn()) return HOME_PAGE;
                else setFacesMessage("cantLogin");
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                setFacesMessage("somethingWrong");
                e.printStackTrace();
            }
        }
        // Not likely to be thrown (fields are required), added for consistency
        else setFacesMessage("emptyInput");

        return INDEX_PAGE;
    }

    @Override
    public String logout() {
        getContext().getExternalContext().invalidateSession();
        return INDEX_REDIRECT;
    }

    @Override
    public String register() {
        assert ajaxLoaderIsShown();

        if (getId() != null && getPassword() != null) {
            if (getClient() == null) {
                try {
                    addNewClient();
                    return HOME_PAGE;
                } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                    e.printStackTrace(); // logging is not yet implemented
                    setFacesMessage("somethingWrong");
                }
            } else setFacesMessage("idExist");
        }
        // Not likely to be thrown (fields are required), added for consistency
        else setFacesMessage("emptyInput");

        return REGISTER_PAGE;
    }

    @Override
    public void validateId(FacesContext context, UIComponent component,
                           Object value) throws ValidatorException {
        // if id is empty (i.e. null)
        if (!(value instanceof String))
            throwErrorMessage("loginerrmsgname");

        // or has incorrect length
        String userId = ((String) value).trim();
        if (userId.length() < minNameLength || userId.length() > maxNameLength)
            throwErrorMessage("loginNameValMsgParam", minNameLength, maxNameLength);

        // if provided userId is already registered
        if (getClient(userId) != null)
            throwErrorMessage("idExist");
    }

    @Override
    public void validatePassword(FacesContext context, UIComponent component,
                                 Object value) throws ValidatorException {
        if (!(value instanceof String) || !isPasswordValid((String) value))
            throwErrorMessage("invalidPassword", minPassLength);
    }

    private boolean isPasswordValid(String value) {
        // Password should be at least 6 characters long
        // and should contain at least 1 number
        if (value.length() < minPassLength) return false;
        for (char ch : value.toCharArray())
            if (Character.isDigit(ch)) return true;
        return false;
    }

    private boolean ajaxLoaderIsShown() {
        // "Please wait" message tester, it
        // pauses page loading for visual control
        try { Thread.sleep(20); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return true;
    }
}
