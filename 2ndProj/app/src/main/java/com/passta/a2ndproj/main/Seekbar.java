package com.passta.a2ndproj.main;

import android.os.AsyncTask;
import android.util.Log;

import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.data.FilterDAO;
import com.passta.a2ndproj.data.FilterDTO;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

public class Seekbar {

    private MainActivity mainActivity;

    public Seekbar(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    //seekbar set 메소드
    public void setSeekbar() {
        //seekbar controll
        mainActivity.seekbar1.setProgress(5 - mainActivity.filterList.get(0).filter_1);
        mainActivity.seekbar2.setProgress(5 - mainActivity.filterList.get(0).filter_2);
        mainActivity.seekbar3.setProgress(5 - mainActivity.filterList.get(0).filter_3);
        mainActivity.seekbar4.setProgress(5 - mainActivity.filterList.get(0).filter_4);
        mainActivity.seekbar5.setProgress(5 - mainActivity.filterList.get(0).filter_5);

        mainActivity.seekbar1.setOnSeekChangeListener(new OnSeekChangeListener() {

            @Override
            public void onSeeking(SeekParams seekParams) {
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                mainActivity.filterList.get(0).setFilter_1(5 - mainActivity.seekbar1.getProgress());
                updateFilter();
            }
        });

        mainActivity.seekbar2.setOnSeekChangeListener(new OnSeekChangeListener() {

            @Override
            public void onSeeking(SeekParams seekParams) {
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                mainActivity.filterList.get(0).setFilter_2(5 - mainActivity.seekbar2.getProgress());
                updateFilter();
            }
        });

        mainActivity.seekbar3.setOnSeekChangeListener(new OnSeekChangeListener() {

            @Override
            public void onSeeking(SeekParams seekParams) {
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                mainActivity.filterList.get(0).setFilter_3(5 - mainActivity.seekbar3.getProgress());
                updateFilter();
            }
        });

        mainActivity.seekbar4.setOnSeekChangeListener(new OnSeekChangeListener() {

            @Override
            public void onSeeking(SeekParams seekParams) {
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                mainActivity.filterList.get(0).setFilter_4(5 - mainActivity.seekbar4.getProgress());
                updateFilter();
            }
        });

        mainActivity.seekbar5.setOnSeekChangeListener(new OnSeekChangeListener() {

            @Override
            public void onSeeking(SeekParams seekParams) {
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                mainActivity.filterList.get(0).setFilter_5(5 - mainActivity.seekbar5.getProgress());
                updateFilter();
            }
        });
    }

    private void updateFilter(){
        FilterDTO temp = new FilterDTO(0,5 - mainActivity.seekbar1.getProgress(),5 - mainActivity.seekbar2.getProgress(),5 - mainActivity.seekbar3.getProgress(),
                5 - mainActivity.seekbar4.getProgress(),5 - mainActivity.seekbar5.getProgress());
        new UpdateFilterDatabaseAsyncTask(mainActivity.db.filterDAO(),temp).execute();

        mainActivity.setMsgLevel();
        mainActivity.oneDayMsgRecyclerViewAdapter.notifyDataSetChanged();

    }

    // 데이터 AsyncvTask Update
    public class UpdateFilterDatabaseAsyncTask extends AsyncTask<FilterDTO, Void, Void> {

        private FilterDAO filterDAO;
        private FilterDTO filterDTO;

        UpdateFilterDatabaseAsyncTask(FilterDAO filterDAO,FilterDTO filterDTO) {
            this.filterDAO = filterDAO;
            this.filterDTO = filterDTO;
        }

        @Override
        protected Void doInBackground(FilterDTO... filterDTOS) {
                filterDAO.update(filterDTO);
            return null;
        }

    }
}
