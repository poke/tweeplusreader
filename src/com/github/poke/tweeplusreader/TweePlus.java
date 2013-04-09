package com.github.poke.tweeplusreader;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TweePlus library.
 *
 * Based on the web version on http://tweeplus.com by Lea Verou.
 *
 */
public class TweePlus
{
	private static final Pattern reLinks = Pattern.compile("(?i)(@\\w{1,20}|#\\w+|(https?|ftps?)://\\w[\\w._+%/-]+)");

	/**
	 * Character a mention link starts with.
	 */
	public static final char CHAR_MENTION = '@';

	/**
	 * Character a hashtag link starts with.
	 */
	public static final char CHAR_HASHTAG = '#';

	/**
	 * URL-encode the given text following the same rules as on tweeplus.com.
	 *
	 * @param text the text to encode.
	 * @return the encoded text.
	 */
	public static String encode (String text)
	{
		try
		{
			text = URLEncoder.encode(text, "UTF-8");

			// replace common special characters
			text = text.replace(".", "%2E").replace("*", "%2A");

			// undo replacement for URLEncoder's special characters
			text = text.replace("%21", "!");

			return text;
		}
		catch (UnsupportedEncodingException e)
		{
			return text;
		}
	}

	/**
	 * URL-decode the given text following the same rules as on tweeplus.com.
	 *
	 * @param text the text to decode.
	 * @return the decoded text.
	 */
	public static String decode (String text)
	{
		try
		{
			return URLDecoder.decode(text, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			return text;
		}
	}

	/**
	 * Create a regular expression Matcher object selecting all links within the
	 * given text.
	 *
	 * @param text the text.
	 * @return the created matcher.
	 */
	public static Matcher createLinkMatcher (String text)
	{
		return reLinks.matcher(text);
	}
}