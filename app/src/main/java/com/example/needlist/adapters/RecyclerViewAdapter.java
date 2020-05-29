package com.example.needlist.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.needlist.MainActivity;
import com.example.needlist.R;
import com.example.needlist.model.Needs;

import java.util.ArrayList;

import static com.example.needlist.MainActivity.checkingForText;
import static com.example.needlist.MainActivity.checkingIfEmpty;
import static com.example.needlist.MainActivity.db;
import static com.example.needlist.MainActivity.name;
import static com.example.needlist.MainActivity.popup;
import static com.example.needlist.MainActivity.quantity;
import static com.example.needlist.MainActivity.save;
import static com.example.needlist.MainActivity.saveOrCancel;
import static com.example.needlist.MainActivity.updateUI;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Needs> needsArrayList;

    public RecyclerViewAdapter(Context context1 , ArrayList<Needs> needs){
        context = context1;
        needsArrayList = needs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText("Name: " + needsArrayList.get(position).getName());
        holder.quantity.setText("Quantity: " + needsArrayList.get(position).getQuantity());
        holder.size.setText("Size: " + needsArrayList.get(position).getSize());
        holder.note.setText("Note: " + needsArrayList.get(position).getNote());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Do you want to delete?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteFromDb(needsArrayList.get(position).getId());
                        dialog.cancel();
                        updateUI();
                        checkingForText();
                    }
                }).setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup = LayoutInflater.from(context).inflate(R.layout.pop_up,null,false);
                saveOrCancel = new PopupWindow(popup, RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT,true);
                saveOrCancel.showAtLocation(v, 0,0,0);
                save = popup.findViewById(R.id.save_button);
                MainActivity.cancel = popup.findViewById(R.id.cancel_button);
                MainActivity.name = popup.findViewById(R.id.nameFromPop);
                name.setText(needsArrayList.get(position).getName());
                quantity = popup.findViewById(R.id.quantityFromPop);
                quantity.setText(String.valueOf(needsArrayList.get(position).getQuantity()));
                MainActivity.size = popup.findViewById(R.id.sizeFromPop);
                MainActivity.size.setText(needsArrayList.get(position).getSize());
                MainActivity.note = popup.findViewById(R.id.noteFromPop);
                MainActivity.note.setText(needsArrayList.get(position).getNote());
                //ClickListener for save
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int quantityPart = 0;
                        String quantityField = checkingIfEmpty(quantity.getText().toString());
                        if (!quantityField.equals("")){
                            quantityPart = Integer.parseInt(quantityField);
                        }
                        String nameField = checkingIfEmpty(MainActivity.name.getText().toString());
                        String sizeField = checkingIfEmpty(MainActivity.size.getText().toString());
                        String noteField = checkingIfEmpty(MainActivity.note.getText().toString());

                        Needs need = new Needs(nameField,
                                quantityPart,
                                sizeField,
                                noteField);

                        db.updateElementOfDb(need,needsArrayList.get(position).getId());
                        checkingForText();
                        saveOrCancel.dismiss();
                        updateUI();
                    }
                });

                //ClickListener for cancel
                MainActivity.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveOrCancel.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if (needsArrayList == null)
            return 0;
        return needsArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView quantity;
        public TextView size;
        public TextView note;
        public ImageButton edit;
        public ImageButton delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            size = itemView.findViewById(R.id.size);
            note = itemView.findViewById(R.id.note);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
