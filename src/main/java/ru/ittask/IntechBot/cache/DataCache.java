package ru.ittask.IntechBot.cache;

import ru.ittask.IntechBot.model.BotState;
import ru.ittask.IntechBot.model.IncidentData;
import ru.ittask.IntechBot.model.UserAttachments;
import ru.ittask.IntechBot.model.UserProfileData;
import ru.ittask.IntechBot.repository.UserProfileDataRepository;

public interface DataCache {
    void setUserCurrentBotState(long userId, BotState botState);

    void setUserProfileData(long userId, UserProfileData userProfileData);

    void setIncidentData(long userId, IncidentData incidentData);

    void setUserAttachments(long userId, UserAttachments userAttachments);

    BotState getUserCurrentBotState(long userId);

    UserProfileData getUserProfileData(long userId, UserProfileDataRepository repository);

    IncidentData getIncidentData(long userId);

    UserAttachments getUserFilesData(long userId);
    //void saveUserProfileData(int userId, UserProfileData userProfileData);
}
