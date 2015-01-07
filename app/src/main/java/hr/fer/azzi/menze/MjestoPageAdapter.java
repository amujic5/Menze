package hr.fer.azzi.menze;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by Azzaro on 17.12.2014..
 */
public class MjestoPageAdapter extends FragmentPagerAdapter {

    String[] mjesta;

    public MjestoPageAdapter(FragmentManager fm, Context context) {
        super(fm);

        Resources resources = context.getResources();
        mjesta = resources.getStringArray(R.array.mjesta);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new SaldoFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putInt(MjestoFragment.ID_MJESTA, position - 1);
        MjestoFragment mjestoFragment = new MjestoFragment();
        mjestoFragment.setArguments(bundle);

        return mjestoFragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return  POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mjesta.length + 1;
    }
}
