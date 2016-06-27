package com.pz.demo.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import com.pz.demo.R;

import java.io.DataOutputStream;

public class FragmentFlashlight extends Fragment implements View.OnClickListener{

    private View rootView;
    private NotificationManager mNotificationMananger;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    boolean on=false;

    BroadcastReceiver onClickReceiver = new BroadcastReceiver() {


        @Override

        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ACTION_BUTTON)) {

                //在这里处理点击事件
                Log.d("ddd","on click");
                on=!on;
                if(on){
                    torch();
                }else{
                    turnOff();
                }

            }

        }

    };


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_flashlight, container, false);
            rootView.findViewById(R.id.btn_start).setOnClickListener(this);
            upgradeRootPermission(getActivity().getPackageCodePath());
            mNotificationMananger = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_BUTTON);
            getActivity().registerReceiver(onClickReceiver, filter);
            return rootView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_start:
                    showButtonNotify();
                    break;
            }
        }

        public boolean upgradeRootPermission(String pkgCodePath) {
            Process process = null;
            DataOutputStream os = null;
            try {
                String cmd = "chmod 777 " + pkgCodePath;
                process = Runtime.getRuntime().exec("su"); //切换到root帐号
                os = new DataOutputStream(process.getOutputStream());
                os.writeBytes(cmd + "\n");
                os.writeBytes("exit\n");
                os.flush();
                process.waitFor();
            } catch (Exception e) {
                return false;
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                    process.destroy();
                } catch (Exception e) {
                }
            }
            return true;
        }


        public boolean torch() {
            Process process = null;
            DataOutputStream os = null;
            try {
                String cmd = "echo 1 > /sys/class/leds/led:flash_torch/brightness";
                process = Runtime.getRuntime().exec("su"); //切换到root帐号
                os = new DataOutputStream(process.getOutputStream());
                os.writeBytes(cmd + "\n");
                os.writeBytes("exit\n");
                os.flush();
                process.waitFor();
            } catch (Exception e) {
                return false;
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                    process.destroy();
                } catch (Exception e) {
                }
            }
            return true;
        }

        private boolean turnOff(){
            Process process = null;
            DataOutputStream os = null;
            try {
                String cmd="echo 0 > /sys/class/leds/led:flash_torch/brightness";
                process = Runtime.getRuntime().exec("su"); //切换到root帐号
                os = new DataOutputStream(process.getOutputStream());
                os.writeBytes(cmd + "\n");
                os.writeBytes("exit\n");
                os.flush();
                process.waitFor();
            } catch (Exception e) {
                return false;
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                    process.destroy();
                } catch (Exception e) {
                }
            }
            return true;
        }

    String ACTION_BUTTON="action_button";
    /**
     * 带按钮的通知栏
     */
    public void showButtonNotify(){
        Notification.Builder mBuilder = new Notification.Builder(getActivity());
        RemoteViews mRemoteViews = new RemoteViews(getActivity().getPackageName(), R.layout.layout_flash_light_notification);
        //
        //点击的事件处理
        Intent buttonIntent = new Intent(ACTION_BUTTON);
		/* 上一首按钮 */
        //这里加了广播，所及INTENT的必须用getBroadcast方法
        PendingIntent intent_prev = PendingIntent.getBroadcast(getActivity(), 1, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.layout_flash_light_notificaiton_btn, intent_prev);
        mBuilder.setContent(mRemoteViews)
                .setContentIntent(getDefalutIntent(Notification.FLAG_ONGOING_EVENT))
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setTicker("正在播放")
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setOngoing(true).setSmallIcon(R.drawable.ic_launcher);
        Notification notify = mBuilder.build();
        notify.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationMananger.notify("TExt",1000123, notify);
    }

    public PendingIntent getDefalutIntent(int flags){
        PendingIntent pendingIntent= PendingIntent.getActivity(getActivity(), 1, new Intent(), flags);
        return pendingIntent;
    }


}
