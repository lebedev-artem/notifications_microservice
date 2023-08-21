package ru.skillbox.socialnetwork.notifications.dto.notify;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto настроек оповещений")
public class NotificationSettingDto {

    private String id;

    //Разрешить события с типом LIKE
    private Boolean enableLike;

    //Разрешить события с типом POST
    private Boolean enablePost;

    //Разрешить события с типом POST_COMMENT
    private Boolean enablePostComment;

    //Разрешить события с типом COMMENT_COMMENT
    private Boolean enableCommentComment;

    //Разрешить события с типом MESSAGE
    private Boolean enableMessage;

    //Разрешить события с типом FRIEND_REQUEST
    private Boolean enableFriendRequest;

    //Разрешить события с типом FRIEND_BIRTHDAY
    private Boolean enableFriendBirthday;

    //Разрешить события с типом SEND_EMAIL_MESSAGE
    private Boolean enableSendEmailMessage;
}
