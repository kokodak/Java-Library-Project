package library;

import java.util.List;
import java.util.Map;

public interface DataStorage {
    void initializeData();

    Map<Integer, Book> getBooks();

    Map<String, User> getUsers();

    List<Rental> getRentals();

    void addBook(Book book);

    void removeBook(int bookId);

    void updateBookAvailability(int bookId, boolean isAvailable);

    void addUser(User user);

    void removeUser(String username);

    void addRental(Rental rental);

    void updateRental(Rental rental);

    void removeRental(int rentalId);

    List<Rental> getPendingRentals();

    List<Rental> getPendingReturns();

    List<Rental> getOverdueRentals();
}
