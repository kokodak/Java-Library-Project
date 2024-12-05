package library.ui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import library.persistence.DataStorage;
import library.persistence.DataStorageFactory;
import library.domain.User;

public class SignUpFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private DataStorage dataStorage;

    public SignUpFrame() {
        dataStorage = DataStorageFactory.getInstance();
        setTitle("회원가입");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        JLabel usernameLabel = new JLabel("아이디:");
        JLabel passwordLabel = new JLabel("비밀번호:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton signUpButton = new JButton("회원가입");

        signUpButton.addActionListener(e -> signUp());

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // 빈 레이블로 자리 채우기
        panel.add(signUpButton);

        add(panel);
    }

    private void signUp() {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (dataStorage.getUsers().containsKey(username)) {
            JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다.", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
        } else {
            User newUser = new User(username, password, "user");
            dataStorage.addUser(newUser);
            JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다.");
            dispose();
        }
    }
}
