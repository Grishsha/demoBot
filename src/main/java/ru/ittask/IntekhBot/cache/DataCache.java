package ru.ittask.IntekhBot.cache;

import ru.ittask.IntekhBot.model.BotState;
import ru.ittask.IntekhBot.model.IncidentData;
import ru.ittask.IntekhBot.model.UserAttachments;
import ru.ittask.IntekhBot.model.UserProfileData;
import ru.ittask.IntekhBot.repository.UserProfileDataRepository;

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
