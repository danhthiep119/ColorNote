package com.example.colornote.View;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.example.colornote.R;

public class InfomationDialog extends Dialog {
    Button btnAuthor, btnPrivacy, btnEmail, btnOk;
    Context mcontext;
    public InfomationDialog(@NonNull Context context) {
        super(context);
        this.mcontext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infomation_dialog);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnAuthor.setOnClickListener(listener);
        btnPrivacy.setOnClickListener(listener);
        btnEmail.setOnClickListener(listener);
        btnOk.setOnClickListener(listener);
    }
    CompoundButton.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnAuthor:
                    Intent intent=new Intent(getContext(), WebView.class);
                    intent.putExtra("PUSHDATA","https://hdpsolutions.com");
                    mcontext.startActivity(intent);
                    break;
                case R.id.btnPrivacy:
                    Intent intent1=new Intent(getContext(), WebView.class);
                    intent1.putExtra("PUSHDATA","https://hdpsolution.com/privacy_policy/CameraApps/PrivacyPolicyCameraApps.html");
                    mcontext.startActivity(intent1);
                    break;
                case R.id.btnEmail:
                    break;
                case R.id.btnOk:
                    dismiss();
                    break;
            }
        }
    };

    private void addControls() {
        btnAuthor = findViewById(R.id.btnAuthor);
        btnPrivacy = findViewById(R.id.btnPrivacy);
        btnEmail = findViewById(R.id.btnEmail);
        btnOk = findViewById(R.id.btnOk);
    }
}
