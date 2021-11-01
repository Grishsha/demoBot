package ru.ittask.IntekhBot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

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
        StringBuilder resultString = new StringBuilder();
        int count = 1;
        if (attachmentsPath != null) {
            //resultString = String.format("Id: %s%nФайл 1:", getUserId());
            for (String str : attachmentsPath) {
                resultString.append(String.format("Файл %d: %s\n", count, str));
                count++;
            }
            return resultString.toString();
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