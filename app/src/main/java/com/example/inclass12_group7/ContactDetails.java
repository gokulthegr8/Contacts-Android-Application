package com.example.inclass12_group7;

import java.io.Serializable;

public class ContactDetails implements Serializable {
    String Name,Email,Phone,Image,DocID;

    public ContactDetails(String name, String email, String phone, String image, String docID) {
        Name = name;
        Email = email;
        Phone = phone;
        Image = image;
        DocID = docID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDocID() {
        return DocID;
    }

    public void setDocID(String docID) {
        DocID = docID;
    }

    @Override
    public String toString() {
        return "ContactDetails{" +
                "Name='" + Name + '\'' +
                ", Email='" + Email + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Image='" + Image + '\'' +
                ", DocID='" + DocID + '\'' +
                '}';
    }
}
