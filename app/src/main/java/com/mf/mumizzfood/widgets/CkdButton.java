package com.mf.mumizzfood.widgets;

import android.content.Context;
import android.util.AttributeSet;

public class CkdButton extends android.support.v7.widget.AppCompatButton {


    /**
     * @param context
     */
    public CkdButton(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public CkdButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);

    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CkdButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CustomFontHelper.setCustomFont(this, context, attrs);

    }

}