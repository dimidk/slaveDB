package com.example.slavedb.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Builder
@Setter
public class Student implements Comparable {

    @JsonProperty("uuid")
    private int uuid;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("grade")
    private String grade;

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
