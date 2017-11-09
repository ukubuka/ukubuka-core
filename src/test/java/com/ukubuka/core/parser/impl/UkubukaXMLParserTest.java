package com.ukubuka.core.parser.impl;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.junit.Before;
import org.junit.Ignore;
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
 * Ukubuka XML Parser Test
 * 
 * @author aswinv
 * @version v1.0
 */
public class UkubukaXMLParserTest {

    /**************************** Dependency Mocks ***************************/
    @Mock
    private UkubukaReader reader;

    @Mock
    private XMLStreamReader streamReader;

    @Mock
    private XMLInputFactory inputFactory;

    @InjectMocks
    private UkubukaXMLParser ukubukaXMLParser;

    private XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

    /**************************** Initialize Mocks ***************************/
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /******************************** Test(s) ********************************/
    @Ignore
    public void test_parseXMLFile_success()
            throws ReaderException, ParserException {
        Mockito.when(reader.readFileAsStream(Mockito.any(SupportedSource.class),
                Mockito.anyString())).thenReturn(streamReader);

        Map<String, Object> flags = new HashMap<>();
        FileContents fileContents = ukubukaXMLParser.parseFile("foo", flags);

        assertEquals(3, fileContents.getHeader().size());
        assertEquals(3, fileContents.getData().size());
    }

    @Ignore
    public void test_extractDataFromStream_success() throws ReaderException,
            ParserException, FileNotFoundException, XMLStreamException {
        InputStream in = new FileInputStream(new File(this.getClass()
                .getClassLoader().getResource("test.xml").getFile()));
        XMLStreamReader xmlStreamReader = xmlInputFactory
                .createXMLStreamReader(in);

        // String fileContents = ukubukaXMLParser.extractDataFromStream(xmlStreamReader);

        // assertEquals(4, fileContents.split("\n").length);
    }

    @Test
    public void test_getParserInfo() {
        assertEquals("UkubukaXMLParser", ukubukaXMLParser.getParserInfo());
    }
}
