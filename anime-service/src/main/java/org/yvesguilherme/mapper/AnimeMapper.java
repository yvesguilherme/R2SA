package org.yvesguilherme.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.yvesguilherme.domain.Anime;
import org.yvesguilherme.request.AnimePostRequest;
import org.yvesguilherme.response.AnimeGetResponse;
import org.yvesguilherme.response.AnimePostResponse;

import java.util.List;

@Mapper
public interface AnimeMapper {
  AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

  @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
  Anime toAnime(AnimePostRequest animePostRequest);

  AnimePostResponse toAnimePostResponse(Anime anime);

  AnimeGetResponse toAnimeGetResponse(Anime anime);

  List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> animeList);
}
