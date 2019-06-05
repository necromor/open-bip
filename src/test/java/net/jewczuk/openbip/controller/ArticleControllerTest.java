package net.jewczuk.openbip.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
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

import net.jewczuk.openbip.TestConstants;
import net.jewczuk.openbip.constants.ViewNames;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ArticleControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@Test
	public void shouldDisplaySingleArticle() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/artykul/" + TestConstants.CHILD_2_LINK));
		
		resultActions
			.andExpect(status().isOk())
			.andExpect(view().name(ViewNames.SHOW_ARTICLE))
			.andExpect(model().attribute("article", allOf(
					hasProperty("title", is(TestConstants.CHILD_2_TITLE)),
					hasProperty("content", is(TestConstants.CHILD_2_CONTENT))
					)));
	}
	
	@Test
	public void shouldReturnError404WhenInvalidArticleLink() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/artykul/" + TestConstants.INVALID_LINK));
		
		resultActions
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void shouldDisplayArticleHistory() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/historia/" + TestConstants.CHILD_2_LINK));
		
		resultActions
			.andExpect(status().isOk())
			.andExpect(view().name(ViewNames.SHOW_HISTORY))
			.andExpect(model().attribute("history", allOf(
					hasProperty("title", is(TestConstants.CHILD_2_TITLE)),
					hasProperty("contentHistory", hasSize(1))
					)));
	}
	
	@Test
	public void shouldReturnError404WhenInvalidHistoryLink() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/historia/" + TestConstants.INVALID_LINK));
		
		resultActions
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void shouldDisplayMainPage() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/"));
		
		resultActions
			.andExpect(status().isOk())
			.andExpect(view().name(ViewNames.SHOW_ARTICLE))
			.andExpect(model().attribute("article", allOf(
					hasProperty("title", is(TestConstants.MAIN_PAGE_TITLE)),
					hasProperty("content", is(TestConstants.MAIN_PAGE_CONTENT))
					)));
	}
	

}
