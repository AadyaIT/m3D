package com.aadya.mylibrary;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.aadya.mylibrary.adapter.MaanKiBaatAdapter;
import com.aadya.mylibrary.model.MaanKiBaat;
import com.aadya.mylibrary.model.MyVideo;
import com.aadya.mylibrary.model.Post;
import com.aadya.mylibrary.support.DataSource;
import com.aadya.mylibrary.support.JSONParser;
import com.aadya.mylibrary.support.Utils;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import pub.devrel.easypermissions.EasyPermissions;

public class MaanKiBat extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;
    private MaanKiBaatAdapter mAdapter;
    private List<MaanKiBaat> dataList;
    private List<MyVideo> videoList;
    private LinearLayoutManager mLayoutManager;
    private KProgressHUD kProgressHUD;
    private Spinner spinner_category;
    private List<Post> postList;
    private ArrayAdapter<Post> postAdapter;

    int randomTest=0;
    int random = 0;

    private int downLoadID;

    private DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maan_ki_bat);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        dataSource = new DataSource(this);
        dataSource.open();

        postList=new ArrayList<Post>();
        dataList = new ArrayList<MaanKiBaat>();
        videoList = new ArrayList<MyVideo>();

        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        spinner_category = findViewById(R.id.spinner_category);
        mAdapter = new MaanKiBaatAdapter(dataList,this, videoList);
        mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        postList.addAll(Post.getAllPost());

        postAdapter = new ArrayAdapter<Post>(this, R.layout.sample_myspinner_dropdown, postList);
        postAdapter.setDropDownViewResource(R.layout.sample_myspinner);
        spinner_category.setAdapter(postAdapter);

        new AttemptGetVideoData().execute();

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                new AttemptGetData().execute(""+postList.get(position).getName());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                if(videoList.size()>0)
                    new AttemptGetData().execute(""+postList.get(spinner_category.getSelectedItemPosition()).getName());
                else
                    new AttemptGetVideoData().execute();
            }
        });

        recyclerViewClick();
    }

    private void recyclerViewClick() {

        recyclerView.addOnItemTouchListener(new MaanKiBat.RecyclerTouchListener(this, recyclerView, new MaanKiBat.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                String[] perms = {android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

                if(EasyPermissions.hasPermissions(MaanKiBat.this, perms)) {

                    if(videoList.size()>0) {

                        //random = (randomTest)%videoList.size();//new Random().nextInt((max - min) + 1) + min;
                        random = new Random().nextInt(videoList.size());
                        randomTest++;

                        //Uri uri = Uri.parse(videoList.get(random).getVideoUrl());
                        String uri = videoList.get(random).getVideoUrl();
                        String extension = uri.substring(uri.lastIndexOf("."));

                        String localPath = dataSource.getLocalVideoPath("" + uri.trim());
                        if (localPath != null && localPath.trim().length() > 0) {

                            File file = new File(localPath.trim());

                            if (file.exists()) {
                                open3DVideo(position, file.getPath().trim());
                            } else {
                                downLoadFile(position, uri, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getPath() + "/", Calendar.getInstance().getTimeInMillis() + "" + extension);
                            }

                        } else {

                            downLoadFile(position, uri, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getPath() + "/", Calendar.getInstance().getTimeInMillis() + "" + extension);
                        }

                    }
                    else {
                        new AttemptGetVideoData().execute();
                    }

                }else{
                    EasyPermissions.requestPermissions(MaanKiBat.this, "Permission For Gallery", 799, perms);
                }

            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void downLoadFile(final int pos, final String url, final String dirPath, final String fileName){

        Log.e("pos",""+pos);
        Log.e("url",""+url);
        Log.e("dirPath",""+dirPath);
        Log.e("fileName",""+fileName);

        downLoadID = PRDownloader.download(url, dirPath, fileName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                        showProgress("Please wait...");
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                        dataSource.addUpdateVideoPath(url.trim(), dirPath.trim()+fileName.trim());

                        open3DVideo(pos, dirPath+fileName);
                        hideProgress();
                    }

                    @Override
                    public void onError(Error error) {
                        showToast(""+error.toString());
                        Log.e("error",""+error.toString());
                        hideProgress();
                    }


                });
    }

    private void open3DVideo(int position, String path){

        Log.e("position",""+position);
        Log.e("path",""+path);

        final  MaanKiBaat obj = dataList.get(position);


        Intent intent = new Intent(MaanKiBat.this, ThreeDvideosActivity.class);
        //intent.putExtra("pathfileRes", getRes(random));
        intent.putExtra("pathfileRes", path );
        intent.putExtra("audioPathFileUrl", obj.getAudio_Url());
        intent.putExtra("width", getWidth() );
        intent.putExtra("height", getHeight());
        startActivity(intent);
    }

    private class AttemptGetData extends AsyncTask<String, String, String> {

        boolean issuccess = false;
        boolean failure = false;

        String errorMsg = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgress("Please wait");

        }

        @Override
        protected String doInBackground(String... args) {
            try {

                String str = args[0].trim();

                JSONObject jsonp = new JSONObject();

                //jsonp.put("user_id",getLoginInfo(2));

                //String finalURL = Utils.BASEURL + "?task=getAudioListing";
                final String finalURL = Utils.BASEURL + "getAListing";


                JSONObject json = new JSONParser().makeHttpRequest(finalURL, "GET", jsonp);
                Log.e("Request", "" + jsonp);
                Log.e("Response", "" + json);

                if (json != null) {

                    //JSONObject response = json.getJSONObject("response");

                    if (json.getBoolean("status")) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<MaanKiBaat>>() {
                        }.getType();
                        List<MaanKiBaat> tempList = (List<MaanKiBaat>) gson.fromJson(json.getJSONArray("getAudioListing").toString().trim(), listType);

                        if (dataList.size() > 0)
                            dataList.clear();

                        for(int i=0; i<tempList.size();i++){

                            MaanKiBaat temp = tempList.get(i);
                            if(temp.getLanguage()!=null && temp.getLanguage().equalsIgnoreCase(str))
                                dataList.add(temp);
                        }


                        issuccess = true;

                    } else {
                        failure = true;

                        errorMsg = json.getString("error").toString().trim();
                    }


                }// json null check condition
                else {
                    failure = true;
                    Log.d("Json Null!", "starting");
                }

            } catch (JSONException e) {
                e.printStackTrace();
                failure = true;
                Log.d("Json Exception !", "starting");
            } catch (Exception e) {
                e.printStackTrace();
                failure = true;
                Log.d("Exception !", "starting");
            }

            return null;
        }

        protected void onPostExecute(String file_url) {

            hideProgress();

            if (failure) {
                if (errorMsg.length() > 1) {
                    showToast(errorMsg);
                } else {
                    showToast("Please try again after sometime.");
                }
            } else if (issuccess) {
                try {

                    mAdapter.notifyDataSetChanged();

                    if(dataList.size()==0)
                        showToast("Videos Coming Soon ..");
                    //  todayDataTextView.setText(""+today_data);

                } catch (Exception e) {

                    //showToast("Please try again after sometime.-5");
                }


            }

        }
    }

    private class AttemptGetVideoData extends AsyncTask<String, String, String> {

        boolean issuccess = false;
        boolean failure = false;

        String errorMsg = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //showProgress("Please wait");


        }

        @Override
        protected String doInBackground(String... args) {
            try {


                JSONObject jsonp = new JSONObject();

                //jsonp.put("user_id",getLoginInfo(2));

                final String finalURL = Utils.BASEURL + "getVListing";


                JSONObject json = new JSONParser().makeHttpRequest(finalURL, "GET", jsonp);
                Log.e("Request", "" + jsonp);
                Log.e("Response", "" + json);

                if (json != null) {

                    //JSONObject response = json.getJSONObject("response");

                    if (json.getBoolean("status")) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<MyVideo>>() {
                        }.getType();
                        List<MyVideo> tempList = (List<MyVideo>) gson.fromJson(json.getJSONArray("getVideoListing").toString().trim(), listType);

                        if (videoList.size() > 0)
                            videoList.clear();

                        videoList.addAll(tempList);

                        issuccess = true;

                    } else {
                        failure = true;

                        errorMsg = json.getString("error").toString().trim();
                    }


                }// json null check condition
                else {
                    failure = true;
                    Log.d("Json Null!", "starting");
                }

            } catch (JSONException e) {
                e.printStackTrace();
                failure = true;
                Log.d("Json Exception !", "starting");
            } catch (Exception e) {
                e.printStackTrace();
                failure = true;
                Log.d("Exception !", "starting");
            }

            return null;
        }

        protected void onPostExecute(String file_url) {

            //hideProgress();

            if (failure) {
                if (errorMsg.length() > 1) {
                    showToast(errorMsg);
                } else {
                    showToast("Please try again after sometime.");
                }
            } else if (issuccess) {
                try {

                    new AttemptGetData().execute("Hindi");
                    // mAdapter.notifyDataSetChanged();
                    //  todayDataTextView.setText(""+today_data);

                } catch (Exception e) {

                    //showToast("Please try again after sometime.-5");
                }


            }

        }
    }

    public void showProgress(String val1)
    {
        try {
            kProgressHUD.setLabel(val1)
                    /*.setDetailsLabel(val2)*/
                    .setCancellable(false)
                    .show();

        }catch (Exception e){}
    }

    public void hideProgress()
    {
        try {
            if (kProgressHUD!=null)
                kProgressHUD.dismiss();
        }catch (Exception e){}

    }
    public void showToast(String msg)
    {
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), msg, Snackbar.LENGTH_LONG);
        /*View view = snackbar.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);*/

        //snackbar.setActionTextColor(Color.RED);

        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.RED);
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        //textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.TOP;
        sbView.setLayoutParams(params);

        snackbar.show();
    }

    public int getHeight(){

        return findViewById(R.id.coordinatorLayout).getHeight();
    }
    public int getWidth(){

        return findViewById(R.id.coordinatorLayout).getWidth();
    }

    public void openPermission(){

        String[] perms = {android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        if(EasyPermissions.hasPermissions(this, perms)) {

            //singleSelectionPicker.show(getChildFragmentManager(), "picker");

        }else{
            EasyPermissions.requestPermissions(this, "Permission For Gallery", 799, perms);
        }
    }



    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        /*Intent intent2 = new Intent(this, FilePickActivity.class);
        startActivity(intent2);*/
        finish();
    }


}