package com.ovh.milestone;

import java.time.ZonedDateTime;

public class Invoice
{

    // Attributes
    private String nichandle;
    private String name;
    private String firstName;
    private Double transaction;
    private ZonedDateTime date;

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
        this.date = date;
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

    public ZonedDateTime getDate()
    {
        return date;
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
        return "[Client]" + "NicHandle: " + nichandle + " Nom: " + name + ", Prenom: " + firstName
            + ", Montant: " + transaction
            + ", Date: " + date + "\n";
    }
}
