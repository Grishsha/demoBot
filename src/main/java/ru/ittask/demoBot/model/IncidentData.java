package ru.ittask.demoBot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IncidentData {
    long userId;
    String description;
    String service;
    String respGroup;
    String respPerson;

    public void removeIncidentData() {
        description = null;
        service = null;
        respGroup = null;
        respPerson = null;
    }

    public String toString() {
        String resultString = "";
        resultString += String.format("Ваша заявка со следующим описанием: \n%s" +
                        "\nУслуга: %s\nОтветственная группа: %s" +
                        "\nИмя исполнителя: %s", getDescription(), getService()
                , getRespGroup(), getRespPerson());
        return resultString;
    }
}