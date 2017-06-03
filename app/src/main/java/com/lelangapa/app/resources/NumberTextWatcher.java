package com.lelangapa.app.resources;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

/**
 * Created by andre on 12/01/17.
 */

public class NumberTextWatcher implements TextWatcher {

    private DecimalFormat df;
    private boolean hasFractionalPart;

    private EditText et;
    private TextInputLayout til;

    public NumberTextWatcher(EditText et)
    {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); //menggunakan titik sebagai separator desimal
        df = new DecimalFormat("#,###",symbols);
        this.et = et;
        hasFractionalPart = false;
    }
    public NumberTextWatcher(EditText et, TextInputLayout til) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); //menggunakan titik sebagai separator desimal
        df = new DecimalFormat("#,###",symbols);
        this.et = et;
        this.til = til;
        hasFractionalPart = false;
    }

    @SuppressWarnings("unused")
    private static final String TAG = "NumberTextWatcher";

    @Override
    public void afterTextChanged(Editable s)
    {
        et.removeTextChangedListener(this);

        try {
            int inilen, endlen;
            inilen = et.getText().length(); //mendapatkan panjang text sebelum diformat

            //TAMBAHAN UNTUK MEMBERIKAN WARNING JIKA TIDAK ADA INPUTAN
            if (til != null) {
                if (inilen<=0) {
                    til.setErrorEnabled(true);
                    til.setError("Harus diisi.");
                }
                else {
                    til.setErrorEnabled(false);
                    til.setError(null);
                }
            }

            String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
            Number n = df.parse(v);
            int cp = et.getSelectionStart(); //return posisi kursor pada index ke berapa dari text sesaat setelah merubah text
            et.setText(df.format(n));

            endlen = et.getText().length(); //mendapat panjang text setelah diformat
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= et.getText().length()) {
                Integer selprint = sel;
                //Log.v("nilai sel", selprint.toString());
                et.setSelection(sel);
            }
        } catch (NumberFormatException nfe) {
            // do nothing?
        } catch (ParseException e) {
            // do nothing?
        }

        et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        //tidak dipakai karena tidak ada desimal pada mata uang indonesia
        if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator())))
        {
            hasFractionalPart = true;
        } else {
            hasFractionalPart = false;
        }
    }

}