package com.example.wordlist.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wordlist.R;

public class numEditDialog extends Dialog {
    EditText etNum;
    Button btnNumCancel;
    Button btnNumConfirm;
    public numEditDialog(@NonNull Context context) {
        super(context);
    }

    public numEditDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected numEditDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_num_edit);
        etNum=findViewById(R.id.etNum);
        btnNumCancel=findViewById(R.id.btnNumCancel);
        btnNumConfirm=findViewById(R.id.btnNumConfirm);
        initEvent();
    }

    private void initEvent() {
        btnNumConfirm.setOnClickListener(v -> {
        });
    }
}
