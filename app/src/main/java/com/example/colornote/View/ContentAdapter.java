package com.example.colornote.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colornote.Model.ColorList;
import com.example.colornote.Model.Content;
import com.example.colornote.Model.ListCheck;
import com.example.colornote.Model.Title;
import com.example.colornote.R;
import com.example.colornote.ViewModel.DataBinding;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.DataViewHolder> {
    List<Title> list;
    Context mContext;
    ColorList colorList = new ColorList();

    public ContentAdapter(List<Title> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ContentAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_content_adapter, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentAdapter.DataViewHolder holder, final int position) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("SaveSetting", mContext.MODE_PRIVATE);
        String fontsize = sharedPreferences.getString("default-font-size", "Medium");
        if (fontsize.equals("Medium")) {
            holder.txtTitle.setTextSize(16f);
            holder.txtContentNote.setTextSize(16f);
        } else if (fontsize.equals("Small")) {
            holder.txtTitle.setTextSize(12f);
            holder.txtContentNote.setTextSize(12f);
        } else if (fontsize.equals("Lagre")) {
            holder.txtTitle.setTextSize(20f);
            holder.txtContentNote.setTextSize(20f);
        }

        if ((list.get(position) instanceof Content)) {
            Content content = (Content) list.get(position);
            holder.txtTitle.setText(content.getTitle());
            holder.txtTime.setText(list.get(position).getDate());
            if (!list.get(position).getPassword().equals("")) {
                holder.txtContentNote.setText("");
            }
            else holder.txtContentNote.setText(content.getNote());
            holder.item_view.setBackgroundColor(Color.parseColor(colorList.backgroundColor[Integer.parseInt(list.get(position).getColor())]));
        } else if ((list.get(position) instanceof ListCheck)) {
            ListCheck listCheck = (ListCheck) list.get(position);
            holder.txtTitle.setText(listCheck.getTitle());
            holder.txtTime.setText(list.get(position).getDate());
            holder.item_view.setBackgroundColor(Color.parseColor(colorList.backgroundColor[Integer.parseInt(list.get(position).getColor())]));
        }
        if (list.get(position).getPassword().equals("")) {
            holder.imgLock.setVisibility(View.INVISIBLE);
        } else {
            holder.imgLock.setVisibility(View.VISIBLE);
        }
        if (list.get(position).getIsSetReminder()) {
            holder.imgReminder.setVisibility(View.VISIBLE);
        } else {
            holder.imgReminder.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.get(position).getPassword().equals("")) {
                    final EditText myEdit = new EditText(mContext);
                    myEdit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setTitle("Enter password:")
                            .setView(myEdit)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (myEdit.getText().toString() != "") {
                                        if (myEdit.getText().toString().equals(list.get(position).getPassword())) {
                                            try {
                                                openInfomation(position);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else
                                            Toast.makeText(mContext, "Bạn nhập  sai mật khẩu! Xin vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(mContext, "Bạn Chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
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
                } else {
                    try {
                        openInfomation(position);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    void openInfomation(int position) throws IOException, JSONException {
        String view = "";
        String type = "";
        int passingPosition = -1;
        DataBinding dataBinding = new DataBinding(mContext);
        if (mContext instanceof MainActivity) {
            view = "Main";
            passingPosition = position;
        } else if (mContext instanceof ArchivesView) {
            view = "Archives";
            passingPosition = position;
        } else if (mContext instanceof TrashCanView) {
            view = "Trash";
            passingPosition = position;
        } else if (mContext instanceof SearchView) {
            view = "Search";
            for (int i = 0; i < dataBinding.getList().size(); i++) {
                if (!dataBinding.getList().isEmpty()) {
                    if (list.get(position).getTitle().equals(dataBinding.getList().get(i).getTitle()) &&
                            list.get(position).getDate().equals(dataBinding.getList().get(i).getDate())
                    ) {
                        passingPosition = i;
                        type = "Home";
                        break;
                    }
                }
                if (!dataBinding.getArchives().isEmpty()) {
                    if (list.get(position).getTitle().equals(dataBinding.getArchives().get(i).getTitle()) &&
                            list.get(position).getDate().equals(dataBinding.getArchives().get(i).getDate())
                    ) {
                        passingPosition = i;
                        type = "Archives";
                        break;
                    }
                }
            }
        } else if (mContext instanceof CalendarView) {
            view = "Calendar";
            if (!dataBinding.getList().isEmpty()) {
                for (int i = 0; i < dataBinding.getList().size(); i++) {
                    if (list.get(position).getTitle().equals(dataBinding.getList().get(i).getTitle()) &&
                            list.get(position).getReminer().equals(dataBinding.getList().get(i).getReminer()) &&
                            dataBinding.getList().get(i).isSetReminder()
                    ) {
                        passingPosition = i;
                        type = "Home";
                        break;
                    }
                }
            }
            if (!dataBinding.getArchives().isEmpty()) {
                for (int i = 0; i < dataBinding.getArchives().size(); i++) {
                    if (list.get(position).getTitle().equals(dataBinding.getArchives().get(i).getTitle()) &&
                            list.get(position).getReminer().equals(dataBinding.getArchives().get(i).getReminer()) &&
                            dataBinding.getArchives().get(i).isSetReminder()
                    ) {
                        passingPosition = i;
                        type = "Archives";
                        break;
                    }
                }
            }
        }
        if (list.get(position) instanceof Content) {
            Intent intent = new Intent(mContext, ShowNotes.class);
            intent.putExtra("position", passingPosition);
            intent.putExtra("view", view);
            if (view.equals("Calendar") || view.equals("Search")) {
                intent.putExtra("type", type);
            }
            mContext.startActivity(intent);
        }
        else if (list.get(position) instanceof ListCheck) {
            Intent intent2 = new Intent(mContext, CheckList.class);
            intent2.putExtra("position", passingPosition);
            intent2.putExtra("mode", 1);
            intent2.putExtra("view", view);
            if (view.equals("Calendar") || view.equals("Search")) {
                intent2.putExtra("type", type);
            }
            mContext.startActivity(intent2);
        }
    }

    @Override
    public int getItemCount() {
        return list.isEmpty() ? 0 : list.size();
    }


    public class DataViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtContentNote, txtTime;
        ImageView imgLock, imgReminder;
        LinearLayout item_view;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContentNote = itemView.findViewById(R.id.txtContentNote);
            txtTitle = itemView.findViewById(R.id.txtTilte);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgLock = itemView.findViewById(R.id.imgLock);
            imgReminder = itemView.findViewById(R.id.imgReminder);
            item_view = itemView.findViewById(R.id.item_view);
        }
    }
}
