package info.androidhive.floatingview;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by nadus on 09-04-2018.
 */

public class MainActivity2 extends AppCompatActivity {
    Button change;
    ImageView imageView2;
    TextView textView,textView2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView.setText("Attention!");
        textView2.setText("We have detected that you are using a third-party keyboard which may be liable to loss of your personal information. Please change to native keyboard as soon as possible!");
        imageView2.setImageResource(R.drawable.hacker);


        change = (Button)findViewById(R.id.change);
        change.setText("Change KeyBoard");
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (change.getText().equals("Change KeyBoard")) {
                    final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    System.out.println("EditTextKeyboard " + imm.getCurrentInputMethodSubtype());
                    InputMethodSubtype subtype = imm.getCurrentInputMethodSubtype();
                    List<InputMethodInfo> inputMethodInfos = imm.getEnabledInputMethodList();
                    ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningAppProcessInfo> pids = am.getRunningAppProcesses();
                    Toast.makeText(getApplicationContext(), "Please Change Input Method Type", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity2.this);
                    alert.setMessage("Choose Native KeyBoard or GBoard");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            imm.showInputMethodPicker();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText("Success!");
                                    textView2.setText("Successfully changed to Native Keyboard and you are now safe from Data Theft.");
                                    imageView2.setImageResource(R.drawable.safe);
                                    change.setText("Exit");
                                }
                            }, 3000);
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                } else {
                    MainActivity2.this.finish();
                }
            }
        });
    }
}
