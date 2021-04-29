package com.framework.utils;

public enum ApiResourceEnum {
    RegisterAPI("/api/register"),
    ListAPI("/api/users"), //?page=3
    GetLatestMovieAPI("/latest"),
    GetNowPlayingAPI("/now_playing"),
    GetNowPopularAPI("/popular"),

    GetRequestTokenAPI("/token/new"),
    CreateGuestSessionAPI("/guest_session/new"),
    GetRatingAPI("/rating");

    String resource;

    ApiResourceEnum(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
