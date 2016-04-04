package ru.spbau.csvHandler;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import java.util.List;


/**
 * Created by airvan21 on 24.03.16.
 */
public class CSVHandler {

     public static List<CityEntry> parse(CSVReader reader) {
        HeaderColumnNameMappingStrategy<CityEntry> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(CityEntry.class);
        CsvToBean<CityEntry> csvToBean = new CsvToBean<>();

        return csvToBean.parse(strategy, reader);
    }
}
