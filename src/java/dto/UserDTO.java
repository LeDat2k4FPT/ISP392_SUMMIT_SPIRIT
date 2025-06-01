package dto;

public class UserDTO {
    private int userID;
    private String fullName;
    private String address;
    private String password;
    private String phone;
    private String email;
    private int roleID;

    public UserDTO() {
    }

    public UserDTO(int userID, String fullName, String address, String password, String phone, String email, int roleID) {
        this.userID = userID;
        this.fullName = fullName;
        this.address = address;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.roleID = roleID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
} 