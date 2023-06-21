package cinema.seat;

import java.util.List;

public class SeatResponse {
    private final int total_rows;
    private final int total_columns;
    private final List<Seat> available_seats;

    public SeatResponse(int total_rows, int total_columns, List<Seat> available_seats) {
        this.total_rows = total_rows;
        this.total_columns = total_columns;
        this.available_seats = available_seats;
    }

    public int getTotal_rows() {
        return total_rows;
    }

    public int getTotal_columns() {
        return total_columns;
    }

    public List<Seat> getAvailable_seats() {
        return available_seats;
    }

}
