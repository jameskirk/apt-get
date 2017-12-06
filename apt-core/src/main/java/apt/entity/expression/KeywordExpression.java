package apt.entity.expression;


public interface KeywordExpression {
    
    public KeywordExpressionType getType();
    
    public void setResult(boolean result);
    
    public boolean getResult();
    
    public static enum KeywordExpressionType {
	IF_ELSE, AT_LEAST_ONE, EXACTLY_ONE;
    }
    
}
