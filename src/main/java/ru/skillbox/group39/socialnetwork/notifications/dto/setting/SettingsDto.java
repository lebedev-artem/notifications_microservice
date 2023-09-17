package ru.skillbox.group39.socialnetwork.notifications.dto.setting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Artem Lebedev | 24/08/2023 - 17:46 <p>
 * enableLike - Разрешить события с типом LIKE <p>
 * enablePost - Разрешить события с типом POST <p>
 * enablePostComment - Разрешить события с типом POST_COMMENT <p>
 * enableCommentComment - Разрешить события с типом COMMENT_COMMENT <p>
 * enableMessage - Разрешить события с типом MESSAGE <p>
 * enableFriendRequest - Разрешить события с типом FRIEND_REQUEST <p>
 * enableFriendBirthday - Разрешить события с типом FRIEND_BIRTHDAY <p>
 * enableSendEmailMessage - Разрешить события с типом SEND_EMAIL_MESSAGE
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingsDto {

    private Long id;
    private Boolean enableLike;
    private Boolean enablePost;
    private Boolean enablePostComment;
    private Boolean enableCommentComment;
    private Boolean enableMessage;
    private Boolean enableFriendRequest;
    private Boolean enableFriendBirthday;
    private Boolean enableSendEmailMessage;
}
