package com.example.instasham.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* Lombok generates the constructors, getters, and setters */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

/* This specifies that the class in an entity,
   and is mapped to a db table. */
@Entity

    /*
    This was an example db from the tutorial
        @Table(name = "school")
    We'll want to put in our own db table
    */

public class Instasham {
    /* @Id is the primary key of this entity */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String location;
}
