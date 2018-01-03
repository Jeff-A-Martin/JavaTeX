package JavaTeXTest.core;

import java.io.File;

/**
 * A singleton class used to setup for test cases exactly once. Each testclass should have the following line first
 * in its setUpBeforeClass method:
 *      TestSetup.getInstance().setup();
 */
public class TestSetup {

    public static final String TEMPORARY_OUTPUT_DIRECTORY_NAME = "test/temp/";
    public static final String PERSISTENT_OUTPUT_DIRECTORY_NAME = "test/actual/";

    /* The single instance of TestSetup. */
    private static TestSetup instance;

    /* Enforces that setup executed only once. */
    private boolean setup;

    /**
     * Private constructor. TestSetup can not be instantiated outside of the class.
     */
    private TestSetup(){
        this.setup = false;
    }

    /**
     * This function returns an instance of the TestSetup class (enforces the singleton design).
     * @return The universal instance of the TestSetup class.
     */
    public static TestSetup getInstance(){
        if(instance == null){
            instance = new TestSetup();
        }
        return instance;
    }

    /**
     * This function is used to setup for the test cases. It creates a directory to hold any files generated during
     * testing; if the directory already exists, it is erased.
     * @return A boolean indicating whether the setup was successful.
     */
    public boolean setup(){
        /* If setup was already successfully executed, return. */
        if(setup) return true;

        boolean successful = true;

        File temporaryDirectory = new File(TEMPORARY_OUTPUT_DIRECTORY_NAME);
        File persistentDirectory = new File(PERSISTENT_OUTPUT_DIRECTORY_NAME);

        /* Delete an old instance of the directory if it existed */
        if(temporaryDirectory.exists()) if(!deleteDirectory(temporaryDirectory)) successful = false;
        if(persistentDirectory.exists()) if(!deleteDirectory(persistentDirectory)) successful = false;
        /* Create a new instance of the directory */
        if(!temporaryDirectory.mkdirs()) successful = false;
        if(!persistentDirectory.mkdirs()) successful =false;

        /* If this setup was successful, prevent future setups. */
        if(successful) setup = true;

        return successful;
    }

    /**
     * This function is used to delete a (non-empty) directory.
     * @param dir The directory / file to be deleted
     * @return A boolean indicating whether the directory was successfully deleted.
     */
    private static boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(children[i]);
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

}

