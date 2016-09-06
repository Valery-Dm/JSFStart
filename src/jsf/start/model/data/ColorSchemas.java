package jsf.start.model.data;

import java.io.Serializable;
import java.util.concurrent.*;

//@Named
//@ApplicationScoped
public class ColorSchemas implements Serializable {

    private static final long serialVersionUID = 1L;
    // Color name should be less than 16 and greater than 5
    // characters long (use in mutator methods)
    public static final int LT = 16, GT = 5;

    private static ConcurrentMap<String, String> schemas;

    static {
        schemas = new ConcurrentHashMap<>();
        schemas.put("default", "homeDefault.css");
        schemas.put("darkblue", "homeDarkBlue.css");
    }

    public static String getSchema(String name) {
        return schemas.get(name);
    }

    public static boolean contains(String name) {
        return schemas.containsKey(name);
    }

}
