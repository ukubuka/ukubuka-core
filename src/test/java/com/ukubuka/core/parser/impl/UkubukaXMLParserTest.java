package com.ukubuka.core.parser.impl;

import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.reader.UkubukaReader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.xml.stream.XMLInputFactory;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;

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
    private XMLInputFactory inputFactory;

    @InjectMocks
    private UkubukaXMLParser ukubukaXMLParser;

    /**************************** Initialize Mocks ***************************/
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /******************************** Test(s) ********************************/
    @Test
    public void test_parseXMLFile_success()
            throws ReaderException, ParserException {
        Mockito.when(reader.readXMLAsString(Mockito.any(SupportedSource.class), Mockito.anyString(), eq(ukubukaXMLParser))).thenReturn("x~`~y~`~z\n" +
                "5~`~3~`~9\n" +
                "1~`~8~`~4\n" +
                "3~`~6~`~7\n");

        Map<String, Object> flags = new HashMap<>();
        FileContents fileContents = ukubukaXMLParser.parseFile("foo", flags);

        assertEquals(3, fileContents.getHeader().size());
        assertEquals(3, fileContents.getData().size());
    }

    @Test
    public void test_getParserInfo() {
        assertEquals("UkubukaXMLParser", ukubukaXMLParser.getParserInfo());
    }
}
