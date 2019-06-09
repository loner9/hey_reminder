package info.project.hey.Class.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import info.project.hey.Fragments.ChatFragment;
import info.project.hey.Fragments.GroupFragment;
import info.project.hey.Fragments.RequestFragment;

public class TabsAccessorAdapter extends FragmentPagerAdapter {


    public TabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                ChatFragment chatsFragment = new ChatFragment();
                return chatsFragment;
            case 1:
                GroupFragment groupsFragment = new GroupFragment();
                return  groupsFragment;
            case 2:
                RequestFragment requestsFragment = new RequestFragment();
                return requestsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public  CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Chats";
            case 1:
                return  "Groups";
            case 2:
                return "Requests";
            default:
                return null;
        }
    }
}
