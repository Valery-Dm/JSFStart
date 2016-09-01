package jsf.test.mb;

import static jsf.start.model.data.Pages.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.validator.ValidatorException;

import org.hamcrest.*;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import jsf.start.mb.ClientSession;
import jsf.start.model.data.*;


public class ClientBeanTest extends BaseTestClient {

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @SuppressWarnings("unused")
    private Matcher<String> custom = new CustomMatcher<String>("Custom matcher template") {
        @Override
        public boolean matches(Object obj) {
            return true;
        }
    };

    @Before
    public void setUp() throws Exception {
        client = service.findClientById(testId);
        facesMessageCaptor = ArgumentCaptor.forClass(FacesMessage.class);
    }

    @After
    public void tearDown() throws Exception {
        client = null;
        clientBean = null;
        facesMessageCaptor = null;
    }

    @Test
    public void testLoginSuccess() {
        clientBean.setId(testId);
        clientBean.setPassword(testPassword);
        assertThat(clientBean.login(), is(HOME_PAGE));
        assertThat(clientBean.getFirstName(), is(firstName));

    }

    @Test
    public void testLoginNull() {
        clientBean.setPassword(testPassword);
        assertThat(clientBean.login(), is(INDEX_PAGE));
        assertThat(clientBean.getFirstName(), nullValue());
        assertTrue(checkFacesMessage("emptyInput"));
    }

    @Test
    public void testLoginWrongId() {
        clientBean.setId(testId + "wrong");
        clientBean.setPassword(testPassword);
        assertThat(clientBean.login(), is(INDEX_PAGE));
        assertThat(clientBean.getFirstName(), nullValue());
        assertTrue(checkFacesMessage("cantLogin"));
    }

    @Test
    public void testLoginWrongPassword() {
        clientBean.setId(testId);
        clientBean.setPassword(testPassword + "wrong");
        assertThat(clientBean.login(), is(INDEX_PAGE));
        assertThat(clientBean.getFirstName(), nullValue());
        assertTrue(checkFacesMessage("cantLogin"));
    }

    @Test
    public void testLogout() {
        ExternalContext ext = mock(ExternalContext.class);
        when(clientBean.getContext().getExternalContext()).thenReturn(ext);
        assertThat(clientBean.logout(), is(INDEX_REDIRECT));
    }

    @Test
    public void testRegisterSuccess() {
        clientBean.setFirstName("OtherUser");
        clientBean.setId(testId + "other");
        clientBean.setPassword(testPassword + "other");
        assertThat(clientBean.register(), is(HOME_PAGE));
        assertThat(service.findClientById(testId + "other").getFirstname(),
                   is("OtherUser"));
    }

    @Test
    public void testRegisterNull() {
        clientBean.setPassword(testPassword);
        assertThat(clientBean.register(), is(REGISTER_PAGE));
        assertThat(clientBean.getFirstName(), nullValue());
        assertTrue(checkFacesMessage("emptyInput"));
    }

    @Test
    public void testRegisterIdExist() {
        clientBean.setId(testId);
        clientBean.setPassword(testPassword + "other");
        assertThat(clientBean.register(), is(REGISTER_PAGE));
        assertThat(clientBean.getFirstName(), nullValue());
        assertTrue(checkFacesMessage("idExist"));
    }

    @Test
    public void testValidateId() {
        clientBean.validateId(context, null, "validId");
    }

    @Test
    public void testValidateIdNull() {
        thrown.expect(ValidatorException.class);
        FacesMessage facesMessage = clientBean.getFacesMessage("loginerrmsgname");
        thrown.expectMessage(facesMessage.getDetail());
        clientBean.validateId(context, null, null);
    }

    @Test
    public void testValidateIdShort() {
        thrown.expect(ValidatorException.class);
        FacesMessage facesMessage = clientBean
                .getFacesMessage("loginNameValMsgParam",
                                 ClientSession.minNameLength,
                                 ClientSession.maxNameLength);
        thrown.expectMessage(facesMessage.getDetail());
        clientBean.validateId(context, null, "a");
        // Long id will be catch in the same if-statement
    }

    @Test
    public void testValidateIdExist() {
        thrown.expect(ValidatorException.class);
        FacesMessage facesMessage = clientBean.getFacesMessage("idExist");
        thrown.expectMessage(facesMessage.getDetail());
        clientBean.validateId(context, null, testId);
    }

    @Test
    public void testValidatePassword() {
        clientBean.validatePassword(context, null, "validPa33word");
    }

    @Test
    public void testValidatePasswordNull() {
        thrown.expect(ValidatorException.class);
        FacesMessage facesMessage = clientBean
                .getFacesMessage("invalidPassword",
                                 ClientSession.minPassLength);
        thrown.expectMessage(facesMessage.getDetail());
        clientBean.validatePassword(context, null, null);
    }

    @Test
    public void testValidatePasswordShort() {
        thrown.expect(ValidatorException.class);
        FacesMessage facesMessage = clientBean
                .getFacesMessage("invalidPassword",
                                 ClientSession.minPassLength);
        thrown.expectMessage(facesMessage.getDetail());
        clientBean.validatePassword(context, null, "1a");
    }

    @Test
    public void testValidatePasswordNumber() {
        thrown.expect(ValidatorException.class);
        FacesMessage facesMessage = clientBean
                .getFacesMessage("invalidPassword",
                                 ClientSession.minPassLength);
        thrown.expectMessage(facesMessage.getDetail());
        clientBean.validatePassword(context, null, "qazwsxedcrfvtgb");
    }

    @Test
    public void testId() {
        assertThat(clientBean.getId(), nullValue());
        clientBean.setId(testId);
        assertThat(clientBean.getId(), is(testId));
    }

    @Test
    public void testPassword() {
        assertThat(clientBean.getPassword(), nullValue());
        clientBean.setPassword(testPassword);
        assertThat(clientBean.getPassword(), is(testPassword));
    }

    @Test
    public void testLanguages() {
        Languages languages = new Languages();
        clientBean.setLanguages(languages);
        assertThat(clientBean.getLanguages(), is(languages));
    }

    @Test
    public void testFirstName() {
        assertThat(clientBean.getFirstName(), nullValue());
        clientBean.setFirstName(firstName);
        assertThat(clientBean.getFirstName(), is(firstName));
    }

    @Test
    public void testLastName() {
        assertThat(clientBean.getLastName(), nullValue());
        clientBean.setLastName(lastName);
        assertThat(clientBean.getLastName(), is(lastName));
    }

    @Test
    public void testPlan() {
        // default value - Plan 500
        assertThat(clientBean.getPlan(), is(Plans.PLAN500.getPlanName()));
        // setPlan() set just String planName, to get actual plan
        // other than default new Client should be registered
        clientBean.setId(testId + "other");
        clientBean.setPassword(testPassword);
        clientBean.setPlan(plan); // Plan 1000 here
        clientBean.register();
        assertThat(clientBean.getPlan(), is(plan));
    }

    @Test
    public void testGetPlans() {
        Set<String> plans = Plans.getPlans().keySet();
        assertThat(clientBean.getPlans(), is(plans));
    }

    @Test
    public void testGetDeposit() {
        clientBean.setId(testId);
        clientBean.setPassword(testPassword);
        clientBean.login();
        assertThat(clientBean.getDeposit(), is(deposit));
    }

    @Test
    public void testDueDate() {
        clientBean.setDueDate(dueDate);
        assertThat(clientBean.getDueDate(), is(dueDate));
    }
}
