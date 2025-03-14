package org.yvesguilherme.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.yvesguilherme.commons.FileUtils;

@WebMvcTest(HelloController.class)
@ComponentScan(basePackages = "org.yvesguilherme")
class HelloControllerTest {
  private static final String URL = "/greetings";
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private FileUtils fileUtils;

  @Test
  @DisplayName("GET /greetings returns a string ('OMAE WA MOU SHIND IRU') when successful")
  void hi_ReturnsAStringOmaeWaMouShindIru_WhenSuccessful() throws Exception {
    var response = fileUtils.readResourceFile("hello/get-hello-200.txt");

    mockMvc.perform(MockMvcRequestBuilders.get(URL))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
            .andExpect(MockMvcResultMatchers.content().string(response));
  }

  @Test
  @DisplayName("POST /greetings logs a name and returns a random ID")
  void greetings_LogsNameAndReturnsRandomId_WhenSuccessful() throws Exception {
    var request = fileUtils.readResourceFile("hello/post-hello-200.json");

    mockMvc.perform(MockMvcRequestBuilders.post(URL)
                    .contentType("application/json")
                    .content(request))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(Matchers.matchesPattern("\\d+")));
  }
}