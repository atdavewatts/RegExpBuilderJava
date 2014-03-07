package com.github.atdavewatts.regexbuilderjava;

public class State
{

//    private RegexOptions options;
//    
//    public RegexOptions getOptions()
//    {
//		return options;
//	}
//    
//    public void setOptions(RegexOptions options)
//    {
//		this.options = options;
//	}
//	
//	public State()
//    {
//        options = new RegexOptions();
//    }

    private boolean _some;

    public boolean getSome()
    {
    	boolean v = _some;
        _some = false;
        return v;
	}
    
    public void setSome(boolean some)
    {
		_some = some;
	}
    
    private boolean _zeroOrMore;
    
    public boolean getZeroOrMore()
    {
    	boolean v = _zeroOrMore;
    	_zeroOrMore = false;
        return v;
	}
    
    public void setZeroOrMore(boolean zeroOrMore)
    {
    	_zeroOrMore = zeroOrMore;
	}


    
    private boolean _zeroOrOne;
    
    public boolean getZeroOrOne()
    {
    	boolean v = _zeroOrOne;
    	_zeroOrOne = false;
        return v;
	}
    
    public void setZeroOrOne(boolean zeroOrOne)
    {
    	_zeroOrOne = zeroOrOne;
	}


    private int _minimumOf = -1;

    public int getMinimumOf()
    {
    	int v = _minimumOf;
        _minimumOf = -1;
        return v;
	}
    
    public void setMinimumOf(int minimumOf)
    {
		_minimumOf = minimumOf;
	}
    
    private int _maximumOf = -1;

    public int getMaximumOf()
    {
    	int v = _maximumOf;
        _maximumOf = -1;
        return v;
	}
    
    public void setMaximumOf(int maximumOf)
    {
		_maximumOf = maximumOf;
	}
    

    private boolean MultiLine;
    
    public void setMultiLine(boolean multiLine)
    {
		MultiLine = multiLine;
	}
    
    public boolean getMultiLine()
    {
		return MultiLine;
	}

    private boolean or;
    
    public boolean getOr()
    {
    	return or;
    }
    
    public void setOr(boolean or)
    {
		this.or = or;
	}

}
