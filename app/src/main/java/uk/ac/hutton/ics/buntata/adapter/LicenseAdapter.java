/*
 * Copyright 2016 Information & Computational Sciences, The James Hutton Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.hutton.ics.buntata.adapter;

import android.app.*;
import android.content.*;
import android.net.*;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.*;
import android.text.method.*;
import android.view.*;
import android.widget.*;

import com.transitionseverywhere.*;

import butterknife.*;
import uk.ac.hutton.ics.buntata.R;
import uk.ac.hutton.ics.buntata.activity.*;
import uk.ac.hutton.ics.buntata.util.*;

/**
 * The {@link LicenseAdapter} takes care of all the {@link License}s.
 *
 * @author Sebastian Raubach
 */
public class LicenseAdapter extends RecyclerView.Adapter<LicenseAdapter.ViewHolder>
{
	private Activity     context;
	private RecyclerView parent;

	private int expandedPosition = -1;

	static class ViewHolder extends RecyclerView.ViewHolder
	{
		View view;
		@BindView(R.id.license_view_name)
		TextView  name;
		@BindView(R.id.license_view_author)
		TextView  author;
		@BindView(R.id.license_view_homepage)
		ImageView homepage;
		@BindView(R.id.license_view_description)
		TextView  description;
		@BindView(R.id.license_view_license_text)
		TextView  licenseText;

		ViewHolder(View v)
		{
			super(v);

			view = v;
			ButterKnife.bind(this, v);
		}
	}

	public LicenseAdapter(Activity context, RecyclerView parent)
	{
		this.context = context;
		this.parent = parent;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		/* Create a new view from the layout file */
		final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.license_view, parent, false);

		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position)
	{
		final License item = License.values()[position];

		final boolean isExpanded = position == expandedPosition;

		/* Set the content */
		holder.name.setText(item.name);
		holder.author.setText(item.author);
		holder.description.setText(item.description);
		holder.licenseText.setText(StringUtils.fromHtml(item.licenseText));
		/* Make sure to respect hyperlinks in the content */
		holder.licenseText.setMovementMethod(LinkMovementMethod.getInstance());

		/* Show or hide the content */
		holder.licenseText.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
		/* Activate/deactivate the item */
		holder.itemView.setActivated(isExpanded);
		/* On click change the state */
		holder.view.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (expandedPosition != -1)
					notifyItemChanged(expandedPosition);

				expandedPosition = isExpanded ? -1 : holder.getAdapterPosition();

				/* Set a new transition */
				ChangeBounds transition = new ChangeBounds();
				/* For 150 ms */
				transition.setDuration(150);
				/* And start it */
				TransitionManager.beginDelayedTransition(parent, transition);

				/* Let the parent view know that something changed and that it needs to re-layout */
				if (expandedPosition != -1)
					notifyItemChanged(expandedPosition);
			}
		});

		/* Add a click listener */
		holder.homepage.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				/* If this is our app, give the option to show the license as well */
				if (item == License.BUNTATA)
				{
					final CharSequence[] options = new CharSequence[]{context.getString(R.string.license_view_option_view_license), context.getString(R.string.license_view_option_view_github)};

					new AlertDialog.Builder(context)
							.setTitle(R.string.license_view_option_title)
							.setItems(options, new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									switch (which)
									{
										case 0:
											context.startActivity(new Intent(context, ApacheLicenseActivity.class));
											break;
										case 1:
											context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.url)));
											break;
									}
								}
							})
							.show();
				}
				/* Else, just open the url */
				else
				{
					context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.url)));
				}
			}
		});
	}

	@Override
	public int getItemCount()
	{
		return License.values().length;
	}

	/**
	 * {@link License} is an enum holding all the libraries we use in this app with their license information
	 *
	 * @author Sebastian Raubach
	 */
	private enum License
	{
		BUNTATA("Buntata", "Information & Computational Sciences, The James Hutton Institute", "Apache License, Version 2.0", "https://github.com/knodel-app/knodel", "<p>Copyright 2016 Information & Computational Sciences, The James Hutton Institute</p><p>Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at</p><p><a href=\"http://www.apache.org/licenses/LICENSE-2.0\">http://www.apache.org/licenses/LICENSE-2.0</a></p><p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.</p>"),
		ANDROID_SLIDING_UP_PANEL("Android Sliding Up Panel", "UNKNOWN", "Apache License, Version 2.0", "https://github.com/umano/AndroidSlidingUpPanel", "<p>Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at</p><p><a href=\"http://www.apache.org/licenses/LICENSE-2.0\">http://www.apache.org/licenses/LICENSE-2.0</a></p><p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.</p>"),
		ANDROID_SUPPORT("Android Support Library", "The Android Open Source Project", "Apache License, Version 2.0", "https://github.com/android/platform_frameworks_support", "<p>Copyright (C) 2011 The Android Open Source Project</p><p>Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at</p><p><a href=\"http://www.apache.org/licenses/LICENSE-2.0\">http://www.apache.org/licenses/LICENSE-2.0</a></p><p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.</p>"),
		BUTTERKNIFE("Butter Knife", "Jake Wharton", "Apache License, Version 2.0", "https://github.com/JakeWharton/butterknife", "<p>Copyright 2013 Jake Wharton</p><p>Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at</p><p><a href=\"http://www.apache.org/licenses/LICENSE-2.0\">http://www.apache.org/licenses/LICENSE-2.0</a></p><p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.</p>"),
		CHANGELOGLIB("ChangeLog", "Gabriele Mariotti", "Apache License, Version 2.0", "https://github.com/gabrielemariotti/changeloglib", "<pCopyright 2013-2015 Gabriele Mariotti</p><p>Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at</p><p><a href=\"http://www.apache.org/licenses/LICENSE-2.0\">http://www.apache.org/licenses/LICENSE-2.0</a></p><p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.</p>"),
		CIRCLE_INDICATOR("CircleIndicator", "relex", "Apache License, Version 2.0", "https://github.com/ongakuer/CircleIndicator", "<p>Copyright (C) 2014 relex</p><p>Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at</p><p><a href=\"http://www.apache.org/licenses/LICENSE-2.0\">http://www.apache.org/licenses/LICENSE-2.0</a></p><p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.</p>"),
		GLIDE("Glide", "Google, Inc.", "2-Clause BSD License", "https://github.com/bumptech/glide", "<p>Copyright 2014 Google, Inc. All rights reserved.</p> <p>Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:</p> <ol><li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.</li> <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.</li></ol> <p>THIS SOFTWARE IS PROVIDED BY GOOGLE, INC. ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GOOGLE, INC. OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.</p> <p>The views and conclusions contained in the software and documentation are those of the authors and should not be interpreted as representing official policies, either expressed or implied, of Google, Inc.</p>"),
		IMAGE_PICKER("ImagePicker", "Esa Firman", "MIT License", "https://github.com/esafirm/android-image-picker", "<p>Copyright (c) 2016 Esa Firman</p><p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the \"Software\"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:</p><p>The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.</p><p>THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.</p>"),
		MATERIAL_DESIGN_ICONS("Material design icons", "Material Design Authors", "Apache License, Version 2.0", "https://github.com/google/material-design-icons", "<p>Copyright (C) 2015 Material Design Authors</p><p>Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at</p><p><a href=\"http://www.apache.org/licenses/LICENSE-2.0\">http://www.apache.org/licenses/LICENSE-2.0</a></p><p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.</p>"),
		MATERIAL_INTRO("material-intro", "Heinrich Reimer", "Apache License, Version 2.0", "https://github.com/HeinrichReimer/material-intro", "<p>Copyright 2016 Heinrich Reimer</p><p>Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at</p><p><a href=\"http://www.apache.org/licenses/LICENSE-2.0\">http://www.apache.org/licenses/LICENSE-2.0</a></p><p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.</p>"),
		PICASSO("Picasso", "Square, Inc.", "Apache License, Version 2.0", "https://github.com/square/picasso", "<p>Copyright 2013 Square, Inc.</p><p>Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at</p><p><a href=\"http://www.apache.org/licenses/LICENSE-2.0\">http://www.apache.org/licenses/LICENSE-2.0</a></p><p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.</p>"),
		PHOTO_VIEW("PhotoView", "Chris Banes", "Apache License, Version 2.0", "https://github.com/chrisbanes/PhotoView", "<p>Copyright 2016 Chris Banes</p><p>Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at</p><p><a href=\"http://www.apache.org/licenses/LICENSE-2.0\">http://www.apache.org/licenses/LICENSE-2.0</a></p><p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.</p>"),
		RETROFIT("Retrofit", "Square, Inc.", "Apache License, Version 2.0", "https://github.com/square/retrofit", "<p>Copyright 2013 Square, Inc.</p><p>Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at</p><p><a href=\"http://www.apache.org/licenses/LICENSE-2.0\">http://www.apache.org/licenses/LICENSE-2.0</a></p><p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.</p>"),
		SECTIONED_RECYCLERVIEW("Sectioned RecyclerView", "Aidan Follestad", "Apache License, Version 2.0", "https://github.com/afollestad/sectioned-recyclerview", "<p>Copyright 2016 Aidan Follestad</p><p>Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at</p><p><a href=\"http://www.apache.org/licenses/LICENSE-2.0\">http://www.apache.org/licenses/LICENSE-2.0</a></p><p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.</p>"),
		TRANSITIONS_EVERYWHERE("Transitions-Everywhere", "andkulikov", "Apache License, Version 2.0", "https://github.com/andkulikov/Transitions-Everywhere", "<p>Copyright 2014 andkulikov</p><p>Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at</p><p><a href=\"http://www.apache.org/licenses/LICENSE-2.0\">http://www.apache.org/licenses/LICENSE-2.0</a></p><p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.</p>"),
		ZXING("zxing", "ZXing authors", "Apache License, Version 2.0", "https://github.com/zxing/zxing", "<p>Copyright (C) 2011 ZXing authors</p><p>Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at</p><p><a href=\"http://www.apache.org/licenses/LICENSE-2.0\">http://www.apache.org/licenses/LICENSE-2.0</a></p><p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.</p>"),
		ZXING_ANDROID_EMBEDDED("ZXing Android Embedded", "ZXing authors, Journey Mobile", "Apache License, Version 2.0", "https://github.com/zxing/zxing", "<p>Copyright (C) 2012-2017 ZXing authors, Journey Mobile</p><p>Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at</p><p><a href=\"http://www.apache.org/licenses/LICENSE-2.0\">http://www.apache.org/licenses/LICENSE-2.0</a></p><p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.</p>");

		String name;
		String author;
		String description;
		String url;
		String licenseText;

		License(String name, String author, String description, String url, String licenseText)
		{
			this.name = name;
			this.author = author;
			this.description = description;
			this.url = url;
			this.licenseText = licenseText;
		}
	}
}