package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {

    public static final int MAX_TICKETS_PER_REQUEST = 20;
    public static final int TICKET_PRICE_INFANT = 0;
    public static final int TICKET_PRICE_CHILD = 10;
    public static final int TICKET_PRICE_ADULT = 20;

    private TicketPaymentService ticketPaymentService = new TicketPaymentServiceImpl();

    private SeatReservationService seatReservationService = (accountId, totalSeatsToAllocate) -> {};

    /**
     * Should only have private methods other than the one below.
     */
    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        validateId(accountId);

        int cost = 0;
        int seats = 0;
        int ticketCount = 0;
        boolean hasAdultTicket = false;

        for (TicketTypeRequest request : ticketTypeRequests) {
            if(request.getNoOfTickets() < 1) {
                throw new InvalidPurchaseException();
            }

            cost += getTicketPrice(request) * request.getNoOfTickets();
            seats = request.getTicketType() != TicketTypeRequest.Type.INFANT ? seats + request.getNoOfTickets() : seats;
            ticketCount += request.getNoOfTickets();
            hasAdultTicket = request.getTicketType() == TicketTypeRequest.Type.ADULT || hasAdultTicket;
        }

        if(ticketCount > MAX_TICKETS_PER_REQUEST || !hasAdultTicket || seats < 1 || ticketCount < 1) {
            throw new InvalidPurchaseException();
        }

        ticketPaymentService.makePayment(accountId, cost);
        seatReservationService.reserveSeat(accountId, seats);
    }

    private int getTicketPrice(TicketTypeRequest request) {
        switch (request.getTicketType()) {
            case INFANT:
                return TICKET_PRICE_INFANT;
            case CHILD:
                return TICKET_PRICE_CHILD;
            case ADULT:
                return TICKET_PRICE_ADULT;
            default:
                throw new InvalidPurchaseException();
        }
    }

    private void validateId(long accountId) {
        if(accountId < 0) {
            throw new InvalidPurchaseException();
        }
    }
}
