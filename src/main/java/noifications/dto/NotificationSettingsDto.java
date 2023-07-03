package dto;
import lombok.Data;
import java.util.UUID;

//Dto настроек оповещений
@Data
public class NotificationSettingsDto {
    public UUID id;

    //Разрешить события с типом LIKE
    public boolean enableLike;

    //Разрешить события с типом POST
    public boolean enablePost;

    //Разрешить события с типом POST_COMMENT
    public boolean enablePostComment;

    //Разрешить события с типом COMMENT_COMMENT
    public boolean enableCommentComment;

    //Разрешить события с типом MESSAGE
    public boolean enableMessage;

    //Разрешить события с типом FRIEND_REQUEST
    public boolean enableFriendRequest;

    //Разрешить события с типом FRIEND_BIRTHDAY
    public boolean enableFriendBirthday;

    //Разрешить события с типом SEND_EMAIL_MESSAGE
    public boolean enableSendEmailMessage;
}
