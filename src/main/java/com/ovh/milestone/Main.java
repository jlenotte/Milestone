package com.ovh.milestone;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.CsvReader;

public class Main
{

    /**
     *
     * Program Milestone is a revamp of the project Charlotte
     * which is basically a program that does filtering,
     * mapping, reducing, using Flink and Java Lambdas
     *
     * Milestone will be the first step to a new real Flink
     * project
     *
     *
     */

    public static void main(String[] args)
    {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        CsvReader text = env.readCsvFile("data.csv");

    }
}
