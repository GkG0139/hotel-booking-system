## Challenge: Hotel Booking System

Your task is to create a Java program that implements a hotel booking system. The program should integrate JDBC for database operations, File I/O for data storage, and utilize object-oriented programming (OOP) concepts.

Requirements:

1. Create a Hotel class with the following attributes:

   - hotelName (String): Name of the hotel
   - hotelId (UUID): Unique identifier for the hotel
   - rooms (ArrayList<Room>): List of rooms in the hotel

2. Create a Room class with the following attributes:

   - roomId (int): Unique identifier for the room
   - isBooked (boolean): Indicates whether the room is booked or available

3. Implement the following methods in the Hotel class:

   - Hotel(String hotelName): Creates a new hotel with the given name and assigns a unique hotelId
   - addRoom(int roomId): Adds a room to the hotel with the specified roomId
   - bookRoom(int roomId): Books the room with the given roomId if it is available
   - cancelBooking(int roomId): Cancels the booking for the room with the given roomId

4. Implement a DatabaseUtil class that handles the JDBC operations for storing and retrieving hotel and room data from a database. This class should include methods for:

   - saveHotel(Hotel hotel): Saves the hotel and room(s) data to the database
   - getHotels(): Retrieves the hotel and room objects from the database and returns the list of hotel with room detials

5. Implement a FileUtil class that handles file I/O operations for storing and retrieving hotel and room data from a text file. This class should include methods for:
   - exportHotelToFile(Hotel hotel): Saves the hotel data to a text file

Note: You can use any relational database for JDBC operations and choose an appropriate format for storing data in the text file.

Your implementation should demonstrate the integration of JDBC, File I/O, and OOP concepts in the hotel booking system. Feel free to add any additional methods or classes as needed to complete the challenge.

Good luck!
