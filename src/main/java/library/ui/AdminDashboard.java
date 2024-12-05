package library.ui;

import java.awt.BorderLayout;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import library.domain.Book;
import library.domain.Rental;
import library.domain.User;
import library.persistence.DataStorage;
import library.persistence.DataStorageFactory;

public class AdminDashboard extends JFrame {

    private User admin;
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JTable rentalRequestTable;
    private DefaultTableModel rentalRequestTableModel;
    private JTable returnRequestTable;
    private DefaultTableModel returnRequestTableModel;
    private DataStorage dataStorage;

    public AdminDashboard(User admin) {
        dataStorage = DataStorageFactory.getInstance();
        this.admin = admin;
        setTitle("관리자 대시보드 - " + admin.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // 도서 관리 탭
        JPanel bookManagePanel = new JPanel(new BorderLayout());
        JPanel bookButtonPanel = new JPanel();
        JButton addBookButton = new JButton("도서 추가");
        JButton editBookButton = new JButton("도서 수정");
        JButton deleteBookButton = new JButton("도서 삭제");
        JButton logoutButton = new JButton("로그아웃");

        addBookButton.addActionListener(e -> addBook());
        editBookButton.addActionListener(e -> editBook());
        deleteBookButton.addActionListener(e -> deleteBook());
        logoutButton.addActionListener(e -> logout());

        bookButtonPanel.add(addBookButton);
        bookButtonPanel.add(editBookButton);
        bookButtonPanel.add(deleteBookButton);
        bookButtonPanel.add(logoutButton);

        String[] bookColumnNames = {"ID", "제목", "저자", "카테고리", "대여 가능 여부"};
        bookTableModel = new DefaultTableModel(bookColumnNames, 0);
        bookTable = new JTable(bookTableModel);
        loadBookData();
        JScrollPane bookScrollPane = new JScrollPane(bookTable);

        bookManagePanel.add(bookButtonPanel, BorderLayout.NORTH);
        bookManagePanel.add(bookScrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("도서 관리", bookManagePanel);

        // 대여 관리 탭
        JTabbedPane rentalTabbedPane = new JTabbedPane();

        // 대여 요청 탭
        JPanel rentalRequestPanel = new JPanel(new BorderLayout());
        String[] rentalRequestColumnNames = {"ID", "회원", "도서", "대여일"};
        rentalRequestTableModel = new DefaultTableModel(rentalRequestColumnNames, 0);
        rentalRequestTable = new JTable(rentalRequestTableModel);
        loadRentalRequestData();
        JScrollPane rentalRequestScrollPane = new JScrollPane(rentalRequestTable);

        JButton approveRentalButton = new JButton("승인");
        JButton rejectRentalButton = new JButton("거부");

        approveRentalButton.addActionListener(e -> approveRentalRequest());
        rejectRentalButton.addActionListener(e -> rejectRentalRequest());

        JPanel rentalRequestButtonPanel = new JPanel();
        rentalRequestButtonPanel.add(approveRentalButton);
        rentalRequestButtonPanel.add(rejectRentalButton);

        rentalRequestPanel.add(rentalRequestScrollPane, BorderLayout.CENTER);
        rentalRequestPanel.add(rentalRequestButtonPanel, BorderLayout.SOUTH);

        rentalTabbedPane.addTab("대여 요청", rentalRequestPanel);

        // 반납 요청 탭
        JPanel returnRequestPanel = new JPanel(new BorderLayout());
        String[] returnRequestColumnNames = {"ID", "회원", "도서", "반납 요청일"};
        returnRequestTableModel = new DefaultTableModel(returnRequestColumnNames, 0);
        returnRequestTable = new JTable(returnRequestTableModel);
        loadReturnRequestData();
        JScrollPane returnRequestScrollPane = new JScrollPane(returnRequestTable);

        JButton processReturnButton = new JButton("반납 처리");
        processReturnButton.addActionListener(e -> processReturnRequest());

        JPanel returnRequestButtonPanel = new JPanel();
        returnRequestButtonPanel.add(processReturnButton);

        returnRequestPanel.add(returnRequestScrollPane, BorderLayout.CENTER);
        returnRequestPanel.add(returnRequestButtonPanel, BorderLayout.SOUTH);

        rentalTabbedPane.addTab("반납 요청", returnRequestPanel);

        tabbedPane.addTab("대여 관리", rentalTabbedPane);

        add(tabbedPane);
    }

    private void loadBookData() {
        bookTableModel.setRowCount(0);
        for (Book book : dataStorage.getBooks().values()) {
            bookTableModel.addRow(new Object[]{
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getCategory(),
                    book.isAvailable() ? "가능" : "대여중"
            });
        }
    }

    private void loadRentalRequestData() {
        rentalRequestTableModel.setRowCount(0);
        for (Rental rental : dataStorage.getPendingRentals()) {
            Book book = dataStorage.getBooks().get(rental.getBookId());
            rentalRequestTableModel.addRow(new Object[]{
                    rental.getId(),
                    rental.getUsername(),
                    book.getTitle(),
                    rental.getRentalDate()
            });
        }
    }

    private void loadReturnRequestData() {
        returnRequestTableModel.setRowCount(0);
        for (Rental rental : dataStorage.getPendingReturns()) {
            Book book = dataStorage.getBooks().get(rental.getBookId());
            returnRequestTableModel.addRow(new Object[]{
                    rental.getId(),
                    rental.getUsername(),
                    book.getTitle(),
                    LocalDate.now()
            });
        }
    }

    private void addBook() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField categoryField = new JTextField();
        Object[] message = {
                "제목:", titleField,
                "저자:", authorField,
                "카테고리:", categoryField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "도서 추가", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String author = authorField.getText();
            String category = categoryField.getText();

            if (title.isEmpty() || author.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(this, "모든 필드를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book newBook = new Book(title, author, category, true);
            dataStorage.addBook(newBook);
            loadBookData();
            JOptionPane.showMessageDialog(this, "도서가 추가되었습니다.");
        }
    }

    private void editBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            Long bookId = (long) bookTableModel.getValueAt(selectedRow, 0);
            Book book = dataStorage.getBooks().get(bookId);

            JTextField titleField = new JTextField(book.getTitle());
            JTextField authorField = new JTextField(book.getAuthor());
            JTextField categoryField = new JTextField(book.getCategory());
            Object[] message = {
                    "제목:", titleField,
                    "저자:", authorField,
                    "카테고리:", categoryField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "도서 수정", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String title = titleField.getText();
                String author = authorField.getText();
                String category = categoryField.getText();

                if (title.isEmpty() || author.isEmpty() || category.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "모든 필드를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                book.setTitle(title);
                book.setAuthor(author);
                book.setCategory(category);
                loadBookData();
                JOptionPane.showMessageDialog(this, "도서가 수정되었습니다.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "수정할 도서를 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            Long bookId = (long) bookTableModel.getValueAt(selectedRow, 0);
            dataStorage.removeBook(bookId);
            loadBookData();
            JOptionPane.showMessageDialog(this, "도서가 삭제되었습니다.");
        } else {
            JOptionPane.showMessageDialog(this, "삭제할 도서를 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void approveRentalRequest() {
        int selectedRow = rentalRequestTable.getSelectedRow();
        if (selectedRow != -1) {
            Long rentalId = (long) rentalRequestTableModel.getValueAt(selectedRow, 0);
            Rental rental = dataStorage.getRentals().stream()
                                       .filter(r -> r.getId() == rentalId)
                                       .findFirst()
                                       .orElse(null);

            if (rental != null) {
                rental.setApproved(true);
                dataStorage.updateRental(rental);

                // 책의 대여 가능 여부를 업데이트
                dataStorage.updateBookAvailability(rental.getBookId(), false);

                JOptionPane.showMessageDialog(this, "대여 요청이 승인되었습니다.");
                loadRentalRequestData();
                loadBookData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "승인할 대여 요청을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rejectRentalRequest() {
        int selectedRow = rentalRequestTable.getSelectedRow();
        if (selectedRow != -1) {
            Long rentalId = (Long) rentalRequestTableModel.getValueAt(selectedRow, 0);

            // 데이터 저장소에서 대여 요청 제거
            Rental rental = dataStorage.getRentals().stream()
                                       .filter(r -> r.getId() == rentalId)
                                       .findFirst()
                                       .orElse(null);

            if (rental != null) {
                dataStorage.removeRental(rentalId);
                JOptionPane.showMessageDialog(this, "대여 요청이 거절되었습니다.");
                loadRentalRequestData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "거절할 대여 요청을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processReturnRequest() {
        int selectedRow = returnRequestTable.getSelectedRow();
        if (selectedRow != -1) {
            Long rentalId = (long) returnRequestTableModel.getValueAt(selectedRow, 0);
            Rental rental = dataStorage.getRentals().stream()
                                       .filter(r -> r.getId() == rentalId)
                                       .findFirst()
                                       .orElse(null);

            if (rental != null) {
                rental.setReturned(true);
                rental.setReturnDate(LocalDate.now());
                dataStorage.updateRental(rental);

                // 책의 대여 가능 여부를 업데이트
                dataStorage.updateBookAvailability(rental.getBookId(), true);

                JOptionPane.showMessageDialog(this, "반납이 처리되었습니다.");
                loadReturnRequestData();
                loadBookData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "처리할 반납 요청을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}
