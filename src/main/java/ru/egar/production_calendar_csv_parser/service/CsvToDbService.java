package ru.egar.production_calendar_csv_parser.service;

import au.com.bytecode.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import ru.egar.production_calendar_csv_parser.entity.DayType;
import ru.egar.production_calendar_csv_parser.entity.WeekendAndShortDays;
import ru.egar.production_calendar_csv_parser.repository.WeekendAndShortDayRepository;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvToDbService {

    private final WeekendAndShortDayRepository weekendAndShortDayRepository;

    @SuppressWarnings("resource")
    @PostConstruct
    public void createProductionCalendar() throws Exception
    {

        CSVReader reader = new CSVReader(new FileReader(ResourceUtils.getFile(ResourceUtils
                .CLASSPATH_URL_PREFIX.concat("data.csv"))), ',' , '"' , 1);
        String[] nextLine;
        weekendAndShortDayRepository.deleteAll();

        while ((nextLine = reader.readNext()) != null) {
            if (nextLine != null) {
                String year = nextLine[0];
                for (int i = 1; i<=12; i++){
                    int month = i;
                    List<String> days = List.of(nextLine[i].split(","));
                    for (int day=0; day<=days.size()-1; day++) {
                        String someDay = days.get(day);
                        // '+' это перенесённый праздничный день
                        if (someDay.contains("+")) {
                            String holiday = someDay.substring(0, someDay.length() - 1);
                            LocalDate holidayDate = LocalDate.of(Integer.parseInt(year),
                                    month, Integer.parseInt(holiday));
                            WeekendAndShortDays weekendAndShortDay = new WeekendAndShortDays(holidayDate, DayType.HOLIDAY);
                            weekendAndShortDayRepository.save(weekendAndShortDay);
                        }
                        // '*' это сокращённый предпраздничный день
                        else if (someDay.contains("*")) {
                            String shortDay = someDay.substring(0, someDay.length() - 1);
                            LocalDate holidayDate = LocalDate.of(Integer.parseInt(year),
                                    month, Integer.parseInt(shortDay));
                            WeekendAndShortDays weekendAndShortDay = new WeekendAndShortDays(holidayDate, DayType.SHORTDAY);
                            weekendAndShortDayRepository.save(weekendAndShortDay);
                        } else {
                            LocalDate holidayDate = LocalDate.of(Integer.parseInt(year),
                                    month, Integer.parseInt(someDay));
                            WeekendAndShortDays weekendAndShortDay = new WeekendAndShortDays(holidayDate, DayType.WEEKEND);
                            weekendAndShortDayRepository.save(weekendAndShortDay);
                        }
                    }
                }
            }
        }
    }
}
