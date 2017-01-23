package com.qq.googleplay.ui.animation.listviewanim.itemmanipulation;

import android.view.View;

/**
 * An OnTouchListener that should be used when list-view items can be swiped horizontally.
 */
public interface SwipeOnTouchListener extends View.OnTouchListener {
    /**
     * @return True if the user is currently swiping a list-item horizontally.
     */
    public boolean isSwiping();
}
