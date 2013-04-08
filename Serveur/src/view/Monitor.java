package view;

import java.awt.Dimension;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;

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
 *
 */

public class Monitor extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TimeSeries tsConnect = new TimeSeries("Nombre de joueurs connectés", Millisecond.class);
	private TimeSeries tsGames = new TimeSeries("Nombre de parties en cours", Millisecond.class);
	private TimeSeries tsMemory = new TimeSeries("Mémoire utilisée par le serveur", Millisecond.class);
	private ValueAxis axisM;
	private ValueAxis axisC;
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JPanel memory;
    private JPanel connected;
    private JPanel games;
    private JPanel infos;
    private JLabel timerLaunch;
    private JLabel timeLocal;
	private JLabel nbC;
	private JLabel nbG;
	private JLabel nbM;
	private JLabel lblMaxRam;
	private long maxRam;
	
    public Monitor() {
    	memory = getGraph(tsMemory, -1, 200000, axisM, "Heure", "Ko");
    	connected = getGraph(tsConnect, -1, 10, axisC, "Heure", "");
    	games = getGraph(tsGames, -1, 10, axisC, "Heure", "");
    	infos = new JPanel(new SpringLayout());
    	timerLaunch = new JLabel("0");
    	timeLocal = new JLabel("0");
    	nbC = new JLabel("");
    	nbG = new JLabel("");
    	nbM = new JLabel("");
    	lblMaxRam = new JLabel("");
    	maxRam = 0;
    	infos.add(new JLabel("Heure du serveur :"));
    	infos.add(timeLocal);
    	infos.add(new JLabel("Temps d'activité du serveur :"));
    	infos.add(timerLaunch);
    	infos.add(new JLabel("Adresse ip du serveur :"));
    	try {
    		infos.add(new JLabel(InetAddress.getLocalHost().getHostAddress()));
    	} catch(UnknownHostException e) {
    		infos.add(new JLabel("Impossible de récupérer l'adresse ip."));
    	}
    	infos.add(new JLabel("Intervalle de sauvegarde historique :"));
    	infos.add(new JLabel("Toutes les 10 minutes"));
    	infos.add(new JLabel(""));
    	infos.add(new JLabel(""));
    	infos.add(new JLabel("Joueurs connectés :"));
    	infos.add(nbC);
    	infos.add(new JLabel("Parties en cours :"));
    	infos.add(nbG);
    	infos.add(new JLabel("Charge mémoire actuelle :"));
    	infos.add(nbM);
    	infos.add(new JLabel("Charge mémoire maximum :"));
    	infos.add(lblMaxRam);
    	SpringUtilities.makeGrid(infos, 9, 2, 5, 5, 5, 5);
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
    	nbC.setText(conn+" joueurs connectés");
    	nbG.setText(gam+" parties en cours");
    	nbM.setText(l+" Ko");
    	
    	if(l>maxRam)
    		maxRam = l;
    	
    	lblMaxRam.setText(maxRam+" Ko");
    }
    
	public void setTimes(long l, String s2) {
		String h,m,s;
		h = String.valueOf(TimeUnit.SECONDS.toHours(l));
		m = String.valueOf(TimeUnit.SECONDS.toMinutes(l));
		s = String.valueOf(TimeUnit.SECONDS.toSeconds(l)-TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(l)));
		if(Integer.parseInt(h)<10)
			h="0"+h;
		if(Integer.parseInt(m)<10)
			m="0"+m;
		if(Integer.parseInt(s)<10)
			s="0"+s;
		String time = String.format("%d jours et %s:%s:%s",
				TimeUnit.SECONDS.toDays(l),
				h,
				m,
				s
		);
		timerLaunch.setText(time);
		timeLocal.setText(s2);
		infos.repaint();
	}
}
