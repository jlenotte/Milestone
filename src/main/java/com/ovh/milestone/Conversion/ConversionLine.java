package com.ovh.milestone.Conversion;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ConversionLine
{

    // Attributes
    private String date;
    private Double sum;



    // Constructor
    public ConversionLine()
    {
        this.date = null;
        this.sum = null;
    }



    public ConversionLine(ZonedDateTime date, Double sum)
    {
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        this.sum = sum;
    }



    public ConversionLine(String date, Double sum)
    {
        this.date = date;
        this.sum = sum;
    }

    // Getters & Setters



    public String getDate()
    {
        return date;
    }



    public ZonedDateTime getZonedDate()
    {
        return ZonedDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }



    public void setDate(String date)
    {
        this.date = date;
    }



    public Double getSum()
    {
        return sum;
    }



    public void setSum(Double sum)
    {
        this.sum = sum;
    }



    @Override
    public String toString()
    {
        return "ConversionMap[" +
            "date='" + date + '\'' +
            ", sum=" + sum +
            ']';
    }
}
