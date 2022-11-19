package com.base.utils;

import com.csvreader.CsvReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CsvUtil {

    /**
     * convert csv from input stream to list of array of string
     *
     * @param inputStream
     * @param skipHeaderRow
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T extends InputStream> List<String[]> readCsv(T inputStream, boolean skipHeaderRow) throws IOException {
        CsvReader csvReader = new CsvReader(inputStream, ',', StandardCharsets.ISO_8859_1);
        List<String[]> records = new ArrayList<>();
        if (skipHeaderRow) {
            csvReader.readRecord();
        }
        while (csvReader.readRecord()) {
            records.add(csvReader.getValues());
        }
        return records;
    }
}
