package com.example.a2340project;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import com.example.a2340project.views.Destination;

import static org.junit.Assert.*;
import com.example.a2340project.views.Destination;

import java.text.SimpleDateFormat;


public class ExampleUnitTest {

    private Destination destinationActivity;
    private MockEditText startDateCalc;
    private MockEditText endDateCalc;
    private MockEditText durationCalc;
    private MockTextView resultText;

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
    }

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
    public void testCalculateVacationTimeWithStartAndEndDate() {
        // Arrange
        startDateCalc.setText("10/01/24");
        endDateCalc.setText("10/10/24");
        durationCalc.setText("");  // Leave duration empty

        // Act
        destinationActivity.calculateVacationTime();

        // Assert
        assertEquals("9", durationCalc.getText());
        assertEquals("9 days", resultText.getText());
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

    @Test
    public void testCalculateVacationTimeWithEndDateAndDuration() {
        // Arrange
        startDateCalc.setText("");  // Leave start date empty
        endDateCalc.setText("10/10/24");
        durationCalc.setText("5");

        // Act
        destinationActivity.calculateVacationTime();

        // Assert
        assertEquals("10/05/24", startDateCalc.getText());
        assertEquals("5 days", resultText.getText());
    }

    // Helper method to set private fields using reflection
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

    // Mock class for TextView to simulate getText() and setText()
    private static class MockTextView {
        private String text = "";

        public void setText(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }
    }
}
