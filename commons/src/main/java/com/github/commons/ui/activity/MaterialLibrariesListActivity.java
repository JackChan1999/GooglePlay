package com.github.commons.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.commons.R;


public class MaterialLibrariesListActivity extends Activity {

    Context mContext;
    Activity mActivity;
    ListView sdkFunctionalityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_list);
        mContext = MaterialLibrariesListActivity.this;
        mActivity = MaterialLibrariesListActivity.this;

        String[] sdkFunctionalityListValue = new String[]{
                "Material Design Library",/* 0 */
                "Drawer Arrow Drawable",/* 1 */
                "Material Tabs",/* 2 */
                "Pager Sliding Tab Strip",/* 3 */
                "Material Ripple",/* 4 */
                "Ripple Effect",/* 5 */
                "L Drawer",/* 6 */
                "Material Design Icons",/* 7 */
                "Android Material Design Toolbar",/* 8 */
                "Material Edit Text",/* 9 */
                "Material Menu",/* 10 */
                "Material Dialogs",/* 11 */
                "Alert Dialog Pro",/* 12 */
                "Material Navigation Drawer",/* 13 */
                "Material Dialog",/* 14 */
                "Materialish Progress",/* 15 */
                "Floating Action Button",/* 16 */
                "Android Floating Action Button",/* 17 */
                "Snack Bar",/* 18 */
                "Circular Reveal",/* 19 */
                "Material Range Bar",/* 20 */
                "Lollipop AppCompat Widgets Skeleton",/* 21 */
                "Carbon",/* 22 */
                "Material Calendar View",/* 23 */
                "Material",/* 24 */
                "Wave View",/* 25 */
                "Reside Layout",/* 26 */
                "Android Swipe Layout",/* 27 */
                "Free Flow",/* 28 */
                "Swipe Back Layout",/* 29 */
                "Maskable Layout",/* 30 */
                "Expandable Layout",/* 31 */
                "Android Pull Refresh Layout",/* 32 */
                "Tile View",/* 33 */
                "Showcase View",/* 34 */
                "Ultra Pull To Refresh",/* 35 */
                "Android View Hover",/* 36 */
                "Draggable Panel",/* 37 */
                "Pull to Refresh Rentals Android",/* 38 */
                "Pull to Refresh Tours",/* 39 */
                "Inbox Layout",/* 40 */
                "Swipe Back",/* 41 */
                "Arc Layout",/* 42 */
                "Dragger",/* 43 */
                // Buttons
                "Circular Progress Button",/* 44 */
                "Android Process Button",/* 45 */
                "Android Circle Button",/* 46 */
                "Android Flat Button",/* 47 */
                "Moving Button",
                "Label View",
                // List / Grid
                "Super Recycler View",
                "Flabby List View",
                "Recycler View Sticky Headers",
                "Parallax List View",
                "Pull Zoom View",
                "Swipe Menu List View",
                "Discroll View",
                "Sticky List Headers",
                "List Buddies",
                "Android Observable Scroll View",
                "Asymmetric Grid View",
                "Dynamic Grid",
                "Android Staggered Grid",
                "Swipe List View",
                "Android Parallax Recycler View",
                "Blur Sticky Header List View",
                "Recycler View Animators",
                "Recycler View Flexible Divider",
                "Android Tree View",
                "Recycler View Fast Scroller",
                "Recycler View Multiple View Types Adapter",
                // ViewPager
                "Parallax Pager Transformer",
                "View Pager Transforms",
                "Circle Indicator",
                "Android ViewPager Indicator",
                "Android Parallax Header ViewPager",
                "FreePager",
                "Spring Indicator",
                "Smart Tab Layout",
                // Label / Form
                "Shimmer Android",
                "Shimmer for Android",
                "Titanic",
                "MatchView",
                "Android AutoFitTextView",
                "Secret TextView",
                "TextJustify Android",
                "Rounded Letter View",
                "TextDrawable",
                "Babushka Text",
                "Expandable TextView",
                "Float Labeled EditText"
                // Image
                
                
                
        };

        sdkFunctionalityList = (ListView) findViewById(R.id.Md_list_company);

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, sdkFunctionalityListValue);
        sdkFunctionalityList.setAdapter(stringArrayAdapter);

        sdkFunctionalityList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                selectedListItem(position);
            }

        });

    }

    protected void selectedListItem(int position) {
        Intent intent = null;

        switch (position) {

        }
        if (intent != null)
            startActivity(intent);
    }
}
