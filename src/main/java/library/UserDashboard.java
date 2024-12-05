package library;

import java.awt.BorderLayout;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UserDashboard extends JFrame {
    private User user;
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JTable rentalTable;
    private DefaultTableModel rentalTableModel;
    private DataStorage dataStorage;

    public UserDashboard(User user) {
        dataStorage = DataStorageFactory.getInstance();
        this.user = user;
        setTitle("사용자 대시보드 - " + user.getUsername());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // 도서 검색 및 대여 탭
        JPanel searchPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel();
        JButton logoutButton = new JButton("로그아웃");
        logoutButton.addActionListener(e -> logout());
        topPanel.add(logoutButton);

        String[] bookColumnNames = {"ID", "제목", "저자", "카테고리", "대여 가능 여부"};
        bookTableModel = new DefaultTableModel(bookColumnNames, 0);
        bookTable = new JTable(bookTableModel);
        loadBookData("");
        JScrollPane bookScrollPane = new JScrollPane(bookTable);

        JButton rentButton = new JButton("대여 요청");
        rentButton.addActionListener(e -> requestRental());

        searchPanel.add(topPanel, BorderLayout.NORTH);
        searchPanel.add(bookScrollPane, BorderLayout.CENTER);
        searchPanel.add(rentButton, BorderLayout.SOUTH);

        tabbedPane.addTab("도서 검색 및 대여", searchPanel);

        // 내 대여 목록 및 반납 탭
        JPanel rentalPanel = new JPanel(new BorderLayout());

        String[] rentalColumnNames = {"ID", "도서 제목", "대여일", "반납 기한", "상태"};
        rentalTableModel = new DefaultTableModel(rentalColumnNames, 0);
        rentalTable = new JTable(rentalTableModel);
        loadRentalData(rentalTableModel);
        JScrollPane rentalScrollPane = new JScrollPane(rentalTable);

        JButton returnButton = new JButton("반납 요청");
        returnButton.addActionListener(e -> requestReturn());

        rentalPanel.add(rentalScrollPane, BorderLayout.CENTER);
        rentalPanel.add(returnButton, BorderLayout.SOUTH);

        tabbedPane.addTab("내 대여 목록", rentalPanel);

        add(tabbedPane);
    }

    private void loadBookData(String keyword) {
        bookTableModel.setRowCount(0);
        for (Book book : dataStorage.getBooks().values()) {
            if (keyword.isEmpty() || book.getTitle().contains(keyword) || book.getAuthor().contains(keyword)) {
                bookTableModel.addRow(new Object[]{
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getCategory(),
                        book.isAvailable() ? "가능" : "대여중"
                });
            }
        }
    }

    private void loadRentalData(DefaultTableModel rentalTableModel) {
        rentalTableModel.setRowCount(0);
        for (Rental rental : dataStorage.getRentals()) {
            if (rental.getUsername().equals(user.getUsername())) {
                String status;
                if (rental.isReturned()) {
                    status = "반납 완료";
                } else if (rental.isApproved()) {
                    status = "대여 중";
                } else {
                    status = "대여 요청 중";
                }

                rentalTableModel.addRow(new Object[]{
                        rental.getId(),
                        dataStorage.getBooks().get(rental.getBookId()).getTitle(),
                        rental.getRentalDate(),
                        rental.getDueDate(),
                        status
                });
            }
        }
    }

    private void requestRental() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            int bookId = (int) bookTableModel.getValueAt(selectedRow, 0);
            Book book = dataStorage.getBooks().get(bookId);
            if (book.isAvailable()) {
                LocalDate rentalDate = LocalDate.now();
                LocalDate dueDate = rentalDate.plusDays(14);
                Rental rental = new Rental(user.getUsername(), bookId, rentalDate, dueDate);
                dataStorage.addRental(rental);
                // book.setAvailable(false); // 이 줄을 제거합니다.
                loadBookData("");
                JOptionPane.showMessageDialog(this, "대여 요청이 접수되었습니다.");
                loadRentalData(rentalTableModel);
            } else {
                JOptionPane.showMessageDialog(this, "이미 대여중인 도서입니다.", "대여 불가", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "대여할 도서를 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void requestReturn() {
        int selectedRow = rentalTable.getSelectedRow();
        if (selectedRow != -1) {
            int rentalId = (int) rentalTableModel.getValueAt(selectedRow, 0);
            Rental rental = dataStorage.getRentals().stream()
                                       .filter(r -> r.getId() == rentalId && r.getUsername().equals(user.getUsername()))
                                       .findFirst()
                                       .orElse(null);

            if (rental != null && rental.isApproved() && !rental.isReturned() && !rental.isReturnRequested()) {
                rental.setReturnRequested(true);
                JOptionPane.showMessageDialog(this, "반납 요청이 접수되었습니다.");
                loadRentalData(rentalTableModel);
            } else {
                JOptionPane.showMessageDialog(this, "반납 요청을 할 수 없는 상태입니다.", "반납 불가", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "반납할 대여 기록을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}
