package in.ckd.calenderkhanado.widgets;

import android.content.Context;
import android.util.AttributeSet;

public class CkdEditText extends android.support.v7.widget.AppCompatEditText {

    public CkdEditText(Context context) {
        super(context);
    }


    public CkdEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomFontHelper.setCustomFont(this, context, attrs);

    }

    public CkdEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);

    }


}