package ru.ittask.demoBot.cache;

import ru.ittask.demoBot.model.*;

public interface DataCache {
    void setUserCurrentBotState(long userId, BotState botState);
    void setUserProfileData(long userId, UserProfileData userProfileData);
    void setIncidentData(long userId, IncidentData incidentData);
    void setUserAttachments(long userId, UserAttachments userAttachments);
    void setUserCurrentPhaseState(long userId, PhaseState phaseState);

    PhaseState getUserCurrentPhaseState(long userId);
    BotState getUserCurrentBotState(long userId);
    UserProfileData getUserProfileData(long userId);
    IncidentData getIncidentData(long userId);
    UserAttachments getUserFilesData(long userId);
    //void saveUserProfileData(int userId, UserProfileData userProfileData);
}
