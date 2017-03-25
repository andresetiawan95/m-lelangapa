package com.lelangapa.android.resources;

/**
 * Created by andre on 23/03/17.
 */

public class ChatResources {
    public static final int TYPE_MESSAGE_SENT = 0;
    public static final int TYPE_MESSAGE_RECEIVED = 1;

    private int chatType;
    private String idUser, namaUser, chatRoom, chatMessage;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(String chatRoom) {
        this.chatRoom = chatRoom;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public void setChatType(int type)
    {
        this.chatType = type;
    }

    public int getChatType()
    {
        return this.chatType;
    }
}
