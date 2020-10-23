package com.example.colornote.View;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.colornote.R;
import org.json.JSONException;
import java.io.IOException;

public class OptionMenu extends Dialog {
    String mode;
    Context mContext;
    ListView lvOption;
    TextView txtOption;
    String[] title;
    int[] drawables;

    public OptionMenu(@NonNull Context context, String mode) {
        super(context);
        this.mContext = context;
        this.mode = mode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_option_adapter);
        addControls();
        try {
            checkmode();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkmode() throws IOException, JSONException {
        switch (mode) {
            case "sort":
                sortList();
                break;
            case "view":
                showView();
                break;
            case "search":
                break;
            case "create":
                showCreate();
                break;
        }
        lvOption.setAdapter(new MenuAdapter(drawables,title,mContext,mode,this));
    }

    private void sortList() {
        drawables = new int[]{
                R.drawable.ic_baseline_access_time_24,
                R.drawable.ic_baseline_sort_by_alpha_24,
                R.drawable.ic_baseline_color_lens_24,
                R.drawable.ic_baseline_alarm_add_24
        };
        title = new String[]{
                "By created time",
                "Alphabetically",
                "By color",
                "By reminder time"
        };
    }

    private void showCreate() {
        txtOption.setText("Add");
        drawables = new int[]{
                R.drawable.ic_baseline_note_add_24,
                R.drawable.ic_baseline_check_box_24
        };
        title = new String[] {
                "Text",
                "Check box"
        };
    }

    private void showView() {
        txtOption.setText("View");
        drawables = new int[] {
                R.drawable.ic_baseline_list_24,
                R.drawable.ic_baseline_view_agenda_24,
                R.drawable.ic_baseline_grid_on_24,
                R.drawable.ic_baseline_border_all_24
        };
        title = new String[] {
                "List",
                "Details",
                "Grid",
                "Large grid"
        };
    }

    private void addControls() {
        txtOption = findViewById(R.id.txtOption);
        lvOption = findViewById(R.id.lvListOption);
    }
}
