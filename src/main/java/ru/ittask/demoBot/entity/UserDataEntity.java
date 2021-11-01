package ru.ittask.demoBot.entity;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.Type;
import ru.ittask.demoBot.model.UserProfileData;

import javax.persistence.*;

@Table(name = "user_data")
@Entity
public class UserDataEntity {
    @Id
    @Column(name = "chat_id", nullable = false)
    private Long id;

    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType")
    @Column(name = "user_profile_data")
    private UserProfileData userProfileData;

    public UserProfileData getUserProfileData() {
        return userProfileData;
    }

    public void setUserProfileData(UserProfileData userProfileData) {
        this.userProfileData = userProfileData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}