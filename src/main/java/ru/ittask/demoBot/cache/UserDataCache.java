package ru.ittask.demoBot.cache;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.stereotype.Component;
import ru.ittask.demoBot.entity.UserDataEntity;
import ru.ittask.demoBot.model.*;
import ru.ittask.demoBot.repository.UserProfileDataRepository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class UserDataCache implements DataCache {
    private Map<Long, BotState> botStateMap = new HashMap<>();
    private Map<Long, PhaseState> phaseStateMap = new HashMap<>();
    private Map<Long, UserProfileData> userProfileDataMap = new HashMap<>();
    private Map<Long, UserAttachments> userAttachmentsMap = new HashMap<>();
    private Map<Long, IncidentData> incidentDataMap = new HashMap<>();
    @Autowired
    //EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    UserProfileDataRepository repository;// = entityManagerFactory.createEntityManager();


    @Override
    public void setUserCurrentBotState(long userId, BotState botState) {
        botStateMap.put(userId, botState);
    }

    @Override
    public void setUserCurrentPhaseState(long userId, PhaseState phaseState) {
        phaseStateMap.put(userId, phaseState);
    }

    @Override
    public BotState getUserCurrentBotState(long userId) {
        BotState botState = botStateMap.get(userId);
        if (botState == null) {
            botState = BotState.NOT_AUTHORISED;
        }
        return botState;
        //hibernate.
    }

    @Override
    public PhaseState getUserCurrentPhaseState(long userId) {
        PhaseState phaseState = phaseStateMap.get(userId);
        if (phaseState == null) {
            phaseState = PhaseState.DEFAULT;
        }
        return phaseState;
    }

    @Override
    @PostConstruct
    public UserProfileData getUserProfileData(long userId) {
        UserProfileData userProfileData = userProfileDataMap.get(userId);
        if (userProfileData == null) {

        //Boolean ttt = repository.existsById(userId);//UserProfileDataRepository repository = null;
        //    if (ttt != null && ttt) {
                UserDataEntity entity = repository.findById(userId);
                userProfileData = entity.getUserProfileData();
            } else
                userProfileData = new UserProfileData();
        return userProfileData;
        }
        //return userProfileData;

    @Override
    public IncidentData getIncidentData(long userId) {
        IncidentData incidentData = incidentDataMap.get(userId);
        if (incidentData == null) {
            incidentData = new IncidentData();
        }
        return incidentData;
    }

    @Override
    public UserAttachments getUserFilesData(long userId) {
        UserAttachments userAttachments = userAttachmentsMap.get(userId);
        if (userAttachments == null) {
            userAttachments = new UserAttachments();
        }
        return userAttachments;
    }

    @Override
    public void setIncidentData(long userId, IncidentData incidentData) {
        incidentDataMap.put(userId, incidentData);
    }

    @Override
    public void setUserProfileData(long userId, UserProfileData userProfileData) {
        UserDataEntity entity = new UserDataEntity();
        entity.setId(userId);
        entity.setUserProfileData(userProfileData);

        //if (repository.existsById(userId)){
        repository.save(entity);
        userProfileDataMap.put(userId, userProfileData);
        //}

    }

    @Override
    public void setUserAttachments(long userId, UserAttachments userAttachments) {
        userAttachmentsMap.put(userId, userAttachments);
    }

    /*    @Override
    public void saveUserProfileData(int userId, UserProfileData userProfileData) {
        usersProfileData.put(userId, userProfileData);
    }*/
}
