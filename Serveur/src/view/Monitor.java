package view;

import java.awt.Dimension;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class Monitor extends JPanel {
	
	private TimeSeries ts = new TimeSeries("Nombre de joueurs connectés", Millisecond.class);
	private ValueAxis axis;
	
    public Monitor() {
    	TimeSeriesCollection dataset = new TimeSeriesCollection(ts);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "",
            "Heure",
            "",
            dataset,
            true,
            true,
            false
        );
        final XYPlot plot = chart.getXYPlot();
        axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);
        axis = plot.getRangeAxis();
        axis.setRange(-1, 10); 
        axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        ChartPanel pan = new ChartPanel(chart);
        pan.setPreferredSize(new Dimension(890, 400));
        this.add(pan);
    }
    
    public void updateGraph(int num) {
        ts.addOrUpdate(new Millisecond(), num);
        if(num<20)
        	axis.setRange(-1.0, 20.0); 
        else if(num<50)
    		axis.setRange(-1.0, 50.0);
        else
    		axis.setRange(-1.0, 100.0);
    }
}
