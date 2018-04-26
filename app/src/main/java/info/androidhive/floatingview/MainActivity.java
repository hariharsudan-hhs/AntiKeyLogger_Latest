    package info.androidhive.floatingview;

    import android.content.Intent;
    import android.os.Bundle;
    import android.support.v7.app.AppCompatActivity;
    import android.view.View;
    import android.widget.ImageView;

    public class MainActivity extends AppCompatActivity {
        ImageView b1;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            b1 = (ImageView)findViewById(R.id.notify_me);

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startService(new Intent(MainActivity.this, FloatingViewService.class));
                    finish();
                }
            });

      }


    }
