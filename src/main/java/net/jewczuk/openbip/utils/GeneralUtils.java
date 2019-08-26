package net.jewczuk.openbip.utils;

import org.springframework.stereotype.Component;

@Component
public class GeneralUtils {

	public static String createUniqueLink(Long editorID) {
		StringBuilder output = new StringBuilder("xyz-");
		output.append(editorID).append("-");
		output.append(System.currentTimeMillis());
		return output.toString();
	}
}
