package com.ukubuka.core.reader;

import org.junit.Test;

import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.model.SupportedSource;

/**
 * Ukubuka Reader Test
 * 
 * @author agrawroh
 * @version v1.0
 */
public class UkubukaReaderTest {

    private UkubukaReader ukubukaReader = new UkubukaReader();

    /******************************** Test(s) ********************************/
    @Test
    public void test_readFile_withDelim_success() throws ReaderException {
        ukubukaReader.readFile(
                SupportedSource.FILE, this.getClass().getClassLoader()
                        .getResource("test-dataset.csv").getFile(),
                "UTF-8", "\n");
    }

    @Test
    public void test_readFile_withoutDelim_success() throws ReaderException {
        ukubukaReader.readFile(
                SupportedSource.FILE, this.getClass().getClassLoader()
                        .getResource("test-dataset.csv").getFile(),
                "UTF-8", null);
    }

    @Test
    public void test_readFileAsString_file_withEncoding_success()
            throws ReaderException {
        ukubukaReader
                .readFileAsString(SupportedSource.FILE,
                        this.getClass().getClassLoader()
                                .getResource("test-dataset.csv").getFile(),
                        "UTF-8");
    }

    @Test
    public void test_readFileAsString_file_withoutEncoding_success()
            throws ReaderException {
        ukubukaReader
                .readFileAsString(SupportedSource.FILE,
                        this.getClass().getClassLoader()
                                .getResource("test-dataset.csv").getFile(),
                        null);
    }

    @Test
    public void test_readFileAsString_url_withEncoding_success()
            throws ReaderException {
        ukubukaReader.readFileAsString(SupportedSource.URL,
                this.getClass().getClassLoader().getResource("test-dataset.csv")
                        .toString().replace("file:/", "file:///"),
                "UTF-8");
    }

    @Test
    public void test_readFileAsString_url_withoutEncoding_success()
            throws ReaderException {
        ukubukaReader.readFileAsString(SupportedSource.URL,
                this.getClass().getClassLoader().getResource("test-dataset.csv")
                        .toString().replace("file:/", "file:///"),
                null);
    }

    @Test(expected = ReaderException.class)
    public void test_readFileAsString_url_withoutEncoding_failure()
            throws ReaderException {
        ukubukaReader.readFileAsString(SupportedSource.URL,
                this.getClass().getClassLoader().getResource("test-dataset.csv")
                        .toString().replace("file:/", "file://"),
                null);
    }
}
