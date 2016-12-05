package wadru.dcapp.Download;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * Created by hodongkim on 2016. 11. 27..
 */

public class MediaScanning implements MediaScannerConnection.MediaScannerConnectionClient{

    private MediaScannerConnection mConnection;
    private File mTargetFile;

    public MediaScanning(Context mContext, File targetFile) {
        this.mTargetFile = targetFile;

        mConnection = new MediaScannerConnection(mContext, this);
        mConnection.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mConnection.scanFile(mTargetFile.getAbsolutePath(), null);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mConnection.disconnect();
    }

    // 외장 메모리 전체 MediaScanning
    public static void startExtMediaScan(Context mContext){
        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }

    // 선택된 파일에 대한 MediaScanning
    public static void startFileMediaScan(Context mContext, Uri uri){
        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
//        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }
}