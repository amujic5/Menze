package hr.fer.azzi.menze.activitys;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hr.fer.azzi.menze.R;
import hr.fer.azzi.menze.adapters.MenuAdapter;
import hr.fer.azzi.menze.adapters.MjestoPageAdapter;


public class MainActivity extends ActionBarActivity {


    public MjestoPageAdapter mjestoPageAdapter;
    ViewPager mViewPager;


    DrawerLayout dLayout;
    ListView dList;
    MenuAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        List<String> menu = new ArrayList<>();
        menu.add("Stanje X-ice");
        menu.add("Zagreb");
        menu.add("Split");


        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dList = (ListView) findViewById(R.id.left_drawer);
        adapter = new MenuAdapter(this, android.R.layout.simple_list_item_1, menu);
        dList.setAdapter(adapter);
        dList.setSelector(R.color.my_holo_blue_light);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mjestoPageAdapter = new MjestoPageAdapter(getSupportFragmentManager(), getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mjestoPageAdapter);
        mViewPager.setCurrentItem(1);

        dList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                mViewPager.setCurrentItem(pos);

                dLayout.closeDrawers();
            }
        });

    }

    public void showMenu() {
        dLayout.openDrawer(Gravity.LEFT);
    }
}
