package view;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 * Gère l'ensemble des graphes et du panel avec les onglets
 * @author Matthieu
 *
 */

public class Monitor extends JPanel {
	
	private TimeSeries tsConnect = new TimeSeries("Nombre de joueurs connectés", Millisecond.class);
	private TimeSeries tsGames = new TimeSeries("Nombre de parties en cours", Millisecond.class);
	private TimeSeries tsMemory = new TimeSeries("Mémoire utilisée par le serveur", Millisecond.class);
	private ValueAxis axisM;
	private ValueAxis axisC;
	private ValueAxis axisB;
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JPanel memory;
    private JPanel connected;
    private JPanel games;
    private JPanel infos;
	
    public Monitor() {
    	memory = getGraph(tsMemory, -1, 200000, axisM, "Heure", "Ko");
    	connected = getGraph(tsConnect, -1, 10, axisC, "Heure", "");
    	games = getGraph(tsGames, -1, 10, axisC, "Heure", "");
    	infos = new JPanel();
    	
    	tabbedPane.addTab("Nombre de joueurs connectés", connected);
    	tabbedPane.addTab("Nombre de parties en cours", games);
    	tabbedPane.addTab("Utilisation de la mémoire", memory);
    	tabbedPane.addTab("Informations du serveur", infos);
    	
    	this.add(tabbedPane);
    }
    
    
    /**
     * Retourne un JPanel contenant le graphe
     * @param ts Ensemble de data a traiter
     * @param min Valeur minimum du graphe
     * @param max Valeur maximum du graphe
     * @param axe Definition pour les axes
     * @param axeX Legende du graphe en X
     * @param axeY Legende du graphe en Y
     * @return
     */
    private JPanel getGraph(TimeSeries ts, int min, int max, ValueAxis axe, String axeX, String axeY) {
    	TimeSeriesCollection dataset = new TimeSeriesCollection(ts);
        JFreeChart connectChart = ChartFactory.createTimeSeriesChart(
            "",
            axeX,
            axeY,
            dataset,
            false,
            true,
            false
        );
        final XYPlot plot = connectChart.getXYPlot();
        axe = plot.getDomainAxis();
        axe.setAutoRange(true);
        axe.setFixedAutoRange(60000.0);
        axe = plot.getRangeAxis();
        axe.setRange(min, max); 
        axe.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        ChartPanel genPan = new ChartPanel(connectChart);
        genPan.setPreferredSize(new Dimension(890, 400));
        return genPan;
    }
    
    /**
     * Ajoute des valeurs sur le graphe
     * @param conn Mise a jour du nombre de connectes
     * @param l Mise a jour de la charge mémoire de la JVM
     * @param gam Mise a jour du nombre de parties en cours
     */
    public void updateGraph(int conn, long l, int gam) {
    	tsConnect.addOrUpdate(new Millisecond(), conn);
    	tsMemory.addOrUpdate(new Millisecond(), l);
    	tsGames.addOrUpdate(new Millisecond(), gam);
    }
}
