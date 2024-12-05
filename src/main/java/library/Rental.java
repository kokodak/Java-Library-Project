package library;

import java.time.LocalDate;

public class Rental {
    private int id;
    private String username;
    private int bookId;
    private LocalDate rentalDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean isApproved;
    private boolean isReturned;
    private boolean isReturnRequested;

    public Rental(final int id, final String username, final int bookId, final LocalDate rentalDate, final LocalDate dueDate, final LocalDate returnDate, final boolean isApproved,
                  final boolean isReturned, final boolean isReturnRequested) {
        this.id = id;
        this.username = username;
        this.bookId = bookId;
        this.rentalDate = rentalDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.isApproved = isApproved;
        this.isReturned = isReturned;
        this.isReturnRequested = isReturnRequested;
    }

    public Rental(String username, int bookId, LocalDate rentalDate, LocalDate dueDate) {
        this.username = username;
        this.bookId = bookId;
        this.rentalDate = rentalDate;
        this.dueDate = dueDate;
        this.isApproved = false;
        this.isReturned = false;
        this.isReturnRequested = false;
    }

    // Getter와 Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public int getBookId() {
        return bookId;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public boolean isReturnRequested() {
        return isReturnRequested;
    }

    public void setReturnRequested(boolean returnRequested) {
        isReturnRequested = returnRequested;
    }
}
