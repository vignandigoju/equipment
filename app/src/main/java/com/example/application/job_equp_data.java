package com.example.application;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class job_equp_data {
    private String date, serviceCharge, serviceType, status;

    public job_equp_data() {
        // Default constructor required for Firebase
    }

    public job_equp_data(String date, String serviceCharge, String serviceType, String status) {
        this.date = convertDateString(date);
        this.serviceCharge = serviceCharge;
        this.serviceType = serviceType;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getStatus() {
        return status;
    }

    private String convertDateString(String dateString) {
        try {
            // Assuming the date format is "dd.MM.yyyy"
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd"); // Change the format as needed

            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the exception or return the original string if the conversion fails
            return dateString;
        }
    }
}
