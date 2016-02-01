package app.com.connolly.dillon.popularmovies.fragments;

import android.test.AndroidTestCase;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Dillon Connolly on 1/18/2016.
 */
public class TrailerFragmentTest extends AndroidTestCase {

    public void testStringParse(){
        TrailerFragment testFragment = new TrailerFragment();
        String testString_0 = "NT_TAG";
        String testString_1 = "test";
        String testString_2 = "test|test";
        String testString_3 = "test|test";
        String testString_4 = "test|test|test";

        ArrayList<String> testList = testFragment.parseTrailerString(testString_0);
        assertEquals(testString_0, testList.get(0));

        testList.clear();

        testList = testFragment.parseTrailerString(testString_1);
        assertEquals(testString_1, testList.get(0));

        testList.clear();

        testList = testFragment.parseTrailerString(testString_2);

        for(String temp: testList){
            assertEquals("test",temp);
        }

        testList.clear();

        testList = testFragment.parseTrailerString(testString_3);
        for(String temp: testList){
            assertEquals("test",temp);
        }

        testList.clear();

        testList = testFragment.parseTrailerString(testString_4);
        for(String temp: testList){
            assertEquals("test",temp);
        }

        testList.clear();
    }


    public void testImgURLBuild(){
        TrailerFragment testFragment = new TrailerFragment();
        String testImgUrl = "GViBkwbyd0Q|im5as97YeVg";
        ArrayList<String> testArrayList = testFragment.parseTrailerString(testImgUrl);

        testArrayList = testFragment.buildImgUrl(testArrayList);
        assertEquals("http://img.youtube.com/vi/GViBkwbyd0Q/0.jpg", testArrayList.get(0));
        assertEquals("http://img.youtube.com/vi/im5as97YeVg/0.jpg", testArrayList.get(1));
    }
}
