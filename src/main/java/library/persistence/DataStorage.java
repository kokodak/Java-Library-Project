package library.persistence;

import java.util.List;
import java.util.Map;
import library.domain.Book;
import library.domain.Rental;
import library.domain.User;

public interface DataStorage {

    void initializeData();

    Map<Long, Book> getBooks();

    Map<String, User> getUsers();

    List<Rental> getRentals();

    void addBook(Book book);

    void removeBook(Long bookId);

    void updateBookAvailability(Long bookId, boolean isAvailable);

    void addUser(User user);

    void addRental(Rental rental);

    void updateRental(Rental rental);

    void removeRental(Long rentalId);

    List<Rental> getPendingRentals();

    List<Rental> getPendingReturns();
}
