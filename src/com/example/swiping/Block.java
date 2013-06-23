package com.example.swiping;

import android.util.Log;
import android.widget.TextView;

public class Block {
	private int id;
	private TextView textView;
	private float x_start;
	private float y_top;
	private float x_end;
	private float y_bottom;

	public Block(int id, TextView textView) {
		this.id = id;
		this.textView = textView;
		this.x_start = textView.getX();
		this.y_top = textView.getY();
		this.x_end = textView.getX() + textView.getMeasuredWidth();
		this.y_bottom = textView.getY() + textView.getMeasuredHeight();
	}

	public float getX_top() {
		return x_start;
	}

	public void setX_top(float x_top) {
		this.x_start = x_top;
	}

	public float getY_top() {
		return y_top;
	}

	public void setY_top(float y_top) {
		this.y_top = y_top;
	}

	public float getX_bottom() {
		return x_end;
	}

	public void setX_bottom(float x_bottom) {
		this.x_end = x_bottom;
	}

	public float getY_bottom() {
		return y_bottom;
	}

	public void setY_bottom(float y_bottom) {
		this.y_bottom = y_bottom;
	}

	public TextView getTextView() {
		return textView;
	}

	public void setTextView(TextView textView) {
		this.textView = textView;
	}

	public String getText() {
		return this.textView.getText().toString();
	}

	public void setText(String text) {
		this.textView.setText(text);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float isTouching(Block block) {
		float area = 0;

		float x_start_area = 0;
		float x_end_area = 0;
		float y_top_area = 0;
		float y_bottom_area = 0;

		float x_start2 = block.getX_top();
		float y_top2 = block.getY_top();
		float x_end2 = block.getX_bottom();
		float y_bottom2 = block.getY_bottom();

		boolean touchByRight = ((x_end > x_start2) && (x_start < x_start2));
		if (touchByRight) {
			x_start_area = x_start2;
			x_end_area = x_end;
		}

		boolean touchByLeft = ((x_start < x_end2) && (x_end > x_end2));
		if (touchByLeft) {
			x_start_area = x_start;
			x_end_area = x_end2;
		}

		boolean touchByTop = ((y_bottom > y_top2) && (y_top < y_top2));
		if (touchByTop) {
			y_top_area = y_top2;
			y_bottom_area = y_bottom;
		}

		boolean touchByBottom = ((y_top < y_bottom2) && (y_bottom > y_bottom2));
		if (touchByBottom) {
			y_top_area = y_top;
			y_bottom_area = y_bottom2;
		}

		// Log.d("tag", "x_end = " + x_end + " x_start = " + x_start +
		// " y_top = "
		// + y_top + " y_bottom = " + y_bottom);
		// Log.d("tag", "x_end2 = " + x_end2 + " x_start2 = " + x_start2
		// + " y_top2 = " + y_top2 + " y_bottom2 = " + y_bottom2);
		// Log.d("tag", "touchByRight = " + touchByRight + " touchByLeft = "
		// + touchByLeft + " touchByTop = " + touchByTop
		// + " touchByBottom = " + touchByBottom);
		// Log.d("tag", "x_start_area = " + x_start_area + " x_end_area = "
		// + x_end_area + " y_top_area = " + y_top_area
		// + " y_bottom_area = " + y_bottom_area);
		area = (x_end_area - x_start_area) * (y_bottom_area - y_top_area);

		Log.d("tag", "area = " + area);
		if ((touchByRight || touchByLeft) && (touchByTop || touchByBottom)) {
			return area;
		} else {
			return 0;
		}
	}

	public void updateCoordinates() {
		this.x_start = textView.getX();
		this.y_top = textView.getY();
		this.x_end = textView.getX() + textView.getMeasuredWidth();
		this.y_bottom = textView.getY() + textView.getMeasuredHeight();
	}

}
