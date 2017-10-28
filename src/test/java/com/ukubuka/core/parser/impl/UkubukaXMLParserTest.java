package com.ukubuka.core.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.reader.UkubukaReader;

/**
 * Ukubuka XML Parser Test
 * 
 * @author agrawroh
 * @version v1.0
 */
public class UkubukaXMLParserTest {

    /**************************** Dependency Mocks ***************************/
    @Mock
    private UkubukaReader reader;

    @InjectMocks
    private UkubukaXMLParser ukubukaXMLParser;

    /**************************** Initialize Mocks ***************************/
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /******************************** Test(s) ********************************/
    @Test
    public void test_parseFile_headerPresent_success()
            throws ReaderException, ParserException {
        Map<String, Object> flags = new HashMap<>();
        FileContents fileContents = ukubukaXMLParser.parseFile("foo", flags);

        assertNull(fileContents);
    }

    @Test
    public void test_getParserInfo() {
        assertEquals("UkubukaXMLParser", ukubukaXMLParser.getParserInfo());
    }
}
