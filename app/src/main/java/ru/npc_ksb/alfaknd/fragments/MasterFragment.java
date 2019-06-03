package ru.npc_ksb.alfaknd.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.npc_ksb.alfaknd.MenuActionItem;
import ru.npc_ksb.alfaknd.MenuListAdapter;
import ru.npc_ksb.alfaknd.R;


public class MasterFragment extends ListFragment {

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_master, container);

        setListAdapter(new MenuListAdapter(R.layout.row_menu_action_item, getActivity(), MenuActionItem.values()));
        return view;
    }
}