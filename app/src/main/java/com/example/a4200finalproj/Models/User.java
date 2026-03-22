package com.example.comp4200project;

import java.sql.Date;
import java.sql.Timestamp;

public class User {

    public int UserID;
    public String Username;
    public String Password;
    public String Role; //Admin, Doctor, Nurse, Receptionist
    public String Email;
    public Timestamp CreatedDate;
    public boolean isActive;

    public int getUserID(){
        return UserID;
    }
    public void setUserID(int UserID){
        this.UserID = UserID;
    }

    public String getUsername(){
        return Username;
    }
    public void setUsername(String Username){
        this.Username = Username;
    }

    public String getPassword(){
        return Password;
    }
    public void setPassword(String Password){
        this.Password = Password;
    }

    public String getRole(){
        return Role;
    }
    public void setRole(String Role){
        this.Role = Role;
    }

    public String getEmail(){
        return Email;
    }
    public void setEmail(String Email){
        this.Email = Email;
    }

    public Timestamp getCreatedDate(){
        return CreatedDate;
    }
    public void setCreatedDate(Timestamp CreatedDate){
        this.CreatedDate = CreatedDate;
    }

    public boolean getisActive(){
        return isActive;
    }
    public void setisActive(boolean isActive){
        this.isActive = isActive;
    }
}
