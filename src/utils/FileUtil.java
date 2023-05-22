package utils;

import hotel.Hotel;
import hotel.Room;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileUtil {

  private FileUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static void exportHotelToFile(Hotel hotel) throws IOException {
    try (Writer writer = new FileWriter(hotel.getHotelName() + ".txt")) {
      BufferedWriter bufferedWriter = new BufferedWriter(writer);
      bufferedWriter.write("Hotel Name: " + hotel.getHotelName());
      bufferedWriter.newLine();
      bufferedWriter.write("Hotel ID: " + hotel.getHotelId());
      bufferedWriter.newLine();
      bufferedWriter.write("Total Rooms: " + hotel.getRooms().size());
      bufferedWriter.newLine();
      bufferedWriter.write(
        "Available rooms: " +
        hotel
          .getRooms()
          .values()
          .stream()
          .filter(room -> !room.isBooked())
          .count()
      );
      bufferedWriter.newLine();
      bufferedWriter.write("Rooms status: ");
      bufferedWriter.newLine();
      for (Room room : hotel.getRooms().values()) {
        bufferedWriter.write(
          "  - Rooms " +
          room.getRoomId() +
          " : " +
          (room.isBooked() ? "Booked" : "Available")
        );
      }
      bufferedWriter.close();
    }
  }
}
