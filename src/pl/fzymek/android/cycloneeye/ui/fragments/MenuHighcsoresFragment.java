package pl.fzymek.android.cycloneeye.ui.fragments;

import java.util.LinkedList;
import java.util.List;

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

public class MenuHighcsoresFragment extends ListFragment {

	private final static String TAG = MenuHighcsoresFragment.class
			.getSimpleName();

	private TextView empty;
	private ProgressDialog progressDialog;
	private List<Highscore> highscoresList;
	private HighscoresAdapter highscoresAdapter;
	private Context context;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			Log.d(TAG, "Handler received message: " + msg);
			progressDialog.dismiss();

			synchronized (highscoresList) {
				if (highscoresList == null || highscoresList.size() <= 0) {
					Log.d(TAG, "No highscores found");
					getListView().setVisibility(View.INVISIBLE);
					empty.setVisibility(View.VISIBLE);
				} else {

					Log.d(TAG, "Found " + highscoresList.size() + " highscores");
					highscoresAdapter = new HighscoresAdapter(context,
							highscoresList);
					getListView().setVisibility(View.VISIBLE);
					empty.setVisibility(View.INVISIBLE);
					setListAdapter(highscoresAdapter);
				}
			}
		}

	};

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

		populateHighScores();
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
				for (highscoreCursor.moveToFirst(); !highscoreCursor
						.isAfterLast(); highscoreCursor.moveToNext()) {
					final String player = highscoreCursor
							.getString(playerColumnIndex);
					final String score = highscoreCursor
							.getString(scoresColumnIndex);
					final Highscore hs = new Highscore(player, score);
					highscoresList.add(hs);
				}

				Log.d(TAG,
						"Reading quotes done, retrieved "
								+ highscoresList.size()
						+ " quotes, notyfying handler");
				handler.sendEmptyMessage(0);

			};
		}.start();

	}

}
