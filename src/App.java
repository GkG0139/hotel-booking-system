import exception.HotelInsertionException;
import exception.RoomAlreadyExistsException;
import exception.RoomBookedException;
import exception.RoomInsertionException;
import exception.RoomNotFoundException;
import exception.RoomUpdatingException;
import hotel.Hotel;
import hotel.Room;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.DatabaseUtil;
import utils.FileUtil;

public class App {

  private static final Logger logger = Logger.getLogger("Main");

  private static final Scanner scanner = new Scanner(System.in);

  private static List<Hotel> hotels;

  public static void main(String[] args) {
    boolean isRunning = true;
    try {
      hotels = DatabaseUtil.getHotels();
      while (isRunning) {
        displayMenu();
        int choice = getUserChoice();

        switch (choice) {
          case 0:
            isRunning = false;
            logger.warning("Exiting...");
            break;
          case 1:
            createNewHotel();
            break;
          case 2:
            addRoomToHotel();
            break;
          case 3:
            bookRoom();
            break;
          case 4:
            cancelBook();
            break;
          case 5:
            saveHotel();
            break;
          case 6:
            exportToFile();
            break;
          case 7:
            showAllHotels();
            break;
          default:
            logger.warning("Invalid choice. Please try again.");
            break;
        }
      }
    } catch (Exception e) {
      logger.log(Level.INFO, "Error: {0}", e.getMessage());
    }
  }

  private static void displayMenu() {
    System.out.println("==== Hotel System ====");
    System.out.println("1. Create new hotel");
    System.out.println("2. Add new room for the hotel");
    System.out.println("3. Book a room");
    System.out.println("4. Cancel booking");
    System.out.println("5. Save the hotel information into DB");
    System.out.println("6. Save the hotel information into files");
    System.out.println("7. Show all hotels");
    System.out.println("0. Exit");
    System.out.println("Enter your choice: ");
  }

  private static int getUserChoice() {
    int choice = scanner.nextInt();
    scanner.nextLine(); // Consume the newline character
    return choice;
  }

  private static void createNewHotel() {
    System.out.println("Enter the hotel name: ");
    String name = scanner.nextLine();
    Hotel hotel = new Hotel(name);
    hotels.add(hotel);
    System.out.println("Hotel created successfully.");
  }

  private static void addRoomToHotel() throws RoomAlreadyExistsException {
    System.out.println("Enter the hotel name to search: ");
    String name = scanner.nextLine();

    List<Hotel> matchingHotels = searchHotelsByName(name);
    if (matchingHotels.isEmpty()) {
      System.out.println("No hotel with that name found.");
      return;
    }

    System.out.println("Choose a hotel:");
    displayHotels(matchingHotels);
    int choice = getUserChoice();
    if (choice < 0 || choice >= matchingHotels.size()) {
      System.out.println("Invalid choice. Option not found.");
      return;
    }

    Hotel selectedHotel = matchingHotels.get(choice);
    System.out.println(
      "Selected hotel: " +
      selectedHotel.getHotelName() +
      " (" +
      selectedHotel.getHotelId() +
      ")"
    );
    System.out.println("Enter the room number: ");
    int roomId = scanner.nextInt();
    scanner.nextLine(); // Consume the newline character

    selectedHotel.addRoom(roomId);
    System.out.println(
      "Successfully added room " +
      roomId +
      " to hotel " +
      selectedHotel.getHotelName()
    );
  }

  private static void bookRoom()
    throws RoomNotFoundException, RoomBookedException {
    System.out.println("Enter the hotel name to search: ");
    String name = scanner.nextLine();

    List<Hotel> matchingHotels = searchHotelsByName(name);
    if (matchingHotels.isEmpty()) {
      System.out.println("No hotel with that name found.");
      return;
    }

    System.out.println("Choose a hotel:");
    displayHotels(matchingHotels);
    int choice = getUserChoice();
    if (choice < 0 || choice >= matchingHotels.size()) {
      System.out.println("Invalid choice. Option not found.");
      return;
    }

    Hotel selectedHotel = matchingHotels.get(choice);
    System.out.println(
      "Selected hotel: " +
      selectedHotel.getHotelName() +
      " (" +
      selectedHotel.getHotelId() +
      ")"
    );
    System.out.println("Enter the room number: ");
    int roomId = scanner.nextInt();
    scanner.nextLine(); // Consume the newline character

    selectedHotel.bookRoom(roomId);
    System.out.println(
      "Successfully cancel book " +
      roomId +
      " from hotel " +
      selectedHotel.getHotelName()
    );
  }

  private static void cancelBook() throws RoomNotFoundException {
    System.out.println("Enter the hotel name to search: ");
    String name = scanner.nextLine();

    List<Hotel> matchingHotels = searchHotelsByName(name);
    if (matchingHotels.isEmpty()) {
      System.out.println("No hotel with that name found.");
      return;
    }

    System.out.println("Choose a hotel:");
    displayHotels(matchingHotels);
    int choice = getUserChoice();
    if (choice < 0 || choice >= matchingHotels.size()) {
      System.out.println("Invalid choice. Option not found.");
      return;
    }

    Hotel selectedHotel = matchingHotels.get(choice);
    System.out.println(
      "Selected hotel: " +
      selectedHotel.getHotelName() +
      " (" +
      selectedHotel.getHotelId() +
      ")"
    );
    System.out.println("Enter the room number: ");
    int roomId = scanner.nextInt();
    scanner.nextLine(); // Consume the newline character

    selectedHotel.cancelBooking(roomId);
    System.out.println(
      "Successfully cancel book " +
      roomId +
      " from hotel " +
      selectedHotel.getHotelName()
    );
  }

  private static void saveHotel()
    throws SQLException, HotelInsertionException, RoomInsertionException, RoomUpdatingException {
    for (Hotel hotel : hotels) {
      DatabaseUtil.saveHotel(hotel);
    }
    System.out.println("Save the data successfully");
  }

  private static List<Hotel> searchHotelsByName(String name) {
    List<Hotel> matchingHotels = new ArrayList<>();
    for (Hotel hotel : hotels) {
      if (hotel.getHotelName().contains(name)) {
        matchingHotels.add(hotel);
      }
    }
    return matchingHotels;
  }

  private static void displayHotels(List<Hotel> hotels) {
    for (int i = 0; i < hotels.size(); i++) {
      Hotel hotel = hotels.get(i);
      System.out.println(
        String.format(
          "%d: %s (%s)",
          i,
          hotel.getHotelName(),
          hotel.getHotelId()
        )
      );
    }
  }

  private static void showAllHotels() {
    System.out.println("Hotels in the system:");
    for (Hotel hotel : hotels) {
      System.out.println(
        hotel.getHotelId() +
        ": " +
        hotel.getHotelName() +
        "(" +
        hotel.getRooms().size() +
        ")"
      );
      for (Room room : hotel.getRooms().values()) {
        System.out.println(
          "    Room " + room.getRoomId() + " is booked: " + room.isBooked()
        );
      }
    }
  }

  private static void exportToFile() throws RoomNotFoundException, IOException {
    System.out.println("Enter the hotel name to search: ");
    String name = scanner.nextLine();

    List<Hotel> matchingHotels = searchHotelsByName(name);
    if (matchingHotels.isEmpty()) {
      System.out.println("No hotel with that name found.");
      return;
    }

    System.out.println("Choose a hotel:");
    displayHotels(matchingHotels);
    int choice = getUserChoice();
    if (choice < 0 || choice >= matchingHotels.size()) {
      System.out.println("Invalid choice. Option not found.");
      return;
    }

    Hotel selectedHotel = matchingHotels.get(choice);
    System.out.println(
      "Selected hotel: " +
      selectedHotel.getHotelName() +
      " (" +
      selectedHotel.getHotelId() +
      ")"
    );

    FileUtil.exportHotelToFile(selectedHotel);

    System.out.println("Successfully export to file!");
  }
}
