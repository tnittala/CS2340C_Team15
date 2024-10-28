package com.example.a2340project;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

import com.example.a2340project.views.Destination;
import com.example.a2340project.views.LoginActivity;

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

        public String getText() {
            return this.text;
        }
    }

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
