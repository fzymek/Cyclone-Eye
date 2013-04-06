package pl.fzymek.android.cycloneeye.game.engine.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import pl.fzymek.android.cycloneeye.game.engine.FileIO;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class CEGameFileIO implements FileIO {

	private final static String TAG = CEGameFileIO.class.getSimpleName();

	final Context context;
	final AssetManager assets;
	final String externalStoragePath;

	public CEGameFileIO(Context context) {
        this.context  =  context;
        this.assets  =  context.getAssets();
        this.externalStoragePath  =  Environment.getExternalStorageDirectory()
                .getAbsolutePath()  +  File.separator;
		Log.d(TAG, "File IO created with path: " + externalStoragePath);

    }
    public InputStream readAsset(String fileName) throws IOException {
		Log.d(TAG, "Reading asset: " + fileName);
        return assets.open(fileName);
    }
    public InputStream readFile(String fileName) throws IOException {
		Log.d(TAG, "Reading file: " + fileName);
        return new FileInputStream(externalStoragePath  +  fileName);
    }
    public OutputStream writeFile(String fileName) throws IOException {
		Log.d(TAG, "Writing file: " + fileName);
        return new FileOutputStream(externalStoragePath  +  fileName);
    }
    public SharedPreferences getPreferences() {
		Log.d(TAG, "Reading preferences");
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

	@Override
	public InputStream readResource(int res) throws IOException {
		return context.getResources().openRawResource(res);
	}
}