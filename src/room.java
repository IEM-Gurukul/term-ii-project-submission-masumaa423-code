import java.io.Serializable;

public class Room implements Serializable {
    private String roomId;
    private String type; // Single, Double, Deluxe
    private double price;

    public Room(String roomId, String type, double price) {
        this.roomId = roomId;
        this.type = type;
        this.price = price;
    }

    public String getRoomId() { return roomId; }
    public String getType() { return type; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return roomId + " (" + type + " - $" + price + ")";
    }
}
