package com.ukubuka.core.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.reader.UkubukaReader;

/**
 * Ukubuka BaseParser Test
 * 
 * @author yashvardhannanavati
 * @version v1.0
 */
public class UkubukaBaseParserTest {

    /**************************** Dependency Mocks ***************************/
    @Mock
    private UkubukaReader reader;

    @InjectMocks
    private UkubukaBaseParser ukubukaBaseParser;

    /**************************** Initialize Mocks ***************************/
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /******************************** Test(s) ********************************/
    @Test
    public void test_GetFileContents() {
        FileContents fileContents = ukubukaBaseParser
                .getFileContents("foo,bar\nchus,na\n");
        assertEquals(1, fileContents.getData().size());
        assertEquals(1, fileContents.getHeader().size());
    }
}
