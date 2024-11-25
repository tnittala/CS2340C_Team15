package com.example.a2340project;

import static com.example.a2340project.views.Accommodations.*;
import static org.junit.Assert.*;

import com.example.a2340project.model.DiningReservation;
import com.example.a2340project.model.ReservationManager;
import com.example.a2340project.model.SortByDate;
import com.example.a2340project.model.SortStrategy;
import com.example.a2340project.model.TravelLog;
import com.example.a2340project.model.Trip;
import com.example.a2340project.views.Accommodations;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExampleUnitTest {

    @Test
    public void testIsPastDate_withPastDate() {
        String pastDate = "01/01/22"; // Assuming today's date is after this
        assertTrue(Accommodations.isPastDate(pastDate));
    }

    @Test
    public void testIsPastDate_withFutureDate() {
        String futureDate = "12/31/30"; // Assuming today's date is before this
        assertFalse(Accommodations.isPastDate(futureDate));
    }

    @Test
    public void testIsPastDate_withInvalidDate() {
        String invalidDate = "invalid-date";
        assertFalse(Accommodations.isPastDate(invalidDate));
    }

    @Test
    public void testIsValidDate_withValidDate() {
        String validDate = "12/25/23";
        assertTrue(isValidDate(validDate));
    }

    @Test
    public void testIsValidDate_withInvalidDate() {
        String invalidDate = "99/99/99";
        assertFalse(isValidDate(invalidDate));
    }

    @Test
    public void testIsValidDate_withEmptyDate() {
        String emptyDate = "";
        assertFalse(isValidDate(emptyDate));
    }

    @Test
    public void testParsedCheckInDate_withValidDate() {
        TravelLog travelLog = new TravelLog("New York", "12/12/23", "12/15/23", "Deluxe");

        Date parsedDate = travelLog.getParsedCheckInDate();
        assertNotNull("Check-in date should be parsed correctly", parsedDate);
        assertEquals("Check-in date should match expected value", "Tue Dec 12 00:00:00 EST 2023", parsedDate.toString());
    }

    @Test
    public void testParsedCheckInDate_withInvalidDate() {
        TravelLog travelLog = new TravelLog("New York", "invalid-date", "12/15/23", "Deluxe");

        Date parsedDate = travelLog.getParsedCheckInDate();
        assertNull("Check-in date parsing should return null for invalid format", parsedDate);
    }

    @Test
    public void testParsedCheckOutDate_withValidDate() {
        TravelLog travelLog = new TravelLog("New York", "12/12/23", "12/15/23", "Deluxe");

        Date parsedDate = travelLog.getParsedCheckOutDate();
        assertNotNull("Check-out date should be parsed correctly", parsedDate);
        assertEquals("Check-out date should match expected value", "Fri Dec 15 00:00:00 EST 2023", parsedDate.toString());
    }

    @Test
    public void testParsedCheckOutDate_withInvalidDate() {
        TravelLog travelLog = new TravelLog("New York", "12/12/23", "invalid-date", "Deluxe");

        Date parsedDate = travelLog.getParsedCheckOutDate();
        assertNull("Check-out date parsing should return null for invalid format", parsedDate);
    }

    @Test
    public void testCheckInBeforeCheckOut() {
        TravelLog travelLog = new TravelLog("New York", "12/12/23", "12/15/23", "Deluxe");

        Date checkInDate = travelLog.getParsedCheckInDate();
        Date checkOutDate = travelLog.getParsedCheckOutDate();

        assertNotNull("Check-in date should be parsed", checkInDate);
        assertNotNull("Check-out date should be parsed", checkOutDate);
        assertTrue("Check-in date should be before check-out date", checkInDate.before(checkOutDate));
    }

    @Test
    public void testCheckInAfterCheckOut() {
        TravelLog travelLog = new TravelLog("New York", "12/12/23", "12/15/23", "Deluxe");

        Date checkInDate = travelLog.getParsedCheckInDate();
        Date checkOutDate = travelLog.getParsedCheckOutDate();

        assertNotNull("Check-in date should be parsed", checkInDate);
        assertNotNull("Check-out date should be parsed", checkOutDate);
        assertFalse("Check-in date should not be after check-out date", checkInDate.after(checkOutDate));
    }

    @Test
    public void testSetAndGetSortStrategy() {
        ReservationManager reservationManager = new ReservationManager();
        SortStrategy sortByDate = new SortByDate();
        reservationManager.setSortStrategy(sortByDate);
        assertEquals(sortByDate, reservationManager.getSortStrategy());
    }

    @Test
    public void testSortReservations() {
        ReservationManager reservationManager = new ReservationManager();
        SortStrategy sortByDate = new SortByDate();  // Assuming you have a SortByDate strategy
        reservationManager.setSortStrategy(sortByDate);

        List<DiningReservation> reservations = new ArrayList<>();
        reservations.add(new DiningReservation("Location A", "Website A", "12/01/23"));
        reservations.add(new DiningReservation("Location B", "Website B", "11/01/23"));
        reservations.add(new DiningReservation("Location C", "Website C", "10/01/23"));

        List<DiningReservation> sortedReservations = reservationManager.sortReservations(reservations);
        assertEquals("Location C", sortedReservations.get(0).getLocation());
        assertEquals("Location B", sortedReservations.get(1).getLocation());
        assertEquals("Location A", sortedReservations.get(2).getLocation());
    }

    @Test
    public void testSortReservationsWithNoSortStrategy() {
        ReservationManager reservationManager = new ReservationManager();
        List<DiningReservation> reservations = new ArrayList<>();
        reservations.add(new DiningReservation("Location A", "Website A", "12/01/23"));
        reservations.add(new DiningReservation("Location B", "Website B", "11/01/23"));
        reservations.add(new DiningReservation("Location C", "Website C", "10/01/23"));
        List<DiningReservation> sortedReservations = reservationManager.sortReservations(reservations);
        assertEquals(reservations, sortedReservations);
    }

    @Test
    public void testAddReservationNoObservers() {
        ReservationManager reservationManager = new ReservationManager();
        DiningReservation reservation = new DiningReservation("Location A", "Website A", "12/01/23");
        reservationManager.addReservation(reservation);
    }

    @Test
    public void testTripConstructor_default() {
        Trip trip = new Trip();
        assertNotNull(trip);  // Ensure the trip object is not null
        assertNull(trip.getDestination());
        assertNull(trip.getStartDate());
        assertNull(trip.getEndDate());
        assertNull(trip.getTripId());
        assertNull(trip.getCreatedBy());
        assertFalse(trip.isCollaborative());
    }

    @Test
    public void testTripConstructor_parameterized() {
        Trip trip = new Trip("Paris", "12/01/24", "12/10/24", "trip123", "user1", true);
        assertEquals("Paris", trip.getDestination());
        assertEquals("12/01/24", trip.getStartDate());
        assertEquals("12/10/24", trip.getEndDate());
        assertEquals("trip123", trip.getTripId());
        assertEquals("user1", trip.getCreatedBy());
        assertTrue(trip.isCollaborative());
    }

    @Test
    public void testSetAndGetDestination() {
        Trip trip = new Trip();
        trip.setDestination("New York");
        assertEquals("New York", trip.getDestination());
    }

    @Test
    public void testSetAndGetStartDate() {
        Trip trip = new Trip();
        trip.setStartDate("01/01/25");
        assertEquals("01/01/25", trip.getStartDate());
    }

    @Test
    public void testSetAndGetEndDate() {
        Trip trip = new Trip();
        trip.setEndDate("01/10/25");
        assertEquals("01/10/25", trip.getEndDate());
    }

    @Test
    public void testSetAndGetTripId() {
        Trip trip = new Trip();
        trip.setTripId("trip456");
        assertEquals("trip456", trip.getTripId());
    }

    @Test
    public void testSetAndGetCreatedBy() {
        Trip trip = new Trip();
        trip.setCreatedBy("user2");
        assertEquals("user2", trip.getCreatedBy());
    }

    @Test
    public void testSetAndIsCollaborative() {
        Trip trip = new Trip();
        trip.setCollaborative(true);
        assertTrue(trip.isCollaborative());

        trip.setCollaborative(false);
        assertFalse(trip.isCollaborative());
    }

    @Test
    public void testSortReservationsByDate() {
        ReservationManager reservationManager = new ReservationManager();
        List<DiningReservation> reservations = new ArrayList<>();
        reservations.add(new DiningReservation("Location B", "Website B", "11/01/23"));
        reservations.add(new DiningReservation("Location C", "Website C", "10/01/23"));
        reservations.add(new DiningReservation("Location A", "Website A", "12/01/23"));

        reservationManager.setSortStrategy(new SortByDate());
        List<DiningReservation> sortedReservations = reservationManager.sortReservations(reservations);

        assertEquals("10/01/23", sortedReservations.get(0).getTime());
        assertEquals("11/01/23", sortedReservations.get(1).getTime());
        assertEquals("12/01/23", sortedReservations.get(2).getTime());
    }


    @Test
    public void testTripConstructorWithObservers() {
        Trip trip = new Trip("Paris", "12/01/24", "12/10/24", "trip123", "user1", true);
        assertNotNull(trip);
        assertEquals("Paris", trip.getDestination());
        assertTrue(trip.isCollaborative());
    }


    @Test
    public void testSortStrategyChange() {
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setSortStrategy(new SortByDate());
        assertTrue(reservationManager.getSortStrategy() instanceof SortByDate);

        //reservationManager.setSortStrategy(new SortByTypeStrategy());
        //assertTrue(reservationManager.getSortStrategy() instanceof SortByTypeStrategy);
    }

    @Test
    public void testReservationConstructor() {
        DiningReservation reservation = new DiningReservation("Location A", "Website A", "12/01/23");
        assertEquals("Location A", reservation.getLocation());
        assertEquals("Website A", reservation.getWebsite());
        assertEquals("12/01/23", reservation.getTime());
    }

    @Test
    public void testSetAndGetReservationLocation() {
        DiningReservation reservation = new DiningReservation();
        reservation.setLocation("New Location");
        assertEquals("New Location", reservation.getLocation());
    }

    @Test
    public void testSetAndGetReservationWebsite() {
        DiningReservation reservation = new DiningReservation();
        reservation.setWebsite("new.website.com");
        assertEquals("new.website.com", reservation.getWebsite());
    }

    @Test
    public void testSetAndGetReservationDate() {
        DiningReservation reservation = new DiningReservation();
        reservation.setDate("01/01/25");
        assertEquals("01/01/25", reservation.getTime());
    }

    @Test
    public void testReservationEquality() {
        DiningReservation reservation1 = new DiningReservation("Location A", "Website A", "12/01/23");
        DiningReservation reservation2 = new DiningReservation("Location A", "Website A", "12/01/23");
        assertEquals(reservation1, reservation2);
    }

    @Test
    public void testTripDefaultConstructor() {
        Trip trip = new Trip();
        assertNull(trip.getDestination());
        assertNull(trip.getStartDate());
        assertNull(trip.getEndDate());
    }

    @Test
    public void testSetAndGetTripDestination() {
        Trip trip = new Trip();
        trip.setDestination("Paris");
        assertEquals("Paris", trip.getDestination());
    }

    @Test
    public void testSetAndGetTripEndDate() {
        Trip trip = new Trip();
        trip.setEndDate("12/10/24");
        assertEquals("12/10/24", trip.getEndDate());
    }

    @Test
    public void testIsCollaborativeDefault() {
        Trip trip = new Trip();
        assertFalse(trip.isCollaborative());
    }











}



