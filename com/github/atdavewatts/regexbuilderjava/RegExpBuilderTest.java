package com.github.atdavewatts.regexbuilderjava;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class RegExpBuilderTest
{
	
	@Test
    public void GetRegExp()
    {
        RegExpBuilder builder = new RegExpBuilder();
        Pattern r = builder.Digit().ToRegExp();

        assertTrue(r.matcher("1").find());
        assertFalse(r.matcher("a").find());
    }

    @Test
    public void IsDigit()
    {
        RegExpBuilder builder = new RegExpBuilder();
        Pattern r = builder.Digit().ToRegExp();

        assertTrue("1 " + r.toString(), r.matcher("1").find());
        assertTrue("11 " + r.toString(), r.matcher("11").find());
        assertFalse("a" + r.toString(), r.matcher("a").find());
    }
    
    @Test
    public void IsOnlyOneDigit()
    {
        RegExpBuilder builder = new RegExpBuilder();
        Pattern r = builder
            .StartOfLine()
            .Digit()
            .EndOfLine()
            .ToRegExp();

        assertTrue(r.matcher("1").find());
        assertFalse(r.matcher("11").find());
        assertFalse(r.matcher("a").find());
    }

    @Test
    public void IsSomeDigit()
    {
        RegExpBuilder builder = new RegExpBuilder();
        Pattern r = builder
            .StartOfLine()
            .OneOrMore()
            .Digit()
            .EndOfLine()
            .ToRegExp();

        assertTrue(r.matcher("11").find());
        assertFalse(r.matcher("a").find());
    }

    @Test
    public void IsLetters()
    {
        RegExpBuilder builder = new RegExpBuilder();
        Pattern r = builder
            .StartOfLine()
            .Digit()
            .Letters()
            .Digit()
            .EndOfLine()
            .ToRegExp();

        assertTrue(r.matcher("1a1").find());
        assertTrue(r.matcher("1aa1").find());
        assertFalse(r.matcher("a").find());
    }

    @Test
    public void IsLetter()
    {
        RegExpBuilder builder = new RegExpBuilder();
        Pattern r = builder
            .StartOfLine()
            .Letter()
            .EndOfLine()
            .ToRegExp();

        assertTrue(r.matcher("a").find());
        assertFalse(r.matcher("aa").find());
    }

    @Test
    public void ZeroOrOneLetter()
    {
        RegExpBuilder builder = new RegExpBuilder();
        Pattern r = builder
            .StartOfLine()
            .ZeroOrOne()
            .Letter()
            .EndOfLine()
            .ToRegExp();
      
        assertTrue("NoneMulti:"      + r.pattern(), r.matcher("\n").find());
        assertTrue("None:"      + r.pattern(), r.matcher("").find());
        assertTrue("One:"       + r.pattern(), r.matcher("a").find());
        assertFalse("Multiple:" + r.pattern(), r.matcher("aa").find());        
    }

    @Test
    public void Min3Letter()
    {
        RegExpBuilder builder = new RegExpBuilder();
        Pattern r = builder
            .MinimumOf(3)
            .Letters()
            .ToRegExp();

        assertTrue("Three Letters" + r.toString(), r.matcher("aaa").find());
        assertTrue("Four Letters"  + r.toString(), r.matcher("bbbb").find());
        assertFalse("Two Letters"  + r.toString(), r.matcher("aa").find());
    }

    @Test
    public void Max3Letter()
    {
        RegExpBuilder builder = new RegExpBuilder();
        Pattern r = builder
            .StartOfLine()
            .MaximumOf(3)
            .Letters()
            .EndOfLine()
            .ToRegExp();

        assertTrue("Three Letters", r.matcher("aaa").find());
        assertTrue("Two Letters", r.matcher("aa").find());
        assertFalse("Four Letters", r.matcher("bbbb").find());
    }

    @Test
    public void Min3Max4Letter()
    {
        RegExpBuilder builder = new RegExpBuilder();
        Pattern r = builder
            .StartOfLine()
            .MinimumOf(3)
            .MaximumOf(4)
            .Letters()
            .EndOfLine()
            .ToRegExp();

        assertTrue("Three Letters", r.matcher("aaa").find());
        assertTrue("Four Letters", r.matcher("aaaa").find());
        assertFalse("Five Letters", r.matcher("bbbbb").find());
        assertFalse("Two Letters", r.matcher("bb").find());
    }

    @Test
    public void Exactly()
    {
        RegExpBuilder builder = new RegExpBuilder();
        Pattern r = builder
            .StartOfLine()
            .Exactly(3)
            .Letters()
            .EndOfLine()
            .ToRegExp();

        assertTrue("Three Letters", r.matcher("aaa").find());
        assertFalse("Four Letters", r.matcher("aaaa").find());
        assertFalse("Two Letters", r.matcher("bb").find());
    }

    @Test
    public void ExactlyOfCustom()
    {
        RegExpBuilder builder = new RegExpBuilder();
        Pattern r = builder
            .StartOfLine()
            .Exactly(3)
            .Of("a")
            .EndOfLine()
            .ToRegExp();

        assertTrue("Three Letters", r.matcher("aaa").find());
        assertFalse("Four Letters", r.matcher("aaaa").find());
        assertFalse("Two Letters", r.matcher("aa").find());
    }

    @Test
    public void Or()
    {
        RegExpBuilder builder = new RegExpBuilder();
        Pattern r = builder
            .StartOfLine()
            .Exactly(1).Of("github")
            .Or()
            .Exactly(1).Of("bitbucket")
            .EndOfLine()
            .ToRegExp();

        String regex = r.toString();

        assertTrue("Found one Github", r.matcher("github").find());
        assertTrue("Found one Bitbucket", r.matcher("bitbucket").find());

        assertFalse("Oops, Found too Many Github", r.matcher("githubgithub").find());
        assertFalse("Oops, Found too Many Github", r.matcher("bitbucketbitbucket").find());
    }

    @Test
    public void MultipleOr()
    {
        RegExpBuilder builder = new RegExpBuilder();
        Pattern r = builder
            .StartOfLine()
            .Exactly(1)
            .Of("a")
            .Or()
            .Exactly(1).Of("b")
            .Or()
            .MinimumOf(3).Of("x")
            .EndOfLine()
            .ToRegExp();

        assertTrue("a", r.matcher("a").find());
        assertTrue("b", r.matcher("b").find());
        assertTrue("many x", r.matcher("xxxx").find());

        assertFalse("two Letters", r.matcher("ab").find());
        assertFalse("two Letters", r.matcher("aa").find());
    }

    @Test
    public void ValidateEmailExample()
    {
        // you should never validate emaildresses using regex, but here is one way:
        // This filter will not allow gmail-like, "+ syntax",  tagging: "info+skipinbox@example.com"
    	RegExpBuilder builder = new RegExpBuilder();
        Pattern r = builder
            .StartOfInput()
            .Letter() // Must start with letter a-z
            .Letters() // any number of letters
            .Or() 
            .Digits() // any number of numbers
            .Exactly(1).Of("@") 
            .Letters() // domain
            .Exactly(1).Of(".")
            .Letters() // top-level domain
            .EndOfInput()
            .ToRegExp();

        assertTrue(r.matcher("anders@andersaberg.com").find());
        assertTrue(r.matcher("a1@a.com").find());
        
        // Invalid
        assertFalse(r.matcher("1a@a.com").find());
    }
}
