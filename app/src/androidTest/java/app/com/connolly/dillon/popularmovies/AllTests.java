package app.com.connolly.dillon.popularmovies;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

import app.com.connolly.dillon.popularmovies.data.TestDb;
import app.com.connolly.dillon.popularmovies.data.TestMovieContract;
import app.com.connolly.dillon.popularmovies.data.TestMovieProvider;
import app.com.connolly.dillon.popularmovies.data.TestUriMatcher;
import app.com.connolly.dillon.popularmovies.data.TestUtilities;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class AllTests extends TestSuite {

    public static Test suite() {
        return new TestSuiteBuilder(AllTests.class)
                .includeAllPackagesUnderHere()
                .build();
    }

    public AllTests() {
        super();
    }
}