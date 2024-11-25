package com.example.studentmanagementsystem.controller;


import com.example.studentmanagementsystem.domain.Student;
import com.example.studentmanagementsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    // View all students
    @GetMapping("/students")
    public String viewStudents(Model model) {
        List<Student> students = studentService.viewStudents();
        model.addAttribute("students", students);
        return "students";
    }

    // Show the form to add a new student
    @GetMapping("/students/add")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "add-student";
    }

    // Handle adding a new student
    @PostMapping("/students/add")
    public String addStudent(@ModelAttribute Student student) {
        studentService.addStudent(student);
        return "redirect:/students";
    }

    // Search for a student by ID
    @GetMapping("/students/search")
    public String searchStudent(@RequestParam("id") int id, Model model) {
        Student student = studentService.searchStudent(id);
        model.addAttribute("student", student);
        return "search-result";
    }

    // Show the form to update a student
    @GetMapping("/students/update/{id}")
    public String showUpdateStudentForm(@PathVariable("id") int id, Model model) {
        Student student = studentService.searchStudent(id);
        model.addAttribute("student", student);
        return "update-student";
    }

    // Handle updating a student
    @PostMapping("/students/update")
    public String updateStudent(@ModelAttribute Student student) {
        studentService.updateStudent(student);
        return "redirect:/students";
    }

    // Delete a student by ID
    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable("id") int id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }
}

