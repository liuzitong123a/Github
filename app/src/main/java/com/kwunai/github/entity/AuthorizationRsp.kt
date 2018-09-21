package com.kwunai.github.entity

import com.kwunai.github.GithubConfig
import com.kwunai.github.anno.PoKo

@PoKo
data class AuthorizationReq(var scopes: List<String> = GithubConfig.SCOPES,
                            var note: String = GithubConfig.note,
                            var note_url: String = GithubConfig.noteUrl,
                            var client_secret: String = GithubConfig.clientSecret)


@PoKo
data class AuthorizationRsp(var id: Int,
                            var url: String,
                            var app: App,
                            var token: String,
                            var hashed_token: String,
                            var token_last_eight: String,
                            var note: String,
                            var note_url: String,
                            var created_at: String,
                            var updated_at: String,
                            var scopes: List<String>)

@PoKo
data class App(var name: String, var url: String, var client_id: String)