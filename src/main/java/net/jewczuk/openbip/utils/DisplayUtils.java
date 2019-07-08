package net.jewczuk.openbip.utils;

import org.springframework.stereotype.Component;

@Component
public class DisplayUtils {
	
	public static String formatSize(long size) {
		double newSize = size / 1024.0;
		String prefix = " KB";
		if (newSize > 1024.0) {
			newSize = newSize / 1024.0;
			prefix = " MB";	
		}
		return String.format("%1$,.2f", newSize) + prefix;
	}

}
