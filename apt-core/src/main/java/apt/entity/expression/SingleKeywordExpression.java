package apt.entity.expression;

import apt.entity.Keyword;

public class SingleKeywordExpression implements KeywordExpression {

    private boolean result;

    private Keyword keyword;

    public SingleKeywordExpression(Keyword keyword) {
	this.keyword = keyword;
    }

    @Override
    public KeywordExpressionType getType() {
	return KeywordExpressionType.EXACTLY_ONE;
    }

    @Override
    public boolean getResult() {
	return result;
    }

    @Override
    public void setResult(boolean result) {
	this.result = result;
    }

    public Keyword getKeyword() {
	return keyword;
    }

}
