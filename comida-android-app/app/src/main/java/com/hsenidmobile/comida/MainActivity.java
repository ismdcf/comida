package com.hsenidmobile.comida;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    ArrayAdapter<String> adapter;
    EditText inputSearch;
    ArrayList<HashMap<String, String>> productList;

    Button GoToNewActivity;
    SQLiteHelper sqLiteHelper;
    Button btnAddNewRecord;
    android.widget.LinearLayout parentLayout;
    LinearLayout layoutDisplayPeople;
    TextView tvNoRecordsFound;
    private String rowID = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAllWidgets();
        sqLiteHelper = new SQLiteHelper(this);
        bindWidgetsWithEvent();
        displayAllRecords();

        String products[] = {"Thamali Sugathadasa", "Kokila Vidyaratne", "Binu Kumar", "shanu Fernando", "Jayashani Gunaratne", "Dinuka Jayawardene"};

        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String firstname = data.getStringExtra(Constants.FIRST_NAME);
            String lastname = data.getStringExtra(Constants.LAST_NAME);

            ContactModel contact = new ContactModel();
            contact.setFirstName(firstname);
            contact.setLastName(lastname);

            if (requestCode == Constants.ADD_RECORD) {

                sqLiteHelper.insertRecord(contact);
            } else if (requestCode == Constants.UPDATE_RECORD) {
                contact.setID(rowID);

                sqLiteHelper.updateRecord(contact);
            }
            displayAllRecords();
        }
    }

    private void getAllWidgets() {
        btnAddNewRecord = (Button) findViewById(R.id.btnAddNewRecord);

        parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        layoutDisplayPeople = (LinearLayout) findViewById(R.id.layoutDisplayPeople);

        tvNoRecordsFound = (TextView) findViewById(R.id.tvNoRecordsFound);
    }

    private void bindWidgetsWithEvent() {
        btnAddNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddRecord();
            }
        });
    }

    private void onAddRecord() {
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        intent.putExtra(Constants.DML_TYPE, Constants.INSERT);
        startActivityForResult(intent, Constants.ADD_RECORD);
    }

    private void onUpdateRecord(String firstname, String lastname) {
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        intent.putExtra(Constants.FIRST_NAME, firstname);
        intent.putExtra(Constants.LAST_NAME, lastname);
        intent.putExtra(Constants.DML_TYPE, Constants.UPDATE);
        startActivityForResult(intent, Constants.UPDATE_RECORD);
    }

    private void displayAllRecords() {

        com.rey.material.widget.LinearLayout inflateParentView;
        parentLayout.removeAllViews();

        ArrayList<ContactModel> contacts = sqLiteHelper.getAllRecords();

        if (contacts.size() > 0) {
            tvNoRecordsFound.setVisibility(View.GONE);
            ContactModel contactModel;
            for (int i = 0; i < contacts.size(); i++) {

                contactModel = contacts.get(i);

                final Holder holder = new Holder();
                final View view = LayoutInflater.from(this).inflate(R.layout.inflate_recordlayout, null);
                inflateParentView = (com.rey.material.widget.LinearLayout) view.findViewById(R.id.inflateParentView);
                holder.tvFullName = (TextView) view.findViewById(R.id.tvFullName);


                view.setTag(contactModel.getID());
                holder.firstname = contactModel.getFirstName();
                holder.lastname = contactModel.getLastName();
                String personName = holder.firstname + " " + holder.lastname;
                holder.tvFullName.setText(personName);

                final CharSequence[] items = {Constants.UPDATE, Constants.DELETE};
                inflateParentView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {

                                    rowID = view.getTag().toString();
                                    onUpdateRecord(holder.firstname, holder.lastname.toString());
                                } else {
                                    AlertDialog.Builder deleteDialogOk = new AlertDialog.Builder(MainActivity.this);
                                    deleteDialogOk.setTitle("Delete Contact?");
                                    deleteDialogOk.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //sQLiteHelper.deleteRecord(view.getTag().toString());
                                                    ContactModel contact = new ContactModel();
                                                    contact.setID(view.getTag().toString());
                                                    sqLiteHelper.deleteRecord(contact);
                                                    displayAllRecords();
                                                }
                                            }
                                    );
                                    deleteDialogOk.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    deleteDialogOk.show();
                                }
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        return true;
                    }
                });
                parentLayout.addView(view);
            }
        } else {
            tvNoRecordsFound.setVisibility(View.VISIBLE);
        }
    }

    private class Holder {
        TextView tvFullName;
        String firstname;
        String lastname;
    }
}




