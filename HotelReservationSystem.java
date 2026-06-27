import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Room {
    private int roomNumber;
    private String category;
    private double pricePerNight;
    private boolean isAvailable;

    public Room(int roomNumber, String category, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
    }

    public int getRoomNumber() { return roomNumber; }
    public String getCategory() { return category; }
    public double getPricePerNight() { return pricePerNight; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return "Room #" + roomNumber + " [" + category + "] - Rs." + pricePerNight + "/night (" + (isAvailable ? "Available" : "Booked") + ")";
    }
}

class Reservation {
    private String guestName;
    private int roomNumber;
    private int nights;
    private double totalBill;

    public Reservation(String guestName, int roomNumber, int nights, double totalBill) {
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.nights = nights;
        this.totalBill = totalBill;
    }

    public String getGuestName() { return guestName; }
    public int getRoomNumber() { return roomNumber; }
    
    @Override
    public String toString() {
        return "Guest: " + guestName + " | Room: " + roomNumber + " | Nights: " + nights + " | Total Paid: Rs." + totalBill;
    }
    
    public String toFileString() {
        return guestName + "," + roomNumber + "," + nights + "," + totalBill;
    }
}

public class HotelReservationSystem {
    private static ArrayList<Room> rooms = new ArrayList<>();
    private static ArrayList<Reservation> reservations = new ArrayList<>();
    private static final String DATA_FILE = "reservations.txt";

    public static void main(String[] args) {
        initializeRooms();
        loadReservationsFromFile();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== CodeAlpha Hotel Reservation System ===");
        
        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Search Available Rooms");
            System.out.println("2. Make a Reservation");
            System.out.println("3. Cancel a Reservation");
            System.out.println("4. View All Bookings");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    searchRooms();
                    break;
                case "2":
                    makeReservation(scanner);
                    break;
                case "3":
                    cancelReservation(scanner);
                    break;
                case "4":
                    viewBookings();
                    break;
                case "5":
                    System.out.println("Thank you for using the Hotel Reservation System. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please choose between 1 and 5.");
            }
        }
    }

    private static void initializeRooms() {
        // Standard Rooms
        rooms.add(new Room(101, "Standard", 1500.0));
        rooms.add(new Room(102, "Standard", 1500.0));
        // Deluxe Rooms
        rooms.add(new Room(201, "Deluxe", 3000.0));
        rooms.add(new Room(202, "Deluxe", 3000.0));
        // Suites
        rooms.add(new Room(301, "Suite", 6000.0));
        rooms.add(new Room(302, "Suite", 6000.0));
    }

    private static void searchRooms() {
        System.out.println("\n--- Available Rooms ---");
        boolean found = false;
        for (Room room : rooms) {
            if (room.isAvailable()) {
                System.out.println(room);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Sorry, all rooms are currently booked.");
        }
    }

    private static void makeReservation(Scanner scanner) {
        System.out.println("\n--- Make a Reservation ---");
        searchRooms();
        
        System.out.print("\nEnter your full name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        System.out.print("Enter Room Number you wish to book: ");
        int roomNum;
        try {
            roomNum = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid room number format.");
            return;
        }

        Room selectedRoom = null;
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNum) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom == null || !selectedRoom.isAvailable()) {
            System.out.println("Room is either invalid or already booked.");
            return;
        }

        System.out.print("Enter number of nights to stay: ");
        int nights;
        try {
            nights = Integer.parseInt(scanner.nextLine().trim());
            if (nights <= 0) {
                System.out.println("Nights must be at least 1.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid entry for nights.");
            return;
        }

        double totalBill = selectedRoom.getPricePerNight() * nights;
        System.out.printf("Total billing amount: Rs.%.2f\n", totalBill);
        
        // Payment Simulation
        System.out.println("\n--- Simulating Payment Processing ---");
        System.out.print("Enter Dummy Credit/Debit Card Number (16 digits): ");
        String card = scanner.nextLine().trim();
        if (card.length() != 16 || !card.matches("\\d+")) {
            System.out.println("Payment Declined: Invalid card layout simulated. Booking failed.");
            return;
        }
        
        System.out.println("Processing transaction...");
        System.out.println("Payment Successful! Total received: Rs." + totalBill);

        // Finalize booking
        selectedRoom.setAvailable(false);
        Reservation res = new Reservation(name, roomNum, nights, totalBill);
        reservations.add(res);
        saveReservationsToFile();
        
        System.out.println("\nBooking Confirmed! Details safely recorded.");
    }

    private static void cancelReservation(Scanner scanner) {
        System.out.println("\n--- Cancel a Reservation ---");
        System.out.print("Enter Guest Name used during booking: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Booked Room Number: ");
        
        int roomNum;
        try {
            roomNum = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid room number format.");
            return;
        }

        Reservation targetReservation = null;
        for (Reservation res : reservations) {
            if (res.getGuestName().equalsIgnoreCase(name) && res.getRoomNumber() == roomNum) {
                targetReservation = res;
                break;
            }
        }

        if (targetReservation != null) {
            reservations.remove(targetReservation);
            for (Room room : rooms) {
                if (room.getRoomNumber() == roomNum) {
                    room.setAvailable(true);
                    break;
                }
            }
            saveReservationsToFile();
            System.out.println("Reservation successfully cancelled. Refund processed to original payment method.");
        } else {
            System.out.println("No matching reservation found.");
        }
    }

    private static void viewBookings() {
        System.out.println("\n--- Current Active Reservations ---");
        if (reservations.isEmpty()) {
            System.out.println("No bookings recorded in system database.");
            return;
        }
        for (Reservation res : reservations) {
            System.out.println(res);
        }
    }

    private static void saveReservationsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Reservation res : reservations) {
                writer.println(res.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving local file records: " + e.getMessage());
        }
    }

    private static void loadReservationsFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0];
                    int roomNum = Integer.parseInt(parts[1]);
                    int nights = Integer.parseInt(parts[2]);
                    double bill = Double.parseDouble(parts[3]);

                    reservations.add(new Reservation(name, roomNum, nights, bill));
                    
                    // Mark the room as booked
                    for (Room room : rooms) {
                        if (room.getRoomNumber() == roomNum) {
                            room.setAvailable(false);
                            break;
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading existing records from data file.");
        }
    }
}