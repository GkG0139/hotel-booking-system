package hotel;

import exception.RoomAlreadyExistsException;
import exception.RoomBookedException;
import exception.RoomNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Hotel {

  private final String hotelName;
  private final UUID hotelId;
  private HashMap<Integer, Room> rooms = new HashMap<>();

  public Hotel(String hotelName) {
    this.hotelName = hotelName;
    this.hotelId = UUID.randomUUID();
  }

  public Hotel(UUID hotelId, String hotelName) {
    this.hotelName = hotelName;
    this.hotelId = hotelId;
  }

  private boolean isRoomExists(int roomId) {
    return rooms.get(roomId) != null;
  }

  public boolean addRoom(int roomId, boolean isBooked)
    throws RoomAlreadyExistsException {
    if (isRoomExists(roomId)) {
      throw new RoomAlreadyExistsException("Room " + roomId + " is exists!");
    }

    Room room = new Room(roomId);
    room.setBooked(isBooked);
    rooms.put(roomId, room);
    return true;
  }

  public boolean addRoom(int roomId) throws RoomAlreadyExistsException {
    if (isRoomExists(roomId)) {
      throw new RoomAlreadyExistsException("Room " + roomId + " is exists!");
    }

    Room room = new Room(roomId);
    rooms.put(roomId, room);
    return true;
  }

  public boolean bookRoom(int roomId)
    throws RoomBookedException, RoomNotFoundException {
    if (!isRoomExists(roomId)) {
      throw new RoomNotFoundException("Room " + roomId + " is not exists!");
    }

    Room room = rooms.get(roomId);
    if (room.isBooked()) {
      throw new RoomBookedException("Room " + roomId + " is already booked.");
    }
    room.setBooked(true);
    return true;
  }

  public boolean cancelBooking(int roomId) throws RoomNotFoundException {
    if (!isRoomExists(roomId)) {
      throw new RoomNotFoundException("Room " + roomId + " is not exists!");
    }
    Room room = rooms.get(roomId);
    room.setBooked(false);
    return true;
  }

  public String getHotelName() {
    return hotelName;
  }

  public String getHotelId() {
    return hotelId.toString();
  }

  public Map<Integer, Room> getRooms() {
    return rooms;
  }
}
