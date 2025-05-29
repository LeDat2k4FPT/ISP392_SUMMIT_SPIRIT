/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package user;

/**
 *
 * @author Admin
 */
public class UserDTO {

    //asdaewdasdad
    private String userID;
    private String fullName;
    private String roleID;
    private String password;
    private String phone;
    private String email;
    private String address;

    public UserDTO() {
        this.userID = "";
        this.fullName = "";
        this.roleID = "";
        this.password = "";
        this.phone = "";
        this.email = "";
        this.address = "";
    }

    public UserDTO(String userID, String fullName, String roleID, String password, String phone, String email, String address) {
        this.userID = userID;
        this.fullName = fullName;
        this.roleID = roleID;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
