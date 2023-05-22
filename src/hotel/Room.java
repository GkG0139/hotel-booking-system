package hotel;

public class Room {

  private final int roomId;
  private boolean isBooked;

  public Room(int roomId) {
    this.roomId = roomId;
    this.isBooked = false;
  }

  public int getRoomId() {
    return roomId;
  }

  public boolean isBooked() {
    return isBooked;
  }

  public void setBooked(boolean isBooked) {
    this.isBooked = isBooked;
  }
}
