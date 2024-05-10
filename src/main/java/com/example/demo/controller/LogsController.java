package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.services.Logs_service;

@Controller
@RequestMapping("/logs")
public class LogsController {
	@Autowired
    private Logs_service logService;

    @GetMapping("/logs")
    public String showLogs(Model model) {
        List<Map<String, Object>> logs = logService.showLogs();
        model.addAttribute("logs", logs);
        return "view_logs"; // Return the name of the HTML template
    }
}
