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

package com.amitsid.signupwithgandfb.utils;

import com.amitsid.signupwithgandfb.R;
import com.google.api.services.plus.model.Person;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Christos Papazafeiropoulos, Dimitris Makris
 * 
 * The Android ListAdapter for the "Google+ Profiles".
 * 
 */

public class ProfilesAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Person> data;

	public ProfilesAdapter(Context context, List<Person> people) {
		mInflater = LayoutInflater.from(context);
		data = people;
	}

	public ProfilesAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	public void setData(List<Person> mList) {
		data = mList;
	}

	public List<Person> getData() {
		return data;
	}

	public void addData(List<Person> mList) {
		data.addAll(mList);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.activity_profiles_list_item, null);

			holder = new ViewHolder();

			holder.profile_name = (TextView) convertView
					.findViewById(R.id.profile_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.profile_name.setText(data.get(position).getDisplayName());

		return convertView;
	}

	class ViewHolder {
		TextView profile_name;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
}