package org.yvesguilherme.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.yvesguilherme.domain.Anime;
import org.yvesguilherme.request.AnimePostRequest;
import org.yvesguilherme.request.AnimePutRequest;
import org.yvesguilherme.response.AnimeGetResponse;
import org.yvesguilherme.response.AnimePostResponse;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {
  Anime toAnime(AnimePostRequest animePostRequest);

  Anime toAnime(AnimePutRequest animePutRequest);

  AnimePostResponse toAnimePostResponse(Anime anime);

  AnimeGetResponse toAnimeGetResponse(Anime anime);

  List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> animeList);

}
