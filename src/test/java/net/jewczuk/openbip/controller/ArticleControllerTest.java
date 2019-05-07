package net.jewczuk.openbip.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import net.jewczuk.openbip.constants.ViewNames;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ArticleControllerTest {
	
	private static final String INVALID_LINK = "nie-ma-takiego-artykulu";
	private static final String CHILD_2_TITLE = "Dziecko nr 2";
	private static final String CHILD_2_LINK = "dziecko-nr-2";
	private static final String CHILD_2_CONTENT = "Artykuł dziecko 2 v1";

	@Autowired
    private MockMvc mockMvc;
	
	@Test
	public void shouldDisplaySingleArticle() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/artykul/" + CHILD_2_LINK));
		
		resultActions
			.andExpect(status().isOk())
			.andExpect(view().name(ViewNames.SHOW_ARTICLE))
			.andExpect(model().attribute("article", allOf(
					hasProperty("title", is(CHILD_2_TITLE)),
					hasProperty("content", is(CHILD_2_CONTENT))
					)));
	}
	
	@Test
	public void shouldReturnError404WhenInvalidArticleLink() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/artykul/" + INVALID_LINK));
		
		resultActions
			.andExpect(status().isNotFound())
			.andExpect(view().name(ViewNames.ERROR_404));
		
	}
}
