package utility;

import java.io.Serializable;

public enum AnswerType implements Serializable {
    SUCCESSFULLY_AUTHORIZATION,
    UNSUCCESSFULLY_AUTHORIZATION,
    NEED_ELEMENT,
    RETURN_ACTION,
    GETTER_STATE,
    DIALOG_STATE,
    EXIT
}