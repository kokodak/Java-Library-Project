package library.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import library.domain.Book;
import library.domain.Rental;
import library.domain.User;

public class InMemoryDataStorage implements DataStorage {

    private Map<Long, Book> books = new HashMap<>();
    private Map<String, User> users = new HashMap<>();
    private List<Rental> rentals = new ArrayList<>();
    private AtomicLong bookIdCounter = new AtomicLong(1);
    private AtomicLong rentalIdCounter = new AtomicLong(1);

    @Override
    public void initializeData() {
        addUser(new User("admin", "admin123", "admin"));
        addUser(new User("user1", "password1", "user"));
        addUser(new User("user2", "password2", "user"));

        addBook(new Book("자바의 정석", "남궁성", "프로그래밍", true));
        addBook(new Book("이펙티브 자바", "조슈아 블로크", "프로그래밍", true));
        addBook(new Book("클린 코드", "로버트 C. 마틴", "프로그래밍", true));
    }

    @Override
    public Map<Long, Book> getBooks() {
        return books;
    }

    @Override
    public Map<String, User> getUsers() {
        return users;
    }

    @Override
    public List<Rental> getRentals() {
        return rentals;
    }

    @Override
    public void addBook(Book book) {
        book.setId(bookIdCounter.getAndIncrement());
        books.put(book.getId(), book);
    }

    @Override
    public void removeBook(Long bookId) {
        books.remove(bookId);
    }

    @Override
    public void updateBookAvailability(Long bookId, boolean isAvailable) {
        Book book = books.get(bookId);
        if (book != null) {
            book.setAvailable(isAvailable);
        }
    }

    @Override
    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }


    @Override
    public void addRental(Rental rental) {
        rental.setId(rentalIdCounter.getAndIncrement());
        rentals.add(rental);
    }

    @Override
    public void updateRental(Rental rental) {
        for (int i = 0; i < rentals.size(); i++) {
            if (rentals.get(i).getId() == rental.getId()) {
                rentals.set(i, rental);
                break;
            }
        }
    }

    @Override
    public void removeRental(Long rentalId) {
        rentals.removeIf(rental -> rental.getId() == rentalId);
    }

    @Override
    public List<Rental> getPendingRentals() {
        List<Rental> pendingRentals = new ArrayList<>();
        for (Rental rental : rentals) {
            if (!rental.isApproved() && !rental.isReturned()) {
                pendingRentals.add(rental);
            }
        }
        return pendingRentals;
    }

    @Override
    public List<Rental> getPendingReturns() {
        List<Rental> pendingReturns = new ArrayList<>();
        for (Rental rental : rentals) {
            if (rental.isApproved() && rental.isReturnRequested() && !rental.isReturned()) {
                pendingReturns.add(rental);
            }
        }
        return pendingReturns;
    }
}
