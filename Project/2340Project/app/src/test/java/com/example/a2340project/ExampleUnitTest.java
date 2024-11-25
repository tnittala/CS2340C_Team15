package com.example.a2340project;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.graphics.Color;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

import com.example.a2340project.views.Destination;
import com.example.a2340project.views.Logistics;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.example.a2340project.views.LoginActivity;


import java.text.SimpleDateFormat;

import com.example.a2340project.views.Destination;
import com.example.a2340project.views.LoginActivity;
import com.example.a2340project.model.FirebaseDatabaseSingleton;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(AndroidJUnit4.class)

public class ExampleUnitTest {
    private Destination destinationActivity;
    private MockEditText startDateCalc;
    private MockEditText endDateCalc;
    private MockEditText durationCalc;
    private MockTextView resultText;
    private Destination destination;
    private SimpleDateFormat sdf;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        destinationActivity = new Destination();

        // Initialize mock fields
        startDateCalc = new MockEditText();
        endDateCalc = new MockEditText();
        durationCalc = new MockEditText();
        resultText = new MockTextView();

        // Use reflection to set private fields
        setPrivateField(destinationActivity, "startDateCalc", startDateCalc);
        setPrivateField(destinationActivity, "endDateCalc", endDateCalc);
        setPrivateField(destinationActivity, "durationCalc", durationCalc);
        setPrivateField(destinationActivity, "resultText", resultText);

        destination = new Destination();
        sdf = new SimpleDateFormat("MM/dd/yy");
    }

    @Test
    public void testCalculateVacationTimeWithStartAndEndDate() {
        // Arrange
        startDateCalc.setText("10/01/24");
        endDateCalc.setText("10/10/24");
        durationCalc.setText("");  // Leave duration empty
        durationCalc.setText("");


        private Logistics logisticsActivity = new Logistics();


        @Test
        public void testGraphTripsEntries () {
            logisticsActivity.graphTrips();

            BarData barData = logisticsActivity.barChart.getData();
            BarDataSet dataSet = (BarDataSet) barData.getDataSetByIndex(0);

            // Verify the number of entries
            assertEquals(2, dataSet.getEntryCount());

            // Verify the values of the entries
            assertEquals(4, dataSet.getEntryForIndex(0).getY(), 0);
            assertEquals(3, dataSet.getEntryForIndex(1).getY(), 0);

            // Verify the colors
            assertEquals(Color.RED, dataSet.getColor(0));
            assertEquals(Color.BLUE, dataSet.getColor(1));
        }


        private Destination destinationActivity;
        private MockEditText startDateCalc;
        private MockEditText endDateCalc;
        private MockEditText durationCalc;
        private MockTextView resultText;

        // Act
        destinationActivity.calculateVacationTime();

        // Assert
        assertEquals("9", durationCalc.getText());
        assertEquals("9 days", resultText.getText());
    }


    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        destinationActivity = new Destination();

        startDateCalc = new MockEditText();
        endDateCalc = new MockEditText();
        durationCalc = new MockEditText();
        resultText = new MockTextView();

        setPrivateField(destinationActivity, "startDateCalc", startDateCalc);
        setPrivateField(destinationActivity, "endDateCalc", endDateCalc);
        setPrivateField(destinationActivity, "durationCalc", durationCalc);
        setPrivateField(destinationActivity, "resultText", resultText);

        destination = new Destination();
        sdf = new SimpleDateFormat("MM/dd/yy");
    }

    @Test
    public void testCalculateVacationTimeWithStartAndEndDate() {
        startDateCalc.setText("10/01/24");
        endDateCalc.setText("10/10/24");
        durationCalc.setText("");

        destinationActivity.calculateVacationTime();

        assertEquals("9", durationCalc.getText());
        assertEquals("9 days", resultText.getText());
    }

    @Test
    public void singleton_isCorrect() {

        FirebaseDatabaseSingleton instance1 = FirebaseDatabaseSingleton.getInstance();
        FirebaseDatabaseSingleton instance2 = FirebaseDatabaseSingleton.getInstance();

        // Check if both instances are the same
        assertEquals(instance1, instance2);
    }

    @Test
    public void testCalculateVacationTimeWithEndDateAndDuration() {
        startDateCalc.setText("");
        endDateCalc.setText("10/10/24");
        durationCalc.setText("5");

        destinationActivity.calculateVacationTime();

        assertEquals("10/05/24", startDateCalc.getText());
        assertEquals("5 days", resultText.getText());
    }

    @Test
    public void testCalculateDaysBetweenValidDates() {
        String startDate = "10/01/24";
        String endDate = "10/10/24";

        long daysBetween = destination.calculateDaysBetween(startDate, endDate, sdf);

        assertEquals(9, daysBetween);
    }

    @Test
    public void testIsValidDateWithValidDate() {
        String validDate = "12/31/24";

        assertTrue(destination.isValidDate(validDate));
    }

    @Test
    public void testIsValidDateWithInvalidDate() {
        String invalidDate = "13/01/24";  // Invalid month

        assertFalse(destination.isValidDate(invalidDate));
    }

    private void setPrivateField(Object object, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    private static class MockEditText {
        private String text = "";

        public void setText(String text) {
            this.text = text;
        }

        @Test
        public void testCalculateVacationTimeWithEndDateAndDuration() {
            // Arrange
            startDateCalc.setText("");  // Leave start date empty
            startDateCalc.setText("");
            endDateCalc.setText("10/10/24");
            durationCalc.setText("5");

            // Act
            destinationActivity.calculateVacationTime();

            // Assert
            assertEquals("10/05/24", startDateCalc.getText());
            assertEquals("5 days", resultText.getText());
        }

        // Helper method to set private fields using reflection
        @Test
        public void testCalculateDaysBetweenValidDates() {
            String startDate = "10/01/24";
            String endDate = "10/10/24";
        }

        private static class MockTextView {
            private String text = "";
            long daysBetween = destination.calculateDaysBetween(startDate, endDate, sdf);

            assertEquals(9,daysBetween);
        }

        @Test
        public void testCalculateDaysBetweenSameDates() {
            String startDate = "10/01/24";
            String endDate = "10/01/24";

            long daysBetween = destination.calculateDaysBetween(startDate, endDate, sdf);

            assertEquals(0, daysBetween);
        }

        @Test
        public void testIsValidDateWithValidDate() {
            String validDate = "12/31/24";

            assertTrue(destination.isValidDate(validDate));
        }


        @Test
        public void testIsValidDateWithInvalidDate() {
            String invalidDate = "13/01/24";  // Invalid month

            assertFalse(destination.isValidDate(invalidDate));
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

        }
    }
}