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

import java.util.List;

import com.amitsid.signupwithgandfb.store.SharedPreferencesCredentialStore;
import com.amitsid.signupwithgandfb.utils.ProfileActivitiesAdapter;
import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.Plus.Builder;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;

/**
 * @author Christos Papazafeiropoulos, Dimitris Makris
 * 
 * This Android Activity shows a list of the "Google+ Activities" for a
 * specific profile
 * 
 */

public class ProfileActivitiesActivity extends android.app.Activity {

	private SharedPreferences prefs;

	private List<Activity> activities;

	private ListView list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupViews();
		searchProfileActivities();
	}

	private void setupViews() {
		setContentView(R.layout.activity_profile_activities);

		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

		list = (ListView) findViewById(R.id.list);
	}

	private void updateViews() {
		list.setAdapter(new ProfileActivitiesAdapter(this, activities));
	}

	private void searchProfileActivities() {

		Intent i = getIntent();

		if (i.hasExtra("profile_id")) {
			try {
				JsonFactory jsonFactory = new JacksonFactory();
				HttpTransport transport = new NetHttpTransport();

				SharedPreferencesCredentialStore credentialStore = SharedPreferencesCredentialStore
						.getInstance(prefs);

				AccessTokenResponse accessTokenResponse = credentialStore
						.read();

				GoogleAccessProtectedResource accessProtectedResource = new GoogleAccessProtectedResource(
						accessTokenResponse.accessToken, transport,
						jsonFactory, SharedPreferencesCredentialStore.CLIENT_ID,
						SharedPreferencesCredentialStore.CLIENT_SECRET,
						accessTokenResponse.refreshToken);

				Builder b = Plus.builder(transport, jsonFactory)
						.setApplicationName("Simple-Google-Plus/1.0");
				b.setHttpRequestInitializer(accessProtectedResource);
				Plus plus = b.build();

				Plus.Activities.List listActivities = plus.activities().list(
						i.getStringExtra("profile_id"), "public");
				listActivities.setMaxResults(5L);

				ActivityFeed activityFeed = listActivities.execute();

				activities = activityFeed.getItems();

				updateViews();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}