package com.ukubuka.core.scripts;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.reader.UkubukaReader;
import com.ukubuka.core.utilities.Constants;

/**
 * Ukubuka Scripts Reader Test
 * 
 * @author agrawroh
 * @version v1.0
 */
public class UkubukaScriptsReaderTest {
    /**************************** Dependency Mocks ***************************/
    @Mock
    private UkubukaReader ukubukaReader;

    @InjectMocks
    private UkubukaScriptsReader ukubukaScriptsReader;

    /**************************** Initialize Mocks ***************************/
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /******************************** Test(s) ********************************/
    @Test
    public void test_createHTML_success() throws ReaderException, IOException {
        Mockito.when(ukubukaReader.readFileAsString(
                Mockito.any(SupportedSource.class), Mockito.anyString(),
                Mockito.anyString())).thenReturn("foo");
        ukubukaScriptsReader.createHTML(Constants.CLASSPATH
                + Constants.SCRIPTS_TAG + Constants.FORWARD_SLASH + "tau-charts"
                + Constants.FORWARD_SLASH);
    }
}
