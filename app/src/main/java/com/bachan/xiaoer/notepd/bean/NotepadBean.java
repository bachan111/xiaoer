package com.bachan.xiaoer.notepd.bean;
public class NotepadBean {
    private String id;                  //记录的id
    private String notepadContent;   //记录的内容
    private String title;//标题
    private String notepadTime;       //保存记录的时间

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNotepadContent() {
        return notepadContent;
    }
    public void setNotepadContent(String notepadContent) {
        this.notepadContent = notepadContent;
    }
    public String getNotepadTime() {
        return notepadTime;
    }
    public void setNotepadTime(String notepadTime) {
        this.notepadTime = notepadTime;
    }
}
