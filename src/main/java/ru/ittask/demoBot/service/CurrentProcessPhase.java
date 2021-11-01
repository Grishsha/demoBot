package ru.ittask.demoBot.service;

import lombok.NoArgsConstructor;
import ru.ittask.demoBot.model.BotState;

@NoArgsConstructor
public class CurrentProcessPhase {

    public BotState get(BotState currentBotState) {
        switch (currentBotState) {
            case NOT_AUTHORISED:
            case REG_LAST_NAME:
            case REG_FIRST_NAME:
            case REG_SECOND_NAME:
            case REG_EMAIL:
            case REG_DEPARTMENT:
            case REG_PHONE:
                return BotState.REGISTRATION;
            case NEW_TT:
            case TT_DESCRIPTION:
            case TT_PHOTO:
            case TT_SERVICE:
            case TT_RESP_GROUP:
            case TT_RESP_PERSON:
                return BotState.TT_FILLING;
            case SHOW_HELP_MENU:
                return BotState.SHOW_HELP_MENU;
            case SHOW_USER_PROFILE:
                return BotState.SHOW_USER_PROFILE;
            default:
                return BotState.SHOW_MAIN_MENU;
        }
    }
}
