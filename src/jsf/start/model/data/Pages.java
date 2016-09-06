package jsf.start.model.data;

/* For pages with composite components */
public class Pages {

    private static final String flow = "/client/";

    /* Redirect strings */
    public static final String INDEX_PAGE     = flow + "index-cc";
    public static final String INDEX_REDIRECT = flow + "index-cc?faces-redirect=true";
    public static final String REGISTER_PAGE  = flow + "register-cc";
    public static final String HOME_PAGE      = flow + "home-cc?faces-redirect=true" +
                                                "&amp;includeViewParams=true";
}
