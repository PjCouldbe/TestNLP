package preparing_test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses( { DataReaderFromFileTest.class, DataReaderFromDirectory.class, PathExtractorTest.class } )
@RunWith( Suite.class )
public class DataReaderTestSuite {

}