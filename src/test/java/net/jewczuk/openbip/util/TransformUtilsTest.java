package net.jewczuk.openbip.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import net.jewczuk.openbip.utils.TransformUtils;

public class TransformUtilsTest {

	@Test
	public void shouldReturnTheSameStringWhenNothingToTransform() {
		String given = "test";
		
		String result = TransformUtils.createLinkFromTitle(given);
		
		assertThat(result).isEqualTo(given);
	}
	
	
	@Test
	public void shouldRetrunTextWithNoSpacesAndInLowerCase() {
		String given = "This is a tEsT";
		String expected = "this-is-a-test";
		
		String result = TransformUtils.createLinkFromTitle(given);
		
		assertThat(result).isEqualTo(expected);
	}
	
	@Test
	public void shouldReturnTextWithRemovedAllCorrectCharacters() {
		String given = "Thi$ is# a /tE\\sT";
		String expected = "thi-is-a-test";
		
		String result = TransformUtils.createLinkFromTitle(given);
		
		assertThat(result).isEqualTo(expected);
	}
	
	@Test
	public void shouldReplaceAllSpecialCharacters() {
		String given = "Łąka śżęWóŚźń";
		String expected = "laka-szewoszn";
		
		String result = TransformUtils.createLinkFromTitle(given);
		
		assertThat(result).isEqualTo(expected);
	}
	
}
