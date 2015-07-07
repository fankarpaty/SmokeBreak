package imost.smokingbreak;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends ActionBarActivity {

    private Button btnStartStop;
    private TextView tvWorkingFor;

    private Thread workingForUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartStop = (Button) findViewById(R.id.btnStartStop);
        tvWorkingFor = (TextView) findViewById(R.id.tvWorkingFor);
        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isWorking = Prefs.isWorking();
                btnStartStop.setText(isWorking ? R.string.start_working : R.string.stop_working);
                if (!isWorking) {
                    Prefs.workingStarted();
                } else {
                    Prefs.breakStarted();
                    startActivity(new Intent(MainActivity.this, BreakActivity.class));
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        workingForUpdater.interrupt();
    }

    @Override
    protected void onStart() {
        super.onStart();

        workingForUpdater = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (Thread.interrupted()) {
                        break;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvWorkingFor.setText(Prefs.workingTimeToStr());
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        workingForUpdater.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
