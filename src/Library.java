import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {
    private List<Book> books;
    private Map<String, User> users;

    public Library() {
        this.books = new ArrayList<>();
        this.users = new HashMap<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(String title) {
        books.removeIf(book -> book.getTitle().equals(title));
    }

    public Book findBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    public List<Book> findBooksByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().equals(author)) {
                result.add(book);
            }
        }
        return result;
    }

    public void checkoutBook(String title, String username) {
        User user = users.get(username);
        if (user == null) {
            System.out.println("User does not exist");
            return;
        }

        Book book = findBookByTitle(title);
        if (book != null && book.isAvailable() && user.canCheckoutBook()) {
            book.setAvailable(false);
            user.checkoutBook();
            user.addBookToHistory(book);
        } else {
            System.out.println("Book is not available for checkout");
        }
    }

    public void returnBook(String title, String username) {
        User user = users.get(username);
        if (user == null) {
            System.out.println("User does not exist");
            return;
        }

        Book book = findBookByTitle(title);
        if (book != null && !book.isAvailable() && user.hasBookInHistory(book)) {
            book.setAvailable(true);
            user.returnBook();
            user.removeBookFromHistory(book);
        } else {
            System.out.println("Book cannot be returned");
        }
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public static void main(String[] args) {
        // Sample usage of the Library class
        Library library = new Library();

        // Add books to the library
        library.addBook(new Book("1984", "George Orwell", BookCategory.FICTION));
        library.addBook(new Book("To Kill a Mockingbird", "Harper Lee", BookCategory.FICTION));

        // Add users to the library
        library.addUser(new User("user1", "John Doe"));
        library.addUser(new User("user2", "Jane Smith"));

        // Checkout and return books
        library.checkoutBook("1984", "user1");
        library.returnBook("1984", "user1");
    }
}

enum BookCategory {
    FICTION,
    NON_FICTION,
    SCIENCE_FICTION,
    MYSTERY,
    ROMANCE,
    HORROR,
    THRILLER,
    BIOGRAPHY,
    HISTORY,
    SELF_HELP,
    SCIENCE,
    PHILOSOPHY
}

class Book {
    private String title;
    private String author;
    private boolean available;
    private BookCategory category;

    public Book(String title, String author, BookCategory category) {
        this.title = title;
        this.author = author;
        this.available = true;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public BookCategory getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return title + " by " + author + " [" + category + "]";
    }
}

class User {
    private String username;
    private String fullName;
    private int borrowedBooks;
    private List<Book> borrowingHistory;

    public User(String username, String fullName) {
        this.username = username;
        this.fullName = fullName;
        this.borrowedBooks = 0;
        this.borrowingHistory = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public boolean canCheckoutBook() {
        return borrowedBooks < 3; // Maximum 3 books can be borrowed
    }

    public void checkoutBook() {
        borrowedBooks++;
    }

    public void returnBook() {
        borrowedBooks--;
    }

    public void addBookToHistory(Book book) {
        borrowingHistory.add(book);
    }

    public void removeBookFromHistory(Book book) {
        borrowingHistory.remove(book);
    }

    public boolean hasBookInHistory(Book book) {
        return borrowingHistory.contains(book);
    }
}
