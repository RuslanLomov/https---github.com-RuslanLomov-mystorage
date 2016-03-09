package trainee.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AllFilesForDirectory {
	private static List<File> list = new ArrayList<File>();

	public static List<File> getList() {
		return list;
	}

	public static void resetList() {
		list = new ArrayList<File>();
	}

	public static void getFilesAndDirectories(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				if (f.isFile())
					list.add(f);
				if (f.isDirectory()) {
					getFilesAndDirectories(f);
					list.add(f);
				}
			}
		}
	}
}
