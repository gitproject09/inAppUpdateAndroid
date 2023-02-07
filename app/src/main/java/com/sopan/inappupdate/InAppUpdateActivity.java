package com.sopan.inappupdate;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.sopan.inappupdate.UpdateManager.FlexibleUpdateDownloadListener;
import com.sopan.inappupdate.UpdateManager.UpdateInfoListener;

import static com.sopan.inappupdate.UpdateManager.FLEXIBLE;
import static com.sopan.inappupdate.UpdateManager.IMMEDIATE;

public class InAppUpdateActivity extends AppCompatActivity {

    // Declare the UpdateManager
    UpdateManager mUpdateManager;

    TextView txtFlexibleUpdateProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_update);

        TextView txtCurrentVersion = findViewById(R.id.txt_current_version);
        TextView txtAvailableVersion = findViewById(R.id.txt_available_version);
        TextView txtStalenessDays = findViewById(R.id.txt_staleness_days);
        txtFlexibleUpdateProgress = findViewById(R.id.txt_flexible_progress);

        txtCurrentVersion.setText(String.valueOf(BuildConfig.VERSION_CODE));

        // Initialize the Update Manager with the Activity and the Update Mode
        mUpdateManager = UpdateManager.Builder(this);

        // Callback from UpdateInfoListener
        // You can get the available version code of the apk in Google Play
        // Number of days passed since the user was notified of an update through the Google Play
        mUpdateManager.addUpdateInfoListener(new UpdateInfoListener() {
            @Override
            public void onReceiveVersionCode(final int code) {
                txtAvailableVersion.setText(String.valueOf(code));
            }

            @Override
            public void onReceiveStalenessDays(final int days) {
                txtStalenessDays.setText(String.valueOf(days));
            }
        });

        // Callback from Flexible Update Progress
        // This is only available for Flexible mode
        // Find more from https://developer.android.com/guide/playcore/in-app-updates#monitor_flexible
        mUpdateManager.addFlexibleUpdateDownloadListener(new FlexibleUpdateDownloadListener() {
            @Override
            public void onDownloadProgress(final long bytesDownloaded, final long totalBytes) {
                txtFlexibleUpdateProgress.setText("Downloading: " + bytesDownloaded + " / " + totalBytes);
            }
        });

    }

    public void callFlexibleUpdate(View view) {
        // Start a Flexible Update
        mUpdateManager.mode(FLEXIBLE).start();
        txtFlexibleUpdateProgress.setVisibility(View.VISIBLE);
    }

    public void callImmediateUpdate(View view) {
        // Start a Immediate Update
        mUpdateManager.mode(IMMEDIATE).start();
    }
}
