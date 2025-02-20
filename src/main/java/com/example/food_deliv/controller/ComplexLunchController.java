package com.example.food_deliv.controller;
import com.example.food_deliv.model.ComplexLunch;
import com.example.food_deliv.service.ComplexLunchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api/complex-lunches")
@RequiredArgsConstructor
public class ComplexLunchController {
    private final ComplexLunchService complexLunchService;


    @GetMapping
    public ResponseEntity<List<ComplexLunch>> getAllComplexLunches() {
        return ResponseEntity.ok(complexLunchService.getAllComplexLunches());
    }

    @GetMapping("/day/{dayOfWeek}")
    public ResponseEntity<List<ComplexLunch>> getComplexLunchesByDay(@PathVariable String dayOfWeek) {
        try {
            DayOfWeek day = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
            return ResponseEntity.ok(complexLunchService.getComplexLunchesByDay(day));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplexLunch> getComplexLunchById(@PathVariable Long id) {
        return ResponseEntity.ok(complexLunchService.getComplexLunchById(id));
    }
}
