package com.aadya.mylibrary;

import android.app.ProgressDialog;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Space;

import com.aadya.mylibrary.view.ZoomableRelativeLayout;
import com.github.rongi.rotate_layout.layout.RotateLayout;

import java.io.IOException;



public class ThreeDvideosActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener{

    private MediaPlayer /*mMediaPlayer,*/ mMediaPlayer1, mMediaPlayer2, mMediaPlayer3, mMediaPlayer4;
    private TextureView mVideoView1, mVideoView2, mVideoView3, mVideoView4;

    private MediaPlayer mediaPlayer;

    //VideoView mVideoView;

    private ZoomableRelativeLayout frameLayout;

    private int videoWidth=0, videoHeight=0;
    private int space_H=0, space_V=0, min_space_H=0, max_space_H=0, min_space_V=0, max_space_V=0;

    private RotateLayout view1, view2, view3, view4;
    private Space space1, space2, space3, space4;

    private int screenWidth=0, screenHeight=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_dvideos);
        //getActionBar().hide();
        statusBarColor();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);


        frameLayout = findViewById(R.id.frameLayout);

        mVideoView1 = findViewById(R.id.mVideoView1);
        mVideoView2 = findViewById(R.id.mVideoView2);
        mVideoView3 = findViewById(R.id.mVideoView3);
        mVideoView4 = findViewById(R.id.mVideoView4);

        mVideoView1.setSurfaceTextureListener(this);
        mVideoView2.setSurfaceTextureListener(this);
        mVideoView3.setSurfaceTextureListener(this);
        mVideoView4.setSurfaceTextureListener(this);

        //mVideoView1.lockCanvas();


        view1 = findViewById(R.id.view1);
        space1 = findViewById(R.id.space1);

        view2 = findViewById(R.id.view2);
        space2 = findViewById(R.id.space2);

        view3 = findViewById(R.id.view3);
        space3 = findViewById(R.id.space3);

        view4 = findViewById(R.id.view4);
        space4 = findViewById(R.id.space4);



        new AttemptLoadVideoWidth().execute();



        zoomingAction();
//        Log.e("3D..",""+getMedia());

        playMyAudio();
        mediaPlayerListenerAction();

    }

    private void playMyAudio(/*String audioUrl*/)
    {
        //obj = audioList.get(audioPosition);
        //Log.e("audioPosition",""+audioPosition);

        //Uri uri = Uri.fromFile(new File(obj.getFolder()+"/"+obj.getFile()+obj.getExten()));

        try {


            if(mediaPlayer==null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }

            //mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.setDataSource(this, Uri.parse(getIntent().getStringExtra("audioPathFileUrl")));
            //mediaPlayer.setDataSource(this, Uri.parse("android.resource://" + this.getPackageName() + "/" + getIntent().getIntExtra("AudiopathfileRes",0) ));

            mediaPlayer.prepareAsync();


        }catch (IllegalArgumentException e) {
            e.printStackTrace();
            //Log.e("playRecod","IllegalArgumentException"+audioPosition);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            //Log.e("playRecod","IllegalStateException"+audioPosition);
        } catch (IOException e) {
            e.printStackTrace();
            //Log.e("playRecod","IOException"+audioPosition);
        }

        /*Uri uri = Uri.fromFile(new File(obj.getFolder()+"/"+obj.getFile()+obj.getExten()));
        mediaPlayer = MediaPlayer.create(this, uri);*/


    }


    private void mediaPlayerListenerAction() {

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                Log.e("OnPreparedL","***********************");
                if(!mediaPlayer.isPlaying())
                    mediaPlayer.start();

            }
        });

        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {


            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                //stopTimeIntervalupdationView();
                finish();
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {


                /*mpv.stop();
                stopAnimation();*/
                return false;
            }
        });
    }

    private void zoomingAction() {

        /*Zoomy.Builder builder = new Zoomy.Builder(this).target(frameLayout);
        builder.register();*/

        /*Zoomy.Builder builder = new Zoomy.Builder(this)
                .target(frameLayout)
                .enableImmersiveMode(false)
                .animateZooming(false);

        builder.register();*/


        final ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(this, new OnPinchListener());

        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                scaleGestureDetector.onTouchEvent(event);
                return true;
            }
        });

    }


    class AttemptLoadVideoWidth extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ThreeDvideosActivity.this);
            pDialog.setTitle("Please wait");
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(ThreeDvideosActivity.this, getMedia());
            videoWidth = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            videoHeight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            retriever.release();


            /*if(videoWidth==0){
                videoWidth = 640;
                videoHeight = 480;
            }*/

            videoWidth = 340;
            videoHeight = 180;

            Log.e("*****",videoWidth+"******************************"+videoHeight);


            return null;
        }
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            loadAfterScreenSize();
        }

    }

    private void loadAfterScreenSize() {

        /*DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;*/
        int height = getIntent().getIntExtra("height", 0);
        int width = getIntent().getIntExtra("width", 0);

        Log.e("height","*********"+height);
        Log.e("frameHeight","*********"+frameLayout.getHeight());

        screenWidth = width;
        screenHeight = height;
        setDefaultView();
    }




    private void loadUI_H(int space) {

        Log.e("space",""+space);
        Log.e("screenWidth",""+screenWidth);

        space2.requestLayout();
        space2.getLayoutParams().width = space;

        space4.requestLayout();
        space4.getLayoutParams().width = space;
    }

    private void loadUI_V(int space) {

        Log.e("space-V",""+space);
        Log.e("screenWidth-V",""+screenWidth);

        space1.requestLayout();
        space1.getLayoutParams().height = space;

        space3.requestLayout();
        space3.getLayoutParams().height = space;
    }

    public void button1Click(View view){

        int space = space2.getWidth();
        space = space-20;
        if(space<min_space_H)
            space = min_space_H;

        loadUI_H(space);
    }

    public void button2Click(View view){

        int space = space2.getWidth();
        space = space+20;
        if(space>max_space_H)
            space = max_space_H;

        loadUI_H(space);
    }

    public void button3Click(View view){

        int space = space3.getHeight();
        space = space-20;
        if(space<min_space_V)
            space = min_space_V;

        loadUI_V(space);
    }

    public void button4Click(View view){

        int space = space3.getHeight();
        space = space+20;
        if(space>max_space_V)
            space = max_space_V;

        loadUI_V(space);
    }

    public void button5Click(View view){

        frameLayout.scale(1.2f, 50f, 50f);
        //mVideoView1.scale(1.2f, 50f, 50f);
        //mVideoView2.scale(1.2f, 50f, 50f);
        //mVideoView1.scale(1.2f, 50f, 50f);
        //mVideoView1.scale(1.2f, 50f, 50f);
    }



    private void setDefaultView()
    {

        /*MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(MainActivity.this, getMedia());
        int width_v = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        int height_v = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        retriever.release();*/

        Log.e("width_v",""+videoWidth);
        Log.e("height_v",""+videoHeight);

        int width_r = (screenWidth*7)/10;//((frameLayout.getWidth())*7)/10;
        int height_r;
        /*if(width_r==0)
            width_r=500;*/

        float ratio = 1.0f;

        ratio = (float) videoWidth/width_r;

        height_r =(int) ( ((float)videoHeight)/ratio );

        Log.e("width_r",""+width_r);
        Log.e("height_r",""+height_r);

        view1.requestLayout();
        view2.requestLayout();
        view3.requestLayout();
        view4.requestLayout();

        mVideoView1.requestLayout();
        mVideoView2.requestLayout();
        mVideoView3.requestLayout();
        mVideoView4.requestLayout();

        mVideoView1.getLayoutParams().width = width_r;
        mVideoView1.getLayoutParams().height = height_r;

        mVideoView2.getLayoutParams().width = width_r;
        mVideoView2.getLayoutParams().height = height_r;

        mVideoView3.getLayoutParams().width = width_r;
        mVideoView3.getLayoutParams().height = height_r;

        mVideoView4.getLayoutParams().width = width_r;
        mVideoView4.getLayoutParams().height = height_r;

        //margin_H = (height_r/2)-5;
        //margin_V = (width_r/2) + (height_r/2) -5;

        //ViewGroup.LayoutParams layoutParams = mVideoView4.getLayoutParams();
        //FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mVideoView1.getLayoutParams();
        //RotateLayout topRotateLayout = findViewById(R.id.topRotateLayout);
        //topRotateLayout.getLayoutParams().l

        int view2Width = height_r;//view2.getWidth();

        min_space_H = (screenWidth/2) - ((view2Width)/2) ;
        max_space_H = screenWidth - ((view2Width)/2);

        Log.e("view2Width",""+view2Width);
        loadUI_H(min_space_H + (view2Width/2) +5 );

        min_space_V = (screenHeight/2) - ((view2Width)/2) ;
        max_space_V = screenHeight - ((view2Width)/2);
        loadUI_V(min_space_V + (width_r/2) + (view2Width/2) + 5 );

        loadPostUI(1);


    }

    private void loadPostUI(final int pos){

        //loadSurfaceListener_1();
        //loadSurfaceListener();
        //playAll();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(pos==1){
                    SurfaceTexture surfaceTexture = mVideoView1.getSurfaceTexture();
                    Log.e("","");
                    if(surfaceTexture!=null)
                        loadPlayer_1(surfaceTexture);
                    else
                        loadPostUI(pos);
                }
                else if(pos==2){
                    SurfaceTexture surfaceTexture = mVideoView2.getSurfaceTexture();
                    if(surfaceTexture!=null)
                        loadPlayer_2(surfaceTexture);
                    else
                        loadPostUI(pos);
                }
                else if(pos==3){
                    SurfaceTexture surfaceTexture = mVideoView3.getSurfaceTexture();
                    if(surfaceTexture!=null)
                        loadPlayer_3(surfaceTexture);
                    else
                        loadPostUI(pos);
                }
                else if(pos==4){
                    SurfaceTexture surfaceTexture = mVideoView4.getSurfaceTexture();
                    if(surfaceTexture!=null)
                        loadPlayer_4(surfaceTexture);
                    else
                        loadPostUI(pos);
                }

            }
        }, 300);

    }



    private void loadPlayer_1(SurfaceTexture surface){

        Surface s = new Surface(surface);

        try {
            mMediaPlayer1= new MediaPlayer();
            mMediaPlayer1.setDataSource(ThreeDvideosActivity.this, getMedia());
            //mMediaPlayer1.setDataSource("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4");

            mMediaPlayer1.setSurface(s);
            mMediaPlayer1.prepare();
            mMediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer1.setVolume(0,0);
            mMediaPlayer1.setLooping(true);

            //mMediaPlayer1.start();

            loadPostUI(2);

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void loadPlayer_2(SurfaceTexture surface){
        Surface s = new Surface(surface);

        try {
            mMediaPlayer2= new MediaPlayer();
            mMediaPlayer2.setDataSource(ThreeDvideosActivity.this, getMedia());
            //mMediaPlayer2.setDataSource("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4");

            mMediaPlayer2.setSurface(s);
            mMediaPlayer2.prepare();
            mMediaPlayer2.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer2.setVolume(0,0);
            mMediaPlayer2.setLooping(true);

            //mMediaPlayer2.start();

            loadPostUI(3);

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void loadPlayer_3(SurfaceTexture surface){
        Surface s = new Surface(surface);

        try {
            mMediaPlayer3= new MediaPlayer();
            mMediaPlayer3.setDataSource(ThreeDvideosActivity.this, getMedia());
            //mMediaPlayer1.setDataSource("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4");

            mMediaPlayer3.setSurface(s);
            mMediaPlayer3.prepare();
            mMediaPlayer3.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer3.setVolume(0,0);
            mMediaPlayer3.setLooping(true);

            //mMediaPlayer3.start();

            loadPostUI(4);

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void loadPlayer_4(SurfaceTexture surface){
        Surface s = new Surface(surface);

        try {
            mMediaPlayer4= new MediaPlayer();
            mMediaPlayer4.setDataSource(ThreeDvideosActivity.this, getMedia());
            //mMediaPlayer4.setDataSource("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4");

            mMediaPlayer4.setSurface(s);
            mMediaPlayer4.prepare();
            mMediaPlayer4.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer4.setVolume(0,0);
            mMediaPlayer4.setLooping(true);

            //hide

            mMediaPlayer1.start();
            mMediaPlayer2.start();
            mMediaPlayer3.start();
            mMediaPlayer4.start();

            //loadSurfaceListener_3();

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

/*    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        Surface s = new Surface(surface);

        try {
            mMediaPlayer= new MediaPlayer();
            mMediaPlayer.setDataSource(this, getMedia());
            //mMediaPlayer.setDataSource("http://daily3gp.com/vids/747.3gp");
            mMediaPlayer.setSurface(s);
            mMediaPlayer.prepare();
            *//*mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);*//*
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.start();

            mMediaPlayer.setVolume(0,0);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }*/

    private Uri getMedia() {


       // return Uri.parse("android.resource://" + this.getPackageName() + "/" + getIntent().getIntExtra("pathfileRes",0));

        return Uri.parse(getIntent().getStringExtra("pathfileRes").trim());

    }




    private void statusBarColor(){
        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));
        }
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();

        /*Intent intent2 = new Intent(this, FilePickActivity.class);
        startActivity(intent2);*/
        finish();
    }

    @Override
    protected void onDestroy() {

        /*mVideoView1.getSurfaceTexture().release();
        mVideoView1.getSurfaceTexture().release();
        mVideoView2.getSurfaceTexture().release();
        mVideoView3.getSurfaceTexture().release();
        mVideoView4.getSurfaceTexture().release();*/

        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (mMediaPlayer1 != null) {
            mMediaPlayer1.release();
            mMediaPlayer1 = null;
        }
        if (mMediaPlayer2 != null) {
            mMediaPlayer2.release();
            mMediaPlayer2 = null;
        }
        if (mMediaPlayer3 != null) {
            mMediaPlayer3.release();
            mMediaPlayer3 = null;
        }
        if (mMediaPlayer4 != null) {
            mMediaPlayer4.release();
            mMediaPlayer4 = null;
        }

    }

    private class OnPinchListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        float startingSpan;
        float endSpan;
        float startFocusX;
        float startFocusY;


        public boolean onScaleBegin(ScaleGestureDetector detector) {
            startingSpan = detector.getCurrentSpan();
            startFocusX = detector.getFocusX();
            startFocusY = detector.getFocusY();
            return true;
        }


        public boolean onScale(ScaleGestureDetector detector) {
            frameLayout.scale(detector.getCurrentSpan()/startingSpan, startFocusX, startFocusY);
            return true;
        }

        public void onScaleEnd(ScaleGestureDetector detector) {
            //frameLayout.restore();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.e("TextureAvailable","*******************************");
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.e("TextureSizeChanged","*******************************");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.e("TextureDestroyed","*******************************");
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        Log.e("TextureUpdated","*******************************");
    }

}