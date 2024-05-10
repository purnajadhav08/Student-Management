package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.services.Classes_service;

@Controller
@RequestMapping("/classes")
public class ClassesController {
	
	@Autowired
    private Classes_service classesService;
	
	@GetMapping("/view")
	public String viewClasses(Model model) {
	    List<Map<String, Object>> classes = classesService.showClasses();
	    model.addAttribute("classes", classes);
	    return "view_classes"; // Return the name of the HTML template
	}
	
	@GetMapping("/add")
	public String showAddClassForm() {
	    // Add any necessary data to pass to the view
	    return "add_classes"; // Return the name of the HTML template for the add class form
	}

	@PostMapping("/add")
	public String addClass(@RequestParam String classId, @RequestParam String deptCode, 
	                       @RequestParam int courseNumber, @RequestParam int sectionNumber, 
	                       @RequestParam int year, @RequestParam String semester, 
	                       @RequestParam int limit, @RequestParam int classSize, 
	                       @RequestParam String room, Model model) {
		classesService.addClass(classId, deptCode, courseNumber, sectionNumber, year, 
	                          semester, limit, classSize, room);
	    // Add any necessary data to pass to the view
	    model.addAttribute("message", "Class added successfully.");
	    return "add_classes"; // Return the name of the HTML template
	}

	@GetMapping("/delete")
    public String showDeleteClassForm() {
        // Add any necessary data to pass to the view
        return "delete_class"; // Return the name of the HTML template for the delete class form
    }

    @PostMapping("/delete")
    public String deleteClass(@RequestParam String classId, Model model) {
        try {
        	classesService.deleteClass(classId);
            // Add any necessary data to pass to the view
            model.addAttribute("message", "Class deleted successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting class: " + e.getMessage());
        }
        return "redirect:/"; // Return the name of the HTML template
    }
    
    
    
}



