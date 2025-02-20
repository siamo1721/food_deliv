package com.example.food_deliv.service;

import com.example.food_deliv.model.ComplexLunch;
import com.example.food_deliv.repository.ComplexLunchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplexLunchService {
    private final ComplexLunchRepository complexLunchRepository;

    public List<ComplexLunch> getAllComplexLunches() {
        return complexLunchRepository.findAll();
    }

    public List<ComplexLunch> getComplexLunchesByDay(DayOfWeek dayOfWeek) {
        return complexLunchRepository.findByDayOfWeek(dayOfWeek);
    }

    public ComplexLunch getComplexLunchById(Long id) {
        return complexLunchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Комплексный обед не найден"));
    }
}