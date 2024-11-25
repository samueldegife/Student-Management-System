package com.example.studentmanagementsystem;

import com.example.studentmanagementsystem.domain.Student;
import com.example.studentmanagementsystem.service.StudentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class StudentManagementSystemApplication {

    public static void main(String[] args) throws IOException {

//        StudentService studentService = new StudentService();
//        // Add students
//        studentService.addStudent(new Student(1, "John Doe", 20, "A"));
//        studentService.addStudent(new Student(2, "Jane Smith", 22, "B"));
//
//        // Search for a student
//        Student foundStudent = studentService.searchStudent(1);
//        System.out.println("Found student: " + foundStudent);
//
//        // Update a student
//        studentService.updateStudent(new Student(1, "Johnathan Doe", 21, "A+"));
//
//        // Delete a student
//        studentService.deleteStudent(2);
//
//        // Save to file
//        studentService.saveToFile("students.txt");
//
//        // Load from file
//        studentService.loadFromFile("students.txt");
//
//        // View all students
//        List<Student> allStudents = studentService.viewStudents();
//        allStudents.forEach(System.out::println);

        SpringApplication.run(StudentManagementSystemApplication.class, args);


    }

    @Bean
    public StudentService studentService() {
        return new StudentService();
    }

}
