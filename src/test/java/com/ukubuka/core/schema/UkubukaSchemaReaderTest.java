package com.ukubuka.core.schema;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.reader.UkubukaReader;

/**
 * Ukubuka Schema Reader Test
 * 
 * @author agrawroh
 * @version v1.0
 */
public class UkubukaSchemaReaderTest {

    /**************************** Dependency Mocks ***************************/
    @Mock
    private UkubukaReader reader;

    @InjectMocks
    private UkubukaSchemaReader ukubukaSchemaReader;

    /**************************** Initialize Mocks ***************************/
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /******************************** Test(s) ********************************/
    @Test
    public void test_readSchema_success()
            throws ReaderException, ParserException {
        Mockito.when(reader.readFileAsString(Mockito.any(SupportedSource.class),
                Mockito.anyString(), Mockito.anyString()))
                .thenReturn("{\"extract\":[{\"id\":\"fooBar\"}]}");

        ukubukaSchemaReader.readSchema("foo");

        Mockito.verify(reader, Mockito.times(1)).readFileAsString(
                Mockito.any(SupportedSource.class), Mockito.anyString(),
                Mockito.anyString());
    }

    @Test(expected = ParserException.class)
    public void test_readSchema_failure()
            throws ReaderException, ParserException {
        Mockito.when(reader.readFileAsString(Mockito.any(SupportedSource.class),
                Mockito.anyString(), Mockito.anyString()))
                .thenReturn("{\"foo\":\"bar\"");

        ukubukaSchemaReader.readSchema("foo");

        Mockito.verify(reader, Mockito.times(1)).readFileAsString(
                Mockito.any(SupportedSource.class), Mockito.anyString(),
                Mockito.anyString());
    }
}
