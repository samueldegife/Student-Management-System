package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.domain.Student;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Service
public class StudentService {

    private Connection connection;

    // Constructor: Set up H2 in-memory database and create the STUDENT table
    public StudentService() {
        try {
            // Connect to the in-memory H2 database
            connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");

            // Create the STUDENT table
            String createTableSQL = "CREATE TABLE STUDENT (" +
                    "ID INT PRIMARY KEY, " +
                    "NAME VARCHAR(255), " +
                    "AGE INT, " +
                    "GRADE VARCHAR(10))";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(createTableSQL);
            }
            System.out.println("H2 Database initialized and STUDENT table created.");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to set up the database", e);
        }
    }

    // Add a student to the database
    public boolean addStudent(Student student) {
        if (student == null) {
            System.out.println("Invalid student data.");
            return false;
        }

        String insertSQL = "INSERT INTO STUDENT (ID, NAME, AGE, GRADE) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setInt(1, student.getId());
            pstmt.setString(2, student.getName());
            pstmt.setInt(3, student.getAge());
            pstmt.setString(4, student.getGrade());
            pstmt.executeUpdate();
            System.out.println("Student added successfully: " + student.getName() + " (Grade: " + student.getGrade() + ")");
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 23505) { // Duplicate key
                System.out.println("Student with ID " + student.getId() + " already exists.");
            } else {
                System.err.println("Error adding student: " + e.getMessage());
            }
            return false;
        }
    }

    // View all students in the database
    public List<Student> viewStudents() {
        List<Student> studentList = new ArrayList<>();
        String querySQL = "SELECT * FROM STUDENT";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL)) {

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                int age = rs.getInt("AGE");
                String grade = rs.getString("GRADE");

                Student student = new Student(id, name, age, grade);
                studentList.add(student);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving students: " + e.getMessage());
        }

        return studentList;
    }

    // Search for a student by ID
    public Student searchStudent(int id) {
        String searchSQL = "SELECT * FROM STUDENT WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(searchSQL)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getInt("ID"),
                            rs.getString("NAME"),
                            rs.getInt("AGE"),
                            rs.getString("GRADE")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching for student: " + e.getMessage());
        }
        return null;
    }

    // Update a student’s details
    public boolean updateStudent(Student student) {
        String updateSQL = "UPDATE STUDENT SET NAME = ?, AGE = ?, GRADE = ? WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, student.getName());
            pstmt.setInt(2, student.getAge());
            pstmt.setString(3, student.getGrade());
            pstmt.setInt(4, student.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student updated successfully: " + student.getName());
                return true;
            } else {
                System.out.println("No student found with ID " + student.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
        }
        return false;
    }

    // Delete a student by ID
    public boolean deleteStudent(int id) {
        String deleteSQL = "DELETE FROM STUDENT WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully with ID: " + id);
                return true;
            } else {
                System.out.println("No student found with ID " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
        }
        return false;
    }

    // Save student records to a file
    public void saveToFile(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Student student : viewStudents()) {
                writer.write(student.getId() + "," + student.getName() + "," + student.getAge() + "," + student.getGrade());
                writer.newLine();
            }
            System.out.println("Student records saved to file: " + filePath);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Student student = new Student(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            Integer.parseInt(parts[2]),
                            parts[3]
                    );
                    addStudent(student);
                }
            }
            System.out.println("Student records loaded from file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error loading students from file: " + e.getMessage());
        }
    }


}
