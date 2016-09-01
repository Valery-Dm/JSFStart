package jsf.test.mb;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.mockito.*;

import jsf.start.mb.ClientBean;
import jsf.start.model.*;
import jsf.start.model.data.*;

public abstract class BaseTestClient {

    // test client info
    protected Client client;
    protected String testId = "user@test";
    protected String testPassword = "test1234";
    protected String firstName = "User";
    protected String lastName = "Test";
    protected String plan = Plans.PLAN1000.getPlanName();
    protected String deposit = Plans.PLAN1000.getInitialDeposit().toString();
    protected Date dueDate = new Date();

    protected ArgumentCaptor<FacesMessage> facesMessageCaptor;

    @Spy
    protected FacesContext context = FacesContext.getCurrentInstance();

    @Spy
    protected ClientLookupService service = new TestDataBase();

    @Spy
    protected Languages languages = new Languages();

    @InjectMocks
    protected ClientBean clientBean;

    public BaseTestClient() {
        MockitoAnnotations.initMocks(this);
        languages.init();
    }

    protected void fail() {
        fail("Not yet implemented");
    }

    protected void fail(String msg) {
        assert false : msg;
    }

    protected boolean checkFacesMessage(String message) {
        verify(clientBean.getContext())
              .addMessage(anyString(), facesMessageCaptor.capture());
        FacesMessage facesMessage = facesMessageCaptor.getValue();
        String localMessage = languages.getLocalized(message);
        // As of right now I have errors only.
        // Severity may be accepted as a parameter if needed
        return facesMessage.getSeverity().equals(FacesMessage.SEVERITY_ERROR) &&
               facesMessage.getDetail().equals(localMessage);
    }

    private class TestDataBase implements ClientLookupService {

        Map<String, Client> clients;

        TestDataBase() {
            clients = new HashMap<>();
            String hashedPassword = null;
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                hashedPassword = Base64.getEncoder()
                        .encodeToString(md.digest(testPassword.getBytes("UTF-8")));
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            client = new Client(testId, hashedPassword, firstName, lastName, Plans.PLAN1000);
            clients.put(testId, client);
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
}
