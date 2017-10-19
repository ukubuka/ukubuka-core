package com.ukubuka.core.writer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ukubuka.core.exception.WriterException;
import com.ukubuka.core.model.FileRecord;

/**
 * Ukubuka Writer Test
 * 
 * @author agrawroh
 * @version v1.0
 */
public class UkubukaWriterTest {

    /**************************** Dependency Mocks ***************************/
    @Mock
    private ObjectMapper mapper;

    @Mock
    private ObjectWriter writer;

    @Mock
    private JsonParseException jsonParseException;

    @InjectMocks
    private UkubukaWriter ukubukaWriter;

    /**************************** Initialize Mocks ***************************/
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /******************************** Test(s) ********************************/
    @Test
    public void test_prettyPrint_success() throws WriterException, IOException {
        Mockito.when(mapper.writerWithDefaultPrettyPrinter())
                .thenReturn(writer);
        Mockito.when(
                mapper.readValue(Mockito.anyString(), Mockito.eq(Object.class)))
                .thenReturn("fooBar");
        Mockito.when(writer.writeValueAsString(Mockito.anyString()))
                .thenReturn("fooBar");
        String output = ukubukaWriter.prettyPrint("{\"foo\":\"bar\"}");
        assertEquals("fooBar", output);
    }

    @Test(expected = WriterException.class)
    public void test_prettyPrint_writer_json_parse_exception()
            throws WriterException, IOException {
        Mockito.when(mapper.writerWithDefaultPrettyPrinter())
                .thenReturn(writer);
        Mockito.when(
                mapper.readValue(Mockito.anyString(), Mockito.eq(Object.class)))
                .thenReturn("fooBar");
        Mockito.when(writer.writeValueAsString(Mockito.anyString()))
                .thenThrow(jsonParseException);
        ukubukaWriter.prettyPrint("{\"foo\":\"bar\"}");
    }

    @Test(expected = WriterException.class)
    public void test_prettyPrint_mapper_json_parse_exception()
            throws WriterException, IOException {
        Mockito.when(mapper.writerWithDefaultPrettyPrinter())
                .thenReturn(writer);
        Mockito.when(
                mapper.readValue(Mockito.anyString(), Mockito.eq(Object.class)))
                .thenThrow(jsonParseException);
        Mockito.when(writer.writeValueAsString(Mockito.anyString()))
                .thenThrow(jsonParseException);
        ukubukaWriter.prettyPrint("{\"foo\":\"bar\"}");
    }

    @Test
    public void test_writeJSON_success() {
        List<String> fileHeader = Arrays.asList("foo", "bar");
        List<FileRecord> fileRecords = Arrays
                .asList(new FileRecord(Arrays.asList("bar", "foo")));
        JSONArray jsonArray = ukubukaWriter.writeJSON(fileHeader, fileRecords);
        assertEquals(1, jsonArray.length());
    }

    @Test
    public void test_writeCSV_success() {
        List<String> fileHeader = Arrays.asList("foo", "bar");
        List<FileRecord> fileRecords = Arrays
                .asList(new FileRecord(Arrays.asList("bar", "foo")));
        String csvFile = ukubukaWriter.writeCSV(fileHeader, fileRecords);
        assertEquals(16, csvFile.length());
    }

    @Test(expected = WriterException.class)
    public void test_writeFile_failure() throws WriterException {
        ukubukaWriter.writeFile("", "fooBar");
    }

    @Test
    public void test_writeFile_file_success() throws WriterException {
        ukubukaWriter.writeFile("foo", "fooBar");
    }

    @Test
    public void test_writeFile_writer_success() {
        ukubukaWriter.writeFile(new StringWriter(), "fooBar");
    }
}
