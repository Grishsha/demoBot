package ru.ittask.demoBot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileData {
    String firstName;
    String secondName;
    String lastName;
    String department;
    String eMail;
    String phone;

    public String toString() {
        String resultString = "";
        resultString += String.format("\n\nЗаявитель %s %s %s\n\nПодразделение: %s" +
                        "\nТелефон: %s\nE-mail: %s\n\nотправлена в СУИТ", getLastName(), getFirstName(), getSecondName()
                , getDepartment(), getPhone(), getEMail());
        return resultString;
    }
}
