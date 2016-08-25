package jsf.start.model;

public interface ClientLookupService {
    Client findClientById(String id);
    void createNewClient(Client client);
}
