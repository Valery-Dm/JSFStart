package jsf.start.model.data;

import java.io.*;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class Languages implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Map<String,String> LANGUAGE_MAP =
            new LinkedHashMap<>();

    static {
        LANGUAGE_MAP.put("English", "en");
        LANGUAGE_MAP.put("\u0420\u0443\u0441\u0441\u043A\u0438\u0439", "ru");
    }

    private String language = "en";
    transient private Locale locale = new Locale(language);
    transient private ResourceBundle text;

    @PostConstruct
    public void init() {
        setMessages(locale);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        if (language != null) {
            this.language = language;
            setLocale(new Locale(language));
        }
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        if (locale != null) {
            this.locale = locale;
            setMessages(locale);
        }
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Languages))
            return false;
        Languages other = (Languages) obj;
        // 'text' implies to be also equal (by design)
        // if 'languages' and 'locale' are the same
        return language.equals(other.language) &&
               locale.equals(other.locale);
    }

    // accept default writeObject form,
    // only need special reading
    private void readObject(ObjectInputStream stream)
            throws ClassNotFoundException, IOException {
        stream.defaultReadObject();
        setLanguage(language);
    }
}
