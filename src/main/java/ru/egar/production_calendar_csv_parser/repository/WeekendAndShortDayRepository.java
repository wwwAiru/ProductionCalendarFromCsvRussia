package ru.egar.production_calendar_csv_parser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.egar.production_calendar_csv_parser.entity.WeekendAndShortDays;

import java.time.LocalDate;
import java.util.List;

public interface WeekendAndShortDayRepository extends JpaRepository<WeekendAndShortDays, Long> {
    @Query("select w from WeekendAndShortDays w where w.date between ?1 and ?2")
    List<WeekendAndShortDays> findWeekendAndShortDays(LocalDate startDate, LocalDate endDate);
}
