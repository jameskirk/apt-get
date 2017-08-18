package emerge.entity;

import java.io.Serializable;

public class Keyword implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String value;
    
    private boolean masked = false;
    
    private boolean plus = false;
    
    private boolean minus = false;
    
    private boolean not = false;
    
    public Keyword(String value) {
	char flag = value.charAt(0);
	if (flag == '~') {
	    masked = true;
	    value = value.substring(1);
	}
	if (flag == '+') {
	    plus = true;
	    value = value.substring(1);
	}
	if (flag == '-') {
	    minus = true;
	    value = value.substring(1);
	}
	if (flag == '!') {
	    not = true;
	    value = value.substring(1);
	}
	this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isMasked() {
        return masked;
    }

    public void setMasked(boolean masked) {
        this.masked = masked;
    }

    public boolean isPlus() {
        return plus;
    }

    public void setPlus(boolean plus) {
        this.plus = plus;
    }

    public boolean isMinus() {
        return minus;
    }

    public void setMinus(boolean minus) {
        this.minus = minus;
    }
    
    public boolean isNot() {
        return not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }

    public boolean isNoPrefix() {
	return !masked && !plus && !minus && !not;
    }
    
    @Override
    public String toString() {
	StringBuilder retVal = new StringBuilder();
	if (masked) {
	    retVal.append("~");
	}
	if (plus) {
	    retVal.append("+");
	}
	if (minus) {
	    retVal.append("-");
	}
	if (not) {
	    retVal.append("!");
	}
	retVal.append(value);
        return retVal.toString();
    }
    
}
