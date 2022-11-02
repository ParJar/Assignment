import org.junit.*;
import uk.gov.dwp.uc.pairtest.TicketService;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketTest {
    TicketService ticketService = new TicketServiceImpl();
    TicketTypeRequest childTickets;
    TicketTypeRequest infantTickets;
    TicketTypeRequest adultTickets;

    long id;

    @Before
    public void setUp() {
         childTickets = new TicketTypeRequest( TicketTypeRequest.Type.CHILD , 1);
         infantTickets = new TicketTypeRequest( TicketTypeRequest.Type.INFANT , 1);
         adultTickets = new TicketTypeRequest( TicketTypeRequest.Type.ADULT , 1);
         id = 1;
    }

    @Test(expected = InvalidPurchaseException.class)
    public void purchaseTickets_invalidId_throwsError() {
        id = -1;
        ticketService.purchaseTickets(id, adultTickets);
    }

    @Test
    public void purchaseTickets_validId_success() {
        id = 1;
        ticketService.purchaseTickets(id, adultTickets);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void purchaseTickets_tooManyTickets_throwsError() {
        TicketTypeRequest childTickets = new TicketTypeRequest( TicketTypeRequest.Type.CHILD , 25);
        TicketTypeRequest adultTickets = new TicketTypeRequest( TicketTypeRequest.Type.ADULT , 1);
        TicketTypeRequest infantTickets = new TicketTypeRequest( TicketTypeRequest.Type.INFANT , 5);
        ticketService.purchaseTickets(id, infantTickets, childTickets, adultTickets);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void purchaseTickets_testChildTicketsOnly_throwsError() {
        ticketService.purchaseTickets(id, childTickets);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void purchaseTickets_testInfantTicketsOnly_throwsError() {
        ticketService.purchaseTickets(id, infantTickets);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void purchaseTickets_testChildAndInfantTicketsOnly_throwsError() {
        ticketService.purchaseTickets(id, childTickets, infantTickets);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void purchaseTickets_testNoTickets_throwsError() {
        TicketTypeRequest adultTickets = new TicketTypeRequest( TicketTypeRequest.Type.ADULT , 0);
        ticketService.purchaseTickets(id, adultTickets);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void purchaseTickets_testNegativeTicketCount_throwsError() {
        TicketTypeRequest adultTickets = new TicketTypeRequest( TicketTypeRequest.Type.ADULT , -1);
        ticketService.purchaseTickets(id, adultTickets);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void purchaseTickets_InvalidType_throwsError() {
        TicketTypeRequest adultTickets = new TicketTypeRequest( null , -1);
        ticketService.purchaseTickets(id, adultTickets);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void purchaseTickets_InvalidNullTicketCount_throwsError() {
        TicketTypeRequest adultTickets = new TicketTypeRequest( null , -1);
        ticketService.purchaseTickets(id, adultTickets);
    }

    @Test
    public void purchaseTickets_testValidTickets_success() {
        TicketTypeRequest childTickets = new TicketTypeRequest( TicketTypeRequest.Type.CHILD , 18);
        TicketTypeRequest adultTickets = new TicketTypeRequest( TicketTypeRequest.Type.ADULT , 1);
        TicketTypeRequest infantTickets = new TicketTypeRequest( TicketTypeRequest.Type.INFANT , 1);
        ticketService.purchaseTickets(id, infantTickets, childTickets, adultTickets);
    }
}
