package net.jewczuk.openbip.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import net.jewczuk.openbip.utils.DisplayUtils;

public class DisplayUtilTest {
	
	@Test
	public void shouldDisplayZeroWhenGiven0() {
		String result = DisplayUtils.formatSize(0L);
		
		assertThat(result).isEqualTo("0,00 KB");
	}
	
	@Test
	public void shouldDisplayCorectlyKB() {
		String result = DisplayUtils.formatSize(12345L);
		
		assertThat(result).isEqualTo("12,06 KB");		
	}

	
	@Test
	public void shouldDisplayCorectlyMB() {
		String result = DisplayUtils.formatSize(1048586L);
		
		assertThat(result).isEqualTo("1,00 MB");		
	}
	
	@Test
	public void shouldDisplayCorectlyBigMB() {
		String result = DisplayUtils.formatSize(209717200L);
		
		assertThat(result).isEqualTo("200,00 MB");		
	}
}
