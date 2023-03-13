package com.elearning.app.teacher;

import jakarta.persistence.Id;

public class Teacher {

    @Id
    Long id;
    String name;
    String surname;
    //String tytulNaukowy;
    String email;
    String pesel;


    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", imie='" + name + '\'' +
                ", nazwisko='" + surname + '\'' +
                //", tytulNaukowy='" + tytulNaukowy + '\'' +
                ", email='" + email + '\'' +
                ", pesel='" + pesel + '\'' +
                '}';
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }


}
