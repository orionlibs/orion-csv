package io.github.orionlibs.orion_csv;

import com.opencsv.CSVWriter;
import io.github.orionlibs.orion_csv.tasks.BuildCSVBodyTask;
import io.github.orionlibs.orion_csv.tasks.BuildCSVHeaderTask;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVWriterService
{
    private CSVWriter writer;
    private String header;
    private String body;


    public CSVWriterService()
    {
        this.header = "";
        this.body = "";
    }


    public CSVWriterService(String fullFilePathAndName) throws IOException
    {
        this.writer = new CSVWriter(new FileWriter(fullFilePathAndName));
    }


    public void writeToCSV(String[] row) throws IOException
    {
        List<String> tempRow = new ArrayList<>(row.length);
        Arrays.stream(row).forEach(cell -> tempRow.add(sanitiseValue(cell.replace("\"", ""))));
        writer.writeNext(tempRow.toArray(new String[0]));
    }


    public void closeCSVFile()
    {
        CloseResourceTask.closeResource(this.writer);
    }


    public static String[] buildEmptyHeader()
    {
        return new String[]
                        {""};
    }


    public static String sanitiseValue(String value)
    {
        if(value != null && !value.isEmpty())
        {
            String valueTemp = value.replaceAll("[\\t\\n\\r]+", "  ");
            valueTemp = valueTemp.replace("\"", "\"\"");
            return valueTemp;
        }
        else
        {
            return "";
        }
    }


    public void buildHeader(List<String> headerColumnNames)
    {
        header = BuildCSVHeaderTask.run(headerColumnNames);
    }


    public void buildBody(List<List<String>> entries)
    {
        body = BuildCSVBodyTask.run(entries);
    }


    public String getCSV()
    {
        return header + body;
    }
}