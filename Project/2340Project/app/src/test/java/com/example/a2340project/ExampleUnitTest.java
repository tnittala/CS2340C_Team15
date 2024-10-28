package com.example.a2340project;
import static org.junit.Assert.assertEquals;

import android.graphics.Color;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import com.example.a2340project.views.Destination;
import com.example.a2340project.views.Logistics;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;


public class ExampleUnitTest {

    private Logistics logisticsActivity =  new Logistics();


    @Test
    public void testGraphTripsEntries() {
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

    @Test
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
