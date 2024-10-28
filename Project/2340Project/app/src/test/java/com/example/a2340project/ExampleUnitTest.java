package com.example.a2340project;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import com.example.a2340project.views.Destination;




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

    public void singleton_isCorrect() {

        FirebaseDatabaseSingleton instance1 = FirebaseDatabaseSingleton.getInstance();
        FirebaseDatabaseSingleton instance2 = FirebaseDatabaseSingleton.getInstance();

        // Check if both instances are the same
        assertEquals(instance1, instance2);
    }

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
