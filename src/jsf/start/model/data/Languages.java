package jsf.start.model.data;

import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@SessionScoped
public class Languages implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Map<String,String> LANGUAGE_MAP =
            new LinkedHashMap<String,String>();
    
    static {
        LANGUAGE_MAP.put("English", "en");
        LANGUAGE_MAP.put("\u0420\u0443\u0441\u0441\u043A\u0438\u0439", "ru");
    }

    private Locale locale = new Locale("en");
    private String language = "en";
    transient private ResourceBundle text;
    
    @PostConstruct
    public void init() {
        setMessages(locale);
    }
    
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
        setLocale(new Locale(language));
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        setMessages(locale);
    }
    
    private void setMessages(Locale locale) {
        text = ResourceBundle.getBundle("resources.text", locale);
    }
    
    public Map<String,String> getLanguages() {
        return LANGUAGE_MAP;
    }
    
    public String getLocalized(String name) {
        return text.getString(name);
    }

}
