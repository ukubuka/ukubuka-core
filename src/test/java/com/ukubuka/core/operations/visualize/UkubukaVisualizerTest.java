package com.ukubuka.core.operations.visualize;

import java.io.IOException;
import java.util.ArrayList;
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

import com.ukubuka.core.exception.PipelineException;
import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.exception.WriterException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.FileRecord;
import com.ukubuka.core.model.UkubukaSchema;
import com.ukubuka.core.model.UkubukaSchema.Visualization;
import com.ukubuka.core.model.UkubukaSchema.VisualizeFlags;
import com.ukubuka.core.scripts.UkubukaScriptsReader;
import com.ukubuka.core.writer.UkubukaWriter;

/**
 * Ukubuka Visualizer Test
 * 
 * @author agrawroh
 * @version v1.0
 */
public class UkubukaVisualizerTest {

    /**************************** Dependency Mocks ***************************/
    @Mock
    private UkubukaScriptsReader scriptsReader;

    @Mock
    private UkubukaWriter writer;

    @InjectMocks
    private UkubukaVisualizer ukubukaVisualizer;

    /**************************** Initialize Mocks ***************************/
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /******************************** Test(s) ********************************/
    @Test
    public void test_performOperations_success() throws PipelineException,
            ReaderException, WriterException, IOException {
        Visualization visualization = new Visualization();
        visualization.setId("foo-X");
        visualization.setLocation("foobar");
        VisualizeFlags visualizeFlags = new VisualizeFlags();
        visualizeFlags.setHeight("512");
        visualizeFlags.setWidth("512");
        visualizeFlags.setOptions("foo");
        visualization.setFlags(visualizeFlags);

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<Visualization> visualizations = new ArrayList<>(
                Arrays.asList(visualization));

        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));

        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setVisualizations(visualizations);

        Mockito.when(scriptsReader.createHTML(Mockito.anyString()))
                .thenReturn("fooBar");
        Mockito.doNothing().when(writer).writeFile(Mockito.anyString(),
                Mockito.anyString());
        Mockito.when(writer.prettyPrint(Mockito.anyString())).thenReturn("foo");
        Mockito.when(writer.writeJSON(Mockito.anyListOf(String.class),
                Mockito.anyListOf(FileRecord.class)))
                .thenReturn(new JSONArray());

        ukubukaVisualizer.performOperations(dataFiles, ukubukaSchema);

        Mockito.verify(scriptsReader, Mockito.times(1))
                .createHTML(Mockito.anyString());
        Mockito.verify(writer, Mockito.times(1))
                .prettyPrint(Mockito.anyString());
        Mockito.verify(writer, Mockito.times(1)).writeJSON(
                Mockito.anyListOf(String.class),
                Mockito.anyListOf(FileRecord.class));
    }

    @Test
    public void test_performOperations_empty_location_success()
            throws PipelineException, ReaderException, WriterException,
            IOException {
        Visualization visualization = new Visualization();
        visualization.setId("foo-X");
        VisualizeFlags visualizeFlags = new VisualizeFlags();
        visualizeFlags.setHeight("512");
        visualizeFlags.setWidth("512");
        visualizeFlags.setOptions("foo");
        visualization.setFlags(visualizeFlags);

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<Visualization> visualizations = new ArrayList<>(
                Arrays.asList(visualization));

        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));

        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setVisualizations(visualizations);

        Mockito.when(scriptsReader.createHTML(Mockito.anyString()))
                .thenReturn("fooBar");
        Mockito.doNothing().when(writer).writeFile(Mockito.anyString(),
                Mockito.anyString());
        Mockito.when(writer.prettyPrint(Mockito.anyString())).thenReturn("foo");
        Mockito.when(writer.writeJSON(Mockito.anyListOf(String.class),
                Mockito.anyListOf(FileRecord.class)))
                .thenReturn(new JSONArray());

        ukubukaVisualizer.performOperations(dataFiles, ukubukaSchema);

        Mockito.verify(scriptsReader, Mockito.times(1))
                .createHTML(Mockito.anyString());
        Mockito.verify(writer, Mockito.times(1))
                .prettyPrint(Mockito.anyString());
        Mockito.verify(writer, Mockito.times(1)).writeJSON(
                Mockito.anyListOf(String.class),
                Mockito.anyListOf(FileRecord.class));
    }

    @Test(expected = PipelineException.class)
    public void test_performOperations_writer_exception()
            throws PipelineException, ReaderException, WriterException,
            IOException {
        Visualization visualization = new Visualization();
        visualization.setId("foo-X");
        visualization.setLocation("foobar");
        VisualizeFlags visualizeFlags = new VisualizeFlags();
        visualizeFlags.setHeight("512");
        visualizeFlags.setWidth("512");
        visualizeFlags.setOptions("foo");
        visualization.setFlags(visualizeFlags);

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<Visualization> visualizations = new ArrayList<>(
                Arrays.asList(visualization));

        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));

        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setVisualizations(visualizations);

        Mockito.when(scriptsReader.createHTML(Mockito.anyString()))
                .thenReturn("fooBar");
        Mockito.doThrow(new WriterException("foo")).when(writer)
                .writeFile(Mockito.anyString(), Mockito.anyString());
        Mockito.when(writer.prettyPrint(Mockito.anyString())).thenReturn("foo");
        Mockito.when(writer.writeJSON(Mockito.anyListOf(String.class),
                Mockito.anyListOf(FileRecord.class)))
                .thenReturn(new JSONArray());

        ukubukaVisualizer.performOperations(dataFiles, ukubukaSchema);
    }

    @Test(expected = PipelineException.class)
    public void test_performOperations_scripts_reader_exception()
            throws PipelineException, ReaderException, WriterException,
            IOException {
        Visualization visualization = new Visualization();
        visualization.setId("foo-X");
        visualization.setLocation("foobar");
        VisualizeFlags visualizeFlags = new VisualizeFlags();
        visualizeFlags.setHeight("512");
        visualizeFlags.setWidth("512");
        visualizeFlags.setOptions("foo");
        visualization.setFlags(visualizeFlags);

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<Visualization> visualizations = new ArrayList<>(
                Arrays.asList(visualization));

        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));

        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setVisualizations(visualizations);

        Mockito.when(scriptsReader.createHTML(Mockito.anyString()))
                .thenThrow(new ReaderException("foo"));
        Mockito.doNothing().when(writer).writeFile(Mockito.anyString(),
                Mockito.anyString());
        Mockito.when(writer.prettyPrint(Mockito.anyString())).thenReturn("foo");
        Mockito.when(writer.writeJSON(Mockito.anyListOf(String.class),
                Mockito.anyListOf(FileRecord.class)))
                .thenReturn(new JSONArray());

        ukubukaVisualizer.performOperations(dataFiles, ukubukaSchema);
    }
}
