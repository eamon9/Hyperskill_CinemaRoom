package cinema.token;

import cinema.seat.Seat;

public class TokenResponse {

    private Seat returned_ticket;

    public TokenResponse(Seat returned_ticket) {
        this.returned_ticket = returned_ticket;
    }

    public Seat getReturned_ticket() {
        return returned_ticket;
    }

    public void setReturned_ticket(Seat returned_ticket) {
        this.returned_ticket = returned_ticket;
    }

    /*public TokenResponse(List<Seat> returned_ticket) {
        this.returned_ticket = returned_ticket;
    }*/

    /*public List<Seat> getReturned_ticket() {
        return returned_ticket;
    }

    public void setReturned_ticket(List<Seat> returned_ticket) {
        this.returned_ticket = returned_ticket;
    }*/
}
