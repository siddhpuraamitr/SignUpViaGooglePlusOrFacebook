/* 
 * Copyright (C) 2011 iMellon
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.amitsid.signupwithgandfb;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.amitsid.signupwithgandfb.store.SharedPreferencesCredentialStore;
import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.Plus.Builder;
import com.google.api.services.plus.model.Person;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Christos Papazafeiropoulos, Dimitris Makris
 * 
 * This Android Activity presents the profile information provided by the
 * Google + API. 
 */

public class MyProfileActivity extends Activity {

	private SharedPreferences prefs;

	private ImageView profile_photo;
	private TextView profile_id, profile_name, profile_url;

	private Person profile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupViews();
		retrieveProfile();
	}

	private void setupViews() {
		setContentView(R.layout.activity_my_profile);

		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

		profile_photo = (ImageView) findViewById(R.id.profile_photo);
		profile_id = (TextView) findViewById(R.id.profile_id);
		profile_name = (TextView) findViewById(R.id.profile_name);
		profile_url = (TextView) findViewById(R.id.profile_url);
	}

	private void updateViews() {
		Drawable image = loadImage(this, profile.getImage().getUrl());
		profile_photo.setImageDrawable(image);
		profile_id.setText("Id: " + profile.getId());
		profile_name.setText("Name: " + profile.getDisplayName());
		profile_url.setText("Url: " + profile.getUrl());
	}

	private void retrieveProfile() {
		try {
			JsonFactory jsonFactory = new JacksonFactory();
			HttpTransport transport = new NetHttpTransport();

			SharedPreferencesCredentialStore credentialStore = SharedPreferencesCredentialStore
					.getInstance(prefs);

			AccessTokenResponse accessTokenResponse = credentialStore.read();

			GoogleAccessProtectedResource accessProtectedResource = new GoogleAccessProtectedResource(
					accessTokenResponse.accessToken, transport, jsonFactory,
					SharedPreferencesCredentialStore.CLIENT_ID,
					SharedPreferencesCredentialStore.CLIENT_SECRET,
					accessTokenResponse.refreshToken);

			Builder b = Plus.builder(transport, jsonFactory)
					.setApplicationName("Simple-Google-Plus/1.0");
			b.setHttpRequestInitializer(accessProtectedResource);
			Plus plus = b.build();
			profile = plus.people().get("me").execute();

			updateViews();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private Drawable loadImage(Context c, String url) {
		try {
			InputStream is = this.fetchFromUrl(url);
			Drawable d = Drawable.createFromStream(is, "src");
			return d;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public InputStream fetchFromUrl(String address) throws MalformedURLException,
			IOException {
		URL url = new URL(address);
		InputStream content = (InputStream) url.getContent();
		return content;
	}
}