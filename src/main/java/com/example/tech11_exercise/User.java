package com.example.tech11_exercise;

public class User {

    //@NotBlank(message = "First name is required")
    private String firstname;

    //@NotBlank(message = "Last name is required")
    private String lastname;

    //@NotBlank(message = "Email is required")
    //@Email(message = "Invalid Email")
    private String email;

    //@NotBlank(message = "Birthday is required")
    private String birthday;

    //a length or format constraint would also be possible here
    //@NotBlank(message = "Password is required")
    private String password;

    public User(String firstname, String lastname, String email, String birthday, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.birthday = birthday;
        this.password = password;
    }

    public User() {
    }

    public boolean checkAttributes() {
        // Check for null, empty or only whitespace string
        if (firstname == null || firstname.trim().isEmpty()) return false;
        if (lastname == null || lastname.trim().isEmpty()) return false;
        if (email == null || email.trim().isEmpty()) return false;
        if (birthday == null || birthday.trim().isEmpty()) return false;
        if (password == null || password.trim().isEmpty()) return false;

        // valid birthday format (yyyy-MM-dd)
        if (!birthday.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return false;
        }
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return false;
        }
        return true;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
               "firstname='" + firstname + '\'' +
               ", lastname='" + lastname + '\'' +
               ", email='" + email + '\'' +
               ", birthday='" + birthday + '\'' +
               ", password='" + password + '\'' +
               '}';
    }

}
