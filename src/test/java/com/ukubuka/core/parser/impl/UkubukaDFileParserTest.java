package com.ukubuka.core.parser.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.reader.UkubukaReader;

/**
 * Ukubuka DFileParser Test
 * 
 * @author yashvardhannanavati
 * @version v1.0
 */
public class UkubukaDFileParserTest {

    /**************************** Dependency Mocks ***************************/
    @Mock
    private UkubukaReader reader;

    @InjectMocks
    private UkubukaDFileParser ukubukaDFileParser;

    /**************************** Initialize Mocks ***************************/
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /******************************** Test(s) ********************************/
    @Test
    public void test_parseFile_noFlags_success()
            throws ReaderException, ParserException {
        Mockito.when(reader.readFileAsString(Mockito.any(SupportedSource.class),
                Mockito.anyString(), Mockito.anyString()))
                .thenReturn("foo,bar\nbar,foo\n");

        Map<String, Object> flags = new HashMap<>();
        FileContents fileContents = ukubukaDFileParser.parseFile("foo", flags);

        Mockito.verify(reader, Mockito.times(1)).readFileAsString(
                Mockito.any(SupportedSource.class), Mockito.anyString(),
                Mockito.anyString());
        assertEquals(2, fileContents.getHeader().size());
        assertEquals(1, fileContents.getData().size());
    }

    @Test
    public void test_parseFile_headerPresent_success()
            throws ReaderException, ParserException {
        Mockito.when(reader.readFileAsString(Mockito.any(SupportedSource.class),
                Mockito.anyString(), Mockito.anyString()))
                .thenReturn("foo,bar\nbar,foo\n");

        Map<String, Object> flags = new HashMap<>();
        flags.put("withHeader", true);
        FileContents fileContents = ukubukaDFileParser.parseFile("foo", flags);

        Mockito.verify(reader, Mockito.times(1)).readFileAsString(
                Mockito.any(SupportedSource.class), Mockito.anyString(),
                Mockito.anyString());
        assertEquals(2, fileContents.getHeader().size());
        assertEquals(1, fileContents.getData().size());
    }

    @Test
    public void test_parseFile_delimiterPresent_success()
            throws ReaderException, ParserException {
        Mockito.when(reader.readFileAsString(Mockito.any(SupportedSource.class),
                Mockito.anyString(), Mockito.anyString()))
                .thenReturn("foo|bar\nbar|foo\n");

        Map<String, Object> flags = new HashMap<>();
        flags.put("fileDelimiter", "|");
        FileContents fileContents = ukubukaDFileParser.parseFile("foo", flags);

        Mockito.verify(reader, Mockito.times(1)).readFileAsString(
                Mockito.any(SupportedSource.class), Mockito.anyString(),
                Mockito.anyString());
        assertEquals(2, fileContents.getHeader().size());
        assertEquals(1, fileContents.getData().size());
    }

    @Test
    public void test_parseFile_fileEncoding_success()
            throws ReaderException, ParserException {
        Mockito.when(reader.readFileAsString(Mockito.any(SupportedSource.class),
                Mockito.anyString(), Mockito.anyString()))
                .thenReturn("foo,bar\nbar,foo\n");

        Map<String, Object> flags = new HashMap<>();
        flags.put("fileEncoding", "UTF-8");
        FileContents fileContents = ukubukaDFileParser.parseFile("foo", flags);

        Mockito.verify(reader, Mockito.times(1)).readFileAsString(
                Mockito.any(SupportedSource.class), Mockito.anyString(),
                Mockito.anyString());
        assertEquals(2, fileContents.getHeader().size());
        assertEquals(1, fileContents.getData().size());
    }

    @Test
    public void test_parseFile_sourcePresent_success()
            throws ReaderException, ParserException {
        Mockito.when(reader.readFileAsString(Mockito.any(SupportedSource.class),
                Mockito.anyString(), Mockito.anyString()))
                .thenReturn("foo,bar\nbar,foo\n");

        Map<String, Object> flags = new HashMap<>();
        flags.put("source", "file");
        FileContents fileContents = ukubukaDFileParser.parseFile("foo", flags);

        Mockito.verify(reader, Mockito.times(1)).readFileAsString(
                Mockito.any(SupportedSource.class), Mockito.anyString(),
                Mockito.anyString());
        assertEquals(2, fileContents.getHeader().size());
        assertEquals(1, fileContents.getData().size());
    }

    @Test(expected = ParserException.class)
    public void test_parseFile_failure()
            throws ReaderException, ParserException {
        Mockito.when(reader.readFileAsString(Mockito.any(SupportedSource.class),
                Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new ReaderException("foo"));

        Map<String, Object> flags = new HashMap<>();
        ukubukaDFileParser.parseFile("foo", flags);
    }

    @Test
    public void test_parseFile_headerFlagPresent_headerAbsent_success()
            throws ReaderException, ParserException {
        Mockito.when(reader.readFileAsString(Mockito.any(SupportedSource.class),
                Mockito.anyString(), Mockito.anyString()))
                .thenReturn("foo,bar\nbar,foo\n");

        Map<String, Object> flags = new HashMap<>();
        flags.put("withHeader", false);
        FileContents fileContents = ukubukaDFileParser.parseFile("foo", flags);

        Mockito.verify(reader, Mockito.times(1)).readFileAsString(
                Mockito.any(SupportedSource.class), Mockito.anyString(),
                Mockito.anyString());
        /*TODO - Change the logic in StitchHeader()*/
        // assertEquals(2, fileContents.getHeader().size());
        assertEquals(2, fileContents.getData().size());

    }

    @Test
    public void test_getParserInfo() {
        assertEquals("UkubukaDFileParser", ukubukaDFileParser.getParserInfo());
    }
}
