package bean;

import java.sql.Timestamp;

public class Comment {
	private Integer commentId;
	private Integer newsId;
	private Integer userId;
	private String content;
	private Timestamp time;
	private Integer praise;
	private Integer stair;

	public Integer getStair() {
		return stair;
	}

	public void setStair(Integer stair) {
		this.stair = stair;
	}

	public Integer getPraise() {
		return praise;
	}

	public void setPraise(Integer praise) {
		this.praise = praise;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Integer getNewsId() {
		return newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

}
