package bean;

import java.util.List;

import tools.Tool;

public class ArticleNumberByMonthInAYear {
	private Integer year=0;
	private Integer totalNewsNumber=0;
	private List<Integer> articleNumberByMonthList;
	
	public ArticleNumberByMonthInAYear(){
		articleNumberByMonthList=Tool.getListWithLengthInitIntValue(12, 0);
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<Integer> getArticleNumberByMonthList() {
		return articleNumberByMonthList;
	}

	public void setArticleNumberByMonthList(List<Integer> articleNumberByMonthList) {
		this.articleNumberByMonthList = articleNumberByMonthList;
	}

	public Integer getTotalNewsNumber() {
		return totalNewsNumber;
	}

	public void setTotalNewsNumber(Integer totalNewsNumber) {
		this.totalNewsNumber = totalNewsNumber;
	}	
}
