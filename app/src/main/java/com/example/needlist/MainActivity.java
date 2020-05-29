package com.example.needlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.needlist.R;
import com.example.needlist.adapters.RecyclerViewAdapter;
import com.example.needlist.data.DataBaseHandler;
import com.example.needlist.model.Needs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    public static DataBaseHandler db;
    public static PopupWindow saveOrCancel;
    public static View popup;
    public static Button save;
    public static Button cancel;

    public static EditText name;
    public static EditText quantity;
    public static EditText size;
    public static EditText note;
    private static TextView clickPlus;
    private static RecyclerView recyclerView;
    private static Context context;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        db = DataBaseHandler.getInstance(this);
        FloatingActionButton add = findViewById(R.id.fab);
        clickPlus = findViewById(R.id.text);
        checkingForText();

        recyclerView = findViewById(R.id.recycler);
        updateUI();
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_up,null,false);
                saveOrCancel = new PopupWindow(popup, RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT,true);
                saveOrCancel.showAtLocation(v, 0,0,0);
                save = popup.findViewById(R.id.save_button);
                cancel = popup.findViewById(R.id.cancel_button);
                name = popup.findViewById(R.id.nameFromPop);
                quantity = popup.findViewById(R.id.quantityFromPop);
                size = popup.findViewById(R.id.sizeFromPop);
                note = popup.findViewById(R.id.noteFromPop);
                //ClickListener for save
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int quantityPart = 0;
                        String quantityField = checkingIfEmpty(quantity.getText().toString());
                        if (!quantityField.equals("")){
                            quantityPart = Integer.parseInt(quantityField);
                        }
                        String nameField = checkingIfEmpty(name.getText().toString());
                        String sizeField = checkingIfEmpty(size.getText().toString());
                        String noteField = checkingIfEmpty(note.getText().toString());

                        Needs need = new Needs(nameField,
                                quantityPart,
                                sizeField,
                                noteField);

                        db.addToDb(need);
                        checkingForText();
                        saveOrCancel.dismiss();
                        updateUI();
                    }
                });

                //ClickListener for cancel
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveOrCancel.dismiss();
                    }
                });
            }
        });

    }

    public static void checkingForText(){
        if (db.gettingAll() != null) {
            clickPlus.setVisibility(View.GONE);
        }else{
            clickPlus.setVisibility(View.VISIBLE);
        }
    }

    public static String checkingIfEmpty(String field){
        if (field.equals("")){
            return "";
        }
        return field;
    }
    public static void updateUI(){
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(context, db.gettingAll());
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}
