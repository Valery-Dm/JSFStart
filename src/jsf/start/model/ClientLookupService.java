package jsf.start.model;

import jsf.start.model.impl.Client;

public interface ClientLookupService {
    Client findClientById(String id);
    void createNewClient(Client client);
}
