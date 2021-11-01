package ru.ittask.demoBot.model;

public enum BotState {
    NOT_AUTHORISED,
    REG_FIRST_NAME,
    REG_SECOND_NAME,
    REG_LAST_NAME,
    REG_EMAIL,
    REG_DEPARTMENT,
    REG_PHONE,
    USER_PROFILE_FILLED,
    REGISTRATION,//--------------------------------------
    NEW_TT,
    TT_DESCRIPTION,
    TT_PHOTO,
    TT_SERVICE,
    TT_RESP_GROUP,
    TT_RESP_PERSON,
    TT_REQUEST_FILLED,
    TT_FILLING,//-----------------------------------------
    SHOW_HELP_MENU,
    SHOW_MAIN_MENU,
    SHOW_USER_PROFILE,
    CHANGE_USER_PROFILE,
    CHANGE_INCIDENT_DATA;
}