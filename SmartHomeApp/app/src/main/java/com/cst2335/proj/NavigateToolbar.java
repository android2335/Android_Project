package com.cst2335.proj;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class NavigateToolbar extends AppCompatActivity {
    private String newMessage;

    private void setMessage(String newMessage){
        this.newMessage = newMessage;
    }
    String getMessage(){
        return newMessage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){

        switch(mi.getItemId()){
            case R.id.SmartLivingRoom:
                setMessage("You selected Living Room");
                startActivity(new Intent(NavigateToolbar.this, LivingRoomItemListActivity.class));
                return true;

            case R.id.SmartKitchen:
                //startActivity(new Intent(NavigateToolbar.this, KitchenActivity.class));
                return true;

            case R.id.SmartHouseSettings:
                startActivity(new Intent(NavigateToolbar.this, HouseActivity.class));
                return true;

            case R.id.SmartAutomobile:
                startActivity(new Intent(NavigateToolbar.this, Automobile.class));
                return true;

        }
        return false;
    }

}
