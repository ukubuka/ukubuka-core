package com.ukubuka.core.operations.extract;

import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.SupportedFileType;
import com.ukubuka.core.model.UkubukaSchema.Extract;
import com.ukubuka.core.parser.UkubukaParser;

/**
 * Ukubuka Extractor Test
 * 
 * @author agrawroh
 * @version v1.0
 */
public class UkubukaExtractorTest {

    /**************************** Dependency Mocks ***************************/
    @Mock
    private UkubukaParser xmlParser;

    @Mock
    private UkubukaParser delimitedFileParser;

    @InjectMocks
    private UkubukaExtractor ukubukaExtractor;

    /**************************** Initialize Mocks ***************************/
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /******************************** Test(s) ********************************/
    @Test
    public void test_performOperations_csv_success() throws ParserException {
        Mockito.when(delimitedFileParser.parseFile(Mockito.anyString(),
                Mockito.anyMapOf(String.class, Object.class)))
                .thenReturn(new FileContents());

        Extract extracts = new Extract();
        extracts.setId("foo");
        extracts.setLocation("");
        extracts.setType(SupportedFileType.CSV);

        ukubukaExtractor.performOperations(new HashMap<>(),
                Arrays.asList(extracts));

        Mockito.verify(delimitedFileParser, Mockito.times(1)).parseFile(
                Mockito.anyString(),
                Mockito.anyMapOf(String.class, Object.class));
    }

    @Test
    public void test_performOperations_xml_success() throws ParserException {
        Mockito.when(xmlParser.parseFile(Mockito.anyString(),
                Mockito.anyMapOf(String.class, Object.class)))
                .thenReturn(new FileContents());

        Extract extracts = new Extract();
        extracts.setId("foo");
        extracts.setLocation("");
        extracts.setType(SupportedFileType.XML);

        ukubukaExtractor.performOperations(new HashMap<>(),
                Arrays.asList(extracts));

        Mockito.verify(xmlParser, Mockito.times(1)).parseFile(
                Mockito.anyString(),
                Mockito.anyMapOf(String.class, Object.class));
    }

    @Test(expected = ParserException.class)
    public void test_performOperations_unsupported_operation_failure()
            throws ParserException {
        Mockito.when(delimitedFileParser.parseFile(Mockito.anyString(),
                Mockito.anyMapOf(String.class, Object.class)))
                .thenReturn(new FileContents());

        Extract extracts = new Extract();
        extracts.setId("foo");
        extracts.setLocation("");
        extracts.setType(SupportedFileType.JSON);

        ukubukaExtractor.performOperations(new HashMap<>(),
                Arrays.asList(extracts));
    }

    @Test(expected = NullPointerException.class)
    public void test_performOperations_null_failure() throws ParserException {
        Mockito.when(delimitedFileParser.parseFile(Mockito.anyString(),
                Mockito.anyMapOf(String.class, Object.class)))
                .thenReturn(new FileContents());

        Extract extracts = new Extract();
        extracts.setId("foo");
        extracts.setLocation("");

        ukubukaExtractor.performOperations(new HashMap<>(),
                Arrays.asList(extracts));
    }
}
