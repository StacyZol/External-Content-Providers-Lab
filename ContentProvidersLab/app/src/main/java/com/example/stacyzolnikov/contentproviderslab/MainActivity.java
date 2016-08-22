package com.example.stacyzolnikov.contentproviderslab;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ListView mListView;
    SimpleCursorAdapter mEventsCursorAdapter;
    Context mContext;
    private static final int CALENDAR_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mListView = (ListView) findViewById(R.id.listView);
        mEventsCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null,new String[]{CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART},new int[]{android.R.id.text1, android.R.id.text2}, 0)
         {
           // @Override
           // public View newView(Context context, Cursor cursor, ViewGroup parent) {
           //     return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent,false);
          //  }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView textTitle = (TextView) view.findViewById(android.R.id.text1);
               TextView textDate = (TextView) view.findViewById(android.R.id.text2);

                String title = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE));
                long date = Long.parseLong(getString(cursor.getColumnIndex(CalendarContract.Events.DTSTART)));
                textTitle.setText(title);
                textDate.setText(DateFormat.getDateInstance().format(date));


            }
        };

        mListView.setAdapter(mEventsCursorAdapter);
        getSupportLoaderManager().initLoader(CALENDAR_LOADER, null,this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id){
            case CALENDAR_LOADER:
                return new CursorLoader(this, CalendarContract.Events.CONTENT_URI, new String[]{ContactsContract.Contacts._ID,CalendarContract.Events.TITLE}, null, null, null);
            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    mEventsCursorAdapter.changeCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mEventsCursorAdapter.changeCursor(null);
    }
}
