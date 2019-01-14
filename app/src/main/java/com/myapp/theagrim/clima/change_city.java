package com.myapp.theagrim.clima;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class change_city extends AppCompatActivity {

    private ImageButton takeBack;
    private EditText cityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city);

        takeBack= findViewById(R.id.backButton);
        cityName=findViewById(R.id.queryET);

        takeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(change_city.this,MainActivity.class));
            }
        });

        cityName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String newCity= cityName.getText().toString();
                Intent intent=new Intent(change_city.this,MainActivity.class);
                intent.putExtra("city",newCity);
                startActivity(intent);
                return false;
            }
        });

    }
}
