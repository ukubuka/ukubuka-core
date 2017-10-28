package com.ukubuka.core.execute;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.exception.PipelineException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.UkubukaSchema;
import com.ukubuka.core.operations.extract.UkubukaExtractor;
import com.ukubuka.core.operations.load.UkubukaLoader;
import com.ukubuka.core.operations.transform.UkubukaTransformer;
import com.ukubuka.core.schema.UkubukaSchemaReader;

/**
 * Ukubuka Executor Services Test
 * 
 * @author agrawroh
 * @version v1.0
 */
public class UkubukaExecutorServiceTest {

    /**************************** Dependency Mocks ***************************/
    @Mock
    private UkubukaSchemaReader ukubukaSchemaReader;

    @Mock
    private UkubukaExtractor ukubukaExtractor;

    @Mock
    private UkubukaTransformer ukubukaTransformer;

    @Mock
    private UkubukaLoader ukubukaLoader;

    @Mock
    private UkubukaSchema ukubukaSchema;

    @InjectMocks
    private UkubukaExecutorService ukubukaExecutorService;

    /**************************** Initialize Mocks ***************************/
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /******************************** Test(s) ********************************/
    @Test
    public void test_execute_success()
            throws ParserException, PipelineException {
        Mockito.when(ukubukaSchemaReader.readSchema(Mockito.anyString()))
                .thenReturn(new UkubukaSchema());

        Mockito.doNothing().when(ukubukaExtractor).performOperations(
                Mockito.anyMapOf(String.class, FileContents.class),
                Mockito.any(UkubukaSchema.class));

        Mockito.doNothing().when(ukubukaTransformer).performOperations(
                Mockito.anyMapOf(String.class, FileContents.class),
                Mockito.any(UkubukaSchema.class));

        Mockito.doNothing().when(ukubukaLoader).performOperations(
                Mockito.anyMapOf(String.class, FileContents.class),
                Mockito.any(UkubukaSchema.class));

        ukubukaExecutorService.execute("foobar");

        Mockito.when(ukubukaSchemaReader.readSchema(Mockito.anyString()))
                .thenReturn(new UkubukaSchema());

        Mockito.verify(ukubukaExtractor, Mockito.times(1)).performOperations(
                Mockito.anyMapOf(String.class, FileContents.class),
                Mockito.any(UkubukaSchema.class));

        Mockito.verify(ukubukaTransformer, Mockito.times(1)).performOperations(
                Mockito.anyMapOf(String.class, FileContents.class),
                Mockito.any(UkubukaSchema.class));

        Mockito.verify(ukubukaLoader, Mockito.times(1)).performOperations(
                Mockito.anyMapOf(String.class, FileContents.class),
                Mockito.any(UkubukaSchema.class));
    }

    @Test(expected = ParserException.class)
    public void test_execute_readSchema_failure()
            throws ParserException, PipelineException {
        Mockito.when(ukubukaSchemaReader.readSchema(Mockito.anyString()))
                .thenThrow(new ParserException("foo"));

        Mockito.doNothing().when(ukubukaExtractor).performOperations(
                Mockito.anyMapOf(String.class, FileContents.class),
                Mockito.any(UkubukaSchema.class));

        Mockito.doNothing().when(ukubukaTransformer).performOperations(
                Mockito.anyMapOf(String.class, FileContents.class),
                Mockito.any(UkubukaSchema.class));

        Mockito.doNothing().when(ukubukaLoader).performOperations(
                Mockito.anyMapOf(String.class, FileContents.class),
                Mockito.any(UkubukaSchema.class));

        ukubukaExecutorService.execute("foobar");
    }

    @Test(expected = PipelineException.class)
    public void test_execute_extract_failure()
            throws ParserException, PipelineException {
        Mockito.when(ukubukaSchemaReader.readSchema(Mockito.anyString()))
                .thenReturn(new UkubukaSchema());

        Mockito.doThrow(new PipelineException("foo")).when(ukubukaExtractor)
                .performOperations(
                        Mockito.anyMapOf(String.class, FileContents.class),
                        Mockito.any(UkubukaSchema.class));

        Mockito.doNothing().when(ukubukaTransformer).performOperations(
                Mockito.anyMapOf(String.class, FileContents.class),
                Mockito.any(UkubukaSchema.class));

        Mockito.doNothing().when(ukubukaLoader).performOperations(
                Mockito.anyMapOf(String.class, FileContents.class),
                Mockito.any(UkubukaSchema.class));

        ukubukaExecutorService.execute("foobar");
    }

    @Test(expected = PipelineException.class)
    public void test_execute_transform_failure()
            throws ParserException, PipelineException {
        Mockito.when(ukubukaSchemaReader.readSchema(Mockito.anyString()))
                .thenReturn(new UkubukaSchema());

        Mockito.doNothing().when(ukubukaExtractor).performOperations(
                Mockito.anyMapOf(String.class, FileContents.class),
                Mockito.any(UkubukaSchema.class));

        Mockito.doThrow(new PipelineException("foo")).when(ukubukaTransformer)
                .performOperations(
                        Mockito.anyMapOf(String.class, FileContents.class),
                        Mockito.any(UkubukaSchema.class));

        Mockito.doNothing().when(ukubukaLoader).performOperations(
                Mockito.anyMapOf(String.class, FileContents.class),
                Mockito.any(UkubukaSchema.class));

        ukubukaExecutorService.execute("foobar");
    }

    @Test(expected = PipelineException.class)
    public void test_execute_load_failure()
            throws ParserException, PipelineException {
        Mockito.when(ukubukaSchemaReader.readSchema(Mockito.anyString()))
                .thenReturn(new UkubukaSchema());

        Mockito.doNothing().when(ukubukaExtractor).performOperations(
                Mockito.anyMapOf(String.class, FileContents.class),
                Mockito.any(UkubukaSchema.class));

        Mockito.doNothing().when(ukubukaTransformer).performOperations(
                Mockito.anyMapOf(String.class, FileContents.class),
                Mockito.any(UkubukaSchema.class));

        Mockito.doThrow(new PipelineException("foo")).when(ukubukaLoader)
                .performOperations(
                        Mockito.anyMapOf(String.class, FileContents.class),
                        Mockito.any(UkubukaSchema.class));

        ukubukaExecutorService.execute("foobar");
    }
}
