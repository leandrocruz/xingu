package xingu.taster.impl;

import java.util.HashMap;
import java.util.Map;

import xingu.taster.ByteTaster;
import xingu.utils.ByteUtils;

/*
 * See: http://www.garykessler.net/library/file_sigs.html
 */
public class ByteTasterImpl
	implements ByteTaster
{
	private static final Map<String, String> mimes = new HashMap<String, String>();

	static
	{
		mimes.put("474946383961", 	"image/gif");
		mimes.put("474946383761", 	"image/gif");
		mimes.put("89504E47", 		"image/png");
		mimes.put("47494638",		"image/gif");
		mimes.put("FFD8FFE0",		"image/jpg");
		mimes.put("25504446",		"application/pdf");
		mimes.put("CAFEBABE",		"application/java");
		mimes.put("5F27A889",		"application/java-archive");
		mimes.put("504B0304",		"application/zip");
		mimes.put("4D534346",		"application/vnd.ms-cab-compressed");
		mimes.put("EDABEEDB",		"application/vnd.linux-rpm");
		mimes.put("213C6172",		"application/vnd.linux-deb");
	}

	@Override
	public String mimeFrom(byte[] array)
	{
		String hex = ByteUtils.toHex(array);
		return mimes.get(hex);
	}
}

