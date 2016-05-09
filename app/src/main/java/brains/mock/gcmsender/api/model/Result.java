package brains.mock.gcmsender.api.model;

import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("message_id")
    Long messageId;

    public Result(Long messageId) {
        this.messageId = messageId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
}
