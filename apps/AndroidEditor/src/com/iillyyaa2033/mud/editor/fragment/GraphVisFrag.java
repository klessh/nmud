package com.iillyyaa2033.mud.editor.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.iillyyaa2033.mud.editor.view.MapView;
import com.iillyyaa2033.mud.editor.view.GrVV;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.StringTokenizer;
import java.util.ArrayList;
import android.widget.Toast;

public class GraphVisFrag extends Fragment {

	GrVV shower;
	Button btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout ll = new LinearLayout(getActivity());
		ll.setOrientation(LinearLayout.VERTICAL);
		shower = new GrVV(getActivity());
		btn = new Button(getActivity());
		btn.setText("Load new gr");
		btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					doSmfUsful();
				}
			});
		ll.addView(btn);
		ll.addView(shower);
		return ll;
	}

	void doSmfUsful() {
		final EditText f = new EditText(getActivity());
		(new AlertDialog.Builder(getActivity()))
			.setView(f)
			.setPositiveButton("doit", new AlertDialog.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2) {
					String text = f.getText().toString();
					StringTokenizer t = new StringTokenizer(text, "\n");
					ArrayList<String> points = new ArrayList<String>();

					String tp = t.nextToken();
					StringTokenizer tpt = new StringTokenizer(tp, " ");
					while (tpt.hasMoreTokens()) {
						points.add(tpt.nextToken());
					}
					
					ArrayList<Integer> links = new ArrayList<Integer>();
					while (t.hasMoreTokens()) {
						String tl = t.nextToken();
						StringTokenizer tlt = new StringTokenizer(tl, " ");
						links.add(Integer.parseInt(tlt.nextToken()));
						links.add(Integer.parseInt(tlt.nextToken()));
					}

					shower.updateGr(links, points);
				}
			})
			.show();
	}
}
