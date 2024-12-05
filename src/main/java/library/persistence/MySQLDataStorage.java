package library.persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import library.domain.Book;
import library.domain.Rental;
import library.domain.User;

public class MySQLDataStorage implements DataStorage {

    private Connection connection;
    private Map<Long, Book> books = new HashMap<>();
    private Map<String, User> users = new HashMap<>();
    private List<Rental> rentals = new ArrayList<>();

    public MySQLDataStorage() {
        connect();
    }

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/library_db?serverTimezone=UTC";
            String user = "username";
            String password = "password";
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initializeData() {
        loadBooks();
        loadUsers();
        loadRentals();
    }

    private void loadBooks() {
        books.clear();
        String sql = "SELECT * FROM books";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String category = rs.getString("category");
                boolean isAvailable = rs.getBoolean("is_available");
                books.put(id, new Book(id, title, author, category, isAvailable));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadRentals() {
        rentals.clear();
        String sql = "SELECT * FROM rentals";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                Long bookId = rs.getLong("book_id");
                LocalDate rentalDate = rs.getDate("rental_date").toLocalDate();
                LocalDate dueDate = rs.getDate("due_date").toLocalDate();
                LocalDate returnDate = rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null;
                boolean isApproved = rs.getBoolean("is_approved");
                boolean isReturned = rs.getBoolean("is_returned");
                boolean isReturnRequested = rs.getBoolean("is_return_requested");

                Rental rental = new Rental(id, username, bookId, rentalDate, dueDate, returnDate, isApproved, isReturned, isReturnRequested);
                rentals.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUsers() {
        users.clear();
        String sql = "SELECT * FROM users";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");
                User user = new User(username, password, role);
                users.put(username, user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        String sql = "INSERT INTO books (title, author, category, is_available) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getCategory());
            pstmt.setBoolean(4, book.isAvailable());
            pstmt.executeUpdate();

            // 생성된 키(책 ID) 가져오기
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                Long id = rs.getLong(1);
                book.setId(id);
                books.put(id, book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeBook(Long bookId) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, bookId);
            pstmt.executeUpdate();
            books.remove(bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBookAvailability(Long bookId, boolean isAvailable) {
        String sql = "UPDATE books SET is_available = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, isAvailable);
            pstmt.setLong(2, bookId);
            pstmt.executeUpdate();
            Book book = books.get(bookId);
            if (book != null) {
                book.setAvailable(isAvailable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addUser(User user) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.executeUpdate();
            users.put(user.getUsername(), user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addRental(Rental rental) {
        String sql = "INSERT INTO rentals (username, book_id, rental_date, due_date, is_approved, is_returned, is_return_requested) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, rental.getUsername());
            pstmt.setLong(2, rental.getBookId());
            pstmt.setDate(3, Date.valueOf(rental.getRentalDate()));
            pstmt.setDate(4, Date.valueOf(rental.getDueDate()));
            pstmt.setBoolean(5, rental.isApproved());
            pstmt.setBoolean(6, rental.isReturned());
            pstmt.setBoolean(7, rental.isReturnRequested());
            pstmt.executeUpdate();

            // 생성된 키(대여 ID) 가져오기
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                Long id = rs.getLong(1);
                rental.setId(id);
                rentals.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateRental(Rental rental) {
        String sql = "UPDATE rentals SET is_approved = ?, is_returned = ?, return_date = ?, is_return_requested = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, rental.isApproved());
            pstmt.setBoolean(2, rental.isReturned());
            pstmt.setDate(3, rental.getReturnDate() != null ? Date.valueOf(rental.getReturnDate()) : null);
            pstmt.setBoolean(4, rental.isReturnRequested());
            pstmt.setLong(5, rental.getId());
            pstmt.executeUpdate();
            for (int i = 0; i < rentals.size(); i++) {
                if (rentals.get(i).getId() == rental.getId()) {
                    rentals.set(i, rental);
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeRental(Long rentalId) {
        String sql = "DELETE FROM rentals WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, rentalId);
            pstmt.executeUpdate();
            rentals.removeIf(rental -> rental.getId() == rentalId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
