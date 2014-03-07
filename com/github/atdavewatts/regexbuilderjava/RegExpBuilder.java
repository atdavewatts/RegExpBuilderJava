package com.github.atdavewatts.regexbuilderjava;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegExpBuilder {
	private List<String> _expression;

    private State _state;

    public RegExpBuilder()
    {
        _state = new State();
        _expression = new ArrayList<String>();
    }

    public String toString()
    {
    	StringBuffer sb = new StringBuffer();
    	for(String s : _expression)
    	{
    		sb.append(s);
    	}

        return sb.toString();
    }

    public Pattern ToRegExp()
    {
        int options = 0;
        if (_state.getMultiLine())
        {
            options = options | Pattern.MULTILINE;
        }

        return Pattern.compile(this.toString(), options);
    }

    public RegExpBuilder StartOfInput()
    {
        //_expression.add("(?:^)");
    	_expression.add("^");
        return this;
    }

    public RegExpBuilder EndOfInput()
    {
        //_expression.add("(?:$)");
    	_expression.add("$");
        return this;
    }

    public RegExpBuilder StartOfLine()
    {
        //_state.setMultiLine(true);
        StartOfInput();
        return this;
    }

    public RegExpBuilder EndOfLine()
    {
        //_state.setMultiLine(true);
        EndOfInput();
        return this;
    }
    
    public RegExpBuilder multiline()
    {
        return this.multiline(true);
    }
    
    public RegExpBuilder multiline(boolean multiline)
    {
        _state.setMultiLine(multiline);
        return this;
    }

    public RegExpBuilder OneOrMore()
    {
        _state.setSome(true);
        return this;
    }

    public RegExpBuilder Digit()
    {
        AddExpression("d");

        return this;
    }

    public RegExpBuilder Digits()
    {
        AddExpression("d+");

        return this;
    }

    private String _AZaz = "A-Za-z";

    public RegExpBuilder ZeroOrOne()
    {
        _state.setZeroOrOne(true);
        return this;
    }

    public RegExpBuilder Letter()
    {
        _state.setSome(false);
        addFrom(_AZaz);

        return this;
    }

    public RegExpBuilder Letters()
    {
        _state.setSome(true);
        addFrom(_AZaz);

        return this;
    }

    public RegExpBuilder MinimumOf(int minimumOccurence)
    {
        _state.setMinimumOf(minimumOccurence);
        return this;
    }

    public RegExpBuilder Or()
    {
        //OrLike(new RegExpBuilder().Exactly(1).Of(searchString).ToRegExp());
        _state.setOr(true);
        return this;
    }

    public RegExpBuilder OrLike(Pattern RegExpression)
    {
        String literal = _expression.get(_expression.size() -1);
        _expression.remove(_expression.size() -1);

        literal = stripParenthesis(literal);

        _expression.add(addParenthesis(literal + "|(?:" + RegExpression.toString() + ")"));


        return this;
    }

    private String stripParenthesis(String literal)
    {
        if (literal.startsWith("("))
            literal = literal.substring(1);
        if (literal.endsWith(")"))
            literal = literal.substring(0, literal.length() - 1);

        return literal;
    }

    private String addParenthesis(String literal)
    {
        if (literal.length() > 0)
            return "(" + literal + ")";

        return literal;
    }

    private String getQuantityLiteral()
    {
        int min = _state.getMinimumOf();
        int max = _state.getMaximumOf();

        String literal = "";

        if (min > -1 || max > -1)
        {
            String minValue = min > -1 ? Integer.toString(min) : "0";
            String maxValue = max > -1 ? Integer.toString(max) : "";

            literal = String.format("{%s,%s}", minValue, maxValue);
        }

        return literal;
    }

    private void addFrom(String p)
    {
        String from = String.format("[%s]", p);
        from = addFilters(from);

        _expression.add(from);
    }

    private String addFilters(String literal)
    {
        String quantitySet = getQuantityLiteral();
        literal += quantitySet;

        if (quantitySet.isEmpty())
        {
            if (_state.getZeroOrOne() && !literal.endsWith("?"))
                literal += "?";

            if (_state.getSome() && !literal.endsWith("+"))
                literal += "+";
        }

        return literal;
    }


    private void AddExpression(String literal)
    {
        add("\\" + literal);
    }

    private void add(String _literal)
    {
        _literal = addFilters(_literal);
        _literal = HandleConditions(_literal);
        _literal = addParenthesis(_literal);
        if (_literal.length() > 0)
            _expression.add(_literal);
    }

    private String HandleConditions(String _literal)
    {
        if (_state.getOr())
        {
            this.OrLike(Pattern.compile(_literal));
            _literal = "";
            _state.setOr(false);
        }

        return _literal;
    }

    public RegExpBuilder MaximumOf(int maximumOccurences)
    {
        _state.setMaximumOf(maximumOccurences);
        return this;
    }

    public RegExpBuilder Exactly(int occurences)
    {
        _state.setMinimumOf(occurences);
        _state.setMaximumOf(occurences);
        return this;
    }

    public RegExpBuilder Of(String stringToMatch)
    {
        add(stringToMatch);
        return this;
    }
}
