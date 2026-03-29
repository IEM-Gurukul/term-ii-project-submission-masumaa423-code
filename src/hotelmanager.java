import java.util.ArrayList;
import java.util.Comparator;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class HotelManager {
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Booking> bookings = new ArrayList<>();

    public void addRoom(Room room) { rooms.add(room); }
    public void removeRoom(String id) { rooms.removeIf(r -> r.getRoomId().equals(id)); }
    public ArrayList<Room> getRooms() { return rooms; }
    public ArrayList<Booking> getBookings() { return bookings; }

    /**
     * SMART ALLOCATION LOGIC:
     * 1. Filter rooms by requested type.
     * 2. Exclude rooms that have overlapping bookings for those dates.
     * 3. Pick the room with the lowest price.
     */
    public Room allocateSmartRoom(String type, LocalDate checkIn, LocalDate checkOut) {
        return rooms.stream()
                .filter(r -> r.getType().equalsIgnoreCase(type))
                .filter(r -> isRoomAvailable(r, checkIn, checkOut))
                .min(Comparator.comparingDouble(Room::getPrice))
                .orElse(null);
    }

    private boolean isRoomAvailable(Room room, LocalDate start, LocalDate end) {
        for (Booking b : bookings) {
            if (b.getAssignedRoom().getRoomId().equals(room.getRoomId())) {
                if (b.overlaps(start, end)) return false;
            }
        }
        return true;
    }

    public void addBooking(Booking b) { bookings.add(b); }
    public void cancelBooking(String id) { bookings.removeIf(b -> b.getBookingId().equals(id)); }
}
