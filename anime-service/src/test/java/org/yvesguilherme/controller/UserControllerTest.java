package org.yvesguilherme.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.yvesguilherme.commons.FileUtils;
import org.yvesguilherme.commons.UserUtils;
import org.yvesguilherme.domain.User;
import org.yvesguilherme.mapper.UserMapperImpl;
import org.yvesguilherme.repository.UserData;
import org.yvesguilherme.repository.UserRepository;
import org.yvesguilherme.service.UserService;
import org.yvesguilherme.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

@WebMvcTest(UserController.class)
//@ComponentScan(basePackages = "org.yvesguilherme")
@Import({
        UserRepository.class,
        UserService.class,
        UserMapperImpl.class,
        UserData.class,
        Constants.class,
        FileUtils.class,
        UserUtils.class
})
class UserControllerTest {
  private static final String URL = "/users";
  private static final String THE_FIELD_FIRST_NAME_IS_REQUIRED = "The field 'firstName' is required";
  private static final String THE_FIELD_LAST_NAME_IS_REQUIRED = "The field 'lastName' is required";
  private static final String THE_FIELD_EMAIL_IS_REQUIRED = "The field 'email' is required";
  private static final String THE_FIELD_EMAIL_MUST_BE_A_VALID_E_MAIL = "The field 'email' must be a valid e-mail";
  private static final String THE_FIELD_ID_CANNOT_BE_NULL = "The field 'id' cannot be null";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private FileUtils fileUtils;

  @Autowired
  private UserUtils userUtils;

  @Autowired
  private UserService userService;

  @MockitoBean
  private UserRepository repository;

  private List<User> listUsers = new ArrayList<>();

  @BeforeEach
  void init() {
    listUsers = userUtils.newUserList();
  }

  @Test
  @DisplayName("GET v1/users returns a list of users when successful")
  void findAll_ReturnsListOfUsers_WhenSuccessful() throws Exception {
    BDDMockito.when(repository.findAll()).thenReturn(listUsers);

    var response = fileUtils.readResourceFile("user/get/get-user-200.json");

    mockMvc.perform(MockMvcRequestBuilders.get(URL))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/users/{id} returns a user when successful")
  void findById_ReturnsAUser_WhenSuccessful() throws Exception {
    var expectedUser = listUsers.get(1);
    BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));

    var response = fileUtils.readResourceFile("user/get/get-user-id-200.json");
    var id = 2;

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));

  }

  @Test
  @DisplayName("GET v1/users/firstName return a list with found object when firstName exists")
  void findByFirstName_ReturnsAListOfUsers_WhenFirstNameExists() throws Exception {
    var expectedUser = listUsers.getFirst();
    BDDMockito.when(repository.findByFirstNameIgnoreCase(expectedUser.getFirstName())).thenReturn(singletonList(expectedUser));

    var response = fileUtils.readResourceFile("user/get/get-user-first-name-200.json");
    var firstName = "Goku";

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/firstName/{firstName}", firstName))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/users/firstName return an empty when firstName is not found")
  void findByFirstName_ReturnsAnEmptyList_WhenFirstNameIsNotFound() throws Exception {
    var firstName = "Naruto";
    BDDMockito.when(repository.findByFirstNameIgnoreCase(firstName)).thenReturn(Collections.emptyList());

    var response = fileUtils.readResourceFile("user/get/get-user-first-name-not-found-200.json");

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/firstName/{firstName}", firstName))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/users/lastName return a list with found object when lastName exists")
  void findByLastName_ReturnsAListOfUsers_WhenLastNameExists() throws Exception {
    var lastName = "Saiyan";
    BDDMockito.when(repository.findByLastNameIgnoreCase(lastName)).thenReturn(listUsers);

    var response = fileUtils.readResourceFile("user/get/get-user-last-name-200.json");

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/lastName/{lastName}", lastName))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/users/lastName return an empty when lastName is not found")
  void findByLastName_ReturnsAnEmptyList_WhenLastNameIsNotFound() throws Exception {
    var response = fileUtils.readResourceFile("user/get/get-user-last-name-not-found-200.json");
    var lastName = "Uzumaki";

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/lastName/{lastName}", lastName))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/users/email return a user with given email")
  void findByEmail_ReturnsAUserWithGivenEmail_WhenSuccessful() throws Exception {
    var expectedUser = listUsers.getFirst();
    BDDMockito.when(repository.findByEmailIgnoreCase(expectedUser.getEmail())).thenReturn(Optional.of(expectedUser));

    var response = fileUtils.readResourceFile("user/get/get-user-email-200.json");
    var email = "goku@saiyan.com";

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/email/%s".formatted(email)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("GET v1/users/email returns User not found when email is not found")
  void findByEmail_ReturnsUserNotFound_WhenEmailIsNotFound() throws Exception {
    var expectedError = fileUtils.readResourceFile("user/get/get-user-404.json");
    var email = "goku@saiy2an.com";

    mockMvc.perform(MockMvcRequestBuilders.get(URL + "/email/%s".formatted(email)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.content().json(expectedError));
  }

  @Test
  @DisplayName("POST v1/users creates a user when successful")
  void save_CreatesAUser_WhenSuccessful() throws Exception {
    var request = fileUtils.readResourceFile("user/post/post-request-user-200.json");
    var response = fileUtils.readResourceFile("user/post/post-response-user-200.json");
    var userToSave = userUtils.newUserToSave();

    BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(userToSave);

    mockMvc.perform(
                    MockMvcRequestBuilders
                            .post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @ParameterizedTest
  @MethodSource("postUserBadRequestSource")
  @DisplayName("POST v1/users throws BadRequestException when fields are empty, blank or invalid")
  void save_ThrowsBadRequestException_WhenFieldsAreEmptyBlankOrInvalid(String fileName, List<String> errors) throws Exception {
    var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

    var mvcResult = mockMvc.perform(
                    MockMvcRequestBuilders
                            .post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

    var resolvedException = mvcResult.getResolvedException();

    Assertions.assertThat(resolvedException).isNotNull();

    Assertions
            .assertThat(resolvedException.getMessage())
            .contains(errors);
  }

  private static Stream<Arguments> postUserBadRequestSource() {
    var allErrors = new ArrayList<>(
            List.of(
                    THE_FIELD_FIRST_NAME_IS_REQUIRED,
                    THE_FIELD_LAST_NAME_IS_REQUIRED,
                    THE_FIELD_EMAIL_IS_REQUIRED)
    );
    var emailError = singletonList(THE_FIELD_EMAIL_MUST_BE_A_VALID_E_MAIL);

    return Stream.of(
            Arguments.of("post/post-request-user-empty-fields-400.json", allErrors),
            Arguments.of("post/post-request-user-blank-fields-400.json", allErrors),
            Arguments.of("post/post-request-user-invalid-email-400.json", emailError)
    );
  }

  @Test
  @DisplayName("PUT v1/users updates a user when successful")
  void update_UpdatesAUser_WhenSucessful() throws Exception {
    var expectedUser = User.builder().id(1L).firstName("Madara").lastName("Uchiha").email("uchiha@gods.com").build();

    BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));
    BDDMockito.when(repository.save(expectedUser)).thenReturn(expectedUser);

    var request = fileUtils.readResourceFile("user/put/put-request-user-200.json");

    mockMvc.perform(
                    MockMvcRequestBuilders
                            .put(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @ParameterizedTest
  @MethodSource("putUserBadRequestSource")
  @DisplayName("PUT v1/users throws BadRequestException when fields are empty, blank or invalid")
  void put_ThrowsBadRequestException_WhenFieldsAreEmptyBlankOrInvalid(String fileName, List<String> errors) throws Exception {
    var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

    var mvcResult = mockMvc.perform(
                    MockMvcRequestBuilders
                            .put(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();

    var resolvedException = mvcResult.getResolvedException();

    Assertions.assertThat(resolvedException).isNotNull();

    Assertions
            .assertThat(resolvedException.getMessage())
            .contains(errors);
  }

  @Test
  @DisplayName("PUT v1/users throws ResponseStatusException when User is not found")
  void update_ThrowsResponseStatusException_WhenUserIsNotFound() throws Exception {
    var request = fileUtils.readResourceFile("user/put/put-request-user-404.json");
    var expectedError = fileUtils.readResourceFile("user/put/put-response-user-404.json");

    mockMvc.perform(
                    MockMvcRequestBuilders.put(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.content().json(expectedError));
  }

  private static Stream<Arguments> putUserBadRequestSource() {
    var allErrors = new ArrayList<>(
            List.of(
                    THE_FIELD_FIRST_NAME_IS_REQUIRED,
                    THE_FIELD_LAST_NAME_IS_REQUIRED,
                    THE_FIELD_EMAIL_IS_REQUIRED
            )
    );
    var emailError = singletonList(THE_FIELD_EMAIL_MUST_BE_A_VALID_E_MAIL);
    var idNullError = singletonList(THE_FIELD_ID_CANNOT_BE_NULL);

    return Stream.of(
            Arguments.of("put/put-request-user-empty-fields-400.json", allErrors),
            Arguments.of("put/put-request-user-blank-fields-400.json", allErrors),
            Arguments.of("put/put-request-user-invalid-email-400.json", emailError),
            Arguments.of("put/put-request-user-invalid-id-400.json", idNullError)
    );
  }

  @Test
  @DisplayName("DELETE v1/users/{id} deletes a user when successful")
  void delete_DeletesAUser_WhenSuccessful() throws Exception {
    var userToDelete = listUsers.getFirst();

    BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));
    BDDMockito.doNothing().when(repository).delete(userToDelete);

    mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", userToDelete.getId()))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("DELETE v1/users/{id} throws ResponseStatusException when User is not found")
  void delete_ThrowsResponseStatusException_WhenUserIsNotFound() throws Exception {
    var expectedError = fileUtils.readResourceFile("user/delete/delete-response-user-by-id-404.json");
    var userId = 9992L;

    mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", userId))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.content().json(expectedError));
  }
}