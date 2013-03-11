package pl.fzymek.android.cycloneeye.ui.fragments;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import pl.fzymek.android.cycloneeye.R;
import pl.fzymek.android.cycloneeye.adapters.HighscoresAdapter;
import pl.fzymek.android.cycloneeye.database.dao.Highscore;
import pl.fzymek.android.cycloneeye.database.providers.HighscoresProvider.TableMetadata;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author zymekfil
 * 
 */
public class MenuHighcsoresFragment extends ListFragment {

	private final static String TAG = MenuHighcsoresFragment.class
			.getSimpleName();

	private TextView empty;
	private ProgressDialog progressDialog;
	private List<Highscore> highscoresList;
	private HighscoresAdapter highscoresAdapter;
	private Context context;
	private Handler handler = new HighscoresHandler(this);

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated");
		context = getActivity().getApplicationContext();
		Log.d(TAG, "Context: " + context);
		final ListView listView = getListView();
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
		listView.setEmptyView(empty);

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");

		context = getActivity().getApplicationContext();

		Log.d(TAG, "Context: " + context);

		final View view = inflater.inflate(R.layout.menu_highscores_fragment,
				container, false);

		empty = (TextView) view.findViewById(android.R.id.empty);

		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		Log.d(TAG, "changing hidden state to "
				+ (hidden ? "hidden" : "visible"));
		if (hidden) {
			Log.d(TAG, "Hiding fragment");
		} else {
			Log.d(TAG, "Showing fragment");
			populateHighScores();
		}

	}

	private void populateHighScores() {

		Log.d(TAG, "populating quotes...");
		Log.d(TAG, "Context: " + context);
		this.progressDialog = ProgressDialog.show(getActivity(), "Wait...",
				"Loading data...");

		new Thread() {
			public void run() {
				Log.d(TAG, "Reading highscores in separate thread..");
				final Uri quotesUri = TableMetadata.CONTENT_URI;
				final Cursor highscoreCursor = getActivity().managedQuery(
						quotesUri,
						new String[] { TableMetadata.PLAYER,
								TableMetadata.SCORE }, null, null,
						TableMetadata.SCORE + " DESC LIMIT 5");

				final int playerColumnIndex = highscoreCursor
						.getColumnIndex(TableMetadata.PLAYER);
				final int scoresColumnIndex = highscoreCursor
						.getColumnIndex(TableMetadata.SCORE);

				highscoresList = new LinkedList<Highscore>();

				// fill highscores list with sample data
				if (new Random().nextInt(100) + 1 > 70) {

					for (highscoreCursor.moveToFirst(); !highscoreCursor
							.isAfterLast(); highscoreCursor.moveToNext()) {
						final String player = highscoreCursor
								.getString(playerColumnIndex);
						final String score = highscoreCursor
								.getString(scoresColumnIndex);
						final Highscore hs = new Highscore(player, score);
						highscoresList.add(hs);
					}
				} else {

					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					highscoresList.add(new Highscore("Filip", "1236"));
					highscoresList.add(new Highscore("Marcin", "4576"));
					highscoresList.add(new Highscore("Tomiko", "7264"));
					highscoresList.add(new Highscore("Ada", "3456"));
					highscoresList.add(new Highscore("Paulina", "4456"));
				}

				Log.d(TAG,
						"Reading quotes done, retrieved "
								+ highscoresList.size()
								+ " quotes, notyfying handler");
				handler.sendEmptyMessage(0);

			};
		}.start();

	}

	protected static class HighscoresHandler extends Handler {
		private final static String TAG = HighscoresHandler.class
				.getSimpleName();
		private final WeakReference<MenuHighcsoresFragment> highscoresFragmentRef;

		public HighscoresHandler(
				final MenuHighcsoresFragment highscoresFragmentRef) {
			this.highscoresFragmentRef = new WeakReference<MenuHighcsoresFragment>(
					highscoresFragmentRef);
		}

		@Override
		public void handleMessage(final Message msg) {

			if (highscoresFragmentRef.get() != null) {
				final MenuHighcsoresFragment highscoresFragment = highscoresFragmentRef
						.get();
				Log.d(TAG, "Handler received message: " + msg);
				highscoresFragment.progressDialog.dismiss();

				synchronized (highscoresFragment.highscoresList) {
					if (highscoresFragment.highscoresList == null
							|| highscoresFragment.highscoresList.size() <= 0) {
						Log.d(TAG, "No highscores found");
						highscoresFragment.getListView().setVisibility(
								View.INVISIBLE);
						highscoresFragment.empty.setVisibility(View.VISIBLE);
					} else {

						Log.d(TAG,
								"Found "
										+ highscoresFragment.highscoresList
												.size() + " highscores");
						highscoresFragment.highscoresAdapter = new HighscoresAdapter(
								highscoresFragment.getActivity(),
								highscoresFragment.highscoresList);
						highscoresFragment.getListView().setVisibility(
								View.VISIBLE);
						highscoresFragment.empty.setVisibility(View.INVISIBLE);
						highscoresFragment
								.setListAdapter(highscoresFragment.highscoresAdapter);
					}
				}
			}
		}
	}

}
