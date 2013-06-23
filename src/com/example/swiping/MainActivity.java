package com.example.swiping;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.almeros.android.multitouch.gesturedetectors.MoveGestureDetector;
import com.nineoldandroids.view.ViewHelper;

public class MainActivity extends Activity {
	private Block main_block;
	private ArrayList<Block> blocks = new ArrayList<Block>();
	private MoveGestureDetector mMoveDetector;
	private float moveY, moveX;
	private boolean touchMainBlock = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mMoveDetector = new MoveGestureDetector(getApplicationContext(),
				new MoveListener());
	}

	/*
	 * View can be not drawn on create
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		main_block = new Block(0, (TextView) findViewById(R.id.word_tv));
		blocks.add(new Block(1, (TextView) findViewById(R.id.answer1_tv)));
		blocks.add(new Block(2, (TextView) findViewById(R.id.answer2_tv)));
		blocks.add(new Block(3, (TextView) findViewById(R.id.answer3_tv)));
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		updateAllCoordinates();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			/*
			 * if touch started in main_block
			 */
			if (isTouchingMainBlock(event)) {
				touchMainBlock = true;
				animate(main_block.getTextView()).alpha(0.5f).setDuration(0);
			} else {
				touchMainBlock = false;
			}
			break;
		default:
			break;
		}
		if (touchMainBlock) {
			mMoveDetector.onTouchEvent(event);
			return true;
		} else {
			return false;
		}
	}

	private class MoveListener extends
			MoveGestureDetector.SimpleOnMoveGestureListener {
		public boolean onMove(MoveGestureDetector detector) {
			moveX = detector.getFocusX()
					+ main_block.getTextView().getMeasuredWidth() / 2;
			moveY = detector.getFocusY()
					+ main_block.getTextView().getMeasuredHeight() / 2;
			ViewHelper.setX(main_block.getTextView(), moveX);
			ViewHelper.setY(main_block.getTextView(), moveY);

			return true;
		}

		public boolean onMoveBegin(MoveGestureDetector detector) {

			return true;
		}

		public void onMoveEnd(MoveGestureDetector detector) {
			int maxTouchAreaBlockId = getBlockTouchingMaxArea();
			moveMainBlockOverMaxTouchingAreaBlock(maxTouchAreaBlockId);
			animate(main_block.getTextView()).alpha(1);
		}
	}

	private void updateAllCoordinates() {
		main_block.updateCoordinates();
		for (Block block : blocks) {
			block.updateCoordinates();
		}
	}

	/*
	 * if some block touching main_block - return this block, if few blocks
	 * touching main - return one with max touching area, if no blocks touching
	 * area - return null
	 */
	private int getBlockTouchingMaxArea() {
		HashMap<Integer, Float> crossAreas = new HashMap<Integer, Float>();
		for (Block block : blocks) {
			float area = main_block.isTouching(block);
			if (area != 0) {
				crossAreas.put(block.getId(), area);
			}
		}
		if (crossAreas.size() > 0) {
			Map.Entry<Integer, Float> max_area = null;
			for (Map.Entry<Integer, Float> area : crossAreas.entrySet()) {
				if (max_area == null
						|| area.getValue().compareTo(max_area.getValue()) > 0) {
					max_area = area;
				}
			}
			return max_area.getKey();
		} else {
			Log.d("tag", "no touching blocks");
			return 0;
		}
	}

	private Block getBlockById(int id) {
		for (Block block : blocks) {
			if (block.getId() == id) {
				Log.d("tag", "max area has block " + block.getId());
				return block;
			}
		}
		return null;
	}

	private void moveMainBlockOverMaxTouchingAreaBlock(int block_id) {
		if (block_id != 0) {
			Block block = getBlockById(block_id);
			ViewHelper.setX(main_block.getTextView(), block.getTextView()
					.getX());
			ViewHelper.setY(main_block.getTextView(), block.getTextView()
					.getY());
		}
	}

	/*
	 * if finger touching main_block
	 */
	public boolean isTouchingMainBlock(MotionEvent event) {
		return (event.getRawX() > main_block.getX_top())
				&& (event.getRawY() - 110 > main_block.getY_top())
				&& (event.getRawX() < main_block.getX_bottom())
				&& (event.getRawY() - 110 < main_block.getY_bottom());
	}
}
