package com.example.colornote.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.colornote.R;
import com.example.colornote.ViewModel.DataBinding;

import org.json.JSONException;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class MenuAdapter extends BaseAdapter {
    int[] drawables;
    String[] title;
    Context mContext;
    String mode;
    OptionMenu menu;
    DataBinding dataBinding;

    public MenuAdapter(int[] drawables, String[] title, Context mContext, String mode, OptionMenu menu) throws IOException, JSONException {
        this.drawables = drawables;
        this.title = title;
        this.mContext = mContext;
        this.mode = mode;
        this.menu = menu;
        dataBinding = new DataBinding(mContext);
    }


    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        return title[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu, parent, false);
        ImageView imgMenu = convertView.findViewById(R.id.imgMenu);
        TextView txtMenu = convertView.findViewById(R.id.txtMenu);
        imgMenu.setImageResource(drawables[position]);
        txtMenu.setText(title[position]);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mode) {
                    case "view":
                        switch (position) {
                            case 0:
                                if (mContext instanceof MainActivity) {
                                    ((MainActivity) mContext).rvListNote.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                                } else if (mContext instanceof ArchivesView) {
                                    ((ArchivesView) mContext).rvListNote.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                                }
                                break;
                            case 1:
                                break;
                            case 2:
                                if (mContext instanceof MainActivity) {
                                    ((MainActivity) mContext).rvListNote.setLayoutManager(new GridLayoutManager(mContext, 3));
                                } else if (mContext instanceof ArchivesView) {
                                    ((ArchivesView) mContext).rvListNote.setLayoutManager(new GridLayoutManager(mContext, 3));
                                }
                                break;
                            case 3:
                                if (mContext instanceof MainActivity) {
                                    ((MainActivity) mContext).rvListNote.setLayoutManager(new GridLayoutManager(mContext, 2));
                                }
                                if (mContext instanceof ArchivesView) {
                                    ((ArchivesView) mContext).rvListNote.setLayoutManager(new GridLayoutManager(mContext, 2));
                                }
                                break;
                        }
                        menu.dismiss();
                        break;
                    case "sort":
                        switch (position) {
                            case 0:
                                if (mContext instanceof MainActivity) {
                                    ((MainActivity) mContext).showView(new ContentAdapter(dataBinding.sortList(0, dataBinding.getList()), mContext));
                                } else if (mContext instanceof ArchivesView) {
                                    ((ArchivesView) mContext).showView(new ContentAdapter(dataBinding.sortList(0, dataBinding.getArchives()), mContext));
                                }
                                break;
                            case 1:
                                if (mContext instanceof MainActivity) {
                                    ((MainActivity) mContext).showView(new ContentAdapter(dataBinding.sortList(1, dataBinding.getList()), mContext));
                                } else if (mContext instanceof ArchivesView) {
                                    ((ArchivesView) mContext).showView(new ContentAdapter(dataBinding.sortList(1, dataBinding.getArchives()), mContext));
                                }
                                break;
                            case 2:
                                if (mContext instanceof MainActivity) {
                                    ((MainActivity) mContext).showView(new ContentAdapter(dataBinding.sortList(2, dataBinding.getList()), mContext));
                                } else if (mContext instanceof ArchivesView) {
                                    ((ArchivesView) mContext).showView(new ContentAdapter(dataBinding.sortList(2, dataBinding.getArchives()), mContext));
                                }
                                break;
                            case 3:
                                if (mContext instanceof MainActivity) {
                                    ((MainActivity) mContext).showView(new ContentAdapter(dataBinding.sortList(3, dataBinding.getList()), mContext));
                                } else if (mContext instanceof ArchivesView) {
                                    ((ArchivesView) mContext).showView(new ContentAdapter(dataBinding.sortList(3, dataBinding.getArchives()), mContext));
                                }
                                break;
                        }
                        menu.dismiss();
                        break;
                    case "create":
                        switch (position) {
                            case 0:
                                Intent intent = new Intent(mContext, InputContent.class);
                                if (mContext instanceof CalendarView) {
                                    CalendarView calendarView = (CalendarView) mContext;
                                    intent.putExtra("time",calendarView.timeReminder);
                                }
                                mContext.startActivity(intent);
                                break;
                            case 1:
                                Intent intent1 = new Intent(mContext, CheckList.class);
                                if (mContext instanceof CalendarView) {
                                    CalendarView calendarView = (CalendarView) mContext;
                                    intent1.putExtra("time",calendarView.timeReminder);
                                }
                                mContext.startActivity(intent1);
                                break;
                        }
                        menu.dismiss();
                }
            }
        });
        return convertView;
    }
}
