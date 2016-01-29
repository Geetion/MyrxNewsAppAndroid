package com.example.geetion.rxnewsappandroid;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.geetion.rxnewsappandroid.CollageNews.CollageFragment;
import com.example.geetion.rxnewsappandroid.pictureNews.PictureFragment;
import com.example.geetion.rxnewsappandroid.rxNews.RXNewsFragment;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity{
    ViewPager viewPager;
    ArrayList<Fragment> viewList = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        RXNewsFragment view1 = new RXNewsFragment();
        CollageFragment view2 = new CollageFragment();
        PictureFragment view3 = new PictureFragment();

        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewPager.setAdapter(new MainViewPagerAdapter(getFragmentManager(),viewList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
