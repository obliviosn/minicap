package com.android.recoder.kikt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import eu.chainfire.libsuperuser.Shell;
import io.devicefarmer.minicap.R;

public class Main extends Activity {
    final int COLOR_DISABLE = Color.parseColor("#55c7c7c7");
    final int COLOR_ENABLE = Color.parseColor("#1787d5");
    int bitrate = 4;
    Button button;
    CheckBox cbBitrate;
    CheckBox cbCountDown;
    CheckBox cbResolution;
    CheckBox cbRotate;
    CheckBox cbTime;
    int countdown = 3;
    SharedPreferences.Editor editor;
    LinearLayout layoutBitrate;
    LinearLayout layoutCountDown;
    LinearLayout layoutResolution;
    LinearLayout layoutTime;

    /* renamed from: p */
    Process f1p;
    int resolution = 1;
    int[] scrRes;

    /* renamed from: sp */
    SharedPreferences f2sp;
    boolean suAvailable = false;
    int time = 180;
    TextView tvBitrate;
    TextView tvBitrateSub;
    TextView tvCountDown;
    TextView tvCountDownSub;
    TextView tvResolution;
    TextView tvResolutionSub;
    TextView tvTime;
    TextView tvTimeSub;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        requestRoot();
        this.f2sp = getSharedPreferences("settings", 0);
        this.editor = this.f2sp.edit();
        this.resolution = this.f2sp.getInt("resolution", 1);
        this.time = this.f2sp.getInt("time", 180);
        this.bitrate = this.f2sp.getInt("bitrate", 4);
        this.countdown = this.f2sp.getInt("countdown", 3);
        this.cbResolution = (CheckBox) findViewById(R.id.cbResolution);
        this.cbResolution.setChecked(this.f2sp.getBoolean("cbResolution", false));
        this.cbResolution.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (isChecked) {
                    Main.this.tvResolution.setTextColor(Main.this.COLOR_ENABLE);
                    Main.this.tvResolutionSub.setTextColor(Main.this.COLOR_ENABLE);
                } else {
                    Main.this.tvResolution.setTextColor(Main.this.COLOR_DISABLE);
                    Main.this.tvResolutionSub.setTextColor(Main.this.COLOR_DISABLE);
                }
                Main.this.layoutResolution.setEnabled(isChecked);
                Main.this.editor.putBoolean("cbResolution", isChecked);
                Main.this.editor.commit();
            }
        });
        this.cbTime = (CheckBox) findViewById(R.id.cbTime);
        this.cbTime.setChecked(this.f2sp.getBoolean("cbTime", false));
        this.cbTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (isChecked) {
                    Main.this.tvTime.setTextColor(Main.this.COLOR_ENABLE);
                    Main.this.tvTimeSub.setTextColor(Main.this.COLOR_ENABLE);
                } else {
                    Main.this.tvTime.setTextColor(Main.this.COLOR_DISABLE);
                    Main.this.tvTimeSub.setTextColor(Main.this.COLOR_DISABLE);
                }
                Main.this.layoutTime.setEnabled(isChecked);
                Main.this.editor.putBoolean("cbTime", isChecked);
                Main.this.editor.commit();
            }
        });
        this.cbBitrate = (CheckBox) findViewById(R.id.cbBitrate);
        this.cbBitrate.setChecked(this.f2sp.getBoolean("cbBitrate", false));
        this.cbBitrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (isChecked) {
                    Main.this.tvBitrate.setTextColor(Main.this.COLOR_ENABLE);
                    Main.this.tvBitrateSub.setTextColor(Main.this.COLOR_ENABLE);
                } else {
                    Main.this.tvBitrate.setTextColor(Main.this.COLOR_DISABLE);
                    Main.this.tvBitrateSub.setTextColor(Main.this.COLOR_DISABLE);
                }
                Main.this.layoutBitrate.setEnabled(isChecked);
                Main.this.editor.putBoolean("cbBitrate", isChecked);
                Main.this.editor.commit();
            }
        });
        this.cbCountDown = (CheckBox) findViewById(R.id.cbCountDown);
        this.cbCountDown.setChecked(this.f2sp.getBoolean("cbCountDown", false));
        this.cbCountDown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (isChecked) {
                    Main.this.tvCountDown.setTextColor(Main.this.COLOR_ENABLE);
                    Main.this.tvCountDownSub.setTextColor(Main.this.COLOR_ENABLE);
                } else {
                    Main.this.tvCountDown.setTextColor(Main.this.COLOR_DISABLE);
                    Main.this.tvCountDownSub.setTextColor(Main.this.COLOR_DISABLE);
                }
                Main.this.layoutCountDown.setEnabled(isChecked);
                Main.this.editor.putBoolean("cbCountDown", isChecked);
                Main.this.editor.commit();
            }
        });
        this.cbRotate = (CheckBox) findViewById(R.id.cbRotate);
        this.cbRotate.setChecked(this.f2sp.getBoolean("cbRotate", false));
        this.cbRotate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                Main.this.editor.putBoolean("cbRotate", isChecked);
                Main.this.editor.commit();
            }
        });
        this.tvResolutionSub = (TextView) findViewById(R.id.tvResolutionSub);
        this.tvResolution = (TextView) findViewById(R.id.tvResolution);
        this.tvResolution.setText(new StringBuilder().append(this.resolution).toString());
        if (this.cbResolution.isChecked()) {
            this.tvResolution.setTextColor(this.COLOR_ENABLE);
            this.tvResolutionSub.setTextColor(this.COLOR_ENABLE);
        } else {
            this.tvResolution.setTextColor(this.COLOR_DISABLE);
            this.tvResolutionSub.setTextColor(this.COLOR_DISABLE);
        }
        this.tvTimeSub = (TextView) findViewById(R.id.tvTimeSub);
        this.tvTime = (TextView) findViewById(R.id.tvTime);
        this.tvTime.setText(new StringBuilder().append(this.time).toString());
        if (this.cbTime.isChecked()) {
            this.tvTime.setTextColor(this.COLOR_ENABLE);
            this.tvTimeSub.setTextColor(this.COLOR_ENABLE);
        } else {
            this.tvTime.setTextColor(this.COLOR_DISABLE);
            this.tvTimeSub.setTextColor(this.COLOR_DISABLE);
        }
        this.tvBitrateSub = (TextView) findViewById(R.id.tvBitrateSub);
        this.tvBitrate = (TextView) findViewById(R.id.tvBitrate);
        this.tvBitrate.setText(new StringBuilder().append(this.bitrate).toString());
        if (this.cbBitrate.isChecked()) {
            this.tvBitrate.setTextColor(this.COLOR_ENABLE);
            this.tvBitrateSub.setTextColor(this.COLOR_ENABLE);
        } else {
            this.tvBitrate.setTextColor(this.COLOR_DISABLE);
            this.tvBitrateSub.setTextColor(this.COLOR_DISABLE);
        }
        this.tvCountDownSub = (TextView) findViewById(R.id.tvCountDownSub);
        this.tvCountDown = (TextView) findViewById(R.id.tvCountDown);
        this.tvCountDown.setText(new StringBuilder().append(this.countdown).toString());
        if (this.cbCountDown.isChecked()) {
            this.tvCountDown.setTextColor(this.COLOR_ENABLE);
            this.tvCountDownSub.setTextColor(this.COLOR_ENABLE);
        } else {
            this.tvCountDown.setTextColor(this.COLOR_DISABLE);
            this.tvCountDownSub.setTextColor(this.COLOR_DISABLE);
        }
        this.layoutResolution = (LinearLayout) findViewById(R.id.layoutResolution);
        this.layoutResolution.setEnabled(this.f2sp.getBoolean("cbResolution", false));
        this.layoutResolution.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(Main.this);
                b.setTitle("Video Resolution");
                b.setItems(new String[]{"1", "1/2", "1/3", "1/4"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Main.this.resolution = which + 1;
                        Main.this.tvResolution.setText(new StringBuilder().append(Main.this.resolution).toString());
                        Main.this.editor.putInt("resolution", Main.this.resolution);
                        Main.this.editor.commit();
                    }
                });
                b.show();
            }
        });
        this.layoutTime = (LinearLayout) findViewById(R.id.layoutTime);
        this.layoutTime.setEnabled(this.f2sp.getBoolean("cbTime", false));
        this.layoutTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String[] strItem = {"3", "5", "10", "15", "20", "30", "45", "60", "90", "120", "150", "180"};
                AlertDialog.Builder b = new AlertDialog.Builder(Main.this);
                b.setTitle("Time recording");
                b.setItems(strItem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Main.this.time = Integer.parseInt(strItem[which]);
                        Main.this.tvTime.setText(new StringBuilder().append(Main.this.time).toString());
                        Main.this.editor.putInt("time", Main.this.time);
                        Main.this.editor.commit();
                    }
                });
                b.show();
            }
        });
        this.layoutBitrate = (LinearLayout) findViewById(R.id.layoutBitrate);
        this.layoutBitrate.setEnabled(this.f2sp.getBoolean("cbBitrate", false));
        this.layoutBitrate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String[] strItem = {"2", "3", "5", "6", "8", "10", "15", "20"};
                AlertDialog.Builder b = new AlertDialog.Builder(Main.this);
                b.setTitle("Bitrate recording");
                b.setItems(strItem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Main.this.bitrate = Integer.parseInt(strItem[which]);
                        Main.this.tvBitrate.setText(new StringBuilder().append(Main.this.bitrate).toString());
                        Main.this.editor.putInt("bitrate", Main.this.bitrate);
                        Main.this.editor.commit();
                    }
                });
                b.show();
            }
        });
        this.layoutCountDown = (LinearLayout) findViewById(R.id.layoutCountDown);
        this.layoutCountDown.setEnabled(this.f2sp.getBoolean("cbCountDown", false));
        this.layoutCountDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String[] strItem = {"3", "5", "7", "10"};
                AlertDialog.Builder b = new AlertDialog.Builder(Main.this);
                b.setTitle("Countdown timer");
                b.setItems(strItem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Main.this.countdown = Integer.parseInt(strItem[which]);
                        Main.this.tvCountDown.setText(new StringBuilder().append(Main.this.countdown).toString());
                        Main.this.editor.putInt("countdown", Main.this.countdown);
                        Main.this.editor.commit();
                    }
                });
                b.show();
            }
        });
        this.button = (Button) findViewById(R.id.button1);
        this.button.setEnabled(false);
        this.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String cmd = "screenrecord";
                if (Main.this.cbResolution.isChecked()) {
                    cmd = String.valueOf(cmd) + " --size " + (Main.this.scrRes[0] / Main.this.resolution) + "x" + (Main.this.scrRes[1] / Main.this.resolution);
                }
                if (Main.this.cbTime.isChecked()) {
                    cmd = String.valueOf(cmd) + " --time-limit " + Main.this.time;
                }
                if (Main.this.cbBitrate.isChecked()) {
                    cmd = String.valueOf(cmd) + " --bit-rate " + Main.this.bitrate + "000000";
                }
                if (Main.this.cbRotate.isChecked()) {
                    cmd = String.valueOf(cmd) + " --rotate";
                }
                new File("/sdcard/ScreenRecorder/").mkdirs();
                final String filePath = "/sdcard/ScreenRecorder/" + ("VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())) + ".mp4";
                final String[] cmd_set = {"su", "-c", String.valueOf(cmd) + " " + filePath};
                int delay = 0;
                if (Main.this.cbCountDown.isChecked()) {
                    delay = Main.this.countdown;
                }
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Intent i = new Intent(Main.this.getApplicationContext(), RecordService.class);
                        i.putExtra("Command", cmd_set);
                        i.putExtra("Time", Main.this.time);
                        i.putExtra("Path", filePath);
                        Main.this.startService(i);
                    }
                }, (long) (delay * 1000));
                Main.this.finish();
            }
        });
        this.scrRes = getResolution();
    }

    public void onStart() {
        super.onStart();
    }

    public void requestRoot() {
        new Thread(new Runnable() {
            public void run() {
                Main.this.suAvailable = Shell.SU.available();
                Main.this.runOnUiThread(new Runnable() {
                    @SuppressLint({"WrongConstant", "ShowToast"})
                    public void run() {
                        if (!Main.this.suAvailable) {
                            Main.this.finish();
                            Toast.makeText(Main.this.getApplicationContext(), "Please root your device or grant root access.", 0).show();
                            return;
                        }
                        Main.this.button.setEnabled(true);
                    }
                });
            }
        }).start();
    }

    public int[] getResolution() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getRealMetrics(outMetrics);
        return new int[]{outMetrics.widthPixels, outMetrics.heightPixels};
    }
}
