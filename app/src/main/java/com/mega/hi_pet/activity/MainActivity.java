package com.mega.hi_pet.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.macroyau.thingspeakandroid.ThingSpeakChannel;
import com.macroyau.thingspeakandroid.ThingSpeakLineChart;
import com.macroyau.thingspeakandroid.model.ChannelFeed;
import com.macroyau.thingspeakandroid.model.Feed;
import com.mega.hi_pet.App;
import com.mega.hi_pet.R;
import com.mega.hi_pet.activity.alarm.AlarmActivity;
import com.mega.hi_pet.api.APIService;
import com.mega.hi_pet.api.ThingspeakAPI;
import com.mega.hi_pet.model.Alarm;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mega.hi_pet.api.ThingspeakAPI.CHANNEL_TANK_ID;
import static com.mega.hi_pet.api.ThingspeakAPI.READ_API_KEY;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.llFeed)
    LinearLayout llFeed;
    @BindView(R.id.imgIndicator)
    ImageView imgIndicator;
    @BindView(R.id.tvFeed)
    TextView tvFeed;
    @BindView(R.id.tvFeedInfo)
    TextView tvFeedInfo;
    @BindView(R.id.llIndicator)
    LinearLayout llIndicator;
    @BindView(R.id.tvIndicator)
    TextView tvIndicator;
    @BindView(R.id.chart)
    LineChartView chart;
    @BindView(R.id.tvDetail)
    TextView tvDetail;
    @BindView(R.id.tvAlarm)
    TextView tvAlarm;
    @BindView(R.id.imgArrow)
    ImageView imgArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        tvAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AlarmActivity.class);
                startActivity(i);
            }
        });

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                loadDataLine();
                getTankChannel();
            }
        },0,5000);
    }

    @OnClick(R.id.tvDetail)
    public void detail() {
        Intent in = new Intent(MainActivity.this, RFIDHistoryActivity.class);
        startActivity(in);
    }

    private void loadDataLine() {
        ThingSpeakLineChart tsChart = new ThingSpeakLineChart(ThingspeakAPI.CHANNEL_RFID_ID, 2);
        tsChart.setListener(new ThingSpeakLineChart.ChartDataUpdateListener() {
            @Override
            public void onChartDataUpdated(long channelId, int fieldId, String title, LineChartData lineChartData,
                                           Viewport maxViewport, Viewport initialViewport) {
                chart.setLineChartData(lineChartData);
                chart.setMaximumViewport(maxViewport);
                chart.setCurrentViewport(initialViewport);
            }
        });
        tsChart.loadChartData();
    }

    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    public void getTankChannel() {
        ThingSpeakChannel tsPrivateChannel = new ThingSpeakChannel(CHANNEL_TANK_ID, READ_API_KEY);
        tsPrivateChannel.setChannelFieldFeedUpdateListener(new ThingSpeakChannel.ChannelFieldFeedUpdateListener() {
            @Override
            public void onChannelFieldFeedUpdated(long channelId, int fieldId, ChannelFeed channelFieldFeed) {
                for (Feed feed : channelFieldFeed.getFeeds()) {
                    if (feed.getField2() != null) {
                        int distance = Integer.parseInt(feed.getField2());
                        if (distance <= 1) {
                            llFeed.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    updateFeed("0");
                                }
                            });

                            imgIndicator.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorGreen),
                                    android.graphics.PorterDuff.Mode.SRC_IN);
                            tvFeed.setText("Good!");
                            tvFeedInfo.setText("Tank is full");

                            imgArrow.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorGreen),
                                    android.graphics.PorterDuff.Mode.SRC_IN);
                            tvIndicator.setText("Full");
                            layoutParams.setMargins(560, 0, 0, 0);
                        } else if (distance >= 1 && distance <= 10) {

                            llFeed.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    updateFeed("1");
                                }
                            });

                            imgIndicator.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorYellow),
                                    android.graphics.PorterDuff.Mode.SRC_IN);
                            tvFeed.setText("Feed!");
                            tvFeedInfo.setText("Small size");

                            imgArrow.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorYellow),
                                    android.graphics.PorterDuff.Mode.SRC_IN);
                            tvIndicator.setText("Middle");
                            layoutParams.setMargins(320, 0, 0, 0);
                        } else if (distance >= 20) {
                            llFeed.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    updateFeed("2");
                                }
                            });

                            imgIndicator.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorRed),
                                    android.graphics.PorterDuff.Mode.SRC_IN);
                            tvFeed.setText("Feed!");
                            tvFeedInfo.setText("Large size");

                            imgArrow.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorRed),
                                    android.graphics.PorterDuff.Mode.SRC_IN);
                            tvIndicator.setText("Empty");
                            layoutParams.setMargins(90, 0, 0, 0);
                        }
                        llIndicator.setLayoutParams(layoutParams);
                    }
                }
            }
        });
        tsPrivateChannel.loadChannelFieldFeed(2);
    }

    private void updateFeed(final String size) {
        /*
         * 0 = Full
         * 1 = Small Size
         * 2 = Large Size
         */

        APIService service = App.retrofit.create(APIService.class);
        Call<ResponseBody> call = service.updateFeed(ThingspeakAPI.API_KEY_FEED, size);
        Log.d("testUrl", call.request().url().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody event = response.body();
                    try {
                        Log.d("testOutput", event.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(size.equals("0")) {
                        Toast.makeText(MainActivity.this, "Good! Tank is already full", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String sz = "";
                    if(size.equals("1")) sz = "Small";
                    else if(size.equals("2")) sz = "Large";
                    Toast.makeText(MainActivity.this, sz+" size foods has been sent!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("", t.getMessage());
            }
        });
    }
}
