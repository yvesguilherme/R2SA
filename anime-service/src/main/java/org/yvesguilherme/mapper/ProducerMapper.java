package org.yvesguilherme.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.yvesguilherme.domain.Producer;
import org.yvesguilherme.request.ProducerPostRequest;
import org.yvesguilherme.request.ProducerPutRequest;
import org.yvesguilherme.response.ProducerGetResponse;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProducerMapper {
  ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);

  /**
   * Source está no parâmetro (ProducerPostRequest)
   * Target está no que você irá retornar (Producer)
   */
  @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
  @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
  Producer toProducer(ProducerPostRequest postRequest);

  Producer toProducer(ProducerPutRequest producerPutRequest, LocalDateTime createdAt);

  ProducerGetResponse toProducerGetResponse(Producer producer);

  List<ProducerGetResponse> toProducerGetResponseList(List<Producer> producerList);

}
