package ru.spbau.database;

import nl.siegmann.epublib.domain.Author;

/**
 * Created by airvan21 on 07.03.16.
 */
public class BookAuthor {
    private String firstName;
    private String lastName;

    public BookAuthor() {}

    public BookAuthor(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public BookAuthor(Author author) {
        firstName = author.getFirstname();
        lastName = author.getLastname();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
