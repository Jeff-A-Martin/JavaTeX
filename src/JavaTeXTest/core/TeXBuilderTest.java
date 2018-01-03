package JavaTeXTest.core;

import JavaTeX.core.TeXBuilder;
import JavaTeX.core.TeXFile;
import JavaTeX.core.TeXString;
import org.junit.*;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class is used to test the TeXBuilder class. Unfortunately, it is not an easy task to programmatically verify the
 * contents of a binary file. Thus, because TeXBuilder generates pdf and dvi files some manual verification is
 * required as descrribed in test/verification.md.
 */
public class TeXBuilderTest {

    private static final String outputDir = TestSetup.TEMPORARY_OUTPUT_DIRECTORY_NAME;
    private static final String persistentOutputDir = TestSetup.PERSISTENT_OUTPUT_DIRECTORY_NAME;

    private static final String simpleTeXSample = "Hello World";
    private static final String complexTeXSample =
            "\\hrule" +
            "\\vskip 1in" +
            "\\centerline{\\bf A SHORT STORY}" +
            "\\vskip 6pt" +
            "\\centerline{\\sl by A. U. Thor}" +
            "\\vskip .5cm" +
            "Once upon a time, in a distant " +
            "galaxy called \\\"O\\\"o\\c c, " +
            "there lived a computer " +
            "named R.~J. Drofnats. " +
            "\\par Mr.~Drofnats---or ‘‘R. J.,’’ as " +
            "he preferred to be called--- " +
            "was happiest when he was at work " +
            "typesetting beautiful documents." +
            "\\vskip 1in" +
            "\\hrule" +
            "\\vfill\\eject";
    private static final String invalidTeXSample =
            "Some valid TeX followed by an invalid command ( \\invCMD ). And finally more content";
    private static final String simpleTeXFile = outputDir + "simple.tex";
    private static final String complexTeXFile = outputDir + "complex.tex";
    private static final String invalidTeXFile = outputDir + "invalid.tex";

    /**
     * This function is called before any TeXBuilder test cases are ran. It verifies that the testing environment is
     * setup.
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TestSetup.getInstance().setup();
    }

    /**
     * This function tests TeXBuilder.build(TeXString).
     * @throws Exception If the test case encountered an unexpected exception.
     */
    @Test
    public void buildTeXString() throws Exception {
        buildTeXString1();
        buildTeXString2();
        buildTeXString3();
        buildTeXString4();
        buildTeXString5();
        buildTeXString6();
        buildTeXString7(); // generate manual verification files.
    }

    /**
     * This function tests: The log file is kept and deleted when intended.
     * @throws Exception If the test case encountered an unexpected exception.
     */
    private void buildTeXString1() throws Exception {
        /* Create Test Fixtures. */
        TeXBuilder builderWithoutLog = new TeXBuilder(false, false, false, null, outputDir);
        TeXBuilder builderWithLog = new TeXBuilder(true, false, false, null, outputDir);
        TeXString testString = new TeXString(simpleTeXSample);
        String fileName = outputDir + TeXBuilder.DEFAULT_OUTPUT_FILE_NAME + ".log";
        File outputFile = new File(fileName);

        /* Verify that the log file does not exist. */
        assertFalse(outputFile.exists());

        /* Build with the BuilderWithoutLog and verify that log file does not exist. */
        assertTrue(builderWithoutLog.build(testString));
        assertFalse(outputFile.exists());

        /* Build with the BuilderWithLog and verify that log file does exist. */
        assertTrue(builderWithLog.build(testString));
        assertTrue(outputFile.exists());

        /* Clean up by removing the log file, and verify that it was removed. */
        assertTrue(outputFile.delete());
        assertFalse(outputFile.exists());
    }

    /**
     * This function tests: The dvi file is kept and deleted when intended.
     * @throws Exception If the test case encountered an unexpected exception.
     */
    private void buildTeXString2() throws Exception {
        /* Create Test Fixtures. */
        TeXBuilder builderWithoutDVI = new TeXBuilder(false, false, false, null, outputDir);
        TeXBuilder builderWithDVI = new TeXBuilder(false, true, false, null, outputDir);
        TeXString testString = new TeXString(simpleTeXSample);
        String fileName = outputDir + TeXBuilder.DEFAULT_OUTPUT_FILE_NAME + ".dvi";
        File outputFile = new File(fileName);

        /* Verify that the dvi file does not exist. */
        assertFalse(outputFile.exists());

        /* Build with the BuilderWithoutLog and verify that dvi file does not exist. */
        assertTrue(builderWithoutDVI.build(testString));
        assertFalse(outputFile.exists());

        /* Build with the BuilderWithLog and verify that dvi file does exist. */
        assertTrue(builderWithDVI.build(testString));
        assertTrue(outputFile.exists());

        /* Clean up by removing the dvi file, and verify that it was removed. */
        assertTrue(outputFile.delete());
        assertFalse(outputFile.exists());
    }

    /**
     * This function tests: The pdf file is generated when intended.
     * @throws Exception If the test case encountered an unexpected exception.
     */
    private void buildTeXString3() throws Exception {
        /* Create Test Fixtures. */
        TeXBuilder builderWithoutPDF = new TeXBuilder(false, false, false, null, outputDir);
        TeXBuilder builderWithPDF = new TeXBuilder(false, false, true, null, outputDir);
        TeXString testString = new TeXString(simpleTeXSample);
        String fileName = outputDir + TeXBuilder.DEFAULT_OUTPUT_FILE_NAME + ".pdf";
        File outputFile = new File(fileName);

        /* Verify that the pdf file does not exist. */
        assertFalse(outputFile.exists());

        /* Build with the BuilderWithoutLog and verify that pdf file does not exist. */
        assertTrue(builderWithoutPDF.build(testString));
        assertFalse(outputFile.exists());

        /* Build with the BuilderWithLog and verify that pdf file does exist. */
        assertTrue(builderWithPDF.build(testString));
        assertTrue(outputFile.exists());

        /* Clean up by removing the pdf file, and verify that it was removed. */
        assertTrue(outputFile.delete());
        assertFalse(outputFile.exists());
    }

    /**
     * This function tests: The output file name is used.
     * @throws Exception If the test case encountered an unexpected exception.
     */
    private void buildTeXString4() throws Exception {
        /* Create Test Fixtures. */
        String name = "validName";
        TeXBuilder builder = new TeXBuilder(true, true, true, name, outputDir);
        TeXString testString = new TeXString(simpleTeXSample);
        String LogFileName = outputDir + name + ".log";
        String DVIFileName = outputDir + name + ".dvi";
        String PDFFileName = outputDir + name + ".pdf";
        File LogFile = new File(LogFileName);
        File DVIFile = new File(DVIFileName);
        File PDFFile = new File(PDFFileName);

        /* Assert that the valid files do not exist */
        assertFalse(LogFile.exists());
        assertFalse(DVIFile.exists());
        assertFalse(PDFFile.exists());

        /* Build with the BuilderWithValidFileName and verify that all valid files exist. */
        assertTrue(builder.build(testString));
        assertTrue(LogFile.exists());
        assertTrue(DVIFile.exists());
        assertTrue(PDFFile.exists());

        /* Clean up by removing all valid files, and verify that they were removed. */
        assertTrue(LogFile.delete());
        assertTrue(DVIFile.delete());
        assertTrue(PDFFile.delete());
        assertFalse(LogFile.exists());
        assertFalse(DVIFile.exists());
        assertFalse(PDFFile.exists());
    }

    /**
     * This function tests: The output file path is used.
     * @throws Exception If the test case encountered an unexpected exception.
     */
    private void buildTeXString5() throws Exception {
        /* Create Test Fixtures. */
        String dir = outputDir + "newDir/";
        File DirFile = new File(dir);
        DirFile.mkdir();
        TeXBuilder builder = new TeXBuilder(true, true, true, null, dir);
        TeXString testString = new TeXString(simpleTeXSample);
        String LogFileName = dir + TeXBuilder.DEFAULT_OUTPUT_FILE_NAME + ".log";
        String DVIFileName = dir + TeXBuilder.DEFAULT_OUTPUT_FILE_NAME + ".dvi";
        String PDFFileName = dir + TeXBuilder.DEFAULT_OUTPUT_FILE_NAME + ".pdf";
        File LogFile = new File(LogFileName);
        File DVIFile = new File(DVIFileName);
        File PDFFile = new File(PDFFileName);

        /* Assert that the valid files do not exist */
        assertFalse(LogFile.exists());
        assertFalse(DVIFile.exists());
        assertFalse(PDFFile.exists());

        /* Build with the BuilderWithValidFileName and verify that all valid files exist. */
        assertTrue(builder.build(testString));
        assertTrue(LogFile.exists());
        assertTrue(DVIFile.exists());
        assertTrue(PDFFile.exists());

        /* Clean up by removing all valid files, and verify that they were removed. */
        assertTrue(LogFile.delete());
        assertTrue(DVIFile.delete());
        assertTrue(PDFFile.delete());
        assertTrue(DirFile.delete());
        assertFalse(DirFile.exists());
        assertFalse(LogFile.exists());
        assertFalse(DVIFile.exists());
        assertFalse(PDFFile.exists());
    }

    /**
     * This function tests: That a null TeXString or content causes the build to return false.
     * @throws Exception If the test case encountered an unexpected exception.
     */
    private void buildTeXString6() throws Exception {
        /* Create Test Fixtures. */
        TeXBuilder builder = new TeXBuilder(true, false, false, null, outputDir);
        TeXString testString1 = new TeXString(null);
        TeXString testString2 = null;

        /* Assert that both builds fail */
        assertFalse(builder.build(testString1));
        assertFalse(builder.build(testString2));
    }

    /**
     * This function tests: That TeX source is built properly. Unfortunately because binary files can't be (easily)
     * programmatically verified, the files must be manually verified (See test/verification.md for details).
     * @throws Exception If the test case encountered an unexpected exception.
     */
    private void buildTeXString7() throws Exception {
        /* Create Test Fixtures. */
        TeXBuilder builder1 = new TeXBuilder(false, false, true, "buildTexStringTest1", persistentOutputDir);
        TeXBuilder builder2 = new TeXBuilder(false, false, true, "buildTexStringTest2", persistentOutputDir);
        TeXBuilder builder3 = new TeXBuilder(false, false, true, "buildTexStringTest3", persistentOutputDir);
        TeXString simpleValidString = new TeXString(simpleTeXSample);
        TeXString complexValidString = new TeXString(complexTeXSample);
        TeXString invalidString = new TeXString(invalidTeXSample);

        /* Build output files */
        assertTrue(builder1.build(simpleValidString));
        assertTrue(builder2.build(complexValidString));
        assertFalse(builder3.build(invalidString));
    }

    /**
     * This function tests TeXBuilder.build(TeXFile).
     * @throws Exception If the test case encountered an unexpected exception.
     */
    @Test
    public void buildTeXFile() throws Exception {
        /* Write the three sample TeX Strings to File */
        PrintWriter simpleOut = new PrintWriter(simpleTeXFile);
        PrintWriter complexOut = new PrintWriter(complexTeXFile);
        PrintWriter invalidOut = new PrintWriter(invalidTeXFile);
        simpleOut.println(simpleTeXSample);
        complexOut.println(complexTeXSample);
        invalidOut.println(invalidTeXSample);
        simpleOut.close();
        complexOut.close();
        invalidOut.close();

        buildTeXFile1();
        buildTeXFile2();
        buildTeXFile3();
        buildTeXFile4();
        buildTeXFile5();
        buildTeXFile6();
        buildTeXFile7(); // generate manual verification files.

        /* Remove the TeX Files */
        File simpleFile = new File(simpleTeXFile);
        File complexFile = new File(complexTeXFile);
        File invalidFile = new File(invalidTeXFile);

        assertTrue(simpleFile.delete());
        assertTrue(complexFile.delete());
        assertTrue(invalidFile.delete());
    }

    /**
     * This function tests: The log file is kept and deleted when intended.
     * @throws Exception If the test case encountered an unexpected exception.
     */
    private void buildTeXFile1() throws Exception {
        /* Create Test Fixtures. */
        TeXBuilder builderWithoutLog = new TeXBuilder(false, false, false, null, outputDir);
        TeXBuilder builderWithLog = new TeXBuilder(true, false, false, null, outputDir);
        TeXFile testFile = new TeXFile(Paths.get(simpleTeXFile));
        String fileName = outputDir + TeXBuilder.DEFAULT_OUTPUT_FILE_NAME + ".log";
        File outputFile = new File(fileName);

        /* Verify that the log file does not exist. */
        assertFalse(outputFile.exists());

        /* Build with the BuilderWithoutLog and verify that log file does not exist. */
        assertTrue(builderWithoutLog.build(testFile));
        assertFalse(outputFile.exists());

        /* Build with the BuilderWithLog and verify that log file does exist. */
        assertTrue(builderWithLog.build(testFile));
        assertTrue(outputFile.exists());

        /* Clean up by removing the log file, and verify that it was removed. */
        assertTrue(outputFile.delete());
        assertFalse(outputFile.exists());
    }

    /**
     * This function tests: The dvi file is kept and deleted when intended.
     * @throws Exception If the test case encountered an unexpected exception.
     */
    private void buildTeXFile2() throws Exception {
        /* Create Test Fixtures. */
        TeXBuilder builderWithoutDVI = new TeXBuilder(false, false, false, null, outputDir);
        TeXBuilder builderWithDVI = new TeXBuilder(false, true, false, null, outputDir);
        TeXFile testFile = new TeXFile(Paths.get(simpleTeXFile));
        String fileName = outputDir + TeXBuilder.DEFAULT_OUTPUT_FILE_NAME + ".dvi";
        File outputFile = new File(fileName);

        /* Verify that the dvi file does not exist. */
        assertFalse(outputFile.exists());

        /* Build with the BuilderWithoutLog and verify that dvi file does not exist. */
        assertTrue(builderWithoutDVI.build(testFile));
        assertFalse(outputFile.exists());

        /* Build with the BuilderWithLog and verify that dvi file does exist. */
        assertTrue(builderWithDVI.build(testFile));
        assertTrue(outputFile.exists());

        /* Clean up by removing the dvi file, and verify that it was removed. */
        assertTrue(outputFile.delete());
        assertFalse(outputFile.exists());
    }

    /**
     * This function tests: The pdf file is generated when intended.
     * @throws Exception If the test case encountered an unexpected exception.
     */
    private void buildTeXFile3() throws Exception {
        /* Create Test Fixtures. */
        TeXBuilder builderWithoutPDF = new TeXBuilder(false, false, false, null, outputDir);
        TeXBuilder builderWithPDF = new TeXBuilder(false, false, true, null, outputDir);
        TeXFile testFile = new TeXFile(Paths.get(simpleTeXFile));
        String fileName = outputDir + TeXBuilder.DEFAULT_OUTPUT_FILE_NAME + ".pdf";
        File outputFile = new File(fileName);

        /* Verify that the pdf file does not exist. */
        assertFalse(outputFile.exists());

        /* Build with the BuilderWithoutLog and verify that pdf file does not exist. */
        assertTrue(builderWithoutPDF.build(testFile));
        assertFalse(outputFile.exists());

        /* Build with the BuilderWithLog and verify that pdf file does exist. */
        assertTrue(builderWithPDF.build(testFile));
        assertTrue(outputFile.exists());

        /* Clean up by removing the pdf file, and verify that it was removed. */
        assertTrue(outputFile.delete());
        assertFalse(outputFile.exists());
    }

    /**
     * This function tests: The output file name is used.
     * @throws Exception If the test case encountered an unexpected exception.
     */
    private void buildTeXFile4() throws Exception {
        /* Create Test Fixtures. */
        String name = "validName";
        TeXBuilder builder = new TeXBuilder(true, true, true, name, outputDir);
        TeXFile testFile = new TeXFile(Paths.get(simpleTeXFile));
        String LogFileName = outputDir + name + ".log";
        String DVIFileName = outputDir + name + ".dvi";
        String PDFFileName = outputDir + name + ".pdf";
        File LogFile = new File(LogFileName);
        File DVIFile = new File(DVIFileName);
        File PDFFile = new File(PDFFileName);

        /* Assert that the valid files do not exist */
        assertFalse(LogFile.exists());
        assertFalse(DVIFile.exists());
        assertFalse(PDFFile.exists());

        /* Build with the BuilderWithValidFileName and verify that all valid files exist. */
        assertTrue(builder.build(testFile));
        assertTrue(LogFile.exists());
        assertTrue(DVIFile.exists());
        assertTrue(PDFFile.exists());

        /* Clean up by removing all valid files, and verify that they were removed. */
        assertTrue(LogFile.delete());
        assertTrue(DVIFile.delete());
        assertTrue(PDFFile.delete());
        assertFalse(LogFile.exists());
        assertFalse(DVIFile.exists());
        assertFalse(PDFFile.exists());
    }

    /**
     * This function tests: The output file path is used.
     * @throws Exception If the test case encountered an unexpected exception.
     */
    private void buildTeXFile5() throws Exception {
        /* Create Test Fixtures. */
        String dir = outputDir + "newDir/";
        File DirFile = new File(dir);
        DirFile.mkdir();
        TeXBuilder builder = new TeXBuilder(true, true, true, null, dir);
        TeXFile testFile = new TeXFile(Paths.get(simpleTeXFile));
        String LogFileName = dir + TeXBuilder.DEFAULT_OUTPUT_FILE_NAME + ".log";
        String DVIFileName = dir + TeXBuilder.DEFAULT_OUTPUT_FILE_NAME + ".dvi";
        String PDFFileName = dir + TeXBuilder.DEFAULT_OUTPUT_FILE_NAME + ".pdf";
        File LogFile = new File(LogFileName);
        File DVIFile = new File(DVIFileName);
        File PDFFile = new File(PDFFileName);

        /* Assert that the valid files do not exist */
        assertFalse(LogFile.exists());
        assertFalse(DVIFile.exists());
        assertFalse(PDFFile.exists());

        /* Build with the BuilderWithValidFileName and verify that all valid files exist. */
        assertTrue(builder.build(testFile));
        assertTrue(LogFile.exists());
        assertTrue(DVIFile.exists());
        assertTrue(PDFFile.exists());

        /* Clean up by removing all valid files, and verify that they were removed. */
        assertTrue(LogFile.delete());
        assertTrue(DVIFile.delete());
        assertTrue(PDFFile.delete());
        assertTrue(DirFile.delete());
        assertFalse(DirFile.exists());
        assertFalse(LogFile.exists());
        assertFalse(DVIFile.exists());
        assertFalse(PDFFile.exists());
    }

    /**
     * This function tests: That a null TeXFile or content causes the build to return false.
     * @throws Exception If the test case encountered an unexpected exception.
     */
    private void buildTeXFile6() throws Exception {
        /* Create Test Fixtures. */
        TeXBuilder builder = new TeXBuilder(true, false, false, null, outputDir);
        TeXFile testFile1 = new TeXFile(null);
        TeXFile testFile2 = null;

        /* Assert that both builds fail */
        assertFalse(builder.build(testFile1));
        assertFalse(builder.build(testFile2));
    }

    /**
     * This function tests: That TeX source is built properly. Unfortunately because binary files can't be (easily)
     * programmatically verified, the files must be manually verified (See test/verification.md for details).
     * @throws Exception If the test case encountered an unexpected exception.
     */
    private void buildTeXFile7() throws Exception {
        /* Create Test Fixtures. */
        TeXBuilder builder1 = new TeXBuilder(false, false, true, "buildTexFileTest1", persistentOutputDir);
        TeXBuilder builder2 = new TeXBuilder(false, false, true, "buildTexFileTest2", persistentOutputDir);
        TeXBuilder builder3 = new TeXBuilder(false, false, true, "buildTexFileTest3", persistentOutputDir);
        TeXFile simpleFile = new TeXFile(Paths.get(simpleTeXFile));
        TeXFile complexFile = new TeXFile(Paths.get(complexTeXFile));
        TeXFile invalidFile = new TeXFile(Paths.get(invalidTeXFile));

        /* Build output files */
        assertTrue(builder1.build(simpleFile));
        assertTrue(builder2.build(complexFile));
        assertFalse(builder3.build(invalidFile));
    }

}