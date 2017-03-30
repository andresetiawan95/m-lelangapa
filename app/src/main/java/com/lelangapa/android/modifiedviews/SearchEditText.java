package com.lelangapa.android.modifiedviews;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Created by andre on 29/03/17.
 */

public class SearchEditText extends AppCompatEditText {
    public SearchEditText(Context context )
    {
        super( context );
    }

    public SearchEditText(Context context, AttributeSet attribute_set )
    {
        super( context, attribute_set );
    }

    public SearchEditText(Context context, AttributeSet attribute_set, int def_style_attribute )
    {
        super( context, attribute_set, def_style_attribute );
    }

    @Override
    public boolean onKeyPreIme( int key_code, KeyEvent event )
    {
        if ( event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP ) {
            if (this.isFocused())
            {
                // Hide cursor
                setFocusable(false);

                // Set EditText to be focusable again
                setFocusable(true);
                setFocusableInTouchMode(true);
            }
        }
        return super.onKeyPreIme( key_code, event );
    }
}
