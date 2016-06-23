package br.com.staroski.graphs.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import br.com.staroski.graphs.Edge;
import br.com.staroski.graphs.Graph;
import br.com.staroski.graphs.Vertex;
import br.com.staroski.graphs.search.DijkstraAlgorithm;

public class VisualDijkstraTest extends JFrame {

	class Config {

		Graph graph;
		Vertex source;
		Vertex target;
	}

	class GraphBuilder {

		private void execute() {
			Config config = config();
			Graph graph = config.graph;
			Vertex source = config.source;
			Vertex target = config.target;
			DijkstraAlgorithm algorithm = new DijkstraAlgorithm(graph);
			algorithm.execute(source);
			List<Vertex> path = algorithm.getPath(target);
			if (path != null) {
				for (Vertex v : path) {
					Celula celula = v.getValue();
					celula.setBackground(Color.YELLOW);
					System.out.print(" " + v.getId());
				}
				System.out.println();
			} else {
				System.out.println("unreachable");
			}
		}

		private Config config() {
			Config cfg = new Config();
			createGraph(cfg, mapa);
			return cfg;
		}

		private Edge getEdgeDown(Celula[][] map, int i, int j) {
			Vertex a = getVertex(map, i, j);
			Vertex b = getVertex(map, i + 1, j);
			if (b == null || map[i + 1][j].isObstaculo()) {
				return null;
			}
			return new Edge(getEdgeId(a, b), a, b, 1);
		}

		private String getEdgeId(Vertex a, Vertex b) {
			return a.getId() + "-" + b.getId();
		}

		private Edge getEdgeLeft(Celula[][] map, int i, int j) {
			Vertex a = getVertex(map, i, j);
			Vertex b = getVertex(map, i, j - 1);
			if (b == null || map[i][j - 1].isObstaculo()) {
				return null;
			}
			return new Edge(getEdgeId(a, b), a, b, 1);
		}

		private Edge getEdgeRight(Celula[][] map, int i, int j) {
			Vertex a = getVertex(map, i, j);
			Vertex b = getVertex(map, i, j + 1);
			if (b == null || map[i][j + 1].isObstaculo()) {
				return null;
			}
			return new Edge(getEdgeId(a, b), a, b, 1);
		}

		private Edge getEdgeUp(Celula[][] map, int i, int j) {
			Vertex a = getVertex(map, i, j);
			Vertex b = getVertex(map, i - 1, j);
			if (b == null || map[i - 1][j].isObstaculo()) {
				return null;
			}
			return new Edge(getEdgeId(a, b), a, b, 1);
		}

		private Vertex getVertex(Celula[][] map, int i, int j) {
			try {
				Celula check = map[i][j];
				return new Vertex(getVertexId(i, j), check);
			} catch (ArrayIndexOutOfBoundsException badIndex) {
				return null;
			}
		}

		private String getVertexId(int i, int j) {
			return "(" + i + "," + j + ")";
		}

		private void createGraph(Config cfg, Celula[][] map) {
			List<Vertex> vertexes = new ArrayList<>();
			List<Edge> edges = new ArrayList<>();
			for (int i = 0, rows = map.length; i < rows; i++) {
				for (int j = 0, cols = map[i].length; j < cols; j++) {
					Vertex v = getVertex(map, i, j);
					vertexes.add(v);
					Edge e = null;
					if ((e = getEdgeLeft(map, i, j)) != null) {
						edges.add(e);
					}
					if ((e = getEdgeRight(map, i, j)) != null) {
						edges.add(e);
					}
					if ((e = getEdgeUp(map, i, j)) != null) {
						edges.add(e);
					}
					if ((e = getEdgeDown(map, i, j)) != null) {
						edges.add(e);
					}
					if (map[i][j].isInicio()) {
						cfg.source = v;
					}
					if (map[i][j].isDestino()) {
						cfg.target = v;
					}
				}
			}
			cfg.graph = new Graph(vertexes, edges);
		}
	}

	class CellListener extends MouseAdapter {

		final int coluna;
		final int linha;

		public CellListener(int linha, int coluna) {
			this.linha = linha;
			this.coluna = coluna;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			showPopUp(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (!showPopUp(e)) {
				selecionou(linha, coluna, (Celula) e.getSource());
			}
		}
	}

	private class Celula extends JLabel {

		private static final long serialVersionUID = 1;

		private boolean destino;
		private boolean inicio;
		private boolean obstaculo;

		public Celula(int linha, int coluna) {
			super("(" + linha + ", " + coluna + ")", Celula.CENTER);
			addMouseListener(new CellListener(linha, coluna));
			setOpaque(true);
			setBackground(Color.WHITE);
			setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		}

		public boolean isDestino() {
			return destino;
		}

		public boolean isInicio() {
			return inicio;
		}

		public boolean isObstaculo() {
			return obstaculo;
		}

		public void setDestino(boolean destino) {
			if (isObstaculo() || isInicio()) {
				return;
			}
			this.destino = destino;
			Color color = destino ? Color.RED : Color.WHITE;
			setBackground(color);
		}

		public void setInicio(boolean inicio) {
			if (isObstaculo() || isDestino()) {
				return;
			}
			this.inicio = inicio;
			Color color = inicio ? Color.GREEN : Color.WHITE;
			setBackground(color);
		}

		public void setObstaculo(boolean obstaculo) {
			this.obstaculo = obstaculo;
			Border border;
			Color color;
			if (obstaculo) {
				border = BorderFactory.createBevelBorder(BevelBorder.RAISED);
				color = Color.LIGHT_GRAY;
			} else {
				border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
				color = Color.WHITE;
			}
			setBorder(border);
			setBackground(color);
		}
	}

	private static final long serialVersionUID = 1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VisualDijkstraTest frame = new VisualDijkstraTest();
					frame.novoMapa();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JPanel panel_mapa;
	private JSpinner spinnerColunas;
	private JSpinner spinnerLinhas;
	private JPopupMenu popupMenu;

	private Celula[][] mapa = new Celula[0][0];

	/**
	 * Create the frame.
	 */
	public VisualDijkstraTest() {
		setPreferredSize(new Dimension(640, 480));
		setMinimumSize(new Dimension(640, 480));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				sair();
			}
		});
		setTitle("Teste Dijkstra");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		popupMenu = new JPopupMenu();

		JMenuItem mntmIncio = new JMenuItem("Início");
		mntmIncio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JMenuItem item = (JMenuItem) e.getSource();
				JPopupMenu popup = (JPopupMenu) item.getParent();
				marcarInicio((Celula) popup.getInvoker());
			}
		});
		popupMenu.add(mntmIncio);

		JMenuItem mntmDestino = new JMenuItem("Destino");
		mntmDestino.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JMenuItem item = (JMenuItem) e.getSource();
				JPopupMenu popup = (JPopupMenu) item.getParent();
				marcarDestino((Celula) popup.getInvoker());
			}
		});
		popupMenu.add(mntmDestino);
		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));

		panel_mapa = new JPanel();
		panel_mapa.setBackground(Color.WHITE);
		contentPane.add(panel_mapa, BorderLayout.CENTER);

		JPanel panel_buttons = new JPanel();
		contentPane.add(panel_buttons, BorderLayout.SOUTH);
		panel_buttons.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel lblLinhas = new JLabel("Linhas:");
		panel_buttons.add(lblLinhas);

		spinnerLinhas = new JSpinner();
		spinnerLinhas.setModel(new SpinnerNumberModel(10, 5, 500, 1));
		panel_buttons.add(spinnerLinhas);

		JLabel lblColunas = new JLabel("Colunas:");
		panel_buttons.add(lblColunas);

		spinnerColunas = new JSpinner();
		spinnerColunas.setModel(new SpinnerNumberModel(10, 5, 500, 1));
		panel_buttons.add(spinnerColunas);

		JButton btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				novoMapa();
			}
		});
		panel_buttons.add(btnNovo);

		JButton btnExecutar = new JButton("Executar");
		btnExecutar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executar();
			}
		});
		panel_buttons.add(btnExecutar);
	}

	private void executar() {
		GraphBuilder builder = new GraphBuilder();
		builder.execute();
	}

	private void marcarDestino(Celula celula) {
		int linhas = mapa.length;
		for (int linha = 0; linha < linhas; linha++) {
			int colunas = mapa[linha].length;
			for (int coluna = 0; coluna < colunas; coluna++) {
				mapa[linha][coluna].setDestino(false);
			}
		}
		celula.setDestino(true);
	}

	private void marcarInicio(Celula celula) {
		if (celula.isObstaculo()) {
			return;
		}
		int linhas = mapa.length;
		for (int linha = 0; linha < linhas; linha++) {
			int colunas = mapa[linha].length;
			for (int coluna = 0; coluna < colunas; coluna++) {
				mapa[linha][coluna].setInicio(false);
			}
		}
		celula.setInicio(true);
	}

	private void novoMapa() {
		int linhas = (int) spinnerLinhas.getValue();
		int colunas = (int) spinnerColunas.getValue();
		mapa = new Celula[linhas][colunas];
		panel_mapa.setVisible(false);
		panel_mapa.removeAll();
		panel_mapa.setLayout(new GridLayout(linhas, colunas));
		for (int linha = 0; linha < linhas; linha++) {
			for (int coluna = 0; coluna < colunas; coluna++) {
				Celula celula = new Celula(linha, coluna);
				mapa[linha][coluna] = celula;
				panel_mapa.add(celula);
			}
		}
		panel_mapa.setVisible(true);
	}

	private void sair() {
		int option = JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Sair?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (option == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	private void selecionou(int linha, int coluna, Celula celula) {
		celula.setObstaculo(!celula.isObstaculo());
	}

	private boolean showPopUp(MouseEvent e) {
		if (e.isPopupTrigger()) {
			popupMenu.show((Celula) e.getSource(), e.getX(), e.getY());
			return true;
		}
		return false;
	}
}
