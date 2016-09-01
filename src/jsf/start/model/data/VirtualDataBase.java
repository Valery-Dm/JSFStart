package jsf.start.model.data;

import java.io.*;
import java.security.*;
import java.util.Base64;
import java.util.concurrent.*;

import javax.faces.bean.*;

import jsf.start.model.*;

@ManagedBean(eager=true)
@ApplicationScoped
public class VirtualDataBase implements ClientLookupService, Serializable {

    private static final long serialVersionUID = 1L;
    private static ConcurrentMap<String, Client> clients;

    static {
        clients = new ConcurrentHashMap<>();
        Client cl1 = new Client("harry@somemail.com", "ax1234",
                                "Harry", "Rosewell", Plans.PLAN1000);
        clients.put("harry@somemail.com", cl1);
        Client cl2 = new Client("freddy@someothermail.com", "4321xa",
                                "Freddy", "Bears", Plans.PLAN5000);
        clients.put("freddy@someothermail.com", cl2);
        // with hashed password
        MessageDigest md = null;
        String pw3 = "ax1234";
        try {
            md = MessageDigest.getInstance("MD5");
            pw3 = Base64.getEncoder().encodeToString(md.digest(pw3.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Client cl3 = new Client("pall@some", pw3, "Pall", "", Plans.PLAN1000);
        clients.put("pall@some", cl3);
    }

    @Override
    public Client findClientById(String id) {
        return clients.get(id);
    }

    @Override
    public void createNewClient(Client client) {
        clients.putIfAbsent(client.getId(), client);
    }

}
