package com.example.colornote.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.colornote.Model.Work;
import com.example.colornote.R;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CheckListAdapter extends BaseAdapter {
    List<Work> list;
    Context mContext;
    Map<Integer, Boolean> saveState = new HashMap<>();

    public CheckListAdapter(List<Work> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.isEmpty() ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_checkbox_adapter, parent, false);
        final CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        ImageButton btnCancel = convertView.findViewById(R.id.btnCancel);
        checkBox.setText(list.get(position).getTitleWork());
        if (list.get(position).isChecked()) {
            checkBox.setChecked(true);
        }
        else checkBox.setChecked(false);
        saveState.put(position,list.get(position).isChecked());
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final EditText myEdit = new EditText(mContext);
                myEdit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("Change name Check Box")
                        .setView(myEdit)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (myEdit.getText().toString() != "") {
                                    list.get(position).setTitleWork(myEdit.getText().toString());
                                    notifyDataSetChanged();
                                } else
                                    Toast.makeText(mContext, "Bạn Chưa nhập tên check", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
                return true;
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    list.get(position).setChecked(true);
                    checkBox.setPaintFlags(checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    saveState.put(position, true);
                } else {
                    list.get(position).setChecked(false);
                    checkBox.setPaintFlags(checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    saveState.put(position, false);
                }
            }
        });
        if (saveState.get(position)) {
            checkBox.setChecked(true);
            checkBox.setPaintFlags(checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else if (!saveState.get(position)) {
            checkBox.setChecked(false);
            checkBox.setPaintFlags(checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
