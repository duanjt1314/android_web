package duanjt.life.common;

import java.io.File;

import android.os.Environment;

public class logHelper {
	private static String filePathName = "";
	private static logHelper install = null;

	public String WriteToFile(String msg) {
		File file = Environment.getRootDirectory();
		return file.toString();
	}

	public static logHelper GetInstall() {
		if (install == null) {
			install = new logHelper();
		}
		return install;
	}

}
