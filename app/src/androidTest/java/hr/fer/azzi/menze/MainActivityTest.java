package hr.fer.azzi.menze;

/**
 * Created by Azzaro on 4.12.2014..
 */

import android.app.Activity;
import android.app.Instrumentation;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import hr.fer.azzi.menze.activitys.MainActivity;
import hr.fer.azzi.menze.activitys.MenzaDisplay;
import hr.fer.azzi.menze.adapters.MenuAdapter;
import hr.fer.azzi.menze.adapters.MjestoPageAdapter;

import static android.test.TouchUtils.clickView;
import static android.test.ViewAsserts.assertGroupContains;
import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;
import static android.text.InputType.TYPE_NUMBER_FLAG_SIGNED;
import static org.assertj.android.api.Assertions.assertThat;


public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private static final long ACTIVITY_TIMEOUT = 10000;
    private MainActivity activity;
    ViewPager mViewPager;


    DrawerLayout dLayout;
    ListView dList;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        dLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        dList = (ListView) activity.findViewById(R.id.left_drawer);
        mViewPager = (ViewPager) activity.findViewById(R.id.pager);

    }


    @MediumTest
    public void testLayout() {
        assertThat(dLayout)
                .hasChildCount(2)
                .isVisible();
        assertGroupContains(dLayout, mViewPager);
        assertGroupContains(dLayout, dList);

    }


    private void testLaunchActivity(final View button, Class<? extends Activity> activityClass) {
        final Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(activityClass.getName(), null, false);
        clickView(this, button);

        Activity activity = monitor.waitForActivityWithTimeout(ACTIVITY_TIMEOUT);
        assertThat(activity).isNotNull();
        assertEquals(activityClass, activity.getClass());

        activity.finish();
    }

    private void input(final View view, final String text) {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                view.requestFocus();
            }
        });

        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync(text);
        getInstrumentation().waitForIdleSync();
    }



}

