package ch.modeso.contactsapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText mName, mPhoneNumber;
    Button mAdd, mShowContacts;
    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    String searchQuery = "";
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mName = (EditText) findViewById(R.id.nameEditText);
        mPhoneNumber = (EditText) findViewById(R.id.phoneEditText);
        mAdd = (Button) findViewById(R.id.addButton);
        mShowContacts = (Button) findViewById(R.id.showContactsButton);

        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                searchQuery = charSequence.toString();
                if (searchQuery.length() > 3) {
                    searchContacts();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Add a new student record
                String name = mName.getText().toString();
                String phoneNumber = mPhoneNumber.getText().toString();
                if (!name.trim().equals("") && !phoneNumber.trim().equals("")) {
                    ContentValues values = new ContentValues();
                    values.put(Constants.COLUMN_NAME, mName.getText().toString());
                    values.put(Constants.COLUMN_PHONE, mPhoneNumber.getText().toString());
                    Uri uri = getContentResolver().insert(MyContactsProvider.CONTENT_URI, values);
                    if (uri != null) {
                        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
                    }

                    mName.setText("");
                    mPhoneNumber.setText("");
                }else{
                    Toast.makeText(getBaseContext(), "Empty fields!", Toast.LENGTH_LONG).show();
                }
            }
        });

        mShowContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ContactList.class);
                startActivity(intent);
            }
        });
    }

    private void searchContacts() {

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            Cursor cursor = getContentResolver()
                    .query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[]{
                                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                                    + " LIKE ? ",
                            new String[]{"%" + searchQuery + "%"}, null);
            Log.d(TAG, "cursor size:" + cursor.getCount());
            while (cursor.moveToNext()) {
                Log.d(TAG, cursor.getString(1) + ": " + cursor.getString(0));
                mPhoneNumber.setText(cursor.getString(0));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                searchContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
