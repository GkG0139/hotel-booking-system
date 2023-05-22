package utils;

import exception.HotelInsertionException;
import exception.RoomAlreadyExistsException;
import exception.RoomInsertionException;
import exception.RoomUpdatingException;
import hotel.Hotel;
import hotel.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DatabaseUtil {

  private static final String DB_URL =
    "jdbc:mysql://localhost:3306/final_practice01";
  private static final String USERNAME = "root";
  private static final String PASSWORD = "INSERT YOUR PASSWORD HERE";

  private DatabaseUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static boolean saveHotel(Hotel hotel)
    throws SQLException, HotelInsertionException, RoomInsertionException, RoomUpdatingException {
    if (hotel == null) throw new NullPointerException("Hotel cannot be null");

    try (
      Connection connection = DriverManager.getConnection(
        DB_URL,
        USERNAME,
        PASSWORD
      )
    ) {
      // Insert hotel if it doesn't exist in the DB
      if (!isHotelExists(connection, hotel)) {
        insertHotel(connection, hotel);
      }

      // Insert/update rooms
      for (Room room : hotel.getRooms().values()) {
        if (isRoomExists(connection, hotel.getHotelId(), room)) {
          updateRoom(connection, hotel.getHotelId(), room);
        } else {
          insertRoom(connection, hotel.getHotelId(), room);
        }
      }
      return true;
    }
  }

  private static boolean isHotelExists(Connection connection, Hotel hotel)
    throws SQLException {
    String sql = "SELECT COUNT(*) FROM hotels WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, hotel.getHotelId());

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getInt(1) > 0;
        }
      }
    }
    return false;
  }

  private static boolean isRoomExists(
    Connection connection,
    String hotelId,
    Room room
  ) throws SQLException {
    String sql =
      "SELECT COUNT(*) FROM rooms WHERE room_no = ? AND hotel_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, room.getRoomId());
      statement.setString(2, hotelId);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getInt(1) > 0;
        }
      }
    }
    return false;
  }

  private static void insertHotel(Connection connection, Hotel hotel)
    throws SQLException, HotelInsertionException {
    String sql = "INSERT INTO hotels (id, name) VALUES (?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, hotel.getHotelId());
      statement.setString(2, hotel.getHotelName());

      int count = statement.executeUpdate();
      if (count == 0) {
        throw new HotelInsertionException(
          "Something went wrong while inserting a new hotel."
        );
      }
    }
  }

  private static void insertRoom(
    Connection connection,
    String hotelId,
    Room room
  ) throws SQLException, RoomInsertionException {
    String sql =
      "INSERT INTO rooms (room_no, booked, hotel_id) VALUES (?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, room.getRoomId());
      statement.setInt(2, room.isBooked() ? 1 : 0);
      statement.setString(3, hotelId);

      int count = statement.executeUpdate();
      if (count == 0) {
        throw new RoomInsertionException(
          "Something went wrong while inserting a new room."
        );
      }
    }
  }

  private static void updateRoom(
    Connection connection,
    String hotelId,
    Room room
  ) throws SQLException, RoomUpdatingException {
    String sql =
      "UPDATE rooms SET booked = ? WHERE room_no = ? AND hotel_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, room.isBooked() ? 1 : 0);
      statement.setInt(2, room.getRoomId());
      statement.setString(3, hotelId);

      int count = statement.executeUpdate();
      if (count == 0) {
        throw new RoomUpdatingException(
          "Something went wrong while updating the room."
        );
      }
    }
  }

  public static List<Hotel> getHotels()
    throws SQLException, RoomAlreadyExistsException {
    try (
      Connection connection = DriverManager.getConnection(
        DB_URL,
        USERNAME,
        PASSWORD
      )
    ) {
      Map<UUID, Hotel> map = new HashMap<>();
      String sql = "SELECT * FROM hotels";
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
          String id = resultSet.getString("id");
          UUID uuid = UUID.fromString(id);
          String name = resultSet.getString("name");

          Hotel hotel = new Hotel(uuid, name);
          map.put(uuid, hotel);
        }
      }

      sql = "SELECT * FROM rooms";
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
          int roomId = resultSet.getInt("room_no");
          Boolean isBooked = resultSet.getInt("booked") == 1;
          String hotelId = resultSet.getString("hotel_id");
          UUID hotelUUID = UUID.fromString(hotelId);

          Hotel hotel = map.get(hotelUUID);
          hotel.addRoom(roomId, isBooked);
        }
      }
      return new ArrayList<>(map.values());
    }
  }
}
