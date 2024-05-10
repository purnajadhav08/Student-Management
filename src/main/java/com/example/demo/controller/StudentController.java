package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.demo.services.Student_service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/students")
public class StudentController {
	

	@Autowired
    private Student_service studentService;
	
	@Component
    public static class StringToDateConverter implements Converter<String, Date> {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public Date convert(String source) {
            try {
                return dateFormat.parse(source);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.", e);
            }
        }
    }
	
	@GetMapping("/add")
    public String showAddStudentForm() {
        // Add any necessary data to pass to the view
        return "add_students"; // Return the name of the HTML template for the add student form
    }



	@PostMapping("/add")
    public String addStudent(@RequestParam String bNumber, @RequestParam String firstName,
                             @RequestParam String lastName, @RequestParam String status,
                             @RequestParam Double gpa, @RequestParam String email,
                             @RequestParam Date birthDate, Model model) {
        studentService.add_students(bNumber, firstName, lastName, status, gpa, email, birthDate);
        // Add any necessary data to pass to the view
        model.addAttribute("message", "Student added successfully.");
        return "add_students"; // Return the name of the HTML template
    }
	
	 @GetMapping("/view")
	    public String viewStudents(Model model) {
	        List<Map<String, Object>> students = studentService.showStudents();
	        model.addAttribute("students", students);
	        return "view_students"; // Return the name of the HTML template without the extension
	    }
	 
	 @GetMapping("/delete")
	 public String showDeleteStudentForm() {
	     // Add any necessary data to pass to the view
	     return "deleteStudent"; // Return the name of the HTML template for the delete student form
	 }

	 @PostMapping("/delete")
	 public String deleteStudent(@RequestParam String bnum) {
	     // Call the deleteStudent method from the service class
	     studentService.deleteStudent(bnum);
	     // Redirect to a different URL after successful deletion
	     return "redirect:/"; // Redirect to the home page
	 }
}