package com.ukubuka.core.operations.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ukubuka.core.evaluator.UkubukaExpressionEvaluator;
import com.ukubuka.core.exception.TransformException;
import com.ukubuka.core.model.FileRecord;
import com.ukubuka.core.model.TransformOperation;
import com.ukubuka.core.model.UkubukaSchema.TransformOperations;

/**
 * Ukubuka Transformer Test
 * 
 * @author agrawroh
 * @version v1.0
 */
public class UkubukaTransformerTest {

    /**************************** Dependency Mocks ***************************/
    @Mock
    private UkubukaExpressionEvaluator expressionEvaluator;

    @InjectMocks
    private UkubukaTransformer ukubukaTransformer;

    /**************************** Initialize Mocks ***************************/
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /******************************** Test(s) ********************************/
    @Test
    public void test_performOperations_add_success() throws TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.ADD);
        transformOperation.setSource("foobar");
        transformOperation.setTarget("new java.util.Random().nextInt(100)");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Mockito.when(expressionEvaluator.evaluate(Mockito.any(FileRecord.class),
                Mockito.anyString())).thenReturn(0xCafeBabe);

        ukubukaTransformer.performOperations(fileHeader, fileRecords,
                operationsList);

        Mockito.verify(expressionEvaluator, Mockito.times(2))
                .evaluate(Mockito.any(FileRecord.class), Mockito.anyString());
        assertEquals(3, fileHeader.size());
        assertTrue(fileHeader.contains("foobar"));
    }

    @Test
    public void test_performOperations_new_success() throws TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.NEW);
        transformOperation.setSource("foobar");
        transformOperation.setTarget("new String('barfoo')");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Mockito.when(expressionEvaluator.evaluate(Mockito.any(FileRecord.class),
                Mockito.anyString())).thenReturn(0xCafeBabe);

        ukubukaTransformer.performOperations(fileHeader, fileRecords,
                operationsList);

        Mockito.verify(expressionEvaluator, Mockito.times(2))
                .evaluate(Mockito.any(FileRecord.class), Mockito.anyString());
        assertEquals(3, fileHeader.size());
        assertTrue(fileHeader.contains("foobar"));
    }

    @Test
    public void test_performOperations_delete_success()
            throws TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.DELETE);
        transformOperation.setSource("foo");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        ukubukaTransformer.performOperations(fileHeader, fileRecords,
                operationsList);

        assertEquals(1, fileHeader.size());
        assertFalse(fileHeader.contains("foo"));
    }

    @Test(expected = TransformException.class)
    public void test_performOperations_delete_failure()
            throws TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.DELETE);
        transformOperation.setSource("foobar");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        ukubukaTransformer.performOperations(fileHeader, fileRecords,
                operationsList);
    }

    @Test
    public void test_performOperations_remove_success()
            throws TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.REMOVE);
        transformOperation.setSource("bar");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        ukubukaTransformer.performOperations(fileHeader, fileRecords,
                operationsList);

        assertEquals(1, fileHeader.size());
        assertFalse(fileHeader.contains("bar"));
    }

    @Test(expected = TransformException.class)
    public void test_performOperations_remove_failure()
            throws TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.REMOVE);
        transformOperation.setSource("foobar");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        ukubukaTransformer.performOperations(fileHeader, fileRecords,
                operationsList);
    }

    @Test
    public void test_performOperations_rename_success()
            throws TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.RENAME);
        transformOperation.setSource("foo");
        transformOperation.setTarget("foobar");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        ukubukaTransformer.performOperations(fileHeader, fileRecords,
                operationsList);

        assertEquals(2, fileHeader.size());
        assertFalse(fileHeader.contains("foo"));
        assertTrue(fileHeader.contains("foobar"));
    }

    @Test(expected = TransformException.class)
    public void test_performOperations_rename_failure()
            throws TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.RENAME);
        transformOperation.setSource("foobar");
        transformOperation.setTarget("barfoo");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        ukubukaTransformer.performOperations(fileHeader, fileRecords,
                operationsList);
    }

    @Test
    public void test_performOperations_swap_success()
            throws TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.SWAP);
        transformOperation.setSource("foo");
        transformOperation.setTarget("bar");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        ukubukaTransformer.performOperations(fileHeader, fileRecords,
                operationsList);

        assertEquals(2, fileHeader.size());
        assertEquals("bar", fileHeader.get(0));
        assertEquals("foo", fileHeader.get(1));
    }

    @Test(expected = TransformException.class)
    public void test_performOperations_swap_failure()
            throws TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.SWAP);
        transformOperation.setSource("foobar");
        transformOperation.setTarget("barfoo");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        ukubukaTransformer.performOperations(fileHeader, fileRecords,
                operationsList);
    }

    @Test
    public void test_performOperations_move_success()
            throws TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.MOVE);
        transformOperation.setSource("bar");
        transformOperation.setTarget("0");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        ukubukaTransformer.performOperations(fileHeader, fileRecords,
                operationsList);

        assertEquals(2, fileHeader.size());
        assertEquals("bar", fileHeader.get(0));
        assertEquals("foo", fileHeader.get(1));
    }

    @Test(expected = TransformException.class)
    public void test_performOperations_move_failure()
            throws TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.MOVE);
        transformOperation.setSource("foobar");
        transformOperation.setTarget("0");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        ukubukaTransformer.performOperations(fileHeader, fileRecords,
                operationsList);
    }

    @Test(expected = NullPointerException.class)
    public void test_performOperations_invalid_operation()
            throws TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setSource("foo");
        transformOperation.setTarget("0");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        ukubukaTransformer.performOperations(fileHeader, fileRecords,
                operationsList);
    }

    @Test(expected = TransformException.class)
    public void test_performOperations_unsupported_operation()
            throws TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.NONE);
        transformOperation.setSource("foo");
        transformOperation.setTarget("new java.util.Random(100).nextInt(50)");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        ukubukaTransformer.performOperations(fileHeader, fileRecords,
                operationsList);
    }
}
