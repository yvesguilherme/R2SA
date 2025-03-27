package org.yvesguilherme.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.yvesguilherme.domain.Producer;
import org.yvesguilherme.request.ProducerPostRequest;
import org.yvesguilherme.request.ProducerPutRequest;
import org.yvesguilherme.response.ProducerGetResponse;
import org.yvesguilherme.response.ProducerPostResponse;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProducerMapper {
  /**
   * Source está no parâmetro (ProducerPostRequest)
   * Target está no que você irá retornar (Producer)
   */
  @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
  @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
  Producer toProducer(ProducerPostRequest postRequest);

  @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
  Producer toProducer(ProducerPutRequest producerPutRequest);

  ProducerGetResponse toProducerGetResponse(Producer producer);

  ProducerPostResponse toProducerPostResponse(Producer producer);

  List<ProducerGetResponse> toProducerGetResponseList(List<Producer> producerList);

}
