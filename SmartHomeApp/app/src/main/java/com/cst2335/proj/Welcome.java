package com.cst2335.proj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Welcome extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "Welcome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button welcomeButton = (Button)findViewById(R.id.welcome_button);
        welcomeButton.setOnClickListener(new View.OnClickListener()
        {
            //@Override
            public void onClick(View v)
            {
                Toast.makeText(Welcome.this, "Smart Home version 1, by Yu Hou, Jihong Yao, Sulin Zhao, Quan Long",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Welcome.this, NavigateToolbar.class);
                startActivity(intent);
            }
        });
    }
}
