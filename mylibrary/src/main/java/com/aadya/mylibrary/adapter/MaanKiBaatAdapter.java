package com.aadya.mylibrary.adapter;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aadya.mylibrary.MaanKiBat;
import com.aadya.mylibrary.R;
import com.aadya.mylibrary.ThreeDvideosActivity;
import com.aadya.mylibrary.model.MaanKiBaat;
import com.aadya.mylibrary.model.MyVideo;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Admin on 2/26/2018.
 */

public class MaanKiBaatAdapter extends RecyclerView.Adapter<MaanKiBaatAdapter.MyViewHolder1> {

    private List<MaanKiBaat> dataList;
    private List<MyVideo> videoList;
    private Context activity;
    private LinearLayout linear_layout;

    //private DataImportAdapterCallback callback;


    int statusID = 0;

    int min = 0;
    int max = 0;

    int randomTest=0;
    int random = 0;

    //private DataSource dataSource;

    //private DownloadManager downloadManager;

    private ProgressDialog progressDialog;

    public class MyViewHolder1 extends RecyclerView.ViewHolder {



        private CardView cardview;

        private de.hdodenhof.circleimageview.CircleImageView profilePic;
        private TextView titleTextView;

       // private LinearLayout linearLayout;

        private DownloadManager downloadManager;
        private long refrenceId;
        private String filePath="";



        public MyViewHolder1(View view) {
            super(view);


            profilePic = view.findViewById(R.id.profilePic);
            titleTextView = view.findViewById(R.id.titleTextView);
            cardview = view.findViewById(R.id.cardview);
            linear_layout = view.findViewById(R.id.linear_layout);


            }
    }

    public MaanKiBaatAdapter(List<MaanKiBaat> dataList, Context activity, List<MyVideo> videoList) {

        this.dataList = dataList;
        this.activity = activity;
        this.videoList = videoList;

        max = videoList.size();

    }

    @Override
    public MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_maankibaat, parent, false);


        return new MyViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder1 holder, final int position) {

        /*if(position%2 == 1)
            holder.img.setImageResource(R.drawable.deadline_sample1);
        else
            holder.img.setImageResource(R.drawable.deadline_sample2);*/

      final  MaanKiBaat obj = dataList.get(position);


        Glide.with(activity).load(""+obj.getImage_Url()).into(holder.profilePic);

        holder.titleTextView.setText(""+obj.getAudio_Label());
        //holder.titleTextView.setShadowLayer(5, 0, 3, ContextCompat.getColor(activity, R.color.tabColor));

        /*holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String[] perms = {android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

                if(EasyPermissions.hasPermissions(activity, perms)) {

                    //singleSelectionPicker.show(getChildFragmentManager(), "picker");

                    randomTest++;

                    random = (randomTest)%videoList.size();//new Random().nextInt((max - min) + 1) + min;

                    Uri uri = Uri.parse(videoList.get(random).getVideoUrl());
                    Log.e("Myuri",random+"  "+videoList.size()+"---"+uri);

                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    String fileName = URLUtil.guessFileName(videoList.get(random).getVideoUrl(), null, MimeTypeMap.getFileExtensionFromUrl(videoList.get(random).getVideoUrl()));
                    //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setTitle("Please wait...");

                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

                    request.setVisibleInDownloadsUi(true);

                    holder.downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
                    holder.refrenceId = holder.downloadManager.enqueue(request);

                    //file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName);

                    setupProgress(holder.refrenceId,holder.downloadManager, fileName, holder.filePath, position);

                }else{
                    EasyPermissions.requestPermissions((MainActivity)activity, "Permission For Gallery", 799, perms);
                }


            }
        });*/

    }

    private void open3DVideo(int position, String path){

        final  MaanKiBaat obj = dataList.get(position);


        Intent intent = new Intent(activity, ThreeDvideosActivity.class);
        //intent.putExtra("pathfileRes", getRes(random));
        intent.putExtra("pathfileRes", path );
        intent.putExtra("audioPathFileUrl", obj.getAudio_Url());
        intent.putExtra("width", ((MaanKiBat)activity).getWidth() );
        intent.putExtra("height", ((MaanKiBat)activity).getHeight());
        activity.startActivity(intent);
    }


    public void setupProgress(final long refrenceId, final DownloadManager downloadManager, final String fileName, final String filePath, final int position) {

        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("");
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean downloading = true;
                while (downloading) {
                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(refrenceId);
                    Cursor cursor = downloadManager.query(q);
                    cursor.moveToFirst();
                    final int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));

                    //int bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }
//                    final double dlProgress = (int) ((bytesDownloaded * 100l) / bytesTotal);
                    final String msg = "Downloading :\n\n" + ((double) bytesDownloaded) / 1000000.0 + " MB";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.setMessage(msg);

                            Toast.makeText(activity, "Status = "+ msg, Toast.LENGTH_LONG).show();
                        }
                    });
                    Log.d("YO!", statusMessage(cursor, bytesDownloaded, 0, fileName, position));
                    cursor.close();

                }
            }

            private String statusMessage(Cursor c, int bytesDownloaded, int bytesTotal, String fileName, int position) {
                String msg = "???";

                switch (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                    case DownloadManager.STATUS_FAILED:
                        msg = "Download failed!";
                        break;

                    case DownloadManager.STATUS_PAUSED:
                        msg = "Download paused!";
                        break;

                    case DownloadManager.STATUS_PENDING:
                        msg = "Download pending!";
                        break;

                    case DownloadManager.STATUS_RUNNING:
                        msg = "Download in progress! " + bytesDownloaded + " " + bytesTotal;
                        break;

                    case DownloadManager.STATUS_SUCCESSFUL:
                        msg = "Download complete! " + bytesDownloaded + " " + bytesTotal;
                        progressDialog.dismiss();

                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                        if(file.exists()){
                            Log.e("exists",""+file.getPath());
                            //holder.filePath = file.getPath();
                            open3DVideo(position, file.getPath());
                        }
                        else{
                            Log.e("exists","**************");
                        }

            /*            Toast toast=Toast.makeText(activity,"DownLoading completed.", Toast.LENGTH_SHORT);
                        toast.setMargin(50,50);
                        toast.show();*/
                        break;

                    default:
                        msg = "Download is nowhere in sight";
                        break;
                }

                return (msg);
            }
            private void runOnUiThread(Runnable runnable) {
            }
        }).start();

    }


    /*private int getRes(int pos){

        switch (pos){
            case 0: return R.raw.video2;
            case 1: return R.raw.video1;
            case 2: return R.raw.video2;
            case 3: return R.raw.video1;

            default: return R.raw.video1;
        }
    }*/

    /*private Uri getMedia(*//*int res*//*) {
        return Uri.parse("android.resource://" + activity.getPackageName()+"/" +R.raw.sample3);
    }*/

    @Override
    public int getItemCount() {
        //Log.e("getItemCount",""+historyList.size());
        return dataList.size();
    }

    private String getFormattedDate(String str)
    {
        try {

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// yyyy-MM-dd HH:mm:ss //new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")
            cal.setTime(sdf.parse(str));


            return ""+getFormatDateType_1(cal.getTimeInMillis());
        }catch (ParseException e){
            return "";
        }

    }

    private String getFormatDateType_1(long time)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String formattedDate = df.format(cal.getTime());

        return formattedDate;
    }

    private String getTime(long time)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss a");
        String formattedDate = df.format(cal.getTime());

        return formattedDate;
    }

    private String getDay(long time)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        /*SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(cal.getTime());*/


        SimpleDateFormat df = new SimpleDateFormat("dd");
        String formattedDate1 = df.format(cal.getTime());

        df = new SimpleDateFormat("MMMM");
        String formattedDate2 = df.format(cal.getTime());


        df = new SimpleDateFormat("yy");
        String formattedDate3 = df.format(cal.getTime());


        return formattedDate1+" "+formattedDate2+" "+formattedDate3;
    }


}