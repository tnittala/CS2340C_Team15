package com.example.a2340project;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import com.example.a2340project.views.Destination;

import java.text.SimpleDateFormat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private Destination destination;
    private MockEditText startDateCalc;
    private MockEditText endDateCalc;
    private SimpleDateFormat dateFormat;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        destination = new Destination();
        startDateCalc = new MockEditText();
        endDateCalc = new MockEditText();
        dateFormat = new SimpleDateFormat("MM/dd/yy");
        setPrivateField(destination, "startDateCalc", startDateCalc);
        setPrivateField(destination, "endDateCalc", endDateCalc);
        setPrivateField(destination, "destination", destination);
        setPrivateField(destination, "resultText", dateFormat);
    }

    @Test
    public void testCalculateDaysBetween_sameDates() {
        String startDate = "10/01/24";
        String endDate = "10/01/24";
        long daysBetween = destination.calculateDaysBetween(startDate, endDate, dateFormat);
        assertEquals(0, daysBetween);
    }

    @Test
    public void testCalculateDaysBetween_differentDates() {
        String startDate = "10/01/24";
        String endDate = "10/10/24";
        long daysBetween = destination.calculateDaysBetween(startDate, endDate, dateFormat);
        assertEquals(9, daysBetween);
    }

    private void setPrivateField(Object object, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    // Mock class for EditText to simulate getText() and setText()
    private static class MockEditText {
        private String text = "";

        public void setText(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }
    }
}
