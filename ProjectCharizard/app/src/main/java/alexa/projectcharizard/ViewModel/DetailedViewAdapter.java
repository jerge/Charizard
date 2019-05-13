package alexa.projectcharizard.ViewModel;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class DetailedViewAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public DetailedViewAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AboutFragment aboutFragment = new AboutFragment();
                return aboutFragment;
            case 1:
                CommentsFragment commentsFragment = new CommentsFragment();
                return commentsFragment;
            default:
                return null;
        }
    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}