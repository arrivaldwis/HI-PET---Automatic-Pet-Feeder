package com.mega.hi_pet.activity;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.macroyau.thingspeakandroid.ThingSpeakChannel;
import com.macroyau.thingspeakandroid.model.ChannelFeed;
import com.macroyau.thingspeakandroid.model.Feed;
import com.mega.hi_pet.R;
import com.mega.hi_pet.adapter.RFIDHistoryAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mega.hi_pet.api.ThingspeakAPI.CHANNEL_RFID_ID;
import static com.mega.hi_pet.api.ThingspeakAPI.CHANNEL_TANK_ID;
import static com.mega.hi_pet.api.ThingspeakAPI.READ_API_KEY;

public class RFIDHistoryActivity extends AppCompatActivity {

    @BindView(R.id.rvHistory)
    RecyclerView rvHistory;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private ArrayList<Feed> feedList;
    private RFIDHistoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfidhistory);
        ButterKnife.bind(this);
        setUI();
        getRFID();
    }

    private void setUI() {
        setTitle("RFID History");
        feedList = new ArrayList<>();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRFID();
            }
        });

        LinearLayoutManager llManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvHistory.setLayoutManager(llManager);
        mAdapter = new RFIDHistoryAdapter(this, feedList);
        rvHistory.setAdapter(mAdapter);
    }

    public void getRFID() {
        feedList.clear();
        swipeRefresh.setRefreshing(true);
        ThingSpeakChannel tsPrivateChannel = new ThingSpeakChannel(CHANNEL_RFID_ID, READ_API_KEY);
        tsPrivateChannel.setChannelFieldFeedUpdateListener(new ThingSpeakChannel.ChannelFieldFeedUpdateListener() {
            @Override
            public void onChannelFieldFeedUpdated(long channelId, int fieldId, ChannelFeed channelFieldFeed) {
                for (Feed feed : channelFieldFeed.getFeeds()) {
                    feedList.add(feed);
                }
                mAdapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        });
        tsPrivateChannel.loadChannelFieldFeed(2);
    }
}
