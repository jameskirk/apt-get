package apt.entity.expression;

import java.util.ArrayList;
import java.util.List;

public class IfElseKeywordExpression implements KeywordExpression {

    private boolean result;

    private List<KeywordExpression> ifKeywords = new ArrayList<KeywordExpression>();

    private List<KeywordExpression> elseKeywords = new ArrayList<KeywordExpression>();

    public IfElseKeywordExpression(List<KeywordExpression> ifKeywords, List<KeywordExpression> elseKeywords) {
	this.ifKeywords = ifKeywords;
	this.elseKeywords = elseKeywords;
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

    public List<KeywordExpression> getIfKeywords() {
	return ifKeywords;
    }

    public List<KeywordExpression> getElseKeywords() {
	return elseKeywords;
    }

}
