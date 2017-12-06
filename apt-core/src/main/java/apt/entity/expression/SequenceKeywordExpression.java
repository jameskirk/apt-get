package apt.entity.expression;

import java.util.ArrayList;
import java.util.List;

public class SequenceKeywordExpression implements KeywordExpression {

    private boolean result;

    private List<KeywordExpression> keywords = new ArrayList<KeywordExpression>();

    public SequenceKeywordExpression(List<KeywordExpression> keywords) {
	this.keywords = keywords;
    }

    @Override
    public KeywordExpressionType getType() {
	return KeywordExpressionType.IF_ELSE;
    }

    @Override
    public boolean getResult() {
	return result;
    }

    @Override
    public void setResult(boolean result) {
	this.result = result;
    }

    public List<KeywordExpression> getKeywords() {
	return keywords;
    }

}
