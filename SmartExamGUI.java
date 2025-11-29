import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SmartExamGUI {
    public static void main(String[] args) {
        // Use System Look and Feel for better visuals
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        
        // Launch Login Screen
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}

// ================== LOGIN WINDOW ==================
class LoginFrame extends JFrame {
    JTextField emailField;
    JPasswordField passField;

    public LoginFrame() {
        setTitle("Online Exam Proctoring System - Login");
        setSize(400, 350); // Increased size slightly for the new button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10)); // Changed rows to 5

        // UI Components
        JLabel titleLabel = new JLabel("Exam Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Password:"));
        passField = new JPasswordField();
        inputPanel.add(passField);
        add(inputPanel);

        JButton studentLoginBtn = new JButton("Login as Student");
        JButton adminLoginBtn = new JButton("Login as Admin");
        
        // Action Listeners
        studentLoginBtn.addActionListener(e -> handleStudentLogin());
        adminLoginBtn.addActionListener(e -> handleAdminLogin());

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(studentLoginBtn);
        btnPanel.add(adminLoginBtn);
        add(btnPanel);

        // === NEW: REGISTER BUTTON ===
        JButton registerBtn = new JButton("New Student? Register Here");
        registerBtn.addActionListener(e -> handleRegister());
        add(registerBtn);

        setVisible(true);
    }

    private void handleStudentLogin() {
        String email = emailField.getText();
        String pass = new String(passField.getPassword());
        Student s = Student.login(email, pass); 
        
        if (s != null) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            new StudentDashboard(s);
            dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Student Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdminLogin() {
        String email = emailField.getText();
        String pass = new String(passField.getPassword());
        
        if (Admin.login(email, pass)) { 
            JOptionPane.showMessageDialog(this, "Admin Login Successful!");
            new AdminDashboard(new Admin("Admin", email, pass));
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Admin Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // === NEW: REGISTER LOGIC ===
    private void handleRegister() {
        JTextField nameField = new JTextField();
        JTextField regEmailField = new JTextField();
        JPasswordField regPassField = new JPasswordField();

        Object[] message = {
            "Full Name:", nameField,
            "Email:", regEmailField,
            "Password:", regPassField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Student Registration", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String email = regEmailField.getText();
            String pass = new String(regPassField.getPassword());

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (Student.register(name, email, pass)) {
                JOptionPane.showMessageDialog(this, "✅ Registration Successful! You can now login.");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Registration Failed! Email might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

// ================== ADMIN DASHBOARD ==================
class AdminDashboard extends JFrame {
    Admin admin;

    public AdminDashboard(Admin admin) {
        this.admin = admin;
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton viewStudentsBtn = new JButton("View Registered Students");
        JButton addQuestionBtn = new JButton("Add New Question");
        JButton viewResultsBtn = new JButton("View Student Results");
        JButton logoutBtn = new JButton("Logout");

        // Actions
        viewStudentsBtn.addActionListener(e -> {
            admin.viewAllStudents(); 
            JOptionPane.showMessageDialog(this, "Student list printed to Console.");
        });

        addQuestionBtn.addActionListener(e -> showAddQuestionDialog());
        
        viewResultsBtn.addActionListener(e -> {
            admin.viewStudentResults();
            JOptionPane.showMessageDialog(this, "Results printed to Console.");
        });

        logoutBtn.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        add(viewStudentsBtn);
        add(addQuestionBtn);
        add(viewResultsBtn);
        add(logoutBtn);
        setVisible(true);
    }

    private void showAddQuestionDialog() {
        JTextField qField = new JTextField();
        JTextField optA = new JTextField();
        JTextField optB = new JTextField();
        JTextField optC = new JTextField();
        JTextField optD = new JTextField();
        JTextField correctOpt = new JTextField();

        Object[] message = {
            "Question:", qField,
            "Option A:", optA,
            "Option B:", optB,
            "Option C:", optC,
            "Option D:", optD,
            "Correct Option (1-4):", correctOpt
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add Question", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                admin.addQuestion(qField.getText(), optA.getText(), optB.getText(), 
                                  optC.getText(), optD.getText(), Integer.parseInt(correctOpt.getText()));
                JOptionPane.showMessageDialog(this, "Question Added!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding question. Check inputs.");
            }
        }
    }
}

// ================== STUDENT DASHBOARD & EXAM ==================
class StudentDashboard extends JFrame {
    Student student;

    public StudentDashboard(Student s) {
        this.student = s;
        setTitle("Student Dashboard");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JLabel welcome = new JLabel("Welcome, " + s.getName());
        welcome.setFont(new Font("Arial", Font.BOLD, 16));
        add(welcome);

        JButton startExamBtn = new JButton("Start Exam");
        startExamBtn.setBackground(Color.GREEN);
        startExamBtn.addActionListener(e -> {
            new ExamWindow(student);
            dispose();
        });

        add(startExamBtn);
        setVisible(true);
    }
}

class ExamWindow extends JFrame {
    private Student student;
    private List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;
    private int warnings = 0;
    private Timer examTimer;
    private int timeRemaining = 60; // 60 seconds

    // UI Components
    private JLabel timerLabel;
    private JTextArea questionArea;
    private JRadioButton opt1, opt2, opt3, opt4;
    private ButtonGroup optionsGroup;

    public ExamWindow(Student student) {
        this.student = student;
        loadQuestions();

        setTitle("Online Exam - Proctoring Active");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent closing
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Panel: Timer and Warnings
        JPanel topPanel = new JPanel(new BorderLayout());
        timerLabel = new JLabel("Time: 60s");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.RED);
        topPanel.add(timerLabel, BorderLayout.EAST);
        topPanel.add(new JLabel("  DO NOT SWITCH WINDOWS!"), BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Question
        JPanel centerPanel = new JPanel(new GridLayout(0, 1));
        questionArea = new JTextArea();
        questionArea.setEditable(false);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setFont(new Font("Arial", Font.BOLD, 14));
        
        opt1 = new JRadioButton();
        opt2 = new JRadioButton();
        opt3 = new JRadioButton();
        opt4 = new JRadioButton();
        optionsGroup = new ButtonGroup();
        optionsGroup.add(opt1); optionsGroup.add(opt2); optionsGroup.add(opt3); optionsGroup.add(opt4);

        centerPanel.add(questionArea);
        centerPanel.add(opt1);
        centerPanel.add(opt2);
        centerPanel.add(opt3);
        centerPanel.add(opt4);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel: Next Button
        JButton nextBtn = new JButton("Next Question");
        nextBtn.addActionListener(e -> submitAnswer());
        add(nextBtn, BorderLayout.SOUTH);

        // === PROCTORING FEATURE: FOCUS LISTENER ===
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                // Triggered when user switches window (Alt+Tab)
                if (isVisible()) { // Only if exam is still running
                    warnings++;
                    JOptionPane.showMessageDialog(null, 
                        "⚠️ WARNING: You switched windows! This is recorded.\nTotal Warnings: " + warnings, 
                        "Proctoring Alert", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Start Exam
        showQuestion();
        startTimer();
        setVisible(true);
    }

    private void loadQuestions() {
        try (Connection con = DatabaseConnection.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM questions ORDER BY RAND()");
            while (rs.next()) {
                questions.add(new Question(
                    rs.getString("question"),
                    rs.getString("optA"), rs.getString("optB"), rs.getString("optC"), rs.getString("optD"),
                    rs.getInt("correctOpt")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showQuestion() {
        if (currentIndex < questions.size()) {
            Question q = questions.get(currentIndex);
            questionArea.setText("Q" + (currentIndex + 1) + ": " + q.text);
            opt1.setText(q.o1); opt2.setText(q.o2); opt3.setText(q.o3); opt4.setText(q.o4);
            optionsGroup.clearSelection();
        } else {
            finishExam();
        }
    }

    private void submitAnswer() {
        Question q = questions.get(currentIndex);
        int selected = -1;
        if (opt1.isSelected()) selected = 1;
        else if (opt2.isSelected()) selected = 2;
        else if (opt3.isSelected()) selected = 3;
        else if (opt4.isSelected()) selected = 4;

        if (selected == q.correct) score++;

        currentIndex++;
        showQuestion();
    }

    private void startTimer() {
        examTimer = new Timer(1000, e -> {
            timeRemaining--;
            timerLabel.setText("Time: " + timeRemaining + "s");
            if (timeRemaining <= 0) {
                examTimer.stop();
                JOptionPane.showMessageDialog(this, "Time's Up! Auto-submitting.");
                finishExam();
            }
        });
        examTimer.start();
    }

    private void finishExam() {
        if (examTimer != null) examTimer.stop();
        setVisible(false); // Hide window immediately

        // Save to Database
        try (Connection con = DatabaseConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO results(student_id, score, warnings) VALUES (?, ?, ?)");
            ps.setInt(1, student.getStudentId());
            ps.setInt(2, score);
            ps.setInt(3, warnings);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(null, "Exam Finished!\nScore: " + score + "\nWarnings Issued: " + warnings);
        System.exit(0);
    }

    // Helper Class for Questions
    static class Question {
        String text, o1, o2, o3, o4;
        int correct;
        public Question(String t, String a, String b, String c, String d, int ans) {
            text = t; o1 = a; o2 = b; o3 = c; o4 = d; correct = ans;
        }
    }
}