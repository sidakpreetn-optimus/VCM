package com.example.optimus158.virtualcontactmanager;

import java.io.Serializable;

/**
 * @author optimus158
 *         <p/>
 *         Model class for the Contacts Details
 */
public class ContactsModel implements Serializable {

    private static final long serialVersionUID = 0L;

    private String firstName;
    private String lastName;
    private String phoneNo;
    private String picturePath;
    private boolean isSelected;

    // Getter Setter for fetching and saving data
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
