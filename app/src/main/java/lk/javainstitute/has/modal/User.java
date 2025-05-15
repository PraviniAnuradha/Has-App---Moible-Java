package lk.javainstitute.has.modal;

public class User {
    private String fullName;
    private String username;
    private String email;
    private String mobile;
    private String password;
    private String accNo;
    private double amount;
    private String nic;
    private String dob;
    private String address;
    public User(){

    }

    public User(String fullName, String username, String email, String mobile, String password, String accNo, double amount) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.accNo = accNo;
        this.amount = amount;
    }

    public User(String fullName, String username, String email, String mobile, String password, String accNo, double amount, String nic, String dob, String address) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.accNo = accNo;
        this.amount = amount;
        this.nic = nic;
        this.dob = dob;
        this.address = address;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
