/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.holderzone.intelligencepos.mvp.model.network.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.holderzone.intelligencepos.base.Config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by tengchengwei on 2017/3/12.
 */
final class CustomGsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        String json = gson.toJson(value);
        // sign begin
        String jsonSigned = sign(json, Config.UNSIGN_KEY) + "$" + json;
        // sign end
        return RequestBody.create(MEDIA_TYPE, jsonSigned);
    }

    private static String sign(String str, String key) {
        str = str.toLowerCase();
        str = str + "&key=" + key;
        return getMd5Hash(str);
    }

    private static String getMd5Hash(String s) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(s.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        byte[] bytes = hash;
        int len = hash.length;

        for (int i = 0; i < len; ++i) {
            byte b = bytes[i];
            if ((b & 255) < 16) {
                hex.append("0");
            }

            hex.append(Integer.toHexString(b & 255));
        }

        return hex.toString();
    }
}