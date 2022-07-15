package vn.tpsc.it4u.DTOs;

import java.util.Date;

import lombok.Data;

public class ChatMessageDTO {
  private String contents;

  private long fromUserId;
  
  private long toUserId;

  private Date timeSent;

  public ChatMessageDTO(){}
  
  public ChatMessageDTO(String contents, long fromUserId, long toUserId, Date timeSent) {
    this.contents = contents;
    this.fromUserId = fromUserId;
    this.toUserId = toUserId;
    this.timeSent = timeSent;
  }

  public String getContents() {
    return this.contents;
  }

  public void setToUserId(long toUserId) {
    this.toUserId = toUserId;
  }
  
  public long getToUserId() {
    return this.toUserId;
  }
  
  public void setContents(String contents) {
    this.contents = contents;
  }

  public void setFromUserId(long userId) {
    this.fromUserId = userId;
  }

  public long getFromUserId() {
    return this.fromUserId;
  }

  public void setTimeSent(Date timeSent) {
    this.timeSent = timeSent;
  }

  public Date getTimeSent() {
    return this.timeSent;
  }
}
