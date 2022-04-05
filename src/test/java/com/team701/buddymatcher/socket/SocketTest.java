package com.team701.buddymatcher.socket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class SocketTest {

    @Test
    void messageObjectTest() {
        MessageObject messageObj = new MessageObject();
        messageObj = messageObj.setSenderId(1L);
        messageObj = messageObj.setReceiverId(2L);
        Assertions.assertEquals(messageObj.getSenderId(), 1L);
        Assertions.assertEquals(messageObj.getReceiverId(), 2L);
        messageObj = messageObj.setMessage("Hello World!");
        Assertions.assertEquals(messageObj.getMessage(), "Hello World!");
    }

    @Test
    void readObjectTest() {
        ReadObject readObj = new ReadObject();
        readObj = readObj.setBuddyId(1L);
        Assertions.assertEquals(readObj.getBuddyId(), 1L);
    }

    @Test
    void userObjectTest() {
        UserObject userObj = new UserObject();
        userObj = userObj.setUserId(1L);
        Assertions.assertEquals(userObj.getUserId(), 1L);
    }
}
