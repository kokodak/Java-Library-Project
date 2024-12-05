package library.domain;

public class Book {

    private Long id;
    private String title;
    private String author;
    private String category;
    private boolean isAvailable;

    public Book(final Long id,
                final String title,
                final String author,
                final String category,
                final boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isAvailable = isAvailable;
    }

    public Book(final String title,
                final String author,
                final String category,
                final boolean isAvailable) {
        this(null, title, author, category, isAvailable);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(final boolean available) {
        isAvailable = available;
    }
}
