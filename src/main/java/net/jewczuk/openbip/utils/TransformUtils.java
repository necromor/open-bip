package net.jewczuk.openbip.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class TransformUtils {

	public static String createLinkFromTitle(String title) {
		//List<Character> special = Arrays.asList(' ', 'ą', 'ć', 'ę', 'ł', 'ń', 'ó', 'ś', 'ź', 'ż');
		//List<Character> plain = Arrays.asList('-', 'a', 'c', 'e', 'l', 'n', 'o', 's', 'z', 'z');
		Map<Character, Character> toBeReplaced = new HashMap<>();
		toBeReplaced.put(' ', '-');
		toBeReplaced.put('ą', 'a');
		toBeReplaced.put('ć', 'c');
		toBeReplaced.put('ę', 'e');
		toBeReplaced.put('ł', 'l');
		toBeReplaced.put('ń', 'n');
		toBeReplaced.put('ó', 'o');
		toBeReplaced.put('ś', 's');
		toBeReplaced.put('ź', 'z');
		toBeReplaced.put('ż', 'z');
		
		List<Character> toRemove = Arrays.asList('#', '$', '"', '/', '\\');
		
		title = title.toLowerCase();
		StringBuilder toReturn = new StringBuilder();
		
		int titleLenght = title.length();
		for (int i = 0; i < titleLenght; i++) {
			Character tmp = title.charAt(i);
			
			if (toRemove.contains(tmp)) {
				continue;
			}
			
			if (toBeReplaced.containsKey(tmp)) {
				toReturn.append(toBeReplaced.get(tmp));
			} else {
				toReturn.append(tmp);
			}
		}
		
		return toReturn.toString();
	}
	
	
}
