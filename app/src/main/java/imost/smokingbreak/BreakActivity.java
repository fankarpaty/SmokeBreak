package imost.smokingbreak;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;


public class BreakActivity extends ActionBarActivity {

    private TextView tvBreakTime;
    private ProgressBar pbSmoke;
    private Thread breakTimeUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break);

        tvBreakTime = (TextView) findViewById(R.id.tvBreakTime);
        pbSmoke = (ProgressBar) findViewById(R.id.progressBar);
        pbSmoke.setMax(100);
    }

    @Override
    protected void onStart() {
        super.onStart();
        breakTimeUpdater = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Thread.interrupted()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvBreakTime.setText(Prefs.breakTimeToStr());
                            long percentage = 100L * Prefs.getBreakTimeLeft() / Prefs.BREAK_DURATION;
                            pbSmoke.setProgress((int) percentage);
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
        breakTimeUpdater.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        breakTimeUpdater.interrupt();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_break, menu);
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
