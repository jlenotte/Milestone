package com.ovh.milestone;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Invoice
{

    // Attributes
    private String nichandle;
    private String name;
    private String firstName;
    private Double transaction;
    private String date;



    // Constructor
    public Invoice()
    {
        this.nichandle = null;
        this.name = null;
        this.firstName = null;
        this.transaction = null;
        this.date = null;
    }



    public Invoice(String nichandle, String name, String firstName, Double transaction, ZonedDateTime date)
    {

        this.nichandle = nichandle;
        this.name = name;
        this.firstName = firstName;
        this.transaction = transaction;

        // Format ZDT into String using ISO Date conversion
        this.date = date.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        //this.date = date;
    }



    /**
     * Getters & Setters
     */

    public String getNichandle()
    {
        return nichandle;
    }



    public String getName()
    {
        return name;
    }



    public Double getTransaction()
    {
        return transaction;
    }



    public String getDate()
    {
        //return ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        return date;
    }



    public ZonedDateTime getZonedDate()
    {
        return ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }



    public String getFirstName()
    {
        return firstName;
    }



    public void setNichandle(String nichandle)
    {
        this.nichandle = nichandle;
    }



    public void setName(String name)
    {
        this.name = name;
    }



    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }



    public void setDate(ZonedDateTime date)
    {
        this.date = date.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }



    public void setDate(String date)
    {
        this.date = date;
    }



    public void setTransaction(Double transaction)
    {
        this.transaction = transaction;
    }



    @Override
    public int hashCode()
    {
        return super.hashCode();
    }



    @Override
    public boolean equals(Object o)
    {
        return super.equals(o);
    }



    public String toString()
    {
        return "[Client]" + "NicHandle: " + nichandle + ", Nom: " + name + ", Prenom: " + firstName
            + ", Montant: " + transaction
            + ", Date: " + date + "\n";
    }
}
