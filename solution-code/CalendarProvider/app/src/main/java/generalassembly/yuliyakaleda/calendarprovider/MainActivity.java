package generalassembly.yuliyakaleda.calendarprovider;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
  private static final String TAG = "MainActivity";
  private static final int CALENDAR_LOADER = 0;
  private CursorAdapter mCursorAdapter;
  private ListView lv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    lv = (ListView) findViewById(R.id.lv);

    mCursorAdapter = new CursorAdapter(this, null, 0) {
      @Override
      public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
      }

      @Override
      public void bindView(View view, Context context, Cursor cursor) {
        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

        String title = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE));
        long startTime = cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTSTART));

        text1.setText(title);
        text2.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(startTime));
      }
    };
    lv.setAdapter(mCursorAdapter);

    getSupportLoaderManager().initLoader(CALENDAR_LOADER,null,this);

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, final long eventId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                    Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
                    Log.d(TAG,"Deleting uri: "+uri.toString());
                    getContentResolver().delete(uri, null, null);
                  }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                  }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
      }
    });

  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    switch (id){
      case CALENDAR_LOADER:
        return new CursorLoader(
                this,
                CalendarContract.Events.CONTENT_URI,
                null,
                null,
                null,
                CalendarContract.Events.DTSTART+" DESC");
      default:
        return null;
    }
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    mCursorAdapter.changeCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    mCursorAdapter.changeCursor(null);
  }
}


