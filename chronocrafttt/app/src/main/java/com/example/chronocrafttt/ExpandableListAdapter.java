package com.example.chronocrafttt;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.example.chronocrafttt.R;


import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> groupTitles;
    private HashMap<String, List<String>> childData;
    private DatabaseHelper dbHelper;

    public ExpandableListAdapter(Context context, List<String> groupTitles, HashMap<String, List<String>> childData) {
        this.context = context;
        this.groupTitles = groupTitles;
        this.childData = childData;
        this.dbHelper = new DatabaseHelper(context); // Initialize the DatabaseHelper
    }

    @Override
    public int getGroupCount() {
        return groupTitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childData.get(groupTitles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupTitles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childData.get(groupTitles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        }

        TextView groupTitle = convertView.findViewById(android.R.id.text1);
        groupTitle.setText(groupTitles.get(groupPosition));
        return convertView;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_note, parent, false);
        }

        TextView tvNoteContent = convertView.findViewById(R.id.tvNoteContent);
        ImageView imgMenu = convertView.findViewById(R.id.imgMenu);

        // Set the note content
        String noteContent = childData.get(groupTitles.get(groupPosition)).get(childPosition);
        tvNoteContent.setText(noteContent);

        // Handle menu actions
        imgMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.inflate(R.menu.note_options_menu); // Create this menu file in res/menu

            popupMenu.setOnMenuItemClickListener(item -> {
                String menuTitle = item.getTitle().toString(); // Get the title of the clicked item
                switch (menuTitle) {
                    case "Edit": // Match with the title in the XML menu file
                        showEditDialog(groupPosition, childPosition, noteContent);
                        return true;
                    case "Delete": // Match with the title in the XML menu file
                        confirmDelete(groupPosition, childPosition, noteContent);
                        return true;
                }
                return false;
            });


            popupMenu.show();
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    // Show dialog to edit a note
    // Replace showEditDialog with an Intent to launch EditNoteActivity
    private void showEditDialog(int groupPosition, int childPosition, String noteContent) {
        Intent intent = new Intent(context, edit_note.class);
        intent.putExtra("title", groupTitles.get(groupPosition));
        intent.putExtra("content", noteContent);
        intent.putExtra("groupPosition", groupPosition);
        intent.putExtra("childPosition", childPosition);

        // Start EditNoteActivity with a request code
        ((dashboard) context).startActivityForResult(intent, 1);
    }



    // Confirm and delete a note
    private void confirmDelete(int groupPosition, int childPosition, String noteContent) {
        // Create a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Note");
        builder.setMessage("Are you sure you want to delete this note?");

        // Add buttons
        builder.setPositiveButton("Yes", (dialog, which) -> {
            String groupTitle = groupTitles.get(groupPosition);

            // Delete the note from the database
            dbHelper.deleteNote(groupTitle, noteContent);

            // Remove the note from the list and refresh
            childData.get(groupTitle).remove(childPosition);
            notifyDataSetChanged();
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());

        // Show the dialog
        builder.show();
    }

}
