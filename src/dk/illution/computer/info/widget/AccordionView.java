/**
 * The MIT License
 *
 * Copyright (c) 2011 Sentaca Poland sp. z o.o. / http://sentaca.com/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package dk.illution.computer.info.widget;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.view.InputEvent;
import dk.illution.computer.info.ExpandAnimation;

import dk.illution.computer.info.R;

public class AccordionView extends LinearLayout {

	private boolean initialized = false;

	// -- from xml parameter
	private int headerLayoutId;
	private int headerFoldButton;
	private int headerLabel;
	private int sectionContainer;
	private int sectionContainerParent;
	private int sectionBottom;

	private String[] sectionHeaders;
	public boolean useAnimations = true;

	private View[] children;
	private View[] wrappedChildren;

	public static int collapseExpandSpeed = 250;

	private Map<Integer, View> sectionByChildId = new HashMap<Integer, View>();

	public AccordionView(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.accordion);
			headerLayoutId = a.getResourceId(
					R.styleable.accordion_header_layout_id, 0);
			headerFoldButton = a.getResourceId(
					R.styleable.accordion_header_layout_fold_button_id, 0);
			headerLabel = a.getResourceId(
					R.styleable.accordion_header_layout_label_id, 0);
			sectionContainer = a.getResourceId(
					R.styleable.accordion_section_container, 0);
			sectionContainerParent = a.getResourceId(
					R.styleable.accordion_section_container_parent, 0);
			sectionBottom = a.getResourceId(
					R.styleable.accordion_section_bottom, 0);
			int sectionheadersResourceId = a.getResourceId(
					R.styleable.accordion_section_headers, 0);

			if (sectionheadersResourceId == 0) {
				throw new IllegalArgumentException(
						"Please set section_headers as reference to strings array.");
			}
			sectionHeaders = getResources().getStringArray(
					sectionheadersResourceId);
		}

		if (headerLayoutId == 0 || headerLabel == 0 || sectionContainer == 0
				|| sectionContainerParent == 0 || sectionBottom == 0) {
			throw new IllegalArgumentException(
					"Please set all header_layout_id,  header_layout_label_id, section_container, section_container_parent and section_bottom attributes.");
		}

		setOrientation(VERTICAL);
	}

	public View getChildById(int id) {
		for (int i = 0; i < wrappedChildren.length; i++) {
			View v = wrappedChildren[i].findViewById(id);
			if (v != null) {
				return v;
			}
		}
		return null;
	}

	public View getSectionByChildId(int id) {
		return sectionByChildId.get(id);
	}

	private View getView(final LayoutInflater inflater, int i) {
		final View container = inflater.inflate(sectionContainer, null);
		container.setLayoutParams(new ListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 0));
		final ViewGroup newParent = (ViewGroup) container
				.findViewById(sectionContainerParent);
		newParent.addView(children[i]);
		if (container.getId() == -1) {
			container.setId(i);
		}
		return container;
	}

	private View getViewFooter(LayoutInflater inflater) {
		return inflater.inflate(sectionBottom, null);
	}

	private View getViewHeader(LayoutInflater inflater, final int position) {
		final View view = inflater.inflate(headerLayoutId, null);
		((TextView) view.findViewById(headerLabel))
				.setText(sectionHeaders[position]);

		// -- support for no fold button
		if (headerFoldButton == 0) {
			return view;
		}

		final View foldButton = view.findViewById(headerFoldButton);

		if (foldButton instanceof ToggleImageLabeledButton) {
			final ToggleImageLabeledButton toggleButton = (ToggleImageLabeledButton) foldButton;
			toggleButton
					.setState(wrappedChildren[position].getVisibility() == VISIBLE);
		}

		final OnClickListener onClickListener = new OnClickListener() {

			public void onClick(View v) {
				if (useAnimations) {
					ExpandAnimation expandAnimation = new ExpandAnimation(wrappedChildren[position], collapseExpandSpeed);

					wrappedChildren[position].startAnimation(expandAnimation);
				} else {
					if (wrappedChildren[position].getVisibility() == VISIBLE) {
						wrappedChildren[position].setVisibility(GONE);
					} else {
						wrappedChildren[position].setVisibility(VISIBLE);
					}
				}
			}
		};

		final OnTouchListener onTouchListener = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				/*if (event.getAction() == MotionEvent.ACTION_DOWN) {
						Log.d("ComputerInfo", "Shit got pushed on!");
						wrappedChildren[position].findViewById(R.id.header_shape)
				} else if (event.getAction() == MotionEvent.ACTION_UP) {

				}*/

				return false;
			}
		};

		foldButton.setOnClickListener(onClickListener);
		foldButton.setOnTouchListener(onTouchListener);
		view.setOnTouchListener(onTouchListener);
		view.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				onClickListener.onClick(v);

				if (foldButton instanceof ToggleImageLabeledButton) {
					final ToggleImageLabeledButton toggleButton = (ToggleImageLabeledButton) foldButton;
					toggleButton.setState(wrappedChildren[position]
							.getVisibility() == VISIBLE);
				}

			}
		});

		return view;
	}

	@Override
	protected void onFinishInflate() {
		if (initialized) {
			super.onFinishInflate();
			return;
		}

		final int childCount = getChildCount();
		children = new View[childCount];
		wrappedChildren = new View[childCount];

		if (sectionHeaders.length != childCount) {
			throw new IllegalArgumentException(
					"Section headers string array length must be equal to accordion view child count.");
		}

		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		for (int i = 0; i < childCount; i++) {
			children[i] = getChildAt(i);
		}
		removeAllViews();

		for (int i = 0; i < childCount; i++) {
			wrappedChildren[i] = getView(inflater, i);
			View header = getViewHeader(inflater, i);
			View footer = getViewFooter(inflater);
			final LinearLayout section = new LinearLayout(getContext());
			section.setOrientation(LinearLayout.VERTICAL);
			section.addView(header);
			section.addView(wrappedChildren[i]);
			section.addView(footer);

			sectionByChildId.put(children[i].getId(), section);

			addView(section);
		}

		initialized = true;

		super.onFinishInflate();
	}

}
