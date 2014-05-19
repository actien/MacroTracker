package edu.jhu.cs.ctien4.macrotracker;

import java.util.ArrayList;
import java.util.List;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class GraphFragment extends Fragment {
	private static LinearLayout layout;
	private LoggerDB db;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.graph_fragment, container, false);
		view.setBackgroundColor(Color.BLACK);
		
		layout = (LinearLayout) view.findViewById(R.id.graph_layout);
		genGraph();
		
		return view;
	}
	
	public void setLoggerDb(LoggerDB db){
		this.db = db;
	}
	
	/**
	 * Clears the current layout and re-adds the newly generated graph.
	 */
	public void genGraph(){
		layout.removeAllViews();
		
		List<GraphPoint> points = db.getMacrosGroupedDate();
		
		List<String> labels = new ArrayList<String>();
		GraphViewData[] graphViewData = new GraphViewData[points.size()];
		
		for(GraphPoint point : points) {
			labels.add(point.getLabel());
			graphViewData[points.indexOf(point)] = new GraphViewData(point.getX(), point.getY());
		}
		
		GraphViewSeries caloriesSeries = new GraphViewSeries(graphViewData);
		
			
		GraphView graphView = new LineGraphView(getActivity(), "Calories Consumed");
		graphView.addSeries(caloriesSeries);
		
		graphView.setCustomLabelFormatter(new MyCustomLabel(labels));
		graphView.getGraphViewStyle().setTextSize(14.0f);
		
		layout.addView(graphView);
	}
	
	/**
	 * Private custom label class to format labels correctly.
	 * @author Andy
	 *
	 */
	private class MyCustomLabel implements CustomLabelFormatter {
		List<String> labels;
		
		public MyCustomLabel(List<String> labels){
			this.labels = labels;
		}
		
		@Override
		public String formatLabel(double value, boolean isValueX) {
			if (isValueX){
				return labels.get((int) value);
			}
			return null;
		}
	}

}
