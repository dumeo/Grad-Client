package com.grad.information.note;

public class NoteItem {

    private String noteId;
    private String communityName;
    private String content;
    private long readCnt;
    private String createDate;

    public NoteItem(String noteId, String communityName, String content, long readCnt, String createDate) {
        this.noteId = noteId;
        this.communityName = communityName;
        this.content = content;
        this.readCnt = readCnt;
        this.createDate = createDate;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }


    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public long getReadCnt() {
        return readCnt;
    }

    public void setReadCnt(long readCnt) {
        this.readCnt = readCnt;
    }


    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

}
