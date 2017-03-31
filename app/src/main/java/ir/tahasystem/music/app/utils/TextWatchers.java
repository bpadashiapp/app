package ir.tahasystem.music.app.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;

/**
 * Created by root on 6/20/16.
 */
public class TextWatchers implements TextWatcher {

    EditText editText;

    int position;

    public TextWatchers(EditText editText) {
        this.editText = editText;
        editText.addTextChangedListener(this);

    }


    @Override
    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {


    }

    @Override
    public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {


    }

    @Override
    public void afterTextChanged(Editable arg0) {

        position = editText.getSelectionStart();

        editText.removeTextChangedListener(this);

        if (arg0.toString().trim().length() == 0) {
            editText.setText("0");
            editText.addTextChangedListener(this);
            return;
        }


        Long value = Long.parseLong(arg0.toString().replace(",", ""));
        editText.setText(formatM(value));

        editText.addTextChangedListener(this);

        try {

            editText.setSelection(position);

        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    public String formatM(long price) {

        DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###");
        return df.format(price);

    }


}
