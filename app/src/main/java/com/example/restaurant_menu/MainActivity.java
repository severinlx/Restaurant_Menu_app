package com.example.restaurant_menu;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    TextView resultView = null;
    CursorLoader cursorLoader;
    StringBuilder allMenuItems;
    StringBuilder drinks;
    StringBuilder mainDishes;
    StringBuilder appetizers;

    //Constants Needed to read information from Content Provider:
    //for JSON parser
    String FOOD_NAME = "name";
    String FOOD_PRICE = "price";
    String FOOD_TYPE = "type";
    //provider name
    String MY_PROVIDER_NAME = "content://com.example.Restaurant_Support.MyProvider/cte";
    //column Name
    String COLUMN_NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initiate the loader for retrieving menu Items:
        getSupportLoaderManager().initLoader(1, null, this);

        resultView = (TextView) findViewById(R.id.res);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    //method for the Button to display all menu items,
    // it just set  the Stringbuilder allMenuItems, that takes his value in from onLoadFinished() - as Textview
    //same for the next methods for Buttons
    public void onClickDisplayMenu(View view) {
        resultView.setText(allMenuItems);
    }

    public void onClickDisplayMain(View view) {

        resultView.setText(mainDishes);

    }

    public void onClickDisplayDrink(View view) {

        resultView.setText(drinks);
    }

    public void onClickDisplayAppetizer(View view) {

        resultView.setText(appetizers);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {


            cursorLoader = new CursorLoader(this, Uri.parse(MY_PROVIDER_NAME), null, null, null, null);
            return cursorLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {

        String menuName = "";
        String menuPrice = "";
        String jsonMenuType = "";
        JSONObject reader;
        String row = "";

        //initialize String Builders
        allMenuItems = new StringBuilder();
        mainDishes = new StringBuilder();
        drinks = new StringBuilder();
        appetizers = new StringBuilder();

        //put the pointer to the first element in cursor and check if there is data
        if(cursor.moveToFirst()) {

         /*
          1. iterate the cursor to get entries.
          2. For each entrie parse it to JSON. Get the name, price and type for foods.
          3. Save diferent food types in different Stringbuilders to be shown when one of the button is pressed
          */

            while (!cursor.isAfterLast()) {

                //get entries
                row = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));


                //parse  Strings to JSON and get name, price and type
                try {
                    reader = new JSONObject(row);
                    menuName = reader.getString(FOOD_NAME);
                    menuPrice = reader.getString(FOOD_PRICE);
                    jsonMenuType = reader.getString(FOOD_TYPE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //for all Menu items do:
                allMenuItems.append("\n" + menuName + "   " + menuPrice);

                //only for drinks do:
                if (jsonMenuType.matches("drink")) {
                    drinks.append("\n" + menuName + "   " + menuPrice);
                    //only for main Dishes do:
                } else if (jsonMenuType.matches("main")) {
                    mainDishes.append("\n" + menuName + "   " + menuPrice);
                    //only for appetizers do:
                } else if (jsonMenuType.matches("appetizer")) {
                    appetizers.append("\n" + menuName + "   " + menuPrice);
                }

                cursor.moveToNext();
            }
        }

        else {
            resultView.setText("Install and initialize  Restaurant Support to see here the Menu");
        }



    }


    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
