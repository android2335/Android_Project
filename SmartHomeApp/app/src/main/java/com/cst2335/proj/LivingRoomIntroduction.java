package com.cst2335.proj;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LivingRoomIntroduction extends AppCompatActivity {
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
        setContentView(R.layout.activity_livingroom_introduction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.livingroom_introduction_menu, m );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){

        switch(mi.getItemId()){
            case R.id.Setting_LivingRoom:
                setMessage("Show Living Room Settings");
                startActivity(new Intent(LivingRoomIntroduction.this, LivingRoomItemListActivity.class));
                return true;

            case R.id.About_LivingRoom:
                Toast.makeText(LivingRoomIntroduction.this, "About Menu is clicked",Toast.LENGTH_LONG).show();
                setMessage("Help Menu clicked");

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_help);
                dialog.setTitle("Help Instruction");

                // set the custom dialog components - text, image and button
                TextView help_header = (TextView) dialog.findViewById(R.id.helpHeader);
                help_header.setText(R.string.help_header);

                TextView help_author = (TextView) dialog.findViewById(R.id.helpAuthor);
                help_author.setText(R.string.help_author);

                TextView help_version = (TextView) dialog.findViewById(R.id.helpVersion);
                help_version.setText(R.string.help_version);

                TextView help_body = (TextView) dialog.findViewById(R.id.helpBody);
                help_body.setText(R.string.help_body);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                return true;

        }
        return false;
    }

}
