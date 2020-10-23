package com.example.colornote.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.colornote.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SetPassword extends AppCompatActivity {
    TextInputEditText txtPassword,txtConfirmPass;
    TextInputLayout ViewPass,ViewConfirm;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        addControls();
        addEvents();
    }

    private void addEvents() {

    }

    private void addControls() {
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPass = findViewById(R.id.txtConfirmPass);
        ViewPass= findViewById(R.id.ViewPass);
        ViewConfirm = findViewById(R.id.ViewConfirm);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_save){
            setPassword(txtPassword.getText().toString(),txtConfirmPass.getText().toString());
        }
        else return super.onOptionsItemSelected(item);
        return true;
    }

    private void setPassword(String pass,String confirm) {
        boolean isTrue = false;
        if(pass.isEmpty()){
            ViewPass.setErrorEnabled(true);
            ViewPass.setError("Bạn Chưa nhập Pass");
            ViewPass.requestFocus();
        }
        if(confirm.isEmpty()){
            ViewConfirm.setErrorEnabled(true);
            ViewConfirm.setError("Bạn chưa nhập lại mật khẩu");
            ViewPass.requestFocus();
        }

        if(!pass.isEmpty()&&!confirm.isEmpty()&&pass.equals(confirm)){
            isTrue = true;
        }
        else if(!pass.isEmpty()) {
            ViewConfirm.setErrorEnabled(true);
            ViewConfirm.setError("Bạn nhập sai mật khẩu");
            ViewPass.requestFocus();
        }
        if(isTrue){
            Intent intent =new Intent();
            intent.putExtra("pass",pass);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
//        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_password,menu);
        return super.onCreateOptionsMenu(menu);
    }
}