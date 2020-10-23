package com.example.colornote.ViewModel;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.colornote.Model.Content;
import com.example.colornote.Model.ListCheck;
import com.example.colornote.Model.Title;
import com.example.colornote.Model.Work;
import com.example.colornote.View.CheckList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataBinding {
    private static List<Title> list = new ArrayList<>();
    private static List<Title> archives = new ArrayList<>();
    private static List<Title> trash = new ArrayList<>();

    final String TAG = "Data Binding";
    final String NAME_FILE = "note.json";
    Context mContext;

    public DataBinding(Context mContext) throws IOException, JSONException {
        this.mContext = mContext;
        readSave();
    }

    public List<Title> sortList(int modeSort, List<Title> list) {
        switch (modeSort) {
            case 0:
                Collections.sort(list, new Comparator<Title>() {
                    @Override
                    public int compare(Title o1, Title o2) {
                        return o1.getDate().compareTo(o2.getDate());
                    }
                });
                break;
            case 1:
                Collections.sort(list, new Comparator<Title>() {
                    @Override
                    public int compare(Title o1, Title o2) {
                        return o1.getTitle().compareTo(o2.getTitle());
                    }
                });
                break;
            case 2:
                Collections.sort(list, new Comparator<Title>() {
                    @Override
                    public int compare(Title o1, Title o2) {
                        return o1.getColor().compareTo(o2.getColor());
                    }
                });
                break;
            case 3:
                Collections.sort(list, new Comparator<Title>() {
                    @Override
                    public int compare(Title o1, Title o2) {
                        return o1.getReminer().compareTo(o2.getReminer());
                    }
                });
                break;
        }
        return list;
    }


    public void saveJson() {
        try {
            JSONObject jsonObject = new JSONObject();
            List<String> title = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof Content) {
                    Content content = (Content) list.get(i);
                    jsonObject.put("list", "home");
                    jsonObject.put("type", "Content");
                    jsonObject.put("title", content.getTitle());
                    jsonObject.put("pass", content.getPassword());
                    jsonObject.put("reminder", content.getReminer());
                    jsonObject.put("isSet", content.getIsSetReminder());
                    jsonObject.put("note", content.getNote());
                    jsonObject.put("time", content.getDate());
                    jsonObject.put("color", content.getColor());
                    title.add(jsonObject.toString());
                }
                if (list.get(i) instanceof ListCheck) {
                    ListCheck listCheck = (ListCheck) list.get(i);
                    jsonObject.put("list", "home");
                    jsonObject.put("type", "Check");
                    jsonObject.put("title", listCheck.getTitle());
                    jsonObject.put("reminder", listCheck.getReminer());
                    jsonObject.put("pass", listCheck.getPassword());
                    jsonObject.put("isSet", listCheck.getIsSetReminder());
                    jsonObject.put("isCheck", listCheck.isCheck());
                    jsonObject.put("color", listCheck.getColor());
                    jsonObject.put("time", listCheck.getDate());
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonWork = new JSONObject();
                    for (int j = 0; j < listCheck.getWorkList().size(); j++) {
                        jsonWork.put("titlework", listCheck.getWorkList().get(j).getTitleWork());
                        jsonWork.put("isSetwork", listCheck.getWorkList().get(j).isChecked());
                        jsonArray.put(jsonWork);
                    }
                    jsonObject.put("work", jsonArray);
                    title.add(jsonObject.toString());
                }
            }
            if (!archives.isEmpty()) {
                for (int i = 0; i < archives.size(); i++) {
                    if (archives.get(i) instanceof Content) {
                        Content content = (Content) archives.get(i);
                        jsonObject.put("list", "archives");
                        jsonObject.put("type", "Content");
                        jsonObject.put("title", content.getTitle());
                        jsonObject.put("pass", content.getPassword());
                        jsonObject.put("reminder", content.getReminer());
                        jsonObject.put("isSet", content.getIsSetReminder());
                        jsonObject.put("note", content.getNote());
                        jsonObject.put("time", content.getDate());
                        jsonObject.put("color", content.getColor());
                        title.add(jsonObject.toString());
                    }
                    if (archives.get(i) instanceof ListCheck) {
                        ListCheck listCheck = (ListCheck) archives.get(i);
                        jsonObject.put("list", "archives");
                        jsonObject.put("type", "Check");
                        jsonObject.put("title", listCheck.getTitle());
                        jsonObject.put("reminder", listCheck.getReminer());
                        jsonObject.put("pass", listCheck.getPassword());
                        jsonObject.put("isSet", listCheck.getIsSetReminder());
                        jsonObject.put("isCheck", listCheck.isCheck());
                        jsonObject.put("color", listCheck.getColor());
                        jsonObject.put("time", listCheck.getDate());
                        JSONArray jsonArray = new JSONArray();
                        JSONObject jsonWork = new JSONObject();
                        for (int j = 0; j < listCheck.getWorkList().size(); j++) {
                            jsonWork.put("titlework", listCheck.getWorkList().get(j).getTitleWork());
                            jsonWork.put("isSetwork", listCheck.getWorkList().get(j).isChecked());
                            jsonArray.put(j, jsonWork);
                        }
                        jsonObject.put("work", jsonArray);
                        title.add(jsonObject.toString());
                    }
                }
            }
            if (!trash.isEmpty()) {
                for (int i = 0; i < trash.size(); i++) {
                    if (trash.get(i) instanceof Content) {
                        Content content = (Content) trash.get(i);
                        jsonObject.put("list", "trash");
                        jsonObject.put("type", "Content");
                        jsonObject.put("title", content.getTitle());
                        jsonObject.put("pass", content.getPassword());
                        jsonObject.put("reminder", content.getReminer());
                        jsonObject.put("isSet", content.getIsSetReminder());
                        jsonObject.put("note", content.getNote());
                        jsonObject.put("time", content.getDate());
                        jsonObject.put("color", content.getColor());
                        title.add(jsonObject.toString());
                    }
                    if (trash.get(i) instanceof ListCheck) {
                        ListCheck listCheck = (ListCheck) trash.get(i);
                        jsonObject.put("list", "trash");
                        jsonObject.put("type", "Check");
                        jsonObject.put("title", listCheck.getTitle());
                        jsonObject.put("reminder", listCheck.getReminer());
                        jsonObject.put("pass", listCheck.getPassword());
                        jsonObject.put("isSet", listCheck.getIsSetReminder());
                        jsonObject.put("isCheck", listCheck.isCheck());
                        jsonObject.put("color", listCheck.getColor());
                        jsonObject.put("time", listCheck.getDate());
                        JSONArray jsonArray = new JSONArray();
                        JSONObject jsonWork = new JSONObject();
                        for (int j = 0; j < listCheck.getWorkList().size(); j++) {
                            jsonWork.put("titlework", listCheck.getWorkList().get(j).getTitleWork());
                            jsonWork.put("isSetwork", listCheck.getWorkList().get(j).isChecked());
                            jsonArray.put(j, jsonWork);
                        }
                        jsonObject.put("work", jsonArray);
                        title.add(jsonObject.toString());
                    }
                }
            }
            try {
                File file1 = null;
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ColorNote");
                if (!file.exists()) {
                    file.mkdirs();
                }
                file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ColorNote", NAME_FILE);
                FileWriter fw = new FileWriter(file1);
                BufferedWriter bw = new BufferedWriter(fw);
                for (int i = 0; i < title.size(); i++) {
                    bw.write(title.get(i) + "\n");
                    System.out.println(title.get(i));
                }
                bw.close();

            } catch (FileNotFoundException f) {
                Log.e("Databinding", "" + f);
            }
        } catch (Exception e) {

        }
    }

    void readSave() throws IOException, JSONException {
        try {
            list.clear();
            archives.clear();
            trash.clear();
            List<String> responce = new ArrayList<>();
            File file = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ColorNote", NAME_FILE);
            } else {
                file = new File(mContext.getFilesDir() + "/ColorNote", NAME_FILE);
            }
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder stringBuilder = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                responce.add(line);
                stringBuilder.append(line).append("\n");
                line = br.readLine();
            }
            br.close();
            for (int i = 0; i < responce.size(); i++) {
                JSONObject jsonObject = new JSONObject(responce.get(i));
                if (jsonObject.get("type").equals("Content") && jsonObject.get("list").equals("home")) {
                    Title title = new Content(
                            jsonObject.get("title").toString(),
                            jsonObject.get("reminder").toString(),
                            jsonObject.get("isSet").toString().equals("true") ? true : false,
                            jsonObject.get("pass").toString(),
                            jsonObject.get("time").toString(),
                            jsonObject.get("color").toString(),
                            jsonObject.get("note").toString()
                    );
                    list.add(title);
                }
                if (jsonObject.get("type").equals("Check") && jsonObject.get("list").equals("home")) {
                    List<Work> works = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("work");

                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object = (JSONObject) jsonArray.get(j);
                        works.add(new Work(object.get("titlework").toString(),
                                        object.get("isSetwork").toString().equals("true") ? true : false
                                )
                        );
                    }
                    Title title = new ListCheck(
                            jsonObject.get("title").toString(),
                            jsonObject.get("reminder").toString(),
                            jsonObject.get("isSet").toString().equals("true") ? true : false,
                            jsonObject.get("pass").toString(),
                            jsonObject.get("time").toString(),
                            jsonObject.get("color").toString(),
                            jsonObject.get("isCheck").toString().equals("true") ? true : false,
                            works
                    );
                    list.add(title);
                }
                if (jsonObject.get("type").equals("Content") && jsonObject.get("list").equals("archives")) {
                    Title title = new Content(
                            jsonObject.get("title").toString(),
                            jsonObject.get("reminder").toString(),
                            jsonObject.get("isSet").toString().equals("true") ? true : false,
                            jsonObject.get("pass").toString(),
                            jsonObject.get("time").toString(),
                            jsonObject.get("color").toString(),
                            jsonObject.get("note").toString()
                    );
                    archives.add(title);
                }
                if (jsonObject.get("type").equals("Check") && jsonObject.get("list").equals("archives")) {
                    List<Work> works = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("work");

                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object = (JSONObject) jsonArray.get(j);
                        works.add(new Work(object.get("titlework").toString(),
                                        object.get("isSetwork").toString().equals("true") ? true : false
                                )
                        );
                    }
                    Title title = new ListCheck(
                            jsonObject.get("title").toString(),
                            jsonObject.get("reminder").toString(),
                            jsonObject.get("isSet").toString().equals("true") ? true : false,
                            jsonObject.get("pass").toString(),
                            jsonObject.get("time").toString(),
                            jsonObject.get("color").toString(),
                            jsonObject.get("isCheck").toString().equals("true") ? true : false,
                            works
                    );
                    archives.add(title);
                }

                if (jsonObject.get("type").equals("Content") && jsonObject.get("list").equals("trash")) {
                    Title title = new Content(
                            jsonObject.get("title").toString(),
                            jsonObject.get("reminder").toString(),
                            jsonObject.get("isSet").toString().equals("true") ? true : false,
                            jsonObject.get("pass").toString(),
                            jsonObject.get("time").toString(),
                            jsonObject.get("color").toString(),
                            jsonObject.get("note").toString()
                    );
                    trash.add(title);
                }
                if (jsonObject.get("type").equals("Check") && jsonObject.get("list").equals("trash")) {
                    List<Work> works = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("work");

                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object = (JSONObject) jsonArray.get(j);
                        works.add(new Work(object.get("titlework").toString(),
                                        object.get("isSetwork").toString().equals("true") ? true : false
                                )
                        );
                    }
                    Title title = new ListCheck(
                            jsonObject.get("title").toString(),
                            jsonObject.get("reminder").toString(),
                            jsonObject.get("isSet").toString().equals("true") ? true : false,
                            jsonObject.get("pass").toString(),
                            jsonObject.get("time").toString(),
                            jsonObject.get("color").toString(),
                            jsonObject.get("isCheck").toString().equals("true") ? true : false,
                            works
                    );
                    trash.add(title);
                }
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    public void addList(Title title) {
        list.add(title);
    }

    public List<Title> getList() {
        return list;
    }

    public void moveHomeToTrashbin(int position) {
        trash.add(list.get(position));
        list.remove(position);
        saveJson();
    }

    public void moveArchiveToTrashBin(int position) {
        trash.add(archives.get(position));
        archives.remove(position);
        saveJson();
    }

    public List<Title> getTrash() {
        return trash;
    }

    public void moveArchivesToHome(int position){
        list.add(archives.get(position));
        archives.remove(position);
        saveJson();
    }

    public void moveToArchives(int position) {
        archives.add(list.get(position));
        list.remove(position);
        saveJson();
    }

    public void rollBackItem(int position) {
        list.add(trash.get(position));
        trash.remove(position);
        saveJson();
    }

    public List<Title> getArchives() {
        return archives;
    }

    public void removeAllTrash() {
        for (int i = trash.size() - 1; i >= 0; i--) {
            trash.remove(i);
        }
        saveJson();
    }

    public void removeItem(int position) {
        trash.remove(position);
        saveJson();
    }
}
