package com.example.a2340project.views;

public class VacationTime {
    private String startDate;
    private String endDate;
    private String duration;

    public VacationTime() {
        // Default constructor required for calls to DataSnapshot.getValue(VacationTime.class)
    }

    public VacationTime(String startDate, String endDate, String duration) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDuration() {
        return duration;
    }
}

