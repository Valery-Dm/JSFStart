package jsf.start.model.data;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import jsf.start.model.ClientLookupService;
import jsf.start.model.impl.Client;

@ManagedBean(eager=true)
@ApplicationScoped
public class VirtualDataBase implements ClientLookupService, Serializable {
    
    private static final long serialVersionUID = 1L;
    private static Map<String, Client> clients;
    
    static {
        clients = new ConcurrentHashMap<>();
        Client cl1 = new Client("harry@somemail.com", "ax1234", "Harry", "Rosewell", Plans.PLAN1000);
        clients.put("harry@somemail.com", cl1);
        Client cl2 = new Client("freddy@someothermail.com", "4321xa", "Freddy", "Bears", Plans.PLAN5000);
        clients.put("freddy@someothermail.com", cl2);
        // with hashed password
        MessageDigest md = null;
        String pw3 = "";
        try {
            md = MessageDigest.getInstance("MD5");
            pw3 = md.digest("ax1234".getBytes()).toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Client cl3 = new Client("pall@some", pw3, "Pall", "", Plans.PLAN1000);
        clients.put("pall@some", cl3);
    }

    @Override
    public Client findClientById(String id) {
        if (id == null) return null;
        return clients.get(id.trim());
    }

    @Override
    public void createNewClient(Client client) {
        if (client != null)
            clients.put(client.getId(), client);
    }

}
