import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DashboardUI extends JFrame {
    private HotelManager manager = new HotelManager();
    private JTable roomTable, bookingTable;
    private DefaultTableModel roomModel, bookingModel;

    public DashboardUI() {
        setTitle("Elite Hotel Management System");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Setup Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Manage Rooms", createRoomPanel());
        tabs.addTab("Bookings & Smart Allocation", createBookingPanel());
        
        add(tabs);
        
        // Pre-populate some rooms
        manager.addRoom(new Room("101", "Single", 50.0));
        manager.addRoom(new Room("102", "Single", 45.0));
        manager.addRoom(new Room("201", "Deluxe", 150.0));
        refreshTables();
    }

    private JPanel createRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Form
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField idField = new JTextField();
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Single", "Double", "Deluxe"});
        JTextField priceField = new JTextField();
        JButton addBtn = new JButton("Add Room");
        JButton delBtn = new JButton("Delete Selected Room");

        form.add(new JLabel(" Room ID:")); form.add(idField);
        form.add(new JLabel(" Type:")); form.add(typeBox);
        form.add(new JLabel(" Price:")); form.add(priceField);
        form.add(addBtn); form.add(delBtn);

        addBtn.addActionListener(e -> {
            try {
                manager.addRoom(new Room(idField.getText(), (String)typeBox.getSelectedItem(), Double.parseDouble(priceField.getText())));
                refreshTables();
                idField.setText(""); priceField.setText("");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid Input"); }
        });

        delBtn.addActionListener(e -> {
            int row = roomTable.getSelectedRow();
            if(row != -1) {
                manager.removeRoom(roomTable.getValueAt(row, 0).toString());
                refreshTables();
            }
        });

        roomModel = new DefaultTableModel(new String[]{"Room ID", "Type", "Price"}, 0);
        roomTable = new JTable(roomModel);
        
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(roomTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Booking Form
        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField inField = new JTextField(LocalDate.now().toString());
        JTextField outField = new JTextField(LocalDate.now().plusDays(1).toString());
        JComboBox<String> typePref = new JComboBox<>(new String[]{"Single", "Double", "Deluxe"});
        JButton bookBtn = new JButton("Smart Book Now");

        form.add(new JLabel(" Customer Name:")); form.add(nameField);
        form.add(new JLabel(" Contact:")); form.add(contactField);
        form.add(new JLabel(" Check-in (YYYY-MM-DD):")); form.add(inField);
        form.add(new JLabel(" Check-out (YYYY-MM-DD):")); form.add(outField);
        form.add(new JLabel(" Room Type:")); form.add(typePref);
        form.add(new JLabel("")); form.add(bookBtn);

        bookBtn.addActionListener(e -> {
            try {
                LocalDate cin = LocalDate.parse(inField.getText());
                LocalDate cout = LocalDate.parse(outField.getText());
                
                // CORE FEATURE: SMART ALLOCATION
                Room allocated = manager.allocateSmartRoom((String)typePref.getSelectedItem(), cin, cout);
                
                if (allocated != null) {
                    String bId = "B" + (manager.getBookings().size() + 1);
                    manager.addBooking(new Booking(bId, nameField.getText(), contactField.getText(), allocated, cin, cout));
                    refreshTables();
                    JOptionPane.showMessageDialog(this, "Success! Allocated Room " + allocated.getRoomId() + " (Cheapest available)");
                } else {
                    JOptionPane.showMessageDialog(this, "No rooms available for these dates/type.");
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Use format YYYY-MM-DD");
            }
        });

        bookingModel = new DefaultTableModel(new String[]{"ID", "Name", "Room", "In", "Out"}, 0);
        bookingTable = new JTable(bookingModel);
        
        JButton cancelBtn = new JButton("Cancel Selected Booking");
        cancelBtn.addActionListener(e -> {
            int row = bookingTable.getSelectedRow();
            if (row != -1) {
                manager.cancelBooking(bookingTable.getValueAt(row, 0).toString());
                refreshTables();
            }
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
        panel.add(cancelBtn, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshTables() {
        roomModel.setRowCount(0);
        for (Room r : manager.getRooms()) roomModel.addRow(new Object[]{r.getRoomId(), r.getType(), r.getPrice()});
        
        bookingModel.setRowCount(0);
        for (Booking b : manager.getBookings()) 
            bookingModel.addRow(new Object[]{b.getBookingId(), b.getCustomerName(), b.getAssignedRoom().getRoomId(), b.getCheckIn(), b.getCheckOut()});
    }
}
