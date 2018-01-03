# Verifying the Functionality of JavaTeX

JavaTeX is used to generate dvi and pdf documents, which unfortunately are difficult to programatically verify. Although their are many automated test cases JavaTeX also requires manual test cases; these are described below.

## Manual Verification
After running the JUnit test cases, verify that the contents of the test/expected directory are the same as the test/actual directory. Below the individual files in each of these directories is linked to a test case, thus if there is an error in a file the test case that failed will be known. 

#### TeXBuilder.build(TeXString)
1. buildTexStringTest1.pdf
2. buildTexStringTest2.pdf
3. buildTexStringTest3.pdf

#### TeXBuilder.build(TeXFile)
1. buildTexFileTest1.pdf
2. buildTexFileTest2.pdf
3. buildTexFileTest3.pdf
