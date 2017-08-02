package com.knalum;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knalum.domain.User;
import com.knalum.exception.RestServerException;
import com.knalum.service.UserService;
import com.knalum.web.WebApi;

@RunWith(SpringRunner.class)
@WebMvcTest(WebApi.class)
public class WebApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testCreateUserSuccess() throws Exception {
        User user = new User("Name", "Address", 20);

        given(userService.createUser(any(User.class))).willReturn(user);

        String res = mockMvc.perform(post("/api/user")
            .content(mapper.writeValueAsString(user))
            .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        User responseUser = mapper.readValue(res, User.class);
        assertEquals(user, responseUser);
    }

    @Test
    public void testCreateUserFailDueToMissingFirstname() throws Exception {
        User user = new User();

        mockMvc.perform(post("/api/user")
            .content(mapper.writeValueAsString(user))
            .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserFailDueToAge() throws Exception {
        User user = new User("Firstname", "Address", -2);

        mockMvc.perform(post("/api/user")
            .content(mapper.writeValueAsString(user))
            .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserSuccess() throws Exception {
        User user = new User("Name", "Address", 20);

        given(userService.updateUser(anyLong(), any(User.class))).willReturn(user);

        String res = mockMvc.perform(put("/api/user/1")
            .content(mapper.writeValueAsString(user))
            .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        User responseUser = mapper.readValue(res, User.class);
        assertEquals(user, responseUser);
    }

    @Test
    public void testDeleteUserSuccess() throws Exception {
        User user = new User("Name", "Address", 20);
        given(userService.deleteUser(anyLong())).willReturn(user);

        String res = mockMvc.perform(delete("/api/user/1")
            .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        User responseUser = mapper.readValue(res, User.class);
        assertEquals(user, responseUser);
    }

    @Test
    public void testDeleteNonExistingUser() throws Exception {
        given(userService.deleteUser(anyLong())).willThrow(new RestServerException("Nonexisting"));
        mockMvc.perform(delete("/api/user/91919")
            .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
}
