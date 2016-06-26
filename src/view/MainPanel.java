package view;

import controller.Fachada;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.*;
import javax.swing.*;
import static javax.swing.TransferHandler.MOVE;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import model.Grupo;
import model.Sintagma;

public final class MainPanel extends JPanel {

    private Fachada fachada = Fachada.getInstance();
    private ArrayList<Sintagma> listaSintagma, listaOriginal;
    private ArrayList<String> sentencas;
    private ArrayList<Color> colors;
    private ArrayList<JList> jlistas;
    private String barra, texto;
    private Container cont;
    private JSplitPane splitGroupPane, splitPane, upSplitPane, splitAllPane;
    private JPanel rightGroupPanel, leftPanel, upPanel;
    private JScrollPane leftGroupPanel;
    private JButton botao;
    private JTextPane tP;
    private JMenuBar jMenuBar1;
    private JMenu jMenu1, jMenu2, jMenu3;
    private JMenuItem jMenuImportar, jMenuExportar, jMenuItem3;
    private TransferHandler h;
    private Input entradaDados;
    public static int maiorSet;
    private static String path;
    public static MainPanel m;
    private Class leitor;

    public static void setPath(String path) {
        MainPanel.path = path;
    }

    private MainPanel() throws ClassNotFoundException, NoSuchMethodException {
        super(new BorderLayout());
        entradaDados = new Input();

        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuImportar = new javax.swing.JMenuItem();
        jMenuExportar = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1.setText("Arquivo");
        jMenu3.setText("Configurações");
        jMenu2.setText("Ajuda");
        jMenuItem3.setText("Entrada de Dados");
        jMenuImportar.setText("Importar texto");
        jMenuExportar.setText("Exportar alterações");
        jMenu1.add(jMenuImportar);
        jMenu3.add(jMenuItem3);
        jMenu1.add(jMenuExportar);
        jMenuBar1.add(jMenu1);
        jMenuBar1.add(jMenu3);
        jMenuBar1.add(jMenu2);
        tP = new JTextPane();
        tP.setEditable(false);

        barra = "/";
        jlistas = new ArrayList<>();
        colors = new ArrayList<>();
        gerarCores(10);

        leitor = Class.forName("Acesso");
        Method getText = leitor.getMethod("getText", new Class[]{String.class});
        Method getSintagmas = leitor.getMethod("getSintagmas", new Class[]{String.class});
        Method lerSentencas = leitor.getMethod("lerSentencas", new Class[]{String.class});

        upPanel = createHorizontalBoxPanel(100, 100);
        upPanel.add(jMenuBar1);
        leftPanel = createVerticalBoxPanel(this.getPreferredSize());
        leftPanel.add(createPanelForComponent(new JScrollPane(tP), ""));
        leftGroupPanel = createVerticalScrollBoxPanel(this.getPreferredSize());
        rightGroupPanel = createVerticalBoxPanel(this.getPreferredSize());

        JPanel btPanel = createHorizontalBoxPanel(100, 100);
        botao = new JButton("Novo Grupo");
        btPanel.add(botao);
        botao.setEnabled(false);

        botao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBox();
            }

            private void addBox() {

                Component component = cont.getComponent(0);
                JPanel jp = (JPanel) component;
                Component component2 = jp.getComponent(0);
                Component component3 = ((JScrollPane) component2).getComponent(0);
                Component component4 = ((JViewport) component3).getComponent(0);
                JList jl = (JList) component4;
                ArrayList<Sintagma> lista = new ArrayList<>();
                JList jListSintagma = makeList(h, lista);
                jlistas.add(jListSintagma);
                JScrollPane jsp = new JScrollPane(jListSintagma);
                cont.add(createPanelForComponent(jsp, ""), 0);
                //TODO
                jListSintagma.setSelectionModel(new DefaultListSelectionModel() {
                    @Override
                    public void setSelectionInterval(int start, int end) {
                        if (start != end) {
                            super.setSelectionInterval(start, end);
                        } else if (isSelectedIndex(start)) {
                            removeSelectionInterval(start, end);
                            highlightSelecionados();
                        } else {
                            addSelectionInterval(start, end);
                            highlightSelecionados();
                        }
                    }
                });
                //}
                splitAllPane.revalidate();
                splitAllPane.repaint();
            }
        });

        h = new ListItemTransferHandler();
        cont = new Container();
        cont.setLayout(new BoxLayout(cont, BoxLayout.PAGE_AXIS));

        leftGroupPanel.setViewportView(cont);

        this.add(leftPanel);
        this.add(leftGroupPanel);
        this.add(rightGroupPanel);

        splitGroupPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftGroupPanel, rightGroupPanel);
        splitGroupPane.setResizeWeight(0.5);
        this.add(splitGroupPane);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, splitGroupPane);

        upSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, upPanel, btPanel);
        upSplitPane.setResizeWeight(0.527);

        splitAllPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upSplitPane, splitPane);

        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.55);
        splitAllPane.setResizeWeight(0.01);
        this.add(splitAllPane, BorderLayout.CENTER);

        jMenuImportar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuImportarActionPerformed(evt);
            }

            private void jMenuImportarActionPerformed(ActionEvent evt) {
                if ("".equals(path) || path == null) {
                    JOptionPane.showMessageDialog(null, "Voce não configurou nenhum diretorio.");
                } else {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new File(path + barra + "texts"));
                    chooser.showOpenDialog(null);
                    File f = chooser.getSelectedFile();
                    File fSent = new File(path + barra + "sentences" + barra + f.getName() + ".dat");
                    File fBin = new File(path + barra + "binaries" + barra + f.getName() + ".dat");
                    if (!fSent.exists() || !fBin.exists() || !f.exists()) {
                        JOptionPane.showMessageDialog(null, "O arquivo " + f.getName() + ".dat não existe!");
                        return;
                    }

                    try {
                        String arquivoSentenca = path + barra + "sentences" + barra + f.getName() + ".dat";
                        String arquivoTexto = path + barra + "texts" + barra + f.getName();
                        String arquivoBinario = path + barra + "binaries" + barra + f.getName() + ".dat";

                        botao.setEnabled(true);

                        String textoLido = (String) getText.invoke(leitor.newInstance(), arquivoTexto);
                        sentencas = (ArrayList<String>) lerSentencas.invoke(leitor.newInstance(), arquivoSentenca);
                        listaSintagma = (ArrayList<Sintagma>) getSintagmas.invoke(leitor.newInstance(), arquivoBinario);
                        listaOriginal = new ArrayList<>();

                        setTexto(textoLido);
                        fachada.getGrupos().clear();
                        fachada.getGrupoSolitario().getListaSintagmas().clear();
                        maiorSet = -1;

//                        for (String s : sentencas) {
//                            if(s.length() > 5 ){
//                                if(s.charAt(s.length()-1) == '.'){
//                                    s = s.substring(0, s.length()-2);
//                                }
//                                if(s.charAt(s.length()-1) == ' '){
//                                    s = s.substring(0, s.length()-2);
//                                }
//                            }
//                        }
                        for (Sintagma s : listaSintagma) {
                            if (s.set > maiorSet) {
                                maiorSet = s.set;
                            }
                            s.sn = fachada.trataString(s.sn);
                            if (s.sn.startsWith(" ")) {
                                s.sn = s.sn.substring(1);
                            }
                            if (s.sn.endsWith(" ")) {
                                s.sn = s.sn.substring(0, s.sn.length() - 1);
                            }
                            fachada.addSintagmaNoGrupo(s);
                        }
                        fachada.organizaGrupos();
                        fachada.ordenaPorQtdFilhos();

                        for (Sintagma s : listaSintagma) {
                            listaOriginal.add(new Sintagma(s.textName, s.sn, s.sentenca, s.words, s.set, s.snID, s.nucleo, s.lemma, s.prop, s.genero, s.numero, s.nucleoPronome, s.groupedBy, s.shallow, s.paiDe, s.filhoDe));
                        }

                        cont.removeAll();
                        rightGroupPanel.removeAll();
                        JList jListSnSolitarios = makeList(h, fachada.getGrupoSolitario().getListaSintagmas());
                        rightGroupPanel.add(createPanelForComponent(new JScrollPane(jListSnSolitarios), ""));
                        jListSnSolitarios.setSelectionModel(new DefaultListSelectionModel() {
                            @Override
                            public void setSelectionInterval(int start, int end) {
                                if (start != end) {
                                    super.setSelectionInterval(start, end);
                                } else if (isSelectedIndex(start)) {
                                    removeSelectionInterval(start, end);
                                    highlightSelecionados();
                                } else {
                                    addSelectionInterval(start, end);
                                    highlightSelecionados();
                                }
                            }
                        });
                        for (Grupo g : fachada.getGrupos()) {
                            JList jListSintagma = makeList(h, g.getListaSintagmas());
                            jlistas.add(jListSintagma);
                            JScrollPane jsp = new JScrollPane(jListSintagma);
                            cont.add(createPanelForComponent(jsp, ""));
                            jListSintagma.setSelectionModel(new DefaultListSelectionModel() {
                                @Override
                                public void setSelectionInterval(int start, int end) {
                                    if (start != end) {
                                        super.setSelectionInterval(start, end);
                                    } else if (isSelectedIndex(start)) {
                                        removeSelectionInterval(start, end);
                                        highlightSelecionados();
                                    } else {
                                        addSelectionInterval(start, end);
                                        highlightSelecionados();
                                    }
                                }
                            });

                        }

                        jlistas.add(jListSnSolitarios);

                        cont.repaint();

                        for (JList jl : jlistas) {
                            String ACTION_KEY = "theAction";
                            Action actionListener = new AbstractAction() {
                                @Override
                                public void actionPerformed(ActionEvent actionEvent) {
                                    for (JList jlista : jlistas) {
                                        jlista.clearSelection();
                                        highlightSelecionados();
                                    }
                                }
                            };
                            KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
                            InputMap inputMap = jl.getInputMap();
                            inputMap.put(enter, ACTION_KEY);
                            ActionMap actionMap = jl.getActionMap();
                            actionMap.put(ACTION_KEY, actionListener);
                            jl.setActionMap(actionMap);
                        }

                        Component[] component = cont.getComponents();

                        for (int i = 0; i < component.length; i++) {
                            if (component[i] instanceof JPanel) {
                                JPanel jp = (JPanel) component[i];
                                jp.setForeground(colors.get(i));
                                jp.setBackground(colors.get(i));
                            }
                        }

                        Component[] componentSolitarios = rightGroupPanel.getComponents();

                        for (Component componentSolitario : componentSolitarios) {
                            if (componentSolitario instanceof JPanel) {
                                JPanel jp = (JPanel) componentSolitario;
                                jp.setForeground(colors.get(colors.size() - 1));
                                jp.setBackground(colors.get(colors.size() - 1));
                            }
                        }

                    } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {
                        Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                splitAllPane.revalidate();
                splitAllPane.repaint();
            }

        });

        jMenuExportar.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuExportarActionPerformed(evt);
            }

            private void jMenuExportarActionPerformed(ActionEvent evt) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File(path + barra + "binaries" + barra));
                chooser.showSaveDialog(null);
                File f = chooser.getSelectedFile();

                listaSintagma.clear();
                HashMap<Integer, ArrayList<Sintagma>> lista = new HashMap<>();
                Component[] component = cont.getComponents();

                for (Component component1 : component) {
                    JPanel jp = (JPanel) component1;
                    Component[] component2 = jp.getComponents();
                    Component[] component3 = ((JScrollPane) component2[0]).getComponents();
                    Component[] component4 = ((JViewport) component3[0]).getComponents();
                    JList jl = (JList) component4[0];
                    for (int j = 0; j < jl.getModel().getSize(); j++) {
                        listaSintagma.add((Sintagma) jl.getModel().getElementAt(j));
                    }
                }

                Component[] componentSolitarios = rightGroupPanel.getComponents();
                Component[] component2 = ((JPanel) componentSolitarios[0]).getComponents();
                Component[] component3 = ((JScrollPane) component2[0]).getComponents();
                Component[] component4 = ((JViewport) component3[0]).getComponents();
                JList jl = (JList) component4[0];
                for (int j = 0; j < jl.getModel().getSize(); j++) {
                    listaSintagma.add((Sintagma) jl.getModel().getElementAt(j));
                }

                Class leitor;
                try {
                    leitor = Class.forName("Acesso");
                    Method salvarSintagmas = leitor.getMethod("salvarSintagmas", new Class[]{ArrayList.class, File.class});
                    File file;
                    if (f.getName().contains(".dat")) {
                        file = new File(f.getPath());
                    } else {
                        file = new File(f.getPath() + ".dat");
                    }

                    salvarSintagmas.invoke(leitor.newInstance(), listaSintagma, file);
                    try (FileWriter arq = new FileWriter(path + barra + "logs" + barra + f.getName().substring(0, f.getName().indexOf(".")) + "-original.txt")) {
                        PrintWriter gravarArq = new PrintWriter(arq);
                        String log = fachada.imprimeCorref(listaOriginal);
                        gravarArq.printf(log);
                    }
                    try (FileWriter arq = new FileWriter(path + barra + "logs" + barra + f.getName().substring(0, f.getName().indexOf(".")) + "-modificado.txt")) {
                        PrintWriter gravarArq = new PrintWriter(arq);
                        String log = fachada.imprimeCorref(listaSintagma);
                        gravarArq.printf(log);
                    }
                    try (FileWriter arq = new FileWriter(path + barra + "logs" + barra + f.getName().substring(0, f.getName().indexOf(".")) + ".csv")) {
                        PrintWriter gravarArq = new PrintWriter(arq);
                        String log = fachada.getLog(listaOriginal, listaSintagma);
                        gravarArq.printf(log);
                    }

                } catch (InstantiationException | IOException | InvocationTargetException | ClassNotFoundException | IllegalArgumentException | IllegalAccessException | SecurityException | NoSuchMethodException ex) {
                    Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }

            private void jMenuItem3ActionPerformed(ActionEvent evt) {
                entradaDados.setVisible(true);
            }
        });

    }

    public void highlightSelecionados() {
        HashMap<ArrayList<Sintagma>, Color> lista = new HashMap<>();
        Component[] component = cont.getComponents();

        for (int i = 0; i < component.length; i++) {
            JPanel jp = (JPanel) component[i];
            Component[] component2 = jp.getComponents();
            Component[] component3 = ((JScrollPane) component2[0]).getComponents();
            Component[] component4 = ((JViewport) component3[0]).getComponents();
            JList jl = (JList) component4[0];
            lista.put(new ArrayList<>(jl.getSelectedValuesList()), colors.get(i));
            jp.setForeground(colors.get(i));
            jp.setBackground(colors.get(i));
        }

        Component[] componentSolitarios = rightGroupPanel.getComponents();
        Component[] component2 = ((JPanel) componentSolitarios[0]).getComponents();
        Component[] component3 = ((JScrollPane) component2[0]).getComponents();
        Component[] component4 = ((JViewport) component3[0]).getComponents();
        JList jl = (JList) component4[0];
        lista.put(new ArrayList<>(jl.getSelectedValuesList()), colors.get(colors.size() - 1));

        int i = 0;
        setTexto(texto);
        for (ArrayList<Sintagma> lst : lista.keySet()) {
            Color cor = lista.get(lst);
            for (Sintagma s : lst) {
                highlightSintagma(s, cor);
            }
        }
    }

    public void setTexto(String texto) {
        this.texto = texto;
        
        tP.setText(texto);
        tP.setEditable(false);
        tP.setBackground(Color.WHITE);
        tP.setBorder(null);
        

        StyledDocument doc = tP.getStyledDocument();
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, Color.BLACK);
        StyleConstants.setFontSize(keyWord, 15);
//        StyleConstants.setLineSpacing(keyWord, 1f);
        doc.setCharacterAttributes(0, texto.length(), keyWord, false);

        tP.setVisible(true);
    }

    protected JScrollPane createVerticalScrollBoxPanel(Dimension d) {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setSize(d);
        return scrollPane;
    }

    protected JPanel createVerticalBoxPanel(Dimension d) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
        p.setSize(d);
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        return p;
    }

    private JPanel createHorizontalBoxPanel(int w, int h) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setSize(w, h);
        return p;
    }

    public JPanel createPanelForComponent(JComponent comp, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(comp, BorderLayout.CENTER);
        if (title != null) {
            panel.setBorder(BorderFactory.createTitledBorder(title));
        }
        return panel;
    }

    private static JList<Sintagma> makeList(TransferHandler handler, ArrayList<Sintagma> lista) {
        DefaultListModel<Sintagma> listModel = new DefaultListModel<>();
        for (Sintagma s : lista) {
            listModel.addElement(s);
        }

        JList<Sintagma> list = new JList<>(listModel);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, ((Sintagma) value).sn, index, isSelected, cellHasFocus);
                return c;
            }
        });
        list.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setDropMode(DropMode.INSERT);
        list.setDragEnabled(true);
        list.setTransferHandler(handler);

        ActionMap map = list.getActionMap();
        AbstractAction dummy = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };
        map.put(TransferHandler.getCutAction().getValue(Action.NAME), dummy);
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME), dummy);
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME), dummy);

        return list;
    }

    public static void main(String... args) {
        EventQueue.invokeLater(() -> {
            try {
                createAndShowGUI();

            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {
                Logger.getLogger(MainPanel.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public static void createAndShowGUI() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
        JFrame frame = new JFrame("CorrefVisual");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        m = new MainPanel();
        frame.getContentPane().add(m);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public void highlightSintagma(Sintagma s, Color c) {
        onHighlightSintagmaAux(s, c);
    }

    public void onHighlightSintagmaAux(Sintagma s, Color c) {
        String sentenca = sentencas.get(s.sentenca);
        if (sentenca.length() > 5) {
            sentenca = sentenca.substring(1, sentenca.length() - 2);
        }

        int firstIndex = texto.indexOf(sentenca) - 2;
        if (firstIndex < 0) {
            //TODO
        } else {
            int lastIndex = firstIndex + sentenca.length() + 2;
            onHighlightSintagma(s.sn, firstIndex, lastIndex, c);
        }
    }

    private boolean onHighlightSintagma(String sintagma, int firstIndex, int lastIndex, Color c) {
        if (firstIndex < 0) {
            return false;
        } else {
            int index = texto.indexOf(sintagma, firstIndex);
            if (index < 0) {
                return false;
            }
            if (index > lastIndex) {
                return false;
            }

            int startIndex = index;
            int endIndex = startIndex + sintagma.length();

            StyledDocument doc = tP.getStyledDocument();
            SimpleAttributeSet keyWord = new SimpleAttributeSet();
            StyleConstants.setForeground(keyWord, c);
            StyleConstants.setBold(keyWord, true);
            StyleConstants.setFontSize(keyWord, 14);
            doc.setCharacterAttributes(startIndex, sintagma.length(), keyWord, false);
            tP.setVisible(true);

            return onHighlightSintagma(sintagma, startIndex + 1, lastIndex, c);
        }
    }

    private void gerarCores(int n) {
        for (int i = 0; i < n; i++) {
            colors.add(Color.red);
            colors.add(new Color(0, 0, 102));
            colors.add(new Color(0, 153, 0));
            colors.add(new Color(204, 102, 0));
            colors.add(new Color(153, 153, 0));
            colors.add(new Color(153, 0, 153));
            colors.add(new Color(102, 0, 51));
            colors.add(new Color(102, 102, 255));
            colors.add(new Color(204, 102, 0));
            colors.add(new Color(255, 102, 178));
            colors.add(new Color(0, 102, 178));
            colors.add(new Color(0, 0, 102));
            colors.add(new Color(0, 153, 0));
            colors.add(new Color(204, 102, 0));
            colors.add(new Color(153, 153, 0));
            colors.add(new Color(153, 0, 153));
            colors.add(new Color(102, 0, 51));
            colors.add(new Color(102, 102, 255));
            colors.add(new Color(204, 102, 0));
            colors.add(new Color(255, 102, 178));
            colors.add(new Color(0, 102, 178));

        }
    }
}

class ListItemTransferHandler extends TransferHandler {

    private final DataFlavor localObjectFlavor;
    private JList source;
    private int[] indices;
    private int addIndex = -1;
    private int addCount;

    public ListItemTransferHandler() {
        super();
        localObjectFlavor = new ActivationDataFlavor(Object[].class, DataFlavor.javaJVMLocalObjectMimeType, "Array of items");
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        source = (JList) c;
        indices = source.getSelectedIndices();
        Object[] transferedObjects = source.getSelectedValues();
        return new DataHandler(transferedObjects, localObjectFlavor.getMimeType());
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport info) {
        return info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
    }

    @Override
    public int getSourceActions(JComponent c) {
        return MOVE;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
        if (!canImport(info)) {
            return false;
        }
        TransferHandler.DropLocation tdl = info.getDropLocation();
        if (!(tdl instanceof JList.DropLocation)) {
            return false;
        }
        JList.DropLocation dl = (JList.DropLocation) tdl;
        JList target = (JList) info.getComponent();
        DefaultListModel listModel = (DefaultListModel) target.getModel();
        int index = dl.getIndex();
        int max = listModel.getSize();
        if (index < 0 || index > max) {
            index = max;
        }
        addIndex = index;

        try {
            Object[] values = (Object[]) info.getTransferable().getTransferData(localObjectFlavor);
            for (Object value : values) {
                int idx = index++;
                listModel.add(idx, value);
                target.addSelectionInterval(idx, idx);
                if (listModel.size() <= 1) {
                    ((Sintagma) value).set = MainPanel.maiorSet++;
                } else {
                    ((Sintagma) value).set = ((Sintagma) listModel.get(0)).set;
                }
            }
            addCount = target.equals(source) ? values.length : 0;
            return true;
        } catch (UnsupportedFlavorException | IOException ex) {
        }
        return false;
    }

    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        cleanup(c, action == MOVE);
    }

    private void cleanup(JComponent c, boolean remove) {
        if (remove && indices != null) {
            if (addCount > 0) {
                for (int i = 0; i < indices.length; i++) {
                    if (indices[i] >= addIndex) {
                        indices[i] += addCount;
                    }
                }
            }
            JList src = (JList) c;
            DefaultListModel model = (DefaultListModel) src.getModel();
            for (int i = indices.length - 1; i >= 0; i--) {
                model.remove(indices[i]);
            }
        }
        indices = null;
        addCount = 0;
        addIndex = -1;
        MainPanel.m.highlightSelecionados();
    }
}
