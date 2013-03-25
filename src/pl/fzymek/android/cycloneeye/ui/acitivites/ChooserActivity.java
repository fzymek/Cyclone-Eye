package pl.fzymek.android.cycloneeye.ui.acitivites;

import pl.fzymek.android.cycloneeye.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooserActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.chooser);

		ListView lv = (ListView) findViewById(R.id.mylist);

		String[] examples = { "TriangleActivity", "TexturedActivity",
				"MultipleTornadosActivity", "BasicPhysicsActivity",
				"CollisionActivity", "CameraActivity", "AnimationActivity","TextActivity",
				"GameActivity" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				examples);
		
		lv.setAdapter(adapter);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				final String pkg = "pl.fzymek.android.cycloneeye.game.screens";
				final CharSequence text = ((TextView)view).getText();
				try {
					
					@SuppressWarnings("unchecked")
					Class<? extends Activity> c = (Class<? extends Activity>) Class
							.forName(pkg + "." + text);
					startActivity(new Intent(getApplicationContext(), c));
				} catch (ClassNotFoundException e) {
					Toast.makeText(getApplicationContext(),
							"Cannot find class: " + pkg + "." + text,
							Toast.LENGTH_SHORT).show();
				}
				
				
			}
		};
		
		lv.setOnItemClickListener(listener);

	}

}
