package bean;

import java.util.ArrayList;
import java.util.List;

public class CommentNumberTopTenInAYear {
	private Integer year = 0;
	private List<Statistics> commentList = new ArrayList<Statistics>();

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<Statistics> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Statistics> commentList) {
		this.commentList = commentList;
	}

}
