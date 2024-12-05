package library.domain;

import java.time.LocalDate;

public class Rental {

    private Long id;
    private String username;
    private Long bookId;
    private LocalDate rentalDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean isApproved;
    private boolean isReturned;
    private boolean isReturnRequested;

    public Rental(final Long id,
                  final String username,
                  final Long bookId,
                  final LocalDate rentalDate,
                  final LocalDate dueDate,
                  final LocalDate returnDate,
                  final boolean isApproved,
                  final boolean isReturned,
                  final boolean isReturnRequested) {
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

    public Rental(String username, Long bookId, LocalDate rentalDate, LocalDate dueDate) {
        this(null,
             username,
             bookId,
             rentalDate,
             dueDate,
             null,
             false,
             false,
             false);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public Long getBookId() {
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
