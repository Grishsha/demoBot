package ru.ittask.demoBot.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserAttachments {

    private long userId;
    private List<String> attachmentsPath = new ArrayList<>();

    public void addNewAttachmentPath(String path) {
        attachmentsPath.add(path);
    }

    public String toString() {
        String resultString = "";
        int count = 1;
        if (attachmentsPath != null) {
            //resultString = String.format("Id: %s%nФайл 1:", getUserId());
            for (String str : attachmentsPath) {
                resultString += String.format("Файл %d: %s\n", count, str);
                count++;
            }
            return resultString;
        }
        return "";
    }

    public int getCount() {
        return attachmentsPath.size();
    }

    public void removeAllPaths() {
        attachmentsPath = null;
        attachmentsPath = new ArrayList<>();
    }
}