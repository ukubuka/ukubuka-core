package com.ukubuka.core.reader;

import org.junit.Test;

import com.ukubuka.core.exception.ReaderException;

/**
 * Ukubuka Reader Test
 * 
 * @author agrawroh
 * @version v1.0
 */
public class UkubukaReaderTest {

    private UkubukaReader ukubukaReader;

    /********************************
     * Test(s)
     * 
     * @throws ReaderException
     ********************************/
    @Test
    public void test_readFile_success() throws ReaderException {
        ukubukaReader.readFile(null, null, null, null);
    }
}
