package jsf.test.model.data;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jsf.start.model.Client;
import jsf.start.model.ClientLookupService;
import jsf.start.model.data.Plans;


public class TestDataBase implements ClientLookupService {

    private static Map<String, Client> clients;

    static {
        clients = new ConcurrentHashMap<>();
        MessageDigest md = null;
        String id = "user@test";
        String pwd = "test1234";
        try {
            md = MessageDigest.getInstance("MD5");
            pwd = Base64.getEncoder().encodeToString(md.digest(pwd.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Client cl = new Client(id, pwd, "Pall", "", Plans.PLAN1000);
        clients.put(id, cl);
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
