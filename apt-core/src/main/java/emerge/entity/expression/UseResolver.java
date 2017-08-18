package emerge.entity.expression;

import java.util.List;

import emerge.entity.Keyword;

public interface UseResolver {
    
    public KeywordExpression buildKeywordExpression(String requiredUseString);
    
    public boolean isRequiredUseOk(
	    List<Keyword> useKeywords, KeywordExpression requeredUse);
}
