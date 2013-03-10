package pl.fzymek.android.cycloneeye.adapters;

import java.util.List;

import pl.fzymek.android.cycloneeye.R;
import pl.fzymek.android.cycloneeye.database.dao.Highscore;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HighscoresAdapter extends BaseAdapter {

	private final static String TAG = HighscoresAdapter.class.getSimpleName();
	private Context context;
	private List<? extends Highscore> highscores;

	public HighscoresAdapter(final Context context,
			final List<? extends Highscore> highscores) {

		this.context = context;
		this.highscores = highscores;
	}

	@Override
	public int getCount() {
		return highscores.size();
	}

	@Override
	public Object getItem(int position) {
		return highscores.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			Log.d(TAG, "Creating list item from xml");
			final LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.highscore_list_item,
					parent, false);
			
			holder = new ViewHolder();
			holder.num = (TextView) convertView
					.findViewById(R.id.highscore_position_entry);
			holder.name = (TextView) convertView
					.findViewById(R.id.highscore_name_entry);
			holder.score = (TextView) convertView
					.findViewById(R.id.highscore_score_entry);
			convertView.setTag(holder);
			Log.d(TAG, "data stored in holder");

		} else {
			Log.d(TAG, "Creating list item from tag");
			holder = (ViewHolder) convertView.getTag();
		}

		final Highscore hs = highscores.get(position);
		holder.num.setText("" + position);
		holder.name.setText(hs.getPlayer());
		holder.score.setText(hs.getScore());

		return convertView;
	}

	private static final class ViewHolder {
		protected TextView name, score, num;
	}

}
