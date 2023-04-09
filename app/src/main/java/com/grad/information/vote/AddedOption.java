package com.grad.information.vote;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AddedOption {
    private EditText editText;
    private OptionDeleteIV optionDeleteIV;
    private LinearLayout linearLayout;

    public AddedOption(EditText editText, OptionDeleteIV optionDeleteIV, LinearLayout linearLayout) {
        this.editText = editText;
        this.optionDeleteIV = optionDeleteIV;
        this.linearLayout = linearLayout;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public OptionDeleteIV getOptionDeleteIV() {
        return optionDeleteIV;
    }

    public void setOptionDeleteIV(OptionDeleteIV optionDeleteIV) {
        this.optionDeleteIV = optionDeleteIV;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }
}
