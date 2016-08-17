package ch.modeso.contactsapp;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 7/29/2016.
 */
public class ContactList extends AppCompatActivity {

    private static final String TAG = ContactList.class.getName();
    ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_list);
        mListView = (ListView) findViewById(R.id.contactListView);

        new loadContacts().execute();
    }

    private class loadContacts extends AsyncTask<Void, Void, List<HashMap<String,String>>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(Void... voids) {
            String[] mProjection = { Constants.COLUMN_NAME, Constants.COLUMN_PHONE};

            Cursor cursor = getContentResolver().query(MyContactsProvider.CONTENT_URI, mProjection, null, null,null);

//            Log.d( TAG, "cursor size:" + cursor.getCount());
            List<HashMap<String, String>> contactsMaps = new ArrayList<HashMap<String, String>>();
            while (cursor.moveToNext()) {
                Log.d( TAG, cursor.getString(1) + ": " + cursor.getString(0));

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(Constants.COLUMN_NAME, cursor.getString(0));
                map.put(Constants.COLUMN_PHONE, cursor.getString(1));
                contactsMaps.add(map);
            }
            return contactsMaps;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMapList) {
            super.onPostExecute(hashMapList);

            SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(), hashMapList, R.layout.item_contact,
                    new String[] { Constants.COLUMN_NAME, Constants.COLUMN_PHONE}, new int[]{R.id.contactNameTextView, R.id.contactPhoneItemTextView});
            mListView.setAdapter(simpleAdapter);
        }
    }
}
