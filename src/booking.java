import java.io.Serializable;
import java.time.LocalDate;

public class Booking implements Serializable {
    private String bookingId;
    private String customerName;
    private String contact;
    private Room assignedRoom;
    private LocalDate checkIn;
    private LocalDate checkOut;

    public Booking(String bookingId, String customerName, String contact, Room assignedRoom, LocalDate checkIn, LocalDate checkOut) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.contact = contact;
        this.assignedRoom = assignedRoom;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    // Helper to check if this booking overlaps with a requested date range
    public boolean overlaps(LocalDate start, LocalDate end) {
        return (start.isBefore(this.checkOut) && end.isAfter(this.checkIn));
    }

    public String getBookingId() { return bookingId; }
    public String getCustomerName() { return customerName; }
    public Room getAssignedRoom() { return assignedRoom; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
}
