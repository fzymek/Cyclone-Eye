package pl.fzymek.android.cycloneeye.ui.fragments;

import pl.fzymek.android.cycloneeye.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MenuOptionsFragment extends Fragment {
	private final static String TAG = MenuOptionsFragment.class
			.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.menu_options_fragment,
				container, false);
		Log.d(TAG, "onCreateView");
		return view;
	}
}
