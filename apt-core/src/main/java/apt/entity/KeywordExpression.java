package apt.entity;

import java.util.ArrayList;
import java.util.List;

public class KeywordExpression {
	
	private List<KeywordExpression> childExpressions = new ArrayList();
	
	private List<Keyword> keywords;
	
	private List<Keyword> keywordsIfCondition;
	
	private List<KeywordExpression> keywordsIfBody;
	
	private List<KeywordExpression> keywordsElseBody;
	
	public List<KeywordExpression> getChildExpressions() {
		return childExpressions;
	}

	public void setChildExpressions(List<KeywordExpression> childExpressions) {
		this.childExpressions = childExpressions;
	}

	public List<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}

	public List<Keyword> getKeywordsIfCondition() {
		return keywordsIfCondition;
	}

	public void setKeywordsIfCondition(List<Keyword> keywordsIfCondition) {
		this.keywordsIfCondition = keywordsIfCondition;
	}

	public List<KeywordExpression> getKeywordsIfBody() {
		return keywordsIfBody;
	}

	public void setKeywordsIfBody(List<KeywordExpression> keywordsIfBody) {
		this.keywordsIfBody = keywordsIfBody;
	}

	public List<KeywordExpression> getKeywordsElseBody() {
		return keywordsElseBody;
	}

	public void setKeywordsElseBody(List<KeywordExpression> keywordsElseBody) {
		this.keywordsElseBody = keywordsElseBody;
	}
	
	
	
	

}
