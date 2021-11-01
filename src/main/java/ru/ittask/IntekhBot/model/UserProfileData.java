package ru.ittask.IntekhBot.model;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class UserProfileData {
    String firstName;
    String secondName;
    String lastName;
    String department;
    String eMail;
    String phone;

    public String getFirstName() {
        if (firstName == null)
            return "";
        return firstName;
    }

    public String getSecondName() {
        if (secondName == null)
            return "";
        return secondName;
    }

    public String getLastName() {
        if (lastName == null)
            return "";
        return lastName;
    }

    public String getDepartment() {
        if (department == null)
            return "";
        return department;
    }

    public String getEMail() {
        if (eMail == null)
            return "";
        return eMail;
    }

    public String getPhone() {
        if (phone == null)
            return "";
        return phone;
    }

    public String toString() {
        String resultString = "";
        resultString += String.format("\n\nЗаявитель: %s %s %s\n\nПодразделение: %s" +
                        "\nТелефон: %s\nE-mail: %s", getLastName(), getFirstName(), getSecondName()
                , getDepartment(), getPhone(), getEMail());
        return resultString;
    }
}
