package com.example.a2340project;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.a2340project.model.FirebaseDatabaseSingleton;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(AndroidJUnit4.class)
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        FirebaseDatabaseSingleton instance1 = FirebaseDatabaseSingleton.getInstance();
        FirebaseDatabaseSingleton instance2 = FirebaseDatabaseSingleton.getInstance();

        // Check if both instances are the same
        assertEquals(instance1, instance2);

    }
}