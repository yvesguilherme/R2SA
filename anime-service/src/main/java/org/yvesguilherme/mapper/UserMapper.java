package org.yvesguilherme.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.yvesguilherme.domain.User;
import org.yvesguilherme.request.UserPostRequest;
import org.yvesguilherme.request.UserPutRequest;
import org.yvesguilherme.response.UserGetResponse;
import org.yvesguilherme.response.UserPostResponse;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
  UserGetResponse toUserGetResponse(User user);

  List<UserGetResponse> toUserGetResponseList(List<User> userList);

  @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
  User toUser(UserPostRequest userPostRequest);

  User toUser(UserPutRequest userPutRequest);

  UserPostResponse toUserPostResponse(User user);
}
