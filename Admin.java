import java.sql.*;

public class Admin extends Person {
    public Admin(String name, String email, String password) {
        super(name, email, password);
    }

    public static boolean login(String email, String password) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM admins WHERE email = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if a match is found
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addQuestion(String question, String optA, String optB, String optC, String optD, int correctOpt) {
        try (Connection con = DatabaseConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO questions(question, optA, optB, optC, optD, correctOpt) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, question);
            ps.setString(2, optA);
            ps.setString(3, optB);
            ps.setString(4, optC);
            ps.setString(5, optD);
            ps.setInt(6, correctOpt);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewAllStudents() {
        try (Connection con = DatabaseConnection.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, name, email FROM students");
            System.out.println("\n=== Registered Students ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | Name: " + rs.getString("name") + " | Email: " + rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // NEW FEATURE: View student marks/results
    public void viewStudentResults() {
        try (Connection con = DatabaseConnection.getConnection()) {
            Statement st = con.createStatement();
            // Join query to combine student details with their exam results
            String query = "SELECT s.id, s.name, s.email, r.score, r.exam_date " +
                           "FROM students s " +
                           "JOIN results r ON s.id = r.student_id " +
                           "ORDER BY r.exam_date DESC";
                           
            ResultSet rs = st.executeQuery(query);
            
            System.out.println("\n==== STUDENT EXAM RESULTS ====");
            // Formatting for a clean table output
            System.out.printf("%-5s %-20s %-30s %-10s %-20s%n", "ID", "Name", "Email", "Score", "Date");
            System.out.println("----------------------------------------------------------------------------------------");
            
            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                System.out.printf("%-5d %-20s %-30s %-10d %-20s%n", 
                    rs.getInt("id"), 
                    rs.getString("name"), 
                    rs.getString("email"), 
                    rs.getInt("score"), 
                    rs.getTimestamp("exam_date"));
            }
            
            if (!hasResults) {
                System.out.println("No exam attempts found yet.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}