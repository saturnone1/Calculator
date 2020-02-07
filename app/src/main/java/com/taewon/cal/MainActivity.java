package com.taewon.cal;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    EditText edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edittext = (EditText)findViewById(R.id.cal_text);
        edittext.setText("");

        findViewById(R.id.minus).setOnClickListener(sansul);
        findViewById(R.id.plus).setOnClickListener(sansul);
        findViewById(R.id.mul).setOnClickListener(sansul);
        findViewById(R.id.div).setOnClickListener(sansul);
    }

    Button.OnClickListener sansul = new Button.OnClickListener(){
        public void onClick(View v){
            switch(v.getId()){
                case R.id.minus:
                    edittext.setText(edittext.getText() + "-");break;
                case R.id.div:
                    edittext.setText(edittext.getText() + "÷");break;
                case R.id.mul:
                    edittext.setText(edittext.getText() + "×");break;
                case R.id.plus:
                    edittext.setText(edittext.getText() + "+");break;
            }
        }
    };

    public void CM(View v){
        switch(v.getId()){
            case R.id.one: edittext.setText(edittext.getText() + "1");break;
            case R.id.two: edittext.setText(edittext.getText() + "2");break;
            case R.id.three: edittext.setText(edittext.getText()+"3");break;
            case R.id.four: edittext.setText(edittext.getText()+"4");break;
            case R.id.five: edittext.setText(edittext.getText()+"5");break;
            case R.id.six: edittext.setText(edittext.getText()+"6");break;
            case R.id.seven: edittext.setText(edittext.getText()+"7");break;
            case R.id.eight: edittext.setText(edittext.getText()+"8");break;
            case R.id.nine: edittext.setText(edittext.getText()+"9");break;
            case R.id.zero: edittext.setText(edittext.getText()+"0");break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_theme) {
            Toast.makeText(this, "테마선택", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.action_save) {
            Toast.makeText(this, "기록선택", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
