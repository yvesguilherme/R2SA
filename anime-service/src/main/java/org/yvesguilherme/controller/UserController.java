package org.yvesguilherme.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yvesguilherme.mapper.UserMapper;
import org.yvesguilherme.request.UserPostRequest;
import org.yvesguilherme.request.UserPutRequest;
import org.yvesguilherme.response.UserGetResponse;
import org.yvesguilherme.response.UserPostResponse;
import org.yvesguilherme.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
  private final UserMapper userMapper;
  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<UserGetResponse>> findAll() {
    log.debug("Requesting all users");

    var userList = userService.findAll();
    var userGetResponseList = userMapper.toUserGetResponseList(userList);

    return ResponseEntity.ok(userGetResponseList);
  }

  @GetMapping("{id}")
  public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
    log.debug("Request to find a User by id: {}", id);

    var user = userService.findByIdOrThrowNotFound(id);
    var userGetResponse = userMapper.toUserGetResponse(user);

    return ResponseEntity.ok(userGetResponse);
  }

  @GetMapping("firstName/{firstName}")
  public ResponseEntity<List<UserGetResponse>> findByFirstName(@PathVariable String firstName) {
    log.debug("Request to find a list of Users by first name: {}", firstName);

    var userList = userService.findByFirstName(firstName);
    var userGetResponseList = userMapper.toUserGetResponseList(userList);

    return ResponseEntity.ok(userGetResponseList);
  }

  @GetMapping("lastName/{lastName}")
  public ResponseEntity<List<UserGetResponse>> findByLastName(@PathVariable String lastName) {
    log.debug("Request to find a list of Users by last name: {}", lastName);

    var userList = userService.findByLastName(lastName);
    var userGetResponseList = userMapper.toUserGetResponseList(userList);

    return ResponseEntity.ok(userGetResponseList);
  }

  @GetMapping("email/{email}")
  public ResponseEntity<UserGetResponse> findByEmail(@PathVariable String email) {
    log.debug("Request to find a User by email: {}", email);

    var user = userService.findByEmailOrThrowNotFound(email);
    var userGetResponse = userMapper.toUserGetResponse(user);

    return ResponseEntity.ok(userGetResponse);
  }

  @PostMapping
  public ResponseEntity<UserPostResponse> save(@RequestBody @Valid UserPostRequest userPostRequest) {
    log.debug("Request to save a User: {}", userPostRequest);

    var userToSave = userMapper.toUser(userPostRequest);
    var userSaved = userService.save(userToSave);
    var userPostResponse = userMapper.toUserPostResponse(userSaved);

    return ResponseEntity.status(HttpStatus.CREATED).body(userPostResponse);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    log.debug("Request to delete a User by id: {}", id);

    userService.delete(id);

    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<Void> update(@RequestBody @Valid UserPutRequest userPutRequest) {
    log.debug("Request to update a User: {}", userPutRequest);

    var userToUpdate = userMapper.toUser(userPutRequest);

    userService.update(userToUpdate);

    return ResponseEntity.noContent().build();
  }

}
