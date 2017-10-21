package br.com.mobiclub.quantoprima.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.util.Ln;

public class CustomEditText extends EditText {

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomFontEditText);
        String customFont = a.getString(R.styleable.CustomFontEditText_customFontEditText);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
		Typeface tf = null;
		try {
			tf = Typeface.createFromAsset(ctx.getAssets(), asset);
		} catch (Exception e) {
			Ln.e("Error to get typeface: " + e.getMessage());
			return false;
		}
		setTypeface(tf);
		return true;
    }

}