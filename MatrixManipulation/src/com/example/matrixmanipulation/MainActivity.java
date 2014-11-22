package com.example.matrixmanipulation;

import java.util.Random;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

	Button generate;
	EditText size;
	TextView computationTime;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        generate = (Button) findViewById(R.id.generateButton);
        size = (EditText) findViewById(R.id.matrixSize);
        computationTime = (TextView) findViewById(R.id.computationTime);
        
        generate.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                    	int matrixSize;
                    	try{
                    		matrixSize = Integer.parseInt(size.getText().toString());
                    		computationTime.setText("the size of matrix is : "+matrixSize);
                    	}
                    	catch(NumberFormatException e){
                    		computationTime.setText("Error!!!! You need to input a number");
                    	}
                    }
                }
        );
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    

    
    
    
    
    
    
}
