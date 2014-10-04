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
import com.amitsid.signupwithgandfb.utils.ProfilesAdapter;
import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.Plus.Builder;
import com.google.api.services.plus.model.PeopleFeed;
import com.google.api.services.plus.model.Person;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * @author Christos Papazafeiropoulos, Dimitris Makris
 * 
 * This Android Activity shows some aspects of the "Google+ Profile" for a
 * specific user
 * 
 */

public class ProfilesActivity extends Activity {

	private SharedPreferences prefs;

	private List<Person> people;

	private ListView list;

	private EditText search_field;

	private ImageView search_action;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupViews();
	}

	private void setupViews() {
		setContentView(R.layout.activity_profiles);

		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

		list = (ListView) findViewById(R.id.list);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				final Intent i = new Intent(ProfilesActivity.this,
						ProfileActivitiesActivity.class);
				i.putExtra("profile_id", people.get(position).getId());
				startActivity(i);
			}
		});

		search_field = (EditText) findViewById(R.id.search_field);

		search_action = (ImageView) findViewById(R.id.search_action);
		search_action.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String key = search_field.getText().toString();

				if (TextUtils.isEmpty(key))
					search_field.setError("Please enter a valid keyphrase!");
				else
					searchProfiles(key);
			}
		});
	}

	private void updateViews() {
		list.setAdapter(new ProfilesAdapter(this, people));
	}

	private void searchProfiles(String s) {
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

			Plus.People.Search searchPeople = plus.people().search();
			searchPeople.setQuery(s);
			searchPeople.setMaxResults(15L);

			PeopleFeed peopleFeed = searchPeople.execute();
			people = peopleFeed.getItems();

			updateViews();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}