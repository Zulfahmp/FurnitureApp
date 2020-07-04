package com.zulfa.furnitureapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;

import com.zulfa.furnitureapp.R;

public class OrderFragment extends Fragment {

    private FragmentTabHost fTabhost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        fTabhost = (FragmentTabHost)view.findViewById(R.id.tabhost);
        fTabhost.setup(getActivity(), getChildFragmentManager(),R.id.tabContent);
        fTabhost.addTab(fTabhost.newTabSpec("Pending").setIndicator("Pending"), FragmentPending.class,null);
        fTabhost.addTab(fTabhost.newTabSpec("Sukses").setIndicator("Sukses"), FragmentSuccess.class,null);
        fTabhost.addTab(fTabhost.newTabSpec("Batal").setIndicator("Batal"), FragmentCancel.class,null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
