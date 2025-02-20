package com.example.food_deliv.repository;

import com.example.food_deliv.model.ComplexLunch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository

public interface ComplexLunchRepository extends JpaRepository<ComplexLunch, Long> {
    List<ComplexLunch> findByDayOfWeek(DayOfWeek dayOfWeek);
}
