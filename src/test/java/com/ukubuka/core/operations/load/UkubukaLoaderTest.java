package com.ukubuka.core.operations.load;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ukubuka.core.exception.WriterException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.FileRecord;
import com.ukubuka.core.model.LoadOperation;
import com.ukubuka.core.model.SupportedFileType;
import com.ukubuka.core.model.UkubukaSchema.Load;
import com.ukubuka.core.model.UkubukaSchema.LoadOperations;
import com.ukubuka.core.writer.UkubukaWriter;

/**
 * Ukubuka Loader Test
 * 
 * @author agrawroh
 * @version v1.0
 */
public class UkubukaLoaderTest {

    /**************************** Dependency Mocks ***************************/
    @Mock
    private UkubukaWriter writer;

    @InjectMocks
    private UkubukaLoader ukubukaLoader;

    /**************************** Initialize Mocks ***************************/
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /******************************** Test(s) ********************************/
    @Test
    public void test_performOperations_csv_success() throws WriterException {
        Mockito.doNothing().when(writer).writeFile(Mockito.anyString(),
                Mockito.anyString());

        Mockito.when(writer.writeCSV(Mockito.anyListOf(String.class),
                Mockito.anyListOf(FileRecord.class))).thenReturn("foo");

        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo", new FileContents(Arrays.asList("foo", "bar"),
                Arrays.asList(new FileRecord(Arrays.asList("bar", "foo")))));
        dataFiles.put("bar", new FileContents(Arrays.asList("bar", "foo"),
                Arrays.asList(new FileRecord(Arrays.asList("foo", "bar")))));

        LoadOperations loadOperations = new LoadOperations();
        loadOperations.setHeader("foo");
        loadOperations.setData(Arrays.asList("foo", "bar"));
        loadOperations.setType(LoadOperation.JOIN);

        Load load = new Load();
        load.setId("foo-X");
        load.setLocation("");
        load.setType(SupportedFileType.CSV);
        load.setOperations(loadOperations);

        List<Load> loads = Arrays.asList(load);

        ukubukaLoader.performOperations(dataFiles, loads);
    }

    @Test
    public void test_performOperations_csv_distinct_success()
            throws WriterException {
        Mockito.doNothing().when(writer).writeFile(Mockito.anyString(),
                Mockito.anyString());

        Mockito.when(writer.writeCSV(Mockito.anyListOf(String.class),
                Mockito.anyListOf(FileRecord.class))).thenReturn("foo");

        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo", new FileContents(Arrays.asList("foo", "bar"),
                Arrays.asList(new FileRecord(Arrays.asList("bar", "foo")))));
        dataFiles.put("bar", new FileContents(Arrays.asList("bar", "foo"),
                Arrays.asList(new FileRecord(Arrays.asList("foo", "bar")))));

        LoadOperations loadOperations = new LoadOperations();
        loadOperations.setHeader("foo");
        loadOperations.setData(Arrays.asList("foo", "bar"));
        loadOperations.setType(LoadOperation.JOIN);
        loadOperations.setFilter(LoadOperation.DISTINCT);

        Load load = new Load();
        load.setId("foo-X");
        load.setLocation("");
        load.setType(SupportedFileType.CSV);
        load.setOperations(loadOperations);

        List<Load> loads = Arrays.asList(load);

        ukubukaLoader.performOperations(dataFiles, loads);
    }

    @Test
    public void test_performOperations_json_success() throws WriterException {
        Mockito.doNothing().when(writer).writeFile(Mockito.anyString(),
                Mockito.anyString());

        Mockito.when(writer.writeJSON(Mockito.anyListOf(String.class),
                Mockito.anyListOf(FileRecord.class)))
                .thenReturn(new JSONArray());

        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo", new FileContents(Arrays.asList("foo", "bar"),
                Arrays.asList(new FileRecord(Arrays.asList("bar", "foo")))));
        dataFiles.put("bar", new FileContents(Arrays.asList("bar", "foo"),
                Arrays.asList(new FileRecord(Arrays.asList("foo", "bar")))));

        LoadOperations loadOperations = new LoadOperations();
        loadOperations.setHeader("foo");
        loadOperations.setData(Arrays.asList("foo", "bar"));
        loadOperations.setType(LoadOperation.JOIN);

        Load load = new Load();
        load.setId("foo-X");
        load.setLocation("");
        load.setType(SupportedFileType.JSON);
        load.setOperations(loadOperations);

        List<Load> loads = Arrays.asList(load);

        ukubukaLoader.performOperations(dataFiles, loads);
    }

    @Test(expected = WriterException.class)
    public void test_performOperations_unsupported_operation_failure()
            throws WriterException {
        Mockito.doNothing().when(writer).writeFile(Mockito.anyString(),
                Mockito.anyString());

        Mockito.when(writer.writeJSON(Mockito.anyListOf(String.class),
                Mockito.anyListOf(FileRecord.class)))
                .thenReturn(new JSONArray());

        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo", new FileContents(Arrays.asList("foo", "bar"),
                Arrays.asList(new FileRecord(Arrays.asList("bar", "foo")))));
        dataFiles.put("bar", new FileContents(Arrays.asList("bar", "foo"),
                Arrays.asList(new FileRecord(Arrays.asList("foo", "bar")))));

        LoadOperations loadOperations = new LoadOperations();
        loadOperations.setHeader("foo");
        loadOperations.setData(Arrays.asList("foo", "bar"));
        loadOperations.setType(LoadOperation.JOIN);

        Load load = new Load();
        load.setId("foo-X");
        load.setLocation("");
        load.setType(SupportedFileType.XML);
        load.setOperations(loadOperations);

        List<Load> loads = Arrays.asList(load);

        ukubukaLoader.performOperations(dataFiles, loads);
    }

    @Test(expected = WriterException.class)
    public void test_performOperations_writer_failure() throws WriterException {
        Mockito.doThrow(new WriterException("foo")).when(writer)
                .writeFile(Mockito.anyString(), Mockito.anyString());

        Mockito.when(writer.writeJSON(Mockito.anyListOf(String.class),
                Mockito.anyListOf(FileRecord.class)))
                .thenReturn(new JSONArray());

        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo", new FileContents(Arrays.asList("foo", "bar"),
                Arrays.asList(new FileRecord(Arrays.asList("bar", "foo")))));
        dataFiles.put("bar", new FileContents(Arrays.asList("bar", "foo"),
                Arrays.asList(new FileRecord(Arrays.asList("foo", "bar")))));

        LoadOperations loadOperations = new LoadOperations();
        loadOperations.setHeader("foo");
        loadOperations.setData(Arrays.asList("foo", "bar"));
        loadOperations.setType(LoadOperation.JOIN);

        Load load = new Load();
        load.setId("foo-X");
        load.setLocation("");
        load.setType(SupportedFileType.XML);
        load.setOperations(loadOperations);

        List<Load> loads = Arrays.asList(load);

        ukubukaLoader.performOperations(dataFiles, loads);
    }
}
