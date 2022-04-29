package com.assignment.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.assignment.security.UserDetailsServiceImpl;
import com.assignment.security.jwt.JwtUtil;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @MockBean
  private UserService userService;

  @MockBean
  private JwtUtil jwtUtil;

  @MockBean
  private UserDetailsServiceImpl userDetailsServiceImpl;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void create_GivenValidRequest_ShouldCreateNewUser() throws Exception {
    String userRequest = new JSONObject()
        .put("firstName", UserFixtures.FIRST_NAME)
        .put("lastName", UserFixtures.LAST_NAME)
        .put("email", UserFixtures.EMAIL)
        .put("password", UserFixtures.PASSWORD).toString();
    doNothing().when(this.userService).createUser(any());
    this.mockMvc.perform(post("/api/user")
            .content(userRequest)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    ArgumentCaptor<UserRequest> userRequestArgumentCaptor = ArgumentCaptor.forClass(UserRequest.class);
    verify(this.userService).createUser(userRequestArgumentCaptor.capture());
    var request = userRequestArgumentCaptor.getValue();
    assertThat(request.firstName()).isEqualTo(UserFixtures.FIRST_NAME);
    assertThat(request.lastName()).isEqualTo(UserFixtures.LAST_NAME);
    assertThat(request.email()).isEqualTo(UserFixtures.EMAIL);
    assertThat(request.password()).isEqualTo(UserFixtures.PASSWORD);
  }
}
