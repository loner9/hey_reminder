package info.project.hey.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.marcohc.robotocalendar.RobotoCalendarView;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import info.project.hey.R;


public class TigaFragment extends Fragment implements RobotoCalendarView.RobotoCalendarListener {
    private RobotoCalendarView robotoCalendarView;
    private FloatingActionButton markDayButton;


    public TigaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragmenttiga, container, false);
        robotoCalendarView = RootView.findViewById(R.id.robotoCalendarPicker);
        markDayButton = RootView.findViewById(R.id.markDayButton);
        markDayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CustomBottomSheetDialogFragment customBottomSheetDialogFragment = new CustomBottomSheetDialogFragment();
                customBottomSheetDialogFragment.show(getFragmentManager(), customBottomSheetDialogFragment.getTag());
            }
        });

        robotoCalendarView.setRobotoCalendarListener(this);

        robotoCalendarView.setShortWeekDays(false);

        robotoCalendarView.showDateTitle(true);

        robotoCalendarView.setDate(new Date());
        return RootView;
    }

    @Override
    public void onDayClick(Date date) {
        Toast.makeText(TigaFragment.this.getActivity(), "onDayClick: " + date, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDayLongClick(Date date) {
        Toast.makeText(TigaFragment.this.getActivity(), "onDayClick: " + date, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRightButtonClick() {
        Toast.makeText(TigaFragment.this.getActivity(), "onRightButtonClick!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLeftButtonClick() {
        Toast.makeText(TigaFragment.this.getActivity(), "onLeftButtonClick!", Toast.LENGTH_SHORT).show();
    }

}
