package cinema;

import cinema.error.ErrorResponse;
import cinema.purchase.PurchaseResponse;
import cinema.seat.Seat;
import cinema.seat.SeatResponse;
import cinema.stats.StatsResponse;
import cinema.token.Token;
import cinema.token.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class Controller {

    private final int TOTAL_ROWS = 9;
    private final int TOTAL_COLUMNS = 9;
    private final boolean[][] SEATS_ARR = new boolean[TOTAL_ROWS][TOTAL_COLUMNS];
    private final String[][] TOKEN_ARR = new String[TOTAL_ROWS][TOTAL_COLUMNS];

    private int current_income = 0;
    private int number_of_available_seats = TOTAL_ROWS * TOTAL_COLUMNS;
    private int number_of_purchased_tickets = 0;

    @GetMapping("/seats")
    public SeatResponse getSeats() {

        List<Seat> available_seats = new ArrayList<>();
        for (int row = 1; row <= TOTAL_ROWS; row++) {
            for (int column = 1; column <= TOTAL_COLUMNS; column++) {
                int price;
                if (row <= 4) {
                    price = 10;
                } else {
                    price = 8;
                }
                available_seats.add(new Seat(row, column, price));
            }
        }

        return new SeatResponse(TOTAL_ROWS, TOTAL_COLUMNS, available_seats);
    }

    @PostMapping("/purchase")
    public ResponseEntity<Object> purchaseTicket(@RequestBody Seat seat) {

        String token = String.valueOf(UUID.randomUUID());

        int row = seat.getRow();
        int column = seat.getColumn();

        if (row < 1 || row > TOTAL_ROWS || column < 1 || column > TOTAL_COLUMNS) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("The number of a row or a column is out of bounds!"));
        }

        if (SEATS_ARR[row - 1][column - 1]) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("The ticket has been already purchased!"));
        } else {
            SEATS_ARR[row - 1][column - 1] = true;
            TOKEN_ARR[row - 1][column - 1] = token;
            int price = (row <= 4) ? 10 : 8;
            seat.setPrice(price);
            current_income += price;
            number_of_available_seats --;
            number_of_purchased_tickets ++;

            return ResponseEntity.ok(new PurchaseResponse(token, seat));
        }
    }

    @PostMapping("/return")
    public ResponseEntity<Object> refundTicket(@RequestBody Token token, Seat seat) {

        String tokenCall = token.getToken();
        int[] indexOfToken = getIndex(TOKEN_ARR, tokenCall);
        int row = indexOfToken[0] + 1;
        int column = indexOfToken[1] + 1;

        int price = (row <= 4) ? 10 : 8;

        if (!containsValue(TOKEN_ARR, tokenCall)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Wrong token!"));
        } else {
            seat.setRow(row);
            seat.setColumn(column);
            seat.setPrice(price);

            current_income -= price;
            number_of_available_seats ++;
            number_of_purchased_tickets --;

            return ResponseEntity.ok(new TokenResponse(seat));
        }
    }

    @PostMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam(required = false) String password) {
        final String CORRECT_PASSWORD = "super_secret";
        if (password != null && password.equals(CORRECT_PASSWORD)) {

            /*current_income = stats.getCurrent_income();
            number_of_available_seats = stats.getNumber_of_available_seats();
            number_of_purchased_tickets = stats.getNumber_of_purchased_tickets();*/

            return ResponseEntity.ok(
                    new StatsResponse(current_income, number_of_available_seats, number_of_purchased_tickets)
            );

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("The password is wrong!"));
        }
    }

    public static boolean containsValue(String[][] array, String value) {
        for (String[] row : array) {
            for (String element : row) {
                if (element == null && value == null) {
                    return true;
                }
                if (element != null && element.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int[] getIndex(String[][] array, String value) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == null && value == null) {
                    return new int[]{i, j};
                }
                if (array[i][j] != null && array[i][j].equals(value)) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1};
    }
}
