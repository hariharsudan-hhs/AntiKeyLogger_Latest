package info.androidhive.floatingview;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FloatingViewService extends Service {

    private WindowManager mWindowManager;
    private View mFloatingView;
    Boolean bug;
    public FloatingViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Inflate the floating view layout we created
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        //The root element of the collapsed view layout
        final View collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        //The root element of the expanded view layout
        final View expandedView = mFloatingView.findViewById(R.id.expanded_container);


        //Set the close button
        ImageView closeButtonCollapsed = (ImageView) mFloatingView.findViewById(R.id.close_btn);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the service and remove the from from the window
                stopSelf();
            }
        });

        //Set the view while floating view is expanded.
        //Set the play button.
        ImageView playButton = (ImageView) mFloatingView.findViewById(R.id.play_btn);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatingViewService.this, "Playing the song.", Toast.LENGTH_LONG).show();
            }
        });

        //Set the next button.
        ImageView nextButton = (ImageView) mFloatingView.findViewById(R.id.next_btn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatingViewService.this, "Playing next song.", Toast.LENGTH_LONG).show();
            }
        });

        //Set the pause button.
        ImageView prevButton = (ImageView) mFloatingView.findViewById(R.id.prev_btn);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatingViewService.this, "Playing previous song.", Toast.LENGTH_LONG).show();
            }
        });

        //Set the close button
        ImageView closeButton = (ImageView) mFloatingView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
            }
        });

        //Open the application on thi button click
        ImageView openButton = (ImageView) mFloatingView.findViewById(R.id.open_button);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open the application  click.
                Intent intent = new Intent(FloatingViewService.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                //close the service and remove view from the view hierarchy
                stopSelf();
            }
        });

        //Drag and move floating view using user's touch action.
        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                Toast.makeText(getApplicationContext(),"Checking Please Wait...",Toast.LENGTH_SHORT).show();





                                    final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    System.out.println("EditTextKeyboard " + imm.getCurrentInputMethodSubtype());
                                    InputMethodSubtype subtype = imm.getCurrentInputMethodSubtype();
                                    List<InputMethodInfo> inputMethodInfos = imm.getEnabledInputMethodList();

                                for (InputMethodInfo ininfo:inputMethodInfos)
                                {
                                    Log.d("KP: ","Package :"+ ininfo.getPackageName());
                                }
                                if (subtype==null)
                                {
                                    try
                                    {
                                        ContentResolver resolver=getContentResolver();
                                        String oldDefaultKeyboard = Settings.Secure.getString(resolver, Settings.Secure.DEFAULT_INPUT_METHOD);
                                        String[] selectedkeyboard = oldDefaultKeyboard.split("/");
                                        Log.d("Btype","Enabled: "+selectedkeyboard[0]);

                                        for (InputMethodInfo inputinfo : inputMethodInfos)
                                        {
                                            Log.i("info","Installed Package:" + inputinfo.getPackageName());
                                            System.out.println("P"+ getPackageManager());
                                            PackageInfo info = getPackageManager().getPackageInfo(inputinfo.getPackageName(), PackageManager.GET_PERMISSIONS);
                                            String iv=info.packageName;
                                            final PackageManager pm = getApplicationContext().getPackageManager();
                                            ApplicationInfo ai;
                                            try
                                            {
                                                ai = pm.getApplicationInfo( iv, 0);
                                            }
                                            catch (final PackageManager.NameNotFoundException e)
                                            {
                                                ai = null;
                                            }
                                            final String appName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");

                                            System.out.println("iv "+iv);
                                            if(selectedkeyboard[0].equals(iv))
                                            {
                                                ArrayList<String> permission = new ArrayList<String>();
                                                for (String p : info.requestedPermissions)
                                                {
                                                    Log.d(inputinfo.getPackageName(), "Permission : " + p);
                                                    permission.add(p);
                                                }
                                                if(permission.contains("android.permission.WRITE_EXTERNAL_STORAGE")&&(!permission.contains("android.permission.READ_EXTERNAL_STORAGE")))
                                                {
                                                    bug=false;
                                                    ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                                                    List<ActivityManager.RunningAppProcessInfo> pids = am.getRunningAppProcesses();
                                                    Toast.makeText(getApplicationContext(), "Please Change Input Method Type", Toast.LENGTH_SHORT).show();
                                                    AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                                                    alert.setMessage("Choose Native KeyBoard or GBoard");
                                                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            imm.showInputMethodPicker();
                                                        }
                                                    });
                                                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                        }
                                                    });
                                                    AlertDialog alertDialog = alert.create();
                                                    alertDialog.show();
                                                }

                                                if(permission.contains("android.permission.READ_EXTERNAL_STORAGE")&&permission.contains("android.permission.WRITE_EXTERNAL_STORAGE")&&permission.contains("android.permission.ACCESS_NETWORK_STATE"))
                                                {
                                                    bug = false;
                                                    Toast.makeText(getApplicationContext(), "DATA THEFT OCCURS!!!", Toast.LENGTH_SHORT).show();
                                                    String path = Environment.getExternalStorageDirectory().getPath()+"/";
                                                    final Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Intent i = new Intent(FloatingViewService.this,MainActivity2.class);
                                                            startActivity(i);
                                                        }
                                                    }, 3000);

                                                    final Dialog dialog = new Dialog(getApplicationContext());
                                                    dialog.setContentView(R.layout.dialogbox);
                                                    dialog.setTitle("Alert");

                                                    TextView text = (TextView) dialog.findViewById(R.id.titlebar);
                                                    text.setText("Permissions of "+appName);

                                                    ListView plist = (ListView)dialog.findViewById(R.id.permissionlist);
                                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, permission );
                                                    plist.setAdapter(arrayAdapter);

                                                    Button dialogButton = (Button) dialog.findViewById(R.id.done);
                                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v)
                                                        {
                                                            dialog.dismiss();
                                                        }
                                                    });

                                                    dialog.show();
                                                }
                                            }
                                        }
                                        if(bug==true)
                                        {
                                            Toast.makeText(getApplicationContext(), "External Keyboard Being Used but has no bugs", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "You are Safe! Native Keyboard is being used", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Detect if the floating view is collapsed or expanded.
     *
     * @return true if the floating view is collapsed.
     */
    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }
}
