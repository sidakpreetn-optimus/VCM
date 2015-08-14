package com.example.optimus158.virtualcontactmanager;

import java.io.Serializable;

/**
 * @author optimus158
 *         <p/>
 *         Model class for the Contacts Details
 */
public class GroupsModel implements Serializable {

    private static final long serialVersionUID = 0L;

    String groupName;
    String picturePath;
    String groupMembers;
    int groupCount;

    // getters and setters

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }

    public String getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(String groupMembers) {
        this.groupMembers = groupMembers;
    }

}
