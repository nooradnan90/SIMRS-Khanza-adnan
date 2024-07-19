/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package rekammedis;

import fungsi.WarnaTable;
import fungsi.akses;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import kepegawaian.DlgCariPetugas;


/**
 *
 * @author perpustakaan
 */
public final class RMSkriningRisikoKankerPayudara extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i=0;    
    private DlgCariPetugas petugas=new DlgCariPetugas(null,false);
    private String finger="";
    private StringBuilder htmlContent;
    private String TANGGALMUNDUR="yes";
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public RMSkriningRisikoKankerPayudara(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(628,674);

        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.RM","Nama Pasien","Tgl.Lahir","Umur","Kode Petugas","Nama Petugas","Tanggal",
            "Pertanyaan Awal 1","N.P.A.1","Pertanyaan Awal 2","N.P.A.2","Pertanyaan Lanjutan 1","N.P.L.1",
            "Pertanyaan Lanjutan 2","N.P.L.2","Pertanyaan Lanjutan 3","N.P.L.3","Pertanyaan Lanjutan 4","N.P.L.4",
            "Pertanyaan Lanjutan 5","N.P.L.5","Pertanyaan Lanjutan 6","N.P.L.6","Total Skor","Hasil Skrining"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 26; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(105);
            }else if(i==1){
                column.setPreferredWidth(70);
            }else if(i==2){
                column.setPreferredWidth(150);
            }else if(i==3){
                column.setPreferredWidth(65);
            }else if(i==4){
                column.setPreferredWidth(40);
            }else if(i==5){
                column.setPreferredWidth(90);
            }else if(i==6){
                column.setPreferredWidth(150);
            }else if(i==7){
                column.setPreferredWidth(115);
            }else if(i==8){
                column.setPreferredWidth(118);
            }else if(i==9){
                column.setPreferredWidth(45);
            }else if(i==10){
                column.setPreferredWidth(118);
            }else if(i==11){
                column.setPreferredWidth(45);
            }else if(i==12){
                column.setPreferredWidth(118);
            }else if(i==13){
                column.setPreferredWidth(45);
            }else if(i==14){
                column.setPreferredWidth(118);
            }else if(i==15){
                column.setPreferredWidth(45);
            }else if(i==16){
                column.setPreferredWidth(118);
            }else if(i==17){
                column.setPreferredWidth(45);
            }else if(i==18){
                column.setPreferredWidth(118);
            }else if(i==19){
                column.setPreferredWidth(45);
            }else if(i==20){
                column.setPreferredWidth(118);
            }else if(i==21){
                column.setPreferredWidth(45);
            }else if(i==22){
                column.setPreferredWidth(118);
            }else if(i==23){
                column.setPreferredWidth(45);
            }else if(i==24){
                column.setPreferredWidth(60);
            }else if(i==25){
                column.setPreferredWidth(230);
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        KdPetugas.setDocument(new batasInput((byte)20).getKata(KdPetugas));
        TCari.setDocument(new batasInput((int)100).getKata(TCari));
        
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
            });
        }
        
        petugas.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(petugas.getTable().getSelectedRow()!= -1){                   
                    KdPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                    NmPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                }  
                KdPetugas.requestFocus();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        }); 
        
        ChkInput.setSelected(false);
        isForm();
        
        HTMLEditorKit kit = new HTMLEditorKit();
        LoadHTML.setEditable(true);
        LoadHTML.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(
                ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi2 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#323232;}"+
                ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi5 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#AA0000;}"+
                ".isi6 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#FF0000;}"+
                ".isi7 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#C8C800;}"+
                ".isi8 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#00AA00;}"+
                ".isi9 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#969696;}"
        );
        Document doc = kit.createDefaultDocument();
        LoadHTML.setDocument(doc);
        
        try {
            TANGGALMUNDUR=koneksiDB.TANGGALMUNDUR();
        } catch (Exception e) {
            TANGGALMUNDUR="yes";
        }
        
        jam();
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnSkriningKekerasanPadaPerempuan = new javax.swing.JMenuItem();
        buttonGroup1 = new javax.swing.ButtonGroup();
        LoadHTML = new widget.editorpane();
        Umur = new widget.TextBox();
        TanggalRegistrasi = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        scrollInput = new widget.ScrollPane();
        FormInput = new widget.PanelBiasa();
        jLabel4 = new widget.Label();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        Tanggal = new widget.Tanggal();
        TNoRM = new widget.TextBox();
        jLabel16 = new widget.Label();
        Jam = new widget.ComboBox();
        Menit = new widget.ComboBox();
        Detik = new widget.ComboBox();
        ChkKejadian = new widget.CekBox();
        jLabel18 = new widget.Label();
        KdPetugas = new widget.TextBox();
        NmPetugas = new widget.TextBox();
        btnPetugas = new widget.Button();
        jLabel8 = new widget.Label();
        TglLahir = new widget.TextBox();
        PertanyaanAwal1 = new widget.ComboBox();
        jLabel92 = new widget.Label();
        PertanyaanAwal2 = new widget.ComboBox();
        jLabel69 = new widget.Label();
        jLabel73 = new widget.Label();
        TotalHasil = new widget.TextBox();
        NilaiPertanyaanAwal1 = new widget.TextBox();
        NilaiPertanyaanAwal2 = new widget.TextBox();
        PertanyaanLanjutan1 = new widget.ComboBox();
        jLabel70 = new widget.Label();
        NilaiPertanyaanLanjutan1 = new widget.TextBox();
        jLabel71 = new widget.Label();
        NilaiPertanyaanLanjutan2 = new widget.TextBox();
        PertanyaanLanjutan2 = new widget.ComboBox();
        jLabel148 = new widget.Label();
        HasilSkrining = new widget.TextBox();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel99 = new widget.Label();
        jLabel75 = new widget.Label();
        jLabel76 = new widget.Label();
        jLabel77 = new widget.Label();
        jLabel78 = new widget.Label();
        jLabel79 = new widget.Label();
        jLabel80 = new widget.Label();
        jLabel81 = new widget.Label();
        jLabel82 = new widget.Label();
        jLabel83 = new widget.Label();
        jLabel84 = new widget.Label();
        PertanyaanLanjutan3 = new widget.ComboBox();
        jLabel72 = new widget.Label();
        NilaiPertanyaanLanjutan3 = new widget.TextBox();
        jLabel85 = new widget.Label();
        jLabel86 = new widget.Label();
        PertanyaanLanjutan4 = new widget.ComboBox();
        jLabel74 = new widget.Label();
        NilaiPertanyaanLanjutan4 = new widget.TextBox();
        jLabel87 = new widget.Label();
        jLabel88 = new widget.Label();
        PertanyaanLanjutan5 = new widget.ComboBox();
        jLabel89 = new widget.Label();
        NilaiPertanyaanLanjutan5 = new widget.TextBox();
        jLabel90 = new widget.Label();
        PertanyaanLanjutan6 = new widget.ComboBox();
        jLabel91 = new widget.Label();
        NilaiPertanyaanLanjutan6 = new widget.TextBox();
        jLabel93 = new widget.Label();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel101 = new widget.Label();
        jLabel149 = new widget.Label();
        jLabel94 = new widget.Label();
        jLabel95 = new widget.Label();
        jLabel96 = new widget.Label();
        PertanyaanAwal3 = new widget.ComboBox();
        jLabel97 = new widget.Label();
        NilaiPertanyaanAwal3 = new widget.TextBox();
        jLabel98 = new widget.Label();
        jLabel102 = new widget.Label();
        PertanyaanAwal4 = new widget.ComboBox();
        jLabel103 = new widget.Label();
        NilaiPertanyaanAwal4 = new widget.TextBox();
        jLabel104 = new widget.Label();
        jLabel105 = new widget.Label();
        PertanyaanAwal5 = new widget.ComboBox();
        jLabel106 = new widget.Label();
        NilaiPertanyaanAwal5 = new widget.TextBox();
        jLabel107 = new widget.Label();
        jLabel108 = new widget.Label();
        PertanyaanAwal6 = new widget.ComboBox();
        jLabel109 = new widget.Label();
        NilaiPertanyaanAwal6 = new widget.TextBox();
        jLabel110 = new widget.Label();
        jLabel111 = new widget.Label();
        PertanyaanAwal7 = new widget.ComboBox();
        jLabel112 = new widget.Label();
        NilaiPertanyaanAwal7 = new widget.TextBox();
        jLabel113 = new widget.Label();
        jLabel114 = new widget.Label();
        jLabel115 = new widget.Label();
        PertanyaanAwal8 = new widget.ComboBox();
        jLabel116 = new widget.Label();
        NilaiPertanyaanAwal8 = new widget.TextBox();
        jLabel117 = new widget.Label();
        jLabel118 = new widget.Label();
        PertanyaanAwal9 = new widget.ComboBox();
        jLabel119 = new widget.Label();
        NilaiPertanyaanAwal9 = new widget.TextBox();
        NilaiPertanyaanAwal10 = new widget.TextBox();
        jLabel120 = new widget.Label();
        PertanyaanAwal10 = new widget.ComboBox();
        jLabel121 = new widget.Label();
        jLabel122 = new widget.Label();
        jLabel123 = new widget.Label();
        jLabel124 = new widget.Label();
        PertanyaanAwal11 = new widget.ComboBox();
        jLabel125 = new widget.Label();
        NilaiPertanyaanAwal11 = new widget.TextBox();
        jLabel126 = new widget.Label();
        jLabel127 = new widget.Label();
        PertanyaanAwal12 = new widget.ComboBox();
        jLabel128 = new widget.Label();
        NilaiPertanyaanAwal12 = new widget.TextBox();
        jLabel129 = new widget.Label();
        jLabel130 = new widget.Label();
        PertanyaanAwal13 = new widget.ComboBox();
        jLabel131 = new widget.Label();
        NilaiPertanyaanAwal13 = new widget.TextBox();
        jLabel132 = new widget.Label();
        jLabel133 = new widget.Label();
        PertanyaanAwal14 = new widget.ComboBox();
        jLabel134 = new widget.Label();
        NilaiPertanyaanAwal14 = new widget.TextBox();
        jLabel135 = new widget.Label();
        jLabel100 = new widget.Label();
        jLabel136 = new widget.Label();
        PertanyaanLanjutan7 = new widget.ComboBox();
        jLabel137 = new widget.Label();
        NilaiPertanyaanLanjutan7 = new widget.TextBox();
        jLabel138 = new widget.Label();
        jLabel139 = new widget.Label();
        PertanyaanLanjutan8 = new widget.ComboBox();
        jLabel140 = new widget.Label();
        NilaiPertanyaanLanjutan8 = new widget.TextBox();
        NilaiPertanyaanLanjutan9 = new widget.TextBox();
        jLabel141 = new widget.Label();
        PertanyaanLanjutan9 = new widget.ComboBox();
        jLabel142 = new widget.Label();
        jLabel143 = new widget.Label();
        jLabel144 = new widget.Label();
        jLabel145 = new widget.Label();
        PertanyaanLanjutan10 = new widget.ComboBox();
        jLabel146 = new widget.Label();
        NilaiPertanyaanLanjutan10 = new widget.TextBox();
        jLabel147 = new widget.Label();
        jLabel150 = new widget.Label();
        PertanyaanLanjutan11 = new widget.ComboBox();
        jLabel151 = new widget.Label();
        NilaiPertanyaanLanjutan11 = new widget.TextBox();
        jLabel152 = new widget.Label();
        jLabel153 = new widget.Label();
        PertanyaanLanjutan12 = new widget.ComboBox();
        jLabel154 = new widget.Label();
        NilaiPertanyaanLanjutan12 = new widget.TextBox();
        jLabel155 = new widget.Label();
        jLabel156 = new widget.Label();
        PertanyaanLanjutan13 = new widget.ComboBox();
        jLabel157 = new widget.Label();
        NilaiPertanyaanLanjutan13 = new widget.TextBox();
        jLabel158 = new widget.Label();
        jLabel159 = new widget.Label();
        jLabel160 = new widget.Label();
        PertanyaanLanjutan14 = new widget.ComboBox();
        jLabel161 = new widget.Label();
        NilaiPertanyaanLanjutan14 = new widget.TextBox();
        jLabel162 = new widget.Label();
        jLabel163 = new widget.Label();
        PertanyaanLanjutan15 = new widget.ComboBox();
        jLabel164 = new widget.Label();
        NilaiPertanyaanLanjutan15 = new widget.TextBox();
        jLabel165 = new widget.Label();
        jLabel166 = new widget.Label();
        PertanyaanLanjutan16 = new widget.ComboBox();
        jLabel167 = new widget.Label();
        NilaiPertanyaanLanjutan16 = new widget.TextBox();
        jLabel168 = new widget.Label();
        jLabel169 = new widget.Label();
        jLabel170 = new widget.Label();
        PertanyaanLanjutan17 = new widget.ComboBox();
        jLabel171 = new widget.Label();
        NilaiPertanyaanLanjutan17 = new widget.TextBox();
        jLabel172 = new widget.Label();
        jLabel173 = new widget.Label();
        PertanyaanLanjutan18 = new widget.ComboBox();
        jLabel174 = new widget.Label();
        NilaiPertanyaanLanjutan18 = new widget.TextBox();
        jLabel175 = new widget.Label();
        jLabel176 = new widget.Label();
        PertanyaanLanjutan19 = new widget.ComboBox();
        jLabel177 = new widget.Label();
        NilaiPertanyaanLanjutan19 = new widget.TextBox();
        jLabel178 = new widget.Label();
        jLabel179 = new widget.Label();
        PertanyaanLanjutan20 = new widget.ComboBox();
        jLabel180 = new widget.Label();
        NilaiPertanyaanLanjutan20 = new widget.TextBox();
        jLabel181 = new widget.Label();
        jLabel182 = new widget.Label();
        PertanyaanLanjutan21 = new widget.ComboBox();
        jLabel183 = new widget.Label();
        NilaiPertanyaanLanjutan21 = new widget.TextBox();
        jLabel184 = new widget.Label();
        PertanyaanLanjutan22 = new widget.ComboBox();
        jLabel185 = new widget.Label();
        jLabel186 = new widget.Label();
        PertanyaanLanjutan23 = new widget.ComboBox();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel187 = new widget.Label();
        jLabel188 = new widget.Label();
        PertanyaanLanjutan24 = new widget.ComboBox();
        jLabel189 = new widget.Label();
        jLabel190 = new widget.Label();
        HasilSkrining1 = new widget.TextBox();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnSkriningKekerasanPadaPerempuan.setBackground(new java.awt.Color(255, 255, 254));
        MnSkriningKekerasanPadaPerempuan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnSkriningKekerasanPadaPerempuan.setForeground(new java.awt.Color(50, 50, 50));
        MnSkriningKekerasanPadaPerempuan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnSkriningKekerasanPadaPerempuan.setText("Formulir Skrining Kekerasan Pada Perempuan");
        MnSkriningKekerasanPadaPerempuan.setName("MnSkriningKekerasanPadaPerempuan"); // NOI18N
        MnSkriningKekerasanPadaPerempuan.setPreferredSize(new java.awt.Dimension(280, 26));
        MnSkriningKekerasanPadaPerempuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnSkriningKekerasanPadaPerempuanActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnSkriningKekerasanPadaPerempuan);

        LoadHTML.setBorder(null);
        LoadHTML.setName("LoadHTML"); // NOI18N

        Umur.setEditable(false);
        Umur.setFocusTraversalPolicyProvider(true);
        Umur.setName("Umur"); // NOI18N

        TanggalRegistrasi.setHighlighter(null);
        TanggalRegistrasi.setName("TanggalRegistrasi"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Skrining Risiko Kanker Payudara ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 200));

        tbObat.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat.setComponentPopupMenu(jPopupMenu1);
        tbObat.setName("tbObat"); // NOI18N
        tbObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatMouseClicked(evt);
            }
        });
        tbObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObatKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbObat);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
        BtnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        BtnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnSimpan);

        BtnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnBatal.setMnemonic('B');
        BtnBatal.setText("Baru");
        BtnBatal.setToolTipText("Alt+B");
        BtnBatal.setName("BtnBatal"); // NOI18N
        BtnBatal.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBatalActionPerformed(evt);
            }
        });
        BtnBatal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnBatalKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnBatal);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnHapus);

        BtnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit.setMnemonic('G');
        BtnEdit.setText("Ganti");
        BtnEdit.setToolTipText("Alt+G");
        BtnEdit.setName("BtnEdit"); // NOI18N
        BtnEdit.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditActionPerformed(evt);
            }
        });
        BtnEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEditKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnEdit);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        BtnPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrintKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnPrint);

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass8.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(LCount);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnKeluar);

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setText("Tanggal :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "19-07-2024" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari1);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass9.add(jLabel21);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "19-07-2024" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(310, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('3');
        BtnCari.setToolTipText("Alt+3");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnAll);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 426));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('I');
        ChkInput.setText(".: Input Data");
        ChkInput.setToolTipText("Alt+I");
        ChkInput.setBorderPainted(true);
        ChkInput.setBorderPaintedFlat(true);
        ChkInput.setFocusable(false);
        ChkInput.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput.setName("ChkInput"); // NOI18N
        ChkInput.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInputActionPerformed(evt);
            }
        });
        PanelInput.add(ChkInput, java.awt.BorderLayout.PAGE_END);

        scrollInput.setName("scrollInput"); // NOI18N
        scrollInput.setPreferredSize(new java.awt.Dimension(102, 557));

        FormInput.setBackground(new java.awt.Color(250, 255, 245));
        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 1343));
        FormInput.setLayout(null);

        jLabel4.setText("No.Rawat :");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(0, 10, 75, 23);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(79, 10, 141, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        TPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPasienKeyPressed(evt);
            }
        });
        FormInput.add(TPasien);
        TPasien.setBounds(336, 10, 285, 23);

        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "19-07-2024" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
        Tanggal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalKeyPressed(evt);
            }
        });
        FormInput.add(Tanggal);
        Tanggal.setBounds(79, 40, 90, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        FormInput.add(TNoRM);
        TNoRM.setBounds(222, 10, 112, 23);

        jLabel16.setText("Tanggal :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setVerifyInputWhenFocusTarget(false);
        FormInput.add(jLabel16);
        jLabel16.setBounds(0, 40, 75, 23);

        Jam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        Jam.setName("Jam"); // NOI18N
        Jam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JamKeyPressed(evt);
            }
        });
        FormInput.add(Jam);
        Jam.setBounds(173, 40, 62, 23);

        Menit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Menit.setName("Menit"); // NOI18N
        Menit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MenitKeyPressed(evt);
            }
        });
        FormInput.add(Menit);
        Menit.setBounds(238, 40, 62, 23);

        Detik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Detik.setName("Detik"); // NOI18N
        Detik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DetikKeyPressed(evt);
            }
        });
        FormInput.add(Detik);
        Detik.setBounds(303, 40, 62, 23);

        ChkKejadian.setBorder(null);
        ChkKejadian.setSelected(true);
        ChkKejadian.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkKejadian.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkKejadian.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkKejadian.setName("ChkKejadian"); // NOI18N
        FormInput.add(ChkKejadian);
        ChkKejadian.setBounds(368, 40, 23, 23);

        jLabel18.setText("Petugas :");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(400, 40, 70, 23);

        KdPetugas.setEditable(false);
        KdPetugas.setHighlighter(null);
        KdPetugas.setName("KdPetugas"); // NOI18N
        FormInput.add(KdPetugas);
        KdPetugas.setBounds(474, 40, 94, 23);

        NmPetugas.setEditable(false);
        NmPetugas.setName("NmPetugas"); // NOI18N
        FormInput.add(NmPetugas);
        NmPetugas.setBounds(570, 40, 187, 23);

        btnPetugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPetugas.setMnemonic('2');
        btnPetugas.setToolTipText("ALt+2");
        btnPetugas.setName("btnPetugas"); // NOI18N
        btnPetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPetugasActionPerformed(evt);
            }
        });
        btnPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPetugasKeyPressed(evt);
            }
        });
        FormInput.add(btnPetugas);
        btnPetugas.setBounds(761, 40, 28, 23);

        jLabel8.setText("Tgl.Lahir :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(625, 10, 60, 23);

        TglLahir.setHighlighter(null);
        TglLahir.setName("TglLahir"); // NOI18N
        FormInput.add(TglLahir);
        TglLahir.setBounds(689, 10, 100, 23);

        PertanyaanAwal1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanAwal1.setName("PertanyaanAwal1"); // NOI18N
        PertanyaanAwal1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanAwal1ItemStateChanged(evt);
            }
        });
        PertanyaanAwal1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanAwal1KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanAwal1);
        PertanyaanAwal1.setBounds(620, 110, 80, 23);

        jLabel92.setText("Nilai :");
        jLabel92.setName("jLabel92"); // NOI18N
        FormInput.add(jLabel92);
        jLabel92.setBounds(690, 110, 50, 23);

        PertanyaanAwal2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanAwal2.setName("PertanyaanAwal2"); // NOI18N
        PertanyaanAwal2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanAwal2ItemStateChanged(evt);
            }
        });
        PertanyaanAwal2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanAwal2KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanAwal2);
        PertanyaanAwal2.setBounds(620, 140, 80, 23);

        jLabel69.setText("Nilai :");
        jLabel69.setName("jLabel69"); // NOI18N
        FormInput.add(jLabel69);
        jLabel69.setBounds(690, 140, 50, 23);

        jLabel73.setText("Total Skor :");
        jLabel73.setName("jLabel73"); // NOI18N
        FormInput.add(jLabel73);
        jLabel73.setBounds(670, 1210, 70, 23);

        TotalHasil.setEditable(false);
        TotalHasil.setText("0");
        TotalHasil.setFocusTraversalPolicyProvider(true);
        TotalHasil.setName("TotalHasil"); // NOI18N
        FormInput.add(TotalHasil);
        TotalHasil.setBounds(744, 1210, 45, 23);

        NilaiPertanyaanAwal1.setEditable(false);
        NilaiPertanyaanAwal1.setText("0");
        NilaiPertanyaanAwal1.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanAwal1.setName("NilaiPertanyaanAwal1"); // NOI18N
        FormInput.add(NilaiPertanyaanAwal1);
        NilaiPertanyaanAwal1.setBounds(744, 110, 45, 23);

        NilaiPertanyaanAwal2.setEditable(false);
        NilaiPertanyaanAwal2.setText("0");
        NilaiPertanyaanAwal2.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanAwal2.setName("NilaiPertanyaanAwal2"); // NOI18N
        FormInput.add(NilaiPertanyaanAwal2);
        NilaiPertanyaanAwal2.setBounds(744, 140, 45, 23);

        PertanyaanLanjutan1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan1.setName("PertanyaanLanjutan1"); // NOI18N
        PertanyaanLanjutan1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan1ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan1KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan1);
        PertanyaanLanjutan1.setBounds(620, 550, 80, 23);

        jLabel70.setText("Nilai :");
        jLabel70.setName("jLabel70"); // NOI18N
        FormInput.add(jLabel70);
        jLabel70.setBounds(690, 550, 50, 23);

        NilaiPertanyaanLanjutan1.setEditable(false);
        NilaiPertanyaanLanjutan1.setText("0");
        NilaiPertanyaanLanjutan1.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan1.setName("NilaiPertanyaanLanjutan1"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan1);
        NilaiPertanyaanLanjutan1.setBounds(744, 550, 45, 23);

        jLabel71.setText("Nilai :");
        jLabel71.setName("jLabel71"); // NOI18N
        FormInput.add(jLabel71);
        jLabel71.setBounds(690, 580, 50, 23);

        NilaiPertanyaanLanjutan2.setEditable(false);
        NilaiPertanyaanLanjutan2.setText("0");
        NilaiPertanyaanLanjutan2.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan2.setName("NilaiPertanyaanLanjutan2"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan2);
        NilaiPertanyaanLanjutan2.setBounds(744, 580, 45, 23);

        PertanyaanLanjutan2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan2.setName("PertanyaanLanjutan2"); // NOI18N
        PertanyaanLanjutan2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan2ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan2KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan2);
        PertanyaanLanjutan2.setBounds(620, 580, 80, 23);

        jLabel148.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel148.setText("Rekomendasi");
        jLabel148.setName("jLabel148"); // NOI18N
        FormInput.add(jLabel148);
        jLabel148.setBounds(44, 1210, 90, 23);

        HasilSkrining.setEditable(false);
        HasilSkrining.setFocusTraversalPolicyProvider(true);
        HasilSkrining.setName("HasilSkrining"); // NOI18N
        HasilSkrining.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HasilSkriningKeyPressed(evt);
            }
        });
        FormInput.add(HasilSkrining);
        HasilSkrining.setBounds(120, 1210, 520, 23);

        jSeparator1.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator1.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator1.setName("jSeparator1"); // NOI18N
        FormInput.add(jSeparator1);
        jSeparator1.setBounds(0, 70, 807, 1);

        jLabel99.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel99.setText("I. SKOR ANANTO SIDOHUTOMO");
        jLabel99.setName("jLabel99"); // NOI18N
        FormInput.add(jLabel99);
        jLabel99.setBounds(10, 70, 200, 23);

        jLabel75.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel75.setText("1.");
        jLabel75.setName("jLabel75"); // NOI18N
        FormInput.add(jLabel75);
        jLabel75.setBounds(54, 110, 20, 23);

        jLabel76.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel76.setText("Apakah anda mengalami infeksi yang masuk melalui puting susu, areola, atau kulit payudara ?");
        jLabel76.setName("jLabel76"); // NOI18N
        FormInput.add(jLabel76);
        jLabel76.setBounds(72, 110, 530, 23);

        jLabel77.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel77.setText("2.");
        jLabel77.setName("jLabel77"); // NOI18N
        FormInput.add(jLabel77);
        jLabel77.setBounds(54, 140, 20, 23);

        jLabel78.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel78.setText("Apakah anda tidak memperhatikan kebersihan puting susu, areola, atau kulit payudara ?");
        jLabel78.setName("jLabel78"); // NOI18N
        FormInput.add(jLabel78);
        jLabel78.setBounds(72, 140, 530, 23);

        jLabel79.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel79.setText("Apakah anda tidak mempunyai anak ?");
        jLabel79.setName("jLabel79"); // NOI18N
        FormInput.add(jLabel79);
        jLabel79.setBounds(72, 550, 470, 23);

        jLabel80.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel80.setText("1.");
        jLabel80.setName("jLabel80"); // NOI18N
        FormInput.add(jLabel80);
        jLabel80.setBounds(54, 550, 20, 23);

        jLabel81.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel81.setText("2.");
        jLabel81.setName("jLabel81"); // NOI18N
        FormInput.add(jLabel81);
        jLabel81.setBounds(54, 580, 20, 23);

        jLabel82.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel82.setText("Apakah anda menyusui anak kurang dari 6 bulan ?");
        jLabel82.setName("jLabel82"); // NOI18N
        FormInput.add(jLabel82);
        jLabel82.setBounds(72, 580, 470, 23);

        jLabel83.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel83.setText("3.");
        jLabel83.setName("jLabel83"); // NOI18N
        FormInput.add(jLabel83);
        jLabel83.setBounds(54, 610, 20, 23);

        jLabel84.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel84.setText("Apakah anda memakai hormonal kontrasepsi atau terapi sulih hormon ?");
        jLabel84.setName("jLabel84"); // NOI18N
        FormInput.add(jLabel84);
        jLabel84.setBounds(72, 610, 470, 23);

        PertanyaanLanjutan3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan3.setName("PertanyaanLanjutan3"); // NOI18N
        PertanyaanLanjutan3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan3ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan3KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan3);
        PertanyaanLanjutan3.setBounds(620, 610, 80, 23);

        jLabel72.setText("Nilai :");
        jLabel72.setName("jLabel72"); // NOI18N
        FormInput.add(jLabel72);
        jLabel72.setBounds(690, 610, 50, 23);

        NilaiPertanyaanLanjutan3.setEditable(false);
        NilaiPertanyaanLanjutan3.setText("0");
        NilaiPertanyaanLanjutan3.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan3.setName("NilaiPertanyaanLanjutan3"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan3);
        NilaiPertanyaanLanjutan3.setBounds(744, 610, 45, 23);

        jLabel85.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel85.setText("4.");
        jLabel85.setName("jLabel85"); // NOI18N
        FormInput.add(jLabel85);
        jLabel85.setBounds(54, 640, 20, 23);

        jLabel86.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel86.setText("Apakah anda dalam setahun terkena radiasi sinar-X (rontgen) lebih dari 1 kali ?");
        jLabel86.setName("jLabel86"); // NOI18N
        FormInput.add(jLabel86);
        jLabel86.setBounds(72, 640, 470, 23);

        PertanyaanLanjutan4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan4.setName("PertanyaanLanjutan4"); // NOI18N
        PertanyaanLanjutan4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan4ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan4KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan4);
        PertanyaanLanjutan4.setBounds(620, 640, 80, 23);

        jLabel74.setText("Nilai :");
        jLabel74.setName("jLabel74"); // NOI18N
        FormInput.add(jLabel74);
        jLabel74.setBounds(690, 640, 50, 23);

        NilaiPertanyaanLanjutan4.setEditable(false);
        NilaiPertanyaanLanjutan4.setText("0");
        NilaiPertanyaanLanjutan4.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan4.setName("NilaiPertanyaanLanjutan4"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan4);
        NilaiPertanyaanLanjutan4.setBounds(744, 640, 45, 23);

        jLabel87.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel87.setText("Apakah anda pernah menjalani tindakan pembedahan pada payudara ?");
        jLabel87.setName("jLabel87"); // NOI18N
        FormInput.add(jLabel87);
        jLabel87.setBounds(72, 670, 470, 23);

        jLabel88.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel88.setText("5.");
        jLabel88.setName("jLabel88"); // NOI18N
        FormInput.add(jLabel88);
        jLabel88.setBounds(54, 670, 20, 23);

        PertanyaanLanjutan5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan5.setName("PertanyaanLanjutan5"); // NOI18N
        PertanyaanLanjutan5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan5ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan5KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan5);
        PertanyaanLanjutan5.setBounds(620, 670, 80, 23);

        jLabel89.setText("Nilai :");
        jLabel89.setName("jLabel89"); // NOI18N
        FormInput.add(jLabel89);
        jLabel89.setBounds(690, 670, 50, 23);

        NilaiPertanyaanLanjutan5.setEditable(false);
        NilaiPertanyaanLanjutan5.setText("0");
        NilaiPertanyaanLanjutan5.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan5.setName("NilaiPertanyaanLanjutan5"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan5);
        NilaiPertanyaanLanjutan5.setBounds(744, 670, 45, 23);

        jLabel90.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel90.setText("Apakah anda mendapat trauma payudara akibat aktifitas seksual berlebihan ?");
        jLabel90.setName("jLabel90"); // NOI18N
        FormInput.add(jLabel90);
        jLabel90.setBounds(72, 700, 470, 23);

        PertanyaanLanjutan6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan6.setName("PertanyaanLanjutan6"); // NOI18N
        PertanyaanLanjutan6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan6ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan6KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan6);
        PertanyaanLanjutan6.setBounds(620, 700, 80, 23);

        jLabel91.setText("Nilai :");
        jLabel91.setName("jLabel91"); // NOI18N
        FormInput.add(jLabel91);
        jLabel91.setBounds(690, 700, 50, 23);

        NilaiPertanyaanLanjutan6.setEditable(false);
        NilaiPertanyaanLanjutan6.setText("0");
        NilaiPertanyaanLanjutan6.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan6.setName("NilaiPertanyaanLanjutan6"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan6);
        NilaiPertanyaanLanjutan6.setBounds(744, 700, 45, 23);

        jLabel93.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel93.setText("6.");
        jLabel93.setName("jLabel93"); // NOI18N
        FormInput.add(jLabel93);
        jLabel93.setBounds(54, 700, 20, 23);

        jSeparator3.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator3.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator3.setName("jSeparator3"); // NOI18N
        FormInput.add(jSeparator3);
        jSeparator3.setBounds(0, 1240, 807, 1);

        jLabel101.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel101.setText("II. PEMERIKSAAN SADANIS ");
        jLabel101.setName("jLabel101"); // NOI18N
        FormInput.add(jLabel101);
        jLabel101.setBounds(10, 1240, 200, 23);

        jLabel149.setText(":");
        jLabel149.setName("jLabel149"); // NOI18N
        FormInput.add(jLabel149);
        jLabel149.setBounds(0, 1210, 116, 23);

        jLabel94.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel94.setText("A. Faktor Awal (Ya=1, Tidak=0)");
        jLabel94.setName("jLabel94"); // NOI18N
        FormInput.add(jLabel94);
        jLabel94.setBounds(44, 90, 310, 23);

        jLabel95.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel95.setText("Apakah anda melahirkan anak pertama saat usia lebih dari 25 tahun ?");
        jLabel95.setName("jLabel95"); // NOI18N
        FormInput.add(jLabel95);
        jLabel95.setBounds(72, 170, 530, 23);

        jLabel96.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel96.setText("3.");
        jLabel96.setName("jLabel96"); // NOI18N
        FormInput.add(jLabel96);
        jLabel96.setBounds(54, 170, 20, 23);

        PertanyaanAwal3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanAwal3.setName("PertanyaanAwal3"); // NOI18N
        PertanyaanAwal3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanAwal3ItemStateChanged(evt);
            }
        });
        PertanyaanAwal3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanAwal3KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanAwal3);
        PertanyaanAwal3.setBounds(620, 170, 80, 23);

        jLabel97.setText("Nilai :");
        jLabel97.setName("jLabel97"); // NOI18N
        FormInput.add(jLabel97);
        jLabel97.setBounds(690, 170, 50, 23);

        NilaiPertanyaanAwal3.setEditable(false);
        NilaiPertanyaanAwal3.setText("0");
        NilaiPertanyaanAwal3.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanAwal3.setName("NilaiPertanyaanAwal3"); // NOI18N
        FormInput.add(NilaiPertanyaanAwal3);
        NilaiPertanyaanAwal3.setBounds(744, 170, 45, 23);

        jLabel98.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel98.setText("Apakah anda atau orang di sekitar anda merokok ?");
        jLabel98.setName("jLabel98"); // NOI18N
        FormInput.add(jLabel98);
        jLabel98.setBounds(72, 200, 530, 23);

        jLabel102.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel102.setText("4.");
        jLabel102.setName("jLabel102"); // NOI18N
        FormInput.add(jLabel102);
        jLabel102.setBounds(54, 200, 20, 23);

        PertanyaanAwal4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanAwal4.setName("PertanyaanAwal4"); // NOI18N
        PertanyaanAwal4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanAwal4ItemStateChanged(evt);
            }
        });
        PertanyaanAwal4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanAwal4KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanAwal4);
        PertanyaanAwal4.setBounds(620, 200, 80, 23);

        jLabel103.setText("Nilai :");
        jLabel103.setName("jLabel103"); // NOI18N
        FormInput.add(jLabel103);
        jLabel103.setBounds(690, 200, 50, 23);

        NilaiPertanyaanAwal4.setEditable(false);
        NilaiPertanyaanAwal4.setText("0");
        NilaiPertanyaanAwal4.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanAwal4.setName("NilaiPertanyaanAwal4"); // NOI18N
        FormInput.add(NilaiPertanyaanAwal4);
        NilaiPertanyaanAwal4.setBounds(744, 200, 45, 23);

        jLabel104.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel104.setText("Apakah anda mengkonsumsi alkohol ?");
        jLabel104.setName("jLabel104"); // NOI18N
        FormInput.add(jLabel104);
        jLabel104.setBounds(72, 230, 530, 23);

        jLabel105.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel105.setText("5.");
        jLabel105.setName("jLabel105"); // NOI18N
        FormInput.add(jLabel105);
        jLabel105.setBounds(54, 230, 20, 23);

        PertanyaanAwal5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanAwal5.setName("PertanyaanAwal5"); // NOI18N
        PertanyaanAwal5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanAwal5ItemStateChanged(evt);
            }
        });
        PertanyaanAwal5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanAwal5KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanAwal5);
        PertanyaanAwal5.setBounds(620, 230, 80, 23);

        jLabel106.setText("Nilai :");
        jLabel106.setName("jLabel106"); // NOI18N
        FormInput.add(jLabel106);
        jLabel106.setBounds(690, 230, 50, 23);

        NilaiPertanyaanAwal5.setEditable(false);
        NilaiPertanyaanAwal5.setText("0");
        NilaiPertanyaanAwal5.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanAwal5.setName("NilaiPertanyaanAwal5"); // NOI18N
        FormInput.add(NilaiPertanyaanAwal5);
        NilaiPertanyaanAwal5.setBounds(744, 230, 45, 23);

        jLabel107.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel107.setText("6.");
        jLabel107.setName("jLabel107"); // NOI18N
        FormInput.add(jLabel107);
        jLabel107.setBounds(54, 260, 20, 23);

        jLabel108.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel108.setText("Apakah anda tinggal di daerah tinggi polusi (banyak asap kendaraan, asap pabrik, pemanasan global) ?");
        jLabel108.setName("jLabel108"); // NOI18N
        FormInput.add(jLabel108);
        jLabel108.setBounds(72, 260, 530, 23);

        PertanyaanAwal6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanAwal6.setName("PertanyaanAwal6"); // NOI18N
        PertanyaanAwal6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanAwal6ItemStateChanged(evt);
            }
        });
        PertanyaanAwal6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanAwal6KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanAwal6);
        PertanyaanAwal6.setBounds(620, 260, 80, 23);

        jLabel109.setText("Nilai :");
        jLabel109.setName("jLabel109"); // NOI18N
        FormInput.add(jLabel109);
        jLabel109.setBounds(690, 260, 50, 23);

        NilaiPertanyaanAwal6.setEditable(false);
        NilaiPertanyaanAwal6.setText("0");
        NilaiPertanyaanAwal6.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanAwal6.setName("NilaiPertanyaanAwal6"); // NOI18N
        FormInput.add(NilaiPertanyaanAwal6);
        NilaiPertanyaanAwal6.setBounds(744, 260, 45, 23);

        jLabel110.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel110.setText("Apakah anda sering mengkonsumsi makanan yang prosesnya dibakar, digoreng, diasap, diasinkan,");
        jLabel110.setName("jLabel110"); // NOI18N
        FormInput.add(jLabel110);
        jLabel110.setBounds(72, 285, 530, 23);

        jLabel111.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel111.setText("7.");
        jLabel111.setName("jLabel111"); // NOI18N
        FormInput.add(jLabel111);
        jLabel111.setBounds(54, 290, 20, 23);

        PertanyaanAwal7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanAwal7.setName("PertanyaanAwal7"); // NOI18N
        PertanyaanAwal7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanAwal7ItemStateChanged(evt);
            }
        });
        PertanyaanAwal7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanAwal7KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanAwal7);
        PertanyaanAwal7.setBounds(620, 290, 80, 23);

        jLabel112.setText("Nilai :");
        jLabel112.setName("jLabel112"); // NOI18N
        FormInput.add(jLabel112);
        jLabel112.setBounds(690, 290, 50, 23);

        NilaiPertanyaanAwal7.setEditable(false);
        NilaiPertanyaanAwal7.setText("0");
        NilaiPertanyaanAwal7.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanAwal7.setName("NilaiPertanyaanAwal7"); // NOI18N
        FormInput.add(NilaiPertanyaanAwal7);
        NilaiPertanyaanAwal7.setBounds(744, 290, 45, 23);

        jLabel113.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel113.setText("diacar, mengandung bahan pengawet, berlemak, dan cepat saji ?");
        jLabel113.setName("jLabel113"); // NOI18N
        FormInput.add(jLabel113);
        jLabel113.setBounds(72, 300, 530, 23);

        jLabel114.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel114.setText("8.");
        jLabel114.setName("jLabel114"); // NOI18N
        FormInput.add(jLabel114);
        jLabel114.setBounds(54, 320, 20, 23);

        jLabel115.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel115.setText("Apakah anda mengalami menarche (saat haid pertama) di usia sangat muda ?");
        jLabel115.setName("jLabel115"); // NOI18N
        FormInput.add(jLabel115);
        jLabel115.setBounds(72, 320, 530, 23);

        PertanyaanAwal8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanAwal8.setName("PertanyaanAwal8"); // NOI18N
        PertanyaanAwal8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanAwal8ItemStateChanged(evt);
            }
        });
        PertanyaanAwal8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanAwal8KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanAwal8);
        PertanyaanAwal8.setBounds(620, 320, 80, 23);

        jLabel116.setText("Nilai :");
        jLabel116.setName("jLabel116"); // NOI18N
        FormInput.add(jLabel116);
        jLabel116.setBounds(690, 320, 50, 23);

        NilaiPertanyaanAwal8.setEditable(false);
        NilaiPertanyaanAwal8.setText("0");
        NilaiPertanyaanAwal8.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanAwal8.setName("NilaiPertanyaanAwal8"); // NOI18N
        FormInput.add(NilaiPertanyaanAwal8);
        NilaiPertanyaanAwal8.setBounds(744, 320, 45, 23);

        jLabel117.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel117.setText("9.");
        jLabel117.setName("jLabel117"); // NOI18N
        FormInput.add(jLabel117);
        jLabel117.setBounds(54, 350, 20, 23);

        jLabel118.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel118.setText("Apakah selisih kehamilan pertama anda dengan haid pertama lebih dari 15 tahun ?");
        jLabel118.setName("jLabel118"); // NOI18N
        FormInput.add(jLabel118);
        jLabel118.setBounds(72, 350, 530, 23);

        PertanyaanAwal9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanAwal9.setName("PertanyaanAwal9"); // NOI18N
        PertanyaanAwal9.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanAwal9ItemStateChanged(evt);
            }
        });
        PertanyaanAwal9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanAwal9KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanAwal9);
        PertanyaanAwal9.setBounds(620, 350, 80, 23);

        jLabel119.setText("Nilai :");
        jLabel119.setName("jLabel119"); // NOI18N
        FormInput.add(jLabel119);
        jLabel119.setBounds(690, 350, 50, 23);

        NilaiPertanyaanAwal9.setEditable(false);
        NilaiPertanyaanAwal9.setText("0");
        NilaiPertanyaanAwal9.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanAwal9.setName("NilaiPertanyaanAwal9"); // NOI18N
        FormInput.add(NilaiPertanyaanAwal9);
        NilaiPertanyaanAwal9.setBounds(744, 350, 45, 23);

        NilaiPertanyaanAwal10.setEditable(false);
        NilaiPertanyaanAwal10.setText("0");
        NilaiPertanyaanAwal10.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanAwal10.setName("NilaiPertanyaanAwal10"); // NOI18N
        FormInput.add(NilaiPertanyaanAwal10);
        NilaiPertanyaanAwal10.setBounds(744, 380, 45, 23);

        jLabel120.setText("Nilai :");
        jLabel120.setName("jLabel120"); // NOI18N
        FormInput.add(jLabel120);
        jLabel120.setBounds(690, 380, 50, 23);

        PertanyaanAwal10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanAwal10.setName("PertanyaanAwal10"); // NOI18N
        PertanyaanAwal10.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanAwal10ItemStateChanged(evt);
            }
        });
        PertanyaanAwal10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanAwal10KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanAwal10);
        PertanyaanAwal10.setBounds(620, 380, 80, 23);

        jLabel121.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel121.setText("Apakah anda mengalami menopause (henti haid) di usia lebih dari 50 tahun ?");
        jLabel121.setName("jLabel121"); // NOI18N
        FormInput.add(jLabel121);
        jLabel121.setBounds(72, 380, 530, 23);

        jLabel122.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel122.setText("10.");
        jLabel122.setName("jLabel122"); // NOI18N
        FormInput.add(jLabel122);
        jLabel122.setBounds(54, 380, 20, 23);

        jLabel123.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel123.setText("11.");
        jLabel123.setName("jLabel123"); // NOI18N
        FormInput.add(jLabel123);
        jLabel123.setBounds(54, 410, 20, 23);

        jLabel124.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel124.setText("Apakah anda termasuk golongan ras kulit putih (kaukasia) ?");
        jLabel124.setName("jLabel124"); // NOI18N
        FormInput.add(jLabel124);
        jLabel124.setBounds(72, 410, 530, 23);

        PertanyaanAwal11.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanAwal11.setName("PertanyaanAwal11"); // NOI18N
        PertanyaanAwal11.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanAwal11ItemStateChanged(evt);
            }
        });
        PertanyaanAwal11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanAwal11KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanAwal11);
        PertanyaanAwal11.setBounds(620, 410, 80, 23);

        jLabel125.setText("Nilai :");
        jLabel125.setName("jLabel125"); // NOI18N
        FormInput.add(jLabel125);
        jLabel125.setBounds(690, 410, 50, 23);

        NilaiPertanyaanAwal11.setEditable(false);
        NilaiPertanyaanAwal11.setText("0");
        NilaiPertanyaanAwal11.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanAwal11.setName("NilaiPertanyaanAwal11"); // NOI18N
        FormInput.add(NilaiPertanyaanAwal11);
        NilaiPertanyaanAwal11.setBounds(744, 410, 45, 23);

        jLabel126.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel126.setText("Apakah anda terdiagnosa mengalami mutasi gen BRCA 1 & BRCA 2 ?");
        jLabel126.setName("jLabel126"); // NOI18N
        FormInput.add(jLabel126);
        jLabel126.setBounds(72, 440, 530, 23);

        jLabel127.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel127.setText("12.");
        jLabel127.setName("jLabel127"); // NOI18N
        FormInput.add(jLabel127);
        jLabel127.setBounds(54, 440, 20, 23);

        PertanyaanAwal12.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanAwal12.setName("PertanyaanAwal12"); // NOI18N
        PertanyaanAwal12.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanAwal12ItemStateChanged(evt);
            }
        });
        PertanyaanAwal12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanAwal12KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanAwal12);
        PertanyaanAwal12.setBounds(620, 440, 80, 23);

        jLabel128.setText("Nilai :");
        jLabel128.setName("jLabel128"); // NOI18N
        FormInput.add(jLabel128);
        jLabel128.setBounds(690, 440, 50, 23);

        NilaiPertanyaanAwal12.setEditable(false);
        NilaiPertanyaanAwal12.setText("0");
        NilaiPertanyaanAwal12.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanAwal12.setName("NilaiPertanyaanAwal12"); // NOI18N
        FormInput.add(NilaiPertanyaanAwal12);
        NilaiPertanyaanAwal12.setBounds(744, 440, 45, 23);

        jLabel129.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel129.setText("13.");
        jLabel129.setName("jLabel129"); // NOI18N
        FormInput.add(jLabel129);
        jLabel129.setBounds(54, 470, 20, 23);

        jLabel130.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel130.setText("Apakah anda seorang perempuan (lebih tinggi resiko dibandingkan laki-laki) ?");
        jLabel130.setName("jLabel130"); // NOI18N
        FormInput.add(jLabel130);
        jLabel130.setBounds(72, 470, 530, 23);

        PertanyaanAwal13.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanAwal13.setName("PertanyaanAwal13"); // NOI18N
        PertanyaanAwal13.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanAwal13ItemStateChanged(evt);
            }
        });
        PertanyaanAwal13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanAwal13KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanAwal13);
        PertanyaanAwal13.setBounds(620, 470, 80, 23);

        jLabel131.setText("Nilai :");
        jLabel131.setName("jLabel131"); // NOI18N
        FormInput.add(jLabel131);
        jLabel131.setBounds(690, 470, 50, 23);

        NilaiPertanyaanAwal13.setEditable(false);
        NilaiPertanyaanAwal13.setText("0");
        NilaiPertanyaanAwal13.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanAwal13.setName("NilaiPertanyaanAwal13"); // NOI18N
        FormInput.add(NilaiPertanyaanAwal13);
        NilaiPertanyaanAwal13.setBounds(744, 470, 45, 23);

        jLabel132.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel132.setText("Apakah anda termasuk golongan obesitas/kegemukan ?");
        jLabel132.setName("jLabel132"); // NOI18N
        FormInput.add(jLabel132);
        jLabel132.setBounds(72, 500, 530, 23);

        jLabel133.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel133.setText("14.");
        jLabel133.setName("jLabel133"); // NOI18N
        FormInput.add(jLabel133);
        jLabel133.setBounds(54, 500, 20, 23);

        PertanyaanAwal14.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanAwal14.setName("PertanyaanAwal14"); // NOI18N
        PertanyaanAwal14.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanAwal14ItemStateChanged(evt);
            }
        });
        PertanyaanAwal14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanAwal14KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanAwal14);
        PertanyaanAwal14.setBounds(620, 500, 80, 23);

        jLabel134.setText("Nilai :");
        jLabel134.setName("jLabel134"); // NOI18N
        FormInput.add(jLabel134);
        jLabel134.setBounds(690, 500, 50, 23);

        NilaiPertanyaanAwal14.setEditable(false);
        NilaiPertanyaanAwal14.setText("0");
        NilaiPertanyaanAwal14.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanAwal14.setName("NilaiPertanyaanAwal14"); // NOI18N
        FormInput.add(NilaiPertanyaanAwal14);
        NilaiPertanyaanAwal14.setBounds(744, 500, 45, 23);

        jLabel135.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel135.setText("B. Faktor Risiko Tinggi (Ya=5, Tidak=0)");
        jLabel135.setName("jLabel135"); // NOI18N
        FormInput.add(jLabel135);
        jLabel135.setBounds(44, 530, 310, 23);

        jLabel100.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel100.setText("Apakah anda bila sakit tidak kontrol atau tidak tuntas berobat ?");
        jLabel100.setName("jLabel100"); // NOI18N
        FormInput.add(jLabel100);
        jLabel100.setBounds(72, 730, 470, 23);

        jLabel136.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel136.setText("7.");
        jLabel136.setName("jLabel136"); // NOI18N
        FormInput.add(jLabel136);
        jLabel136.setBounds(54, 730, 20, 23);

        PertanyaanLanjutan7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan7.setName("PertanyaanLanjutan7"); // NOI18N
        PertanyaanLanjutan7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan7ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan7KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan7);
        PertanyaanLanjutan7.setBounds(620, 730, 80, 23);

        jLabel137.setText("Nilai :");
        jLabel137.setName("jLabel137"); // NOI18N
        FormInput.add(jLabel137);
        jLabel137.setBounds(690, 730, 50, 23);

        NilaiPertanyaanLanjutan7.setEditable(false);
        NilaiPertanyaanLanjutan7.setText("0");
        NilaiPertanyaanLanjutan7.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan7.setName("NilaiPertanyaanLanjutan7"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan7);
        NilaiPertanyaanLanjutan7.setBounds(744, 730, 45, 23);

        jLabel138.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel138.setText("Apakah anda berusia di atas 25 tahun (semakin tua usia semakin tinggi resiko) ?");
        jLabel138.setName("jLabel138"); // NOI18N
        FormInput.add(jLabel138);
        jLabel138.setBounds(72, 760, 470, 23);

        jLabel139.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel139.setText("8.");
        jLabel139.setName("jLabel139"); // NOI18N
        FormInput.add(jLabel139);
        jLabel139.setBounds(54, 760, 20, 23);

        PertanyaanLanjutan8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan8.setName("PertanyaanLanjutan8"); // NOI18N
        PertanyaanLanjutan8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan8ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan8KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan8);
        PertanyaanLanjutan8.setBounds(620, 760, 80, 23);

        jLabel140.setText("Nilai :");
        jLabel140.setName("jLabel140"); // NOI18N
        FormInput.add(jLabel140);
        jLabel140.setBounds(690, 760, 50, 23);

        NilaiPertanyaanLanjutan8.setEditable(false);
        NilaiPertanyaanLanjutan8.setText("0");
        NilaiPertanyaanLanjutan8.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan8.setName("NilaiPertanyaanLanjutan8"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan8);
        NilaiPertanyaanLanjutan8.setBounds(744, 760, 45, 23);

        NilaiPertanyaanLanjutan9.setEditable(false);
        NilaiPertanyaanLanjutan9.setText("0");
        NilaiPertanyaanLanjutan9.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan9.setName("NilaiPertanyaanLanjutan9"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan9);
        NilaiPertanyaanLanjutan9.setBounds(744, 790, 45, 23);

        jLabel141.setText("Nilai :");
        jLabel141.setName("jLabel141"); // NOI18N
        FormInput.add(jLabel141);
        jLabel141.setBounds(690, 790, 50, 23);

        PertanyaanLanjutan9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan9.setName("PertanyaanLanjutan9"); // NOI18N
        PertanyaanLanjutan9.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan9ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan9KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan9);
        PertanyaanLanjutan9.setBounds(620, 790, 80, 23);

        jLabel142.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel142.setText("Apakah anda pernah memiliki tumor (benjolan) payudara ?");
        jLabel142.setName("jLabel142"); // NOI18N
        FormInput.add(jLabel142);
        jLabel142.setBounds(72, 790, 470, 23);

        jLabel143.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel143.setText("9.");
        jLabel143.setName("jLabel143"); // NOI18N
        FormInput.add(jLabel143);
        jLabel143.setBounds(54, 790, 20, 23);

        jLabel144.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel144.setText("10.");
        jLabel144.setName("jLabel144"); // NOI18N
        FormInput.add(jLabel144);
        jLabel144.setBounds(54, 820, 20, 23);

        jLabel145.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel145.setText("Apakah anda menderita kanker pada salah satu payudara (resiko bagi satu payudara yang lainnya) ?");
        jLabel145.setName("jLabel145"); // NOI18N
        FormInput.add(jLabel145);
        jLabel145.setBounds(72, 820, 530, 23);

        PertanyaanLanjutan10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan10.setName("PertanyaanLanjutan10"); // NOI18N
        PertanyaanLanjutan10.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan10ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan10KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan10);
        PertanyaanLanjutan10.setBounds(620, 820, 80, 23);

        jLabel146.setText("Nilai :");
        jLabel146.setName("jLabel146"); // NOI18N
        FormInput.add(jLabel146);
        jLabel146.setBounds(690, 820, 50, 23);

        NilaiPertanyaanLanjutan10.setEditable(false);
        NilaiPertanyaanLanjutan10.setText("0");
        NilaiPertanyaanLanjutan10.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan10.setName("NilaiPertanyaanLanjutan10"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan10);
        NilaiPertanyaanLanjutan10.setBounds(744, 820, 45, 23);

        jLabel147.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel147.setText("11.");
        jLabel147.setName("jLabel147"); // NOI18N
        FormInput.add(jLabel147);
        jLabel147.setBounds(54, 850, 20, 23);

        jLabel150.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel150.setText("Apakah anda memiliki riwayat sakit kanker endometrium ?");
        jLabel150.setName("jLabel150"); // NOI18N
        FormInput.add(jLabel150);
        jLabel150.setBounds(72, 850, 530, 23);

        PertanyaanLanjutan11.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan11.setName("PertanyaanLanjutan11"); // NOI18N
        PertanyaanLanjutan11.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan11ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan11KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan11);
        PertanyaanLanjutan11.setBounds(620, 850, 80, 23);

        jLabel151.setText("Nilai :");
        jLabel151.setName("jLabel151"); // NOI18N
        FormInput.add(jLabel151);
        jLabel151.setBounds(690, 850, 50, 23);

        NilaiPertanyaanLanjutan11.setEditable(false);
        NilaiPertanyaanLanjutan11.setText("0");
        NilaiPertanyaanLanjutan11.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan11.setName("NilaiPertanyaanLanjutan11"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan11);
        NilaiPertanyaanLanjutan11.setBounds(744, 850, 45, 23);

        jLabel152.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel152.setText("Apakah anda diperiksa secara radiologis dan ditemukan hasil densitas yang sangat tinggi ?");
        jLabel152.setName("jLabel152"); // NOI18N
        FormInput.add(jLabel152);
        jLabel152.setBounds(72, 880, 530, 23);

        jLabel153.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel153.setText("12.");
        jLabel153.setName("jLabel153"); // NOI18N
        FormInput.add(jLabel153);
        jLabel153.setBounds(54, 880, 20, 23);

        PertanyaanLanjutan12.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan12.setName("PertanyaanLanjutan12"); // NOI18N
        PertanyaanLanjutan12.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan12ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan12KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan12);
        PertanyaanLanjutan12.setBounds(620, 880, 80, 23);

        jLabel154.setText("Nilai :");
        jLabel154.setName("jLabel154"); // NOI18N
        FormInput.add(jLabel154);
        jLabel154.setBounds(690, 880, 50, 23);

        NilaiPertanyaanLanjutan12.setEditable(false);
        NilaiPertanyaanLanjutan12.setText("0");
        NilaiPertanyaanLanjutan12.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan12.setName("NilaiPertanyaanLanjutan12"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan12);
        NilaiPertanyaanLanjutan12.setBounds(744, 880, 45, 23);

        jLabel155.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel155.setText("Apakah anda memiliki silsilah keluarga yang menderita kanker ?");
        jLabel155.setName("jLabel155"); // NOI18N
        FormInput.add(jLabel155);
        jLabel155.setBounds(72, 910, 530, 23);

        jLabel156.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel156.setText("13.");
        jLabel156.setName("jLabel156"); // NOI18N
        FormInput.add(jLabel156);
        jLabel156.setBounds(54, 910, 20, 23);

        PertanyaanLanjutan13.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan13.setName("PertanyaanLanjutan13"); // NOI18N
        PertanyaanLanjutan13.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan13ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan13KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan13);
        PertanyaanLanjutan13.setBounds(620, 910, 80, 23);

        jLabel157.setText("Nilai :");
        jLabel157.setName("jLabel157"); // NOI18N
        FormInput.add(jLabel157);
        jLabel157.setBounds(690, 910, 50, 23);

        NilaiPertanyaanLanjutan13.setEditable(false);
        NilaiPertanyaanLanjutan13.setText("0");
        NilaiPertanyaanLanjutan13.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan13.setName("NilaiPertanyaanLanjutan13"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan13);
        NilaiPertanyaanLanjutan13.setBounds(744, 910, 45, 23);

        jLabel158.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel158.setText("C. Kecurigaan Keganasan (Ya=10,Tidak=0)");
        jLabel158.setName("jLabel158"); // NOI18N
        FormInput.add(jLabel158);
        jLabel158.setBounds(44, 940, 310, 23);

        jLabel159.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel159.setText("1.");
        jLabel159.setName("jLabel159"); // NOI18N
        FormInput.add(jLabel159);
        jLabel159.setBounds(54, 960, 20, 23);

        jLabel160.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel160.setText("Apakah ada benjolan/tumor di payudara anda ?");
        jLabel160.setName("jLabel160"); // NOI18N
        FormInput.add(jLabel160);
        jLabel160.setBounds(72, 960, 470, 23);

        PertanyaanLanjutan14.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan14.setName("PertanyaanLanjutan14"); // NOI18N
        PertanyaanLanjutan14.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan14ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan14KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan14);
        PertanyaanLanjutan14.setBounds(620, 960, 80, 23);

        jLabel161.setText("Nilai :");
        jLabel161.setName("jLabel161"); // NOI18N
        FormInput.add(jLabel161);
        jLabel161.setBounds(690, 960, 50, 23);

        NilaiPertanyaanLanjutan14.setEditable(false);
        NilaiPertanyaanLanjutan14.setText("0");
        NilaiPertanyaanLanjutan14.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan14.setName("NilaiPertanyaanLanjutan14"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan14);
        NilaiPertanyaanLanjutan14.setBounds(744, 960, 45, 23);

        jLabel162.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel162.setText("2.");
        jLabel162.setName("jLabel162"); // NOI18N
        FormInput.add(jLabel162);
        jLabel162.setBounds(54, 990, 20, 23);

        jLabel163.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel163.setText("Apakah kulit/puting susu anda tertarik ke dalam ?");
        jLabel163.setName("jLabel163"); // NOI18N
        FormInput.add(jLabel163);
        jLabel163.setBounds(72, 990, 470, 23);

        PertanyaanLanjutan15.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan15.setName("PertanyaanLanjutan15"); // NOI18N
        PertanyaanLanjutan15.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan15ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan15KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan15);
        PertanyaanLanjutan15.setBounds(620, 990, 80, 23);

        jLabel164.setText("Nilai :");
        jLabel164.setName("jLabel164"); // NOI18N
        FormInput.add(jLabel164);
        jLabel164.setBounds(690, 990, 50, 23);

        NilaiPertanyaanLanjutan15.setEditable(false);
        NilaiPertanyaanLanjutan15.setText("0");
        NilaiPertanyaanLanjutan15.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan15.setName("NilaiPertanyaanLanjutan15"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan15);
        NilaiPertanyaanLanjutan15.setBounds(744, 990, 45, 23);

        jLabel165.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel165.setText("Apakah areola payudara anda berwarna merah muda atau kecoklat-coklatan sampai menjadi oedema");
        jLabel165.setName("jLabel165"); // NOI18N
        FormInput.add(jLabel165);
        jLabel165.setBounds(72, 1015, 530, 23);

        jLabel166.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel166.setText("3.");
        jLabel166.setName("jLabel166"); // NOI18N
        FormInput.add(jLabel166);
        jLabel166.setBounds(54, 1020, 20, 23);

        PertanyaanLanjutan16.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan16.setName("PertanyaanLanjutan16"); // NOI18N
        PertanyaanLanjutan16.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan16ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan16KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan16);
        PertanyaanLanjutan16.setBounds(620, 1020, 80, 23);

        jLabel167.setText("Nilai :");
        jLabel167.setName("jLabel167"); // NOI18N
        FormInput.add(jLabel167);
        jLabel167.setBounds(690, 1020, 50, 23);

        NilaiPertanyaanLanjutan16.setEditable(false);
        NilaiPertanyaanLanjutan16.setText("0");
        NilaiPertanyaanLanjutan16.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan16.setName("NilaiPertanyaanLanjutan16"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan16);
        NilaiPertanyaanLanjutan16.setBounds(744, 1020, 45, 23);

        jLabel168.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel168.setText("hingga kulit kelihatan seperti kulit jeruk (peau dorange), mengkerut, atau timbul borok (ulkus) ?");
        jLabel168.setName("jLabel168"); // NOI18N
        FormInput.add(jLabel168);
        jLabel168.setBounds(72, 1030, 490, 23);

        jLabel169.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel169.setText("4.");
        jLabel169.setName("jLabel169"); // NOI18N
        FormInput.add(jLabel169);
        jLabel169.setBounds(54, 1050, 20, 23);

        jLabel170.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel170.setText("Apakah puting susu anda mengeluarkan cairan tidak normal, darah atau nanah ?");
        jLabel170.setName("jLabel170"); // NOI18N
        FormInput.add(jLabel170);
        jLabel170.setBounds(72, 1050, 470, 23);

        PertanyaanLanjutan17.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan17.setName("PertanyaanLanjutan17"); // NOI18N
        PertanyaanLanjutan17.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan17ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan17KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan17);
        PertanyaanLanjutan17.setBounds(620, 1050, 80, 23);

        jLabel171.setText("Nilai :");
        jLabel171.setName("jLabel171"); // NOI18N
        FormInput.add(jLabel171);
        jLabel171.setBounds(690, 1050, 50, 23);

        NilaiPertanyaanLanjutan17.setEditable(false);
        NilaiPertanyaanLanjutan17.setText("0");
        NilaiPertanyaanLanjutan17.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan17.setName("NilaiPertanyaanLanjutan17"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan17);
        NilaiPertanyaanLanjutan17.setBounds(744, 1050, 45, 23);

        jLabel172.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel172.setText("5.");
        jLabel172.setName("jLabel172"); // NOI18N
        FormInput.add(jLabel172);
        jLabel172.setBounds(54, 1080, 20, 23);

        jLabel173.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel173.setText("Apakah ada benjolan di ketiak anda berdiameter lebih 2,5 cm, dapat melekat satu sama lain ?");
        jLabel173.setName("jLabel173"); // NOI18N
        FormInput.add(jLabel173);
        jLabel173.setBounds(72, 1080, 470, 23);

        PertanyaanLanjutan18.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan18.setName("PertanyaanLanjutan18"); // NOI18N
        PertanyaanLanjutan18.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan18ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan18.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan18KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan18);
        PertanyaanLanjutan18.setBounds(620, 1080, 80, 23);

        jLabel174.setText("Nilai :");
        jLabel174.setName("jLabel174"); // NOI18N
        FormInput.add(jLabel174);
        jLabel174.setBounds(690, 1080, 50, 23);

        NilaiPertanyaanLanjutan18.setEditable(false);
        NilaiPertanyaanLanjutan18.setText("0");
        NilaiPertanyaanLanjutan18.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan18.setName("NilaiPertanyaanLanjutan18"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan18);
        NilaiPertanyaanLanjutan18.setBounds(744, 1080, 45, 23);

        jLabel175.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel175.setText("Apakah ada benjolan di sekitar tulang belikat ?");
        jLabel175.setName("jLabel175"); // NOI18N
        FormInput.add(jLabel175);
        jLabel175.setBounds(72, 1110, 470, 23);

        jLabel176.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel176.setText("6.");
        jLabel176.setName("jLabel176"); // NOI18N
        FormInput.add(jLabel176);
        jLabel176.setBounds(54, 1110, 20, 23);

        PertanyaanLanjutan19.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan19.setName("PertanyaanLanjutan19"); // NOI18N
        PertanyaanLanjutan19.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan19ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan19.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan19KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan19);
        PertanyaanLanjutan19.setBounds(620, 1110, 80, 23);

        jLabel177.setText("Nilai :");
        jLabel177.setName("jLabel177"); // NOI18N
        FormInput.add(jLabel177);
        jLabel177.setBounds(690, 1110, 50, 23);

        NilaiPertanyaanLanjutan19.setEditable(false);
        NilaiPertanyaanLanjutan19.setText("0");
        NilaiPertanyaanLanjutan19.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan19.setName("NilaiPertanyaanLanjutan19"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan19);
        NilaiPertanyaanLanjutan19.setBounds(744, 1110, 45, 23);

        jLabel178.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel178.setText("Apakah lengan anda bengkak ?");
        jLabel178.setName("jLabel178"); // NOI18N
        FormInput.add(jLabel178);
        jLabel178.setBounds(72, 1140, 470, 23);

        jLabel179.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel179.setText("7.");
        jLabel179.setName("jLabel179"); // NOI18N
        FormInput.add(jLabel179);
        jLabel179.setBounds(54, 1140, 20, 23);

        PertanyaanLanjutan20.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan20.setName("PertanyaanLanjutan20"); // NOI18N
        PertanyaanLanjutan20.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan20ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan20.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan20KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan20);
        PertanyaanLanjutan20.setBounds(620, 1140, 80, 23);

        jLabel180.setText("Nilai :");
        jLabel180.setName("jLabel180"); // NOI18N
        FormInput.add(jLabel180);
        jLabel180.setBounds(690, 1140, 50, 23);

        NilaiPertanyaanLanjutan20.setEditable(false);
        NilaiPertanyaanLanjutan20.setText("0");
        NilaiPertanyaanLanjutan20.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan20.setName("NilaiPertanyaanLanjutan20"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan20);
        NilaiPertanyaanLanjutan20.setBounds(744, 1140, 45, 23);

        jLabel181.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel181.setText("Apakah ada erosi atau luka yang tidak sembuh-sembuh pada puting susu ?");
        jLabel181.setName("jLabel181"); // NOI18N
        FormInput.add(jLabel181);
        jLabel181.setBounds(72, 1170, 470, 23);

        jLabel182.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel182.setText("8.");
        jLabel182.setName("jLabel182"); // NOI18N
        FormInput.add(jLabel182);
        jLabel182.setBounds(54, 1170, 20, 23);

        PertanyaanLanjutan21.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PertanyaanLanjutan21.setName("PertanyaanLanjutan21"); // NOI18N
        PertanyaanLanjutan21.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan21ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan21.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan21KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan21);
        PertanyaanLanjutan21.setBounds(620, 1170, 80, 23);

        jLabel183.setText("Nilai :");
        jLabel183.setName("jLabel183"); // NOI18N
        FormInput.add(jLabel183);
        jLabel183.setBounds(690, 1170, 50, 23);

        NilaiPertanyaanLanjutan21.setEditable(false);
        NilaiPertanyaanLanjutan21.setText("0");
        NilaiPertanyaanLanjutan21.setFocusTraversalPolicyProvider(true);
        NilaiPertanyaanLanjutan21.setName("NilaiPertanyaanLanjutan21"); // NOI18N
        FormInput.add(NilaiPertanyaanLanjutan21);
        NilaiPertanyaanLanjutan21.setBounds(744, 1170, 45, 23);

        jLabel184.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel184.setText("Hasil Pemeriksaan SADANIS");
        jLabel184.setName("jLabel184"); // NOI18N
        FormInput.add(jLabel184);
        jLabel184.setBounds(44, 1260, 150, 23);

        PertanyaanLanjutan22.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Benjolan", "Tidak Ada Benjolan", "Curiga Kanker" }));
        PertanyaanLanjutan22.setName("PertanyaanLanjutan22"); // NOI18N
        PertanyaanLanjutan22.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan22ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan22.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan22KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan22);
        PertanyaanLanjutan22.setBounds(191, 1260, 170, 23);

        jLabel185.setText(":");
        jLabel185.setName("jLabel185"); // NOI18N
        FormInput.add(jLabel185);
        jLabel185.setBounds(0, 1260, 187, 23);

        jLabel186.setText("Tindak Lanjut Hasil Pemeriksaan SADANIS :");
        jLabel186.setName("jLabel186"); // NOI18N
        FormInput.add(jLabel186);
        jLabel186.setBounds(419, 1260, 240, 23);

        PertanyaanLanjutan23.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Dirujuk", "Tidak Dirujuk" }));
        PertanyaanLanjutan23.setName("PertanyaanLanjutan23"); // NOI18N
        PertanyaanLanjutan23.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan23ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan23.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan23KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan23);
        PertanyaanLanjutan23.setBounds(663, 1260, 126, 23);

        jSeparator4.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator4.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator4.setName("jSeparator4"); // NOI18N
        FormInput.add(jSeparator4);
        jSeparator4.setBounds(0, 1290, 807, 1);

        jLabel187.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel187.setText("III. INTERPRETASI");
        jLabel187.setName("jLabel187"); // NOI18N
        FormInput.add(jLabel187);
        jLabel187.setBounds(10, 1290, 200, 23);

        jLabel188.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel188.setText("Hasil Skrining");
        jLabel188.setName("jLabel188"); // NOI18N
        FormInput.add(jLabel188);
        jLabel188.setBounds(44, 1310, 80, 23);

        PertanyaanLanjutan24.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Normal", "Kemungkinan Kelainan Payudara Jinak", "Curiga Kelainan Payudara Ganas" }));
        PertanyaanLanjutan24.setName("PertanyaanLanjutan24"); // NOI18N
        PertanyaanLanjutan24.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PertanyaanLanjutan24ItemStateChanged(evt);
            }
        });
        PertanyaanLanjutan24.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PertanyaanLanjutan24KeyPressed(evt);
            }
        });
        FormInput.add(PertanyaanLanjutan24);
        PertanyaanLanjutan24.setBounds(121, 1310, 240, 23);

        jLabel189.setText(":");
        jLabel189.setName("jLabel189"); // NOI18N
        FormInput.add(jLabel189);
        jLabel189.setBounds(0, 1310, 117, 23);

        jLabel190.setText("Keterangan :");
        jLabel190.setName("jLabel190"); // NOI18N
        FormInput.add(jLabel190);
        jLabel190.setBounds(356, 1310, 90, 23);

        HasilSkrining1.setEditable(false);
        HasilSkrining1.setFocusTraversalPolicyProvider(true);
        HasilSkrining1.setName("HasilSkrining1"); // NOI18N
        HasilSkrining1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HasilSkrining1KeyPressed(evt);
            }
        });
        FormInput.add(HasilSkrining1);
        HasilSkrining1.setBounds(450, 1310, 339, 23);

        jSeparator2.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator2.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator2.setName("jSeparator2"); // NOI18N
        FormInput.add(jSeparator2);
        jSeparator2.setBounds(44, 530, 763, 1);

        jSeparator5.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator5.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator5.setName("jSeparator5"); // NOI18N
        FormInput.add(jSeparator5);
        jSeparator5.setBounds(44, 940, 763, 1);

        jSeparator6.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator6.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator6.setName("jSeparator6"); // NOI18N
        FormInput.add(jSeparator6);
        jSeparator6.setBounds(44, 1200, 763, 1);

        scrollInput.setViewportView(FormInput);

        PanelInput.add(scrollInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
        }else{            
            Valid.pindah(evt,TCari,Tanggal);
        }
}//GEN-LAST:event_TNoRwKeyPressed

    private void TPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPasienKeyPressed
        Valid.pindah(evt,TCari,BtnSimpan);
}//GEN-LAST:event_TPasienKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"pasien");
        }else if(KdPetugas.getText().trim().equals("")||NmPetugas.getText().trim().equals("")){
            Valid.textKosong(KdPetugas,"Petugas");
        }else{
            if(akses.getkode().equals("Admin Utama")){
                simpan();
            }else{
                if(TanggalRegistrasi.getText().equals("")){
                    TanggalRegistrasi.setText(Sequel.cariIsi("select concat(reg_periksa.tgl_registrasi,' ',reg_periksa.jam_reg) from reg_periksa where reg_periksa.no_rawat=?",TNoRw.getText()));
                }
                if(Sequel.cekTanggalRegistrasi(TanggalRegistrasi.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem())==true){
                    simpan();
                }
            }
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            Valid.pindah(evt,PertanyaanLanjutan6,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
        ChkInput.setSelected(true);
        isForm(); 
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tbObat.getSelectedRow()>-1){
            if(akses.getkode().equals("Admin Utama")){
                hapus();
            }else{
                if(KdPetugas.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString())){
                    if(Sequel.cekTanggal48jam(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString(),Sequel.ambiltanggalsekarang())==true){
                        hapus();
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Hanya bisa dihapus oleh petugas yang bersangkutan..!!");
                }
            }
        }else{
            JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
        }  
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"pasien");
        }else if(KdPetugas.getText().trim().equals("")||NmPetugas.getText().trim().equals("")){
            Valid.textKosong(KdPetugas,"Petugas");
        }else{
            if(tbObat.getSelectedRow()>-1){
                if(akses.getkode().equals("Admin Utama")){
                    ganti();
                }else{
                    if(KdPetugas.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString())){
                        if(Sequel.cekTanggal48jam(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString(),Sequel.ambiltanggalsekarang())==true){
                            if(TanggalRegistrasi.getText().equals("")){
                                TanggalRegistrasi.setText(Sequel.cariIsi("select concat(reg_periksa.tgl_registrasi,' ',reg_periksa.jam_reg) from reg_periksa where reg_periksa.no_rawat=?",TNoRw.getText()));
                            }
                            if(Sequel.cekTanggalRegistrasi(TanggalRegistrasi.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem())==true){
                                ganti();
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,"Hanya bisa diganti oleh petugas yang bersangkutan..!!");
                    }
                }
            }else{
                JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
            }
        }
}//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnPrint);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        petugas.dispose();
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnKeluarActionPerformed(null);
        }else{Valid.pindah(evt,BtnEdit,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBatal.requestFocus();
        }else if(tabMode.getRowCount()!=0){
            try{
                htmlContent = new StringBuilder();
                htmlContent.append(                             
                    "<tr class='isi'>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>No.Rawat</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>No.RM</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Nama Pasien</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Tgl.Lahir</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Umur</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Kode Petugas</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Nama Petugas</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Tanggal</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Secara umum, bagaimana Anda<br>menggambarkan hubungan Anda ?</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>N.P.A.1</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Seperti apa saat Anda<br>dan pasangan berdebat ?</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>N.P.A.2</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Apakah pertengkaran pernah membuat Anda merasa<br>sedih atau buruk tentang diri sendiri ?</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>N.P.L.1</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Apakah pertengkaran pernah menghasilkan<br>pukulan, tendangan, atau dorongan ?</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>N.P.L.2</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Apakah Anda pernah merasa takut dengan apa<br>yang pasangan Anda katakan atau lakukan ?</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>N.P.L.3</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Apakah pasangan Anda pernah<br>melecehkan Anda secara fisik ?</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>N.P.L.4</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Pernahkah pasangan Anda melecehkan<br>Anda secara emosional ?</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>N.P.L.5</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Apakah pasangan Anda pernah<br>melecehkan Anda secara seksual ?</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>N.P.L.6</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Total Skor</b></td>"+
                        "<td valign='middle' bgcolor='#FFFAFA' align='center'><b>Hasil Skrining</b></td>"+
                    "</tr>"
                );
                for (i = 0; i < tabMode.getRowCount(); i++) {
                    htmlContent.append(
                        "<tr class='isi'>"+
                           "<td valign='top'>"+tbObat.getValueAt(i,0).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,1).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,2).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,3).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,4).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,5).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,6).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,7).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,8).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,9).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,10).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,11).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,12).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,13).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,14).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,15).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,16).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,17).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,18).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,19).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,20).toString()+"</td>"+ 
                            "<td valign='top'>"+tbObat.getValueAt(i,21).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,22).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,23).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,24).toString()+"</td>"+
                            "<td valign='top'>"+tbObat.getValueAt(i,25).toString()+"</td>"+
                        "</tr>");
                }
                LoadHTML.setText(
                    "<html>"+
                      "<table width='2600px' border='0' align='center' cellpadding='1px' cellspacing='0' class='tbl_form'>"+
                       htmlContent.toString()+
                      "</table>"+
                    "</html>"
                );

                File g = new File("file2.css");            
                BufferedWriter bg = new BufferedWriter(new FileWriter(g));
                bg.write(
                    ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                    ".isi2 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#323232;}"+
                    ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                    ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                    ".isi5 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#AA0000;}"+
                    ".isi6 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#FF0000;}"+
                    ".isi7 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#C8C800;}"+
                    ".isi8 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#00AA00;}"+
                    ".isi9 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#969696;}"
                );
                bg.close();

                File f = new File("DataSkriningKekerasanPadaPerempuan.html");            
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));            
                bw.write(LoadHTML.getText().replaceAll("<head>","<head>"+
                            "<link href=\"file2.css\" rel=\"stylesheet\" type=\"text/css\" />"+
                            "<table width='2600px' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                "<tr class='isi2'>"+
                                    "<td valign='top' align='center'>"+
                                        "<font size='4' face='Tahoma'>"+akses.getnamars()+"</font><br>"+
                                        akses.getalamatrs()+", "+akses.getkabupatenrs()+", "+akses.getpropinsirs()+"<br>"+
                                        akses.getkontakrs()+", E-mail : "+akses.getemailrs()+"<br><br>"+
                                        "<font size='2' face='Tahoma'>DATA SEKRINING KEKERASAN PADA PEREMPUAN<br><br></font>"+        
                                    "</td>"+
                               "</tr>"+
                            "</table>")
                );
                bw.close();                         
                Desktop.getDesktop().browse(f.toURI());

            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnEdit, BtnKeluar);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampil();
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            tampil();
            TCari.setText("");
        }else{
            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
        Valid.pindah(evt,TCari,Jam);
}//GEN-LAST:event_TanggalKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbObatKeyPressed

    private void JamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JamKeyPressed
        Valid.pindah(evt,Tanggal,Menit);
    }//GEN-LAST:event_JamKeyPressed

    private void MenitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenitKeyPressed
        Valid.pindah(evt,Jam,Detik);
    }//GEN-LAST:event_MenitKeyPressed

    private void DetikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DetikKeyPressed
        Valid.pindah(evt,Menit,btnPetugas);
    }//GEN-LAST:event_DetikKeyPressed

    private void btnPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPetugasActionPerformed
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
    }//GEN-LAST:event_btnPetugasActionPerformed

    private void btnPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPetugasKeyPressed
        Valid.pindah(evt,TCari,PertanyaanAwal1);
    }//GEN-LAST:event_btnPetugasKeyPressed

    private void MnSkriningKekerasanPadaPerempuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnSkriningKekerasanPadaPerempuanActionPerformed
        if(tbObat.getSelectedRow()>-1){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbObat.getValueAt(tbObat.getSelectedRow(),6).toString()+"\nID "+(finger.equals("")?tbObat.getValueAt(tbObat.getSelectedRow(),5).toString():finger)+"\n"+Tanggal.getSelectedItem()); 
            Valid.MyReportqry("rptFormulirSkriningKekerasanPadaPerempuan.jasper","report","::[ Formulir Skrining Kekerasan Pada Perempuan ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,reg_periksa.umurdaftar,reg_periksa.sttsumur,skrining_kekerasan_pada_perempuan.nip,"+
                    "petugas.nama,skrining_kekerasan_pada_perempuan.tanggal,skrining_kekerasan_pada_perempuan.menggambarkan_hubungan,skrining_kekerasan_pada_perempuan.skor_menggambarkan_hubungan,"+
                    "skrining_kekerasan_pada_perempuan.berdebat_dengan_pasangan,skrining_kekerasan_pada_perempuan.skor_berdebat_dengan_pasangan,skrining_kekerasan_pada_perempuan.pertengkaran_membuat_sedih,"+
                    "skrining_kekerasan_pada_perempuan.skor_pertengkaran_membuat_sedih,skrining_kekerasan_pada_perempuan.pertengkaran_menghasilkan_pukulan,"+
                    "skrining_kekerasan_pada_perempuan.skor_pertengkaran_menghasilkan_pukulan,skrining_kekerasan_pada_perempuan.pernah_merasa_takut_dengan_pasangan,"+
                    "skrining_kekerasan_pada_perempuan.skor_pernah_merasa_takut_dengan_pasangan,skrining_kekerasan_pada_perempuan.pasangan_melecehkan_secara_fisik,"+
                    "skrining_kekerasan_pada_perempuan.skor_pasangan_melecehkan_secara_fisik,skrining_kekerasan_pada_perempuan.pasangan_melecehkan_secara_imosional,"+
                    "skrining_kekerasan_pada_perempuan.skor_pasangan_melecehkan_secara_imosional,skrining_kekerasan_pada_perempuan.pasangan_melecehkan_secara_seksual,"+
                    "skrining_kekerasan_pada_perempuan.skor_pasangan_melecehkan_secara_seksual,skrining_kekerasan_pada_perempuan.totalskor,skrining_kekerasan_pada_perempuan.hasil_skrining "+
                    "from skrining_kekerasan_pada_perempuan inner join reg_periksa on skrining_kekerasan_pada_perempuan.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join petugas on skrining_kekerasan_pada_perempuan.nip=petugas.nip "+
                    "where reg_periksa.no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()+"'",param);
        }
    }//GEN-LAST:event_MnSkriningKekerasanPadaPerempuanActionPerformed

    private void PertanyaanAwal1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanAwal1ItemStateChanged
        NilaiPertanyaanAwal1.setText((PertanyaanAwal1.getSelectedIndex()+1)+"");
        isTotal();
    }//GEN-LAST:event_PertanyaanAwal1ItemStateChanged

    private void PertanyaanAwal1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanAwal1KeyPressed
        Valid.pindah(evt,TCari,PertanyaanAwal2);
    }//GEN-LAST:event_PertanyaanAwal1KeyPressed

    private void PertanyaanAwal2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanAwal2ItemStateChanged
        NilaiPertanyaanAwal2.setText((PertanyaanAwal2.getSelectedIndex()+1)+"");
        isTotal();
    }//GEN-LAST:event_PertanyaanAwal2ItemStateChanged

    private void PertanyaanAwal2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanAwal2KeyPressed
        Valid.pindah(evt,PertanyaanAwal1,PertanyaanLanjutan1);
    }//GEN-LAST:event_PertanyaanAwal2KeyPressed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void PertanyaanLanjutan1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan1ItemStateChanged
        NilaiPertanyaanLanjutan1.setText((PertanyaanLanjutan1.getSelectedIndex()+1)+"");
        isTotal();
    }//GEN-LAST:event_PertanyaanLanjutan1ItemStateChanged

    private void PertanyaanLanjutan1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan1KeyPressed
        Valid.pindah(evt,PertanyaanAwal2,PertanyaanLanjutan2);
    }//GEN-LAST:event_PertanyaanLanjutan1KeyPressed

    private void PertanyaanLanjutan2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan2ItemStateChanged
        NilaiPertanyaanLanjutan2.setText((PertanyaanLanjutan2.getSelectedIndex()+1)+"");
        isTotal();
    }//GEN-LAST:event_PertanyaanLanjutan2ItemStateChanged

    private void PertanyaanLanjutan2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan2KeyPressed
        Valid.pindah(evt,PertanyaanLanjutan1,PertanyaanLanjutan3);
    }//GEN-LAST:event_PertanyaanLanjutan2KeyPressed

    private void HasilSkriningKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HasilSkriningKeyPressed
        //Valid.pindah(evt,Lapor,SG1);
    }//GEN-LAST:event_HasilSkriningKeyPressed

    private void PertanyaanLanjutan3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan3ItemStateChanged
        NilaiPertanyaanLanjutan3.setText((PertanyaanLanjutan3.getSelectedIndex()+1)+"");
        isTotal();
    }//GEN-LAST:event_PertanyaanLanjutan3ItemStateChanged

    private void PertanyaanLanjutan3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan3KeyPressed
        Valid.pindah(evt,PertanyaanLanjutan2,PertanyaanLanjutan4);
    }//GEN-LAST:event_PertanyaanLanjutan3KeyPressed

    private void PertanyaanLanjutan4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan4ItemStateChanged
        NilaiPertanyaanLanjutan4.setText((PertanyaanLanjutan4.getSelectedIndex()+1)+"");
        isTotal();
    }//GEN-LAST:event_PertanyaanLanjutan4ItemStateChanged

    private void PertanyaanLanjutan4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan4KeyPressed
        Valid.pindah(evt,PertanyaanLanjutan3,PertanyaanLanjutan5);
    }//GEN-LAST:event_PertanyaanLanjutan4KeyPressed

    private void PertanyaanLanjutan5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan5ItemStateChanged
        NilaiPertanyaanLanjutan5.setText((PertanyaanLanjutan5.getSelectedIndex()+1)+"");
        isTotal();
    }//GEN-LAST:event_PertanyaanLanjutan5ItemStateChanged

    private void PertanyaanLanjutan5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan5KeyPressed
        Valid.pindah(evt,PertanyaanLanjutan4,PertanyaanLanjutan6);
    }//GEN-LAST:event_PertanyaanLanjutan5KeyPressed

    private void PertanyaanLanjutan6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan6ItemStateChanged
        NilaiPertanyaanLanjutan6.setText((PertanyaanLanjutan6.getSelectedIndex()+1)+"");
        isTotal();
    }//GEN-LAST:event_PertanyaanLanjutan6ItemStateChanged

    private void PertanyaanLanjutan6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan6KeyPressed
        Valid.pindah(evt,PertanyaanLanjutan5,BtnSimpan);
    }//GEN-LAST:event_PertanyaanLanjutan6KeyPressed

    private void PertanyaanAwal3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanAwal3ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal3ItemStateChanged

    private void PertanyaanAwal3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanAwal3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal3KeyPressed

    private void PertanyaanAwal4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanAwal4ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal4ItemStateChanged

    private void PertanyaanAwal4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanAwal4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal4KeyPressed

    private void PertanyaanAwal5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanAwal5ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal5ItemStateChanged

    private void PertanyaanAwal5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanAwal5KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal5KeyPressed

    private void PertanyaanAwal6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanAwal6ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal6ItemStateChanged

    private void PertanyaanAwal6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanAwal6KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal6KeyPressed

    private void PertanyaanAwal7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanAwal7ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal7ItemStateChanged

    private void PertanyaanAwal7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanAwal7KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal7KeyPressed

    private void PertanyaanAwal8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanAwal8ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal8ItemStateChanged

    private void PertanyaanAwal8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanAwal8KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal8KeyPressed

    private void PertanyaanAwal9ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanAwal9ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal9ItemStateChanged

    private void PertanyaanAwal9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanAwal9KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal9KeyPressed

    private void PertanyaanAwal10ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanAwal10ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal10ItemStateChanged

    private void PertanyaanAwal10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanAwal10KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal10KeyPressed

    private void PertanyaanAwal11ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanAwal11ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal11ItemStateChanged

    private void PertanyaanAwal11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanAwal11KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal11KeyPressed

    private void PertanyaanAwal12ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanAwal12ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal12ItemStateChanged

    private void PertanyaanAwal12KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanAwal12KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal12KeyPressed

    private void PertanyaanAwal13ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanAwal13ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal13ItemStateChanged

    private void PertanyaanAwal13KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanAwal13KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal13KeyPressed

    private void PertanyaanAwal14ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanAwal14ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal14ItemStateChanged

    private void PertanyaanAwal14KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanAwal14KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanAwal14KeyPressed

    private void PertanyaanLanjutan7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan7ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan7ItemStateChanged

    private void PertanyaanLanjutan7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan7KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan7KeyPressed

    private void PertanyaanLanjutan8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan8ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan8ItemStateChanged

    private void PertanyaanLanjutan8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan8KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan8KeyPressed

    private void PertanyaanLanjutan9ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan9ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan9ItemStateChanged

    private void PertanyaanLanjutan9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan9KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan9KeyPressed

    private void PertanyaanLanjutan10ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan10ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan10ItemStateChanged

    private void PertanyaanLanjutan10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan10KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan10KeyPressed

    private void PertanyaanLanjutan11ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan11ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan11ItemStateChanged

    private void PertanyaanLanjutan11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan11KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan11KeyPressed

    private void PertanyaanLanjutan12ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan12ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan12ItemStateChanged

    private void PertanyaanLanjutan12KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan12KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan12KeyPressed

    private void PertanyaanLanjutan13ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan13ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan13ItemStateChanged

    private void PertanyaanLanjutan13KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan13KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan13KeyPressed

    private void PertanyaanLanjutan14ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan14ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan14ItemStateChanged

    private void PertanyaanLanjutan14KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan14KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan14KeyPressed

    private void PertanyaanLanjutan15ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan15ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan15ItemStateChanged

    private void PertanyaanLanjutan15KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan15KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan15KeyPressed

    private void PertanyaanLanjutan16ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan16ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan16ItemStateChanged

    private void PertanyaanLanjutan16KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan16KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan16KeyPressed

    private void PertanyaanLanjutan17ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan17ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan17ItemStateChanged

    private void PertanyaanLanjutan17KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan17KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan17KeyPressed

    private void PertanyaanLanjutan18ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan18ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan18ItemStateChanged

    private void PertanyaanLanjutan18KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan18KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan18KeyPressed

    private void PertanyaanLanjutan19ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan19ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan19ItemStateChanged

    private void PertanyaanLanjutan19KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan19KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan19KeyPressed

    private void PertanyaanLanjutan20ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan20ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan20ItemStateChanged

    private void PertanyaanLanjutan20KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan20KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan20KeyPressed

    private void PertanyaanLanjutan21ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan21ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan21ItemStateChanged

    private void PertanyaanLanjutan21KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan21KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan21KeyPressed

    private void PertanyaanLanjutan22ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan22ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan22ItemStateChanged

    private void PertanyaanLanjutan22KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan22KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan22KeyPressed

    private void PertanyaanLanjutan23ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan23ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan23ItemStateChanged

    private void PertanyaanLanjutan23KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan23KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan23KeyPressed

    private void PertanyaanLanjutan24ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan24ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan24ItemStateChanged

    private void PertanyaanLanjutan24KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PertanyaanLanjutan24KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PertanyaanLanjutan24KeyPressed

    private void HasilSkrining1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HasilSkrining1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_HasilSkrining1KeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMSkriningRisikoKankerPayudara dialog = new RMSkriningRisikoKankerPayudara(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput;
    private widget.CekBox ChkKejadian;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.ComboBox Detik;
    private widget.PanelBiasa FormInput;
    private widget.TextBox HasilSkrining;
    private widget.TextBox HasilSkrining1;
    private widget.ComboBox Jam;
    private widget.TextBox KdPetugas;
    private widget.Label LCount;
    private widget.editorpane LoadHTML;
    private widget.ComboBox Menit;
    private javax.swing.JMenuItem MnSkriningKekerasanPadaPerempuan;
    private widget.TextBox NilaiPertanyaanAwal1;
    private widget.TextBox NilaiPertanyaanAwal10;
    private widget.TextBox NilaiPertanyaanAwal11;
    private widget.TextBox NilaiPertanyaanAwal12;
    private widget.TextBox NilaiPertanyaanAwal13;
    private widget.TextBox NilaiPertanyaanAwal14;
    private widget.TextBox NilaiPertanyaanAwal2;
    private widget.TextBox NilaiPertanyaanAwal3;
    private widget.TextBox NilaiPertanyaanAwal4;
    private widget.TextBox NilaiPertanyaanAwal5;
    private widget.TextBox NilaiPertanyaanAwal6;
    private widget.TextBox NilaiPertanyaanAwal7;
    private widget.TextBox NilaiPertanyaanAwal8;
    private widget.TextBox NilaiPertanyaanAwal9;
    private widget.TextBox NilaiPertanyaanLanjutan1;
    private widget.TextBox NilaiPertanyaanLanjutan10;
    private widget.TextBox NilaiPertanyaanLanjutan11;
    private widget.TextBox NilaiPertanyaanLanjutan12;
    private widget.TextBox NilaiPertanyaanLanjutan13;
    private widget.TextBox NilaiPertanyaanLanjutan14;
    private widget.TextBox NilaiPertanyaanLanjutan15;
    private widget.TextBox NilaiPertanyaanLanjutan16;
    private widget.TextBox NilaiPertanyaanLanjutan17;
    private widget.TextBox NilaiPertanyaanLanjutan18;
    private widget.TextBox NilaiPertanyaanLanjutan19;
    private widget.TextBox NilaiPertanyaanLanjutan2;
    private widget.TextBox NilaiPertanyaanLanjutan20;
    private widget.TextBox NilaiPertanyaanLanjutan21;
    private widget.TextBox NilaiPertanyaanLanjutan3;
    private widget.TextBox NilaiPertanyaanLanjutan4;
    private widget.TextBox NilaiPertanyaanLanjutan5;
    private widget.TextBox NilaiPertanyaanLanjutan6;
    private widget.TextBox NilaiPertanyaanLanjutan7;
    private widget.TextBox NilaiPertanyaanLanjutan8;
    private widget.TextBox NilaiPertanyaanLanjutan9;
    private widget.TextBox NmPetugas;
    private javax.swing.JPanel PanelInput;
    private widget.ComboBox PertanyaanAwal1;
    private widget.ComboBox PertanyaanAwal10;
    private widget.ComboBox PertanyaanAwal11;
    private widget.ComboBox PertanyaanAwal12;
    private widget.ComboBox PertanyaanAwal13;
    private widget.ComboBox PertanyaanAwal14;
    private widget.ComboBox PertanyaanAwal2;
    private widget.ComboBox PertanyaanAwal3;
    private widget.ComboBox PertanyaanAwal4;
    private widget.ComboBox PertanyaanAwal5;
    private widget.ComboBox PertanyaanAwal6;
    private widget.ComboBox PertanyaanAwal7;
    private widget.ComboBox PertanyaanAwal8;
    private widget.ComboBox PertanyaanAwal9;
    private widget.ComboBox PertanyaanLanjutan1;
    private widget.ComboBox PertanyaanLanjutan10;
    private widget.ComboBox PertanyaanLanjutan11;
    private widget.ComboBox PertanyaanLanjutan12;
    private widget.ComboBox PertanyaanLanjutan13;
    private widget.ComboBox PertanyaanLanjutan14;
    private widget.ComboBox PertanyaanLanjutan15;
    private widget.ComboBox PertanyaanLanjutan16;
    private widget.ComboBox PertanyaanLanjutan17;
    private widget.ComboBox PertanyaanLanjutan18;
    private widget.ComboBox PertanyaanLanjutan19;
    private widget.ComboBox PertanyaanLanjutan2;
    private widget.ComboBox PertanyaanLanjutan20;
    private widget.ComboBox PertanyaanLanjutan21;
    private widget.ComboBox PertanyaanLanjutan22;
    private widget.ComboBox PertanyaanLanjutan23;
    private widget.ComboBox PertanyaanLanjutan24;
    private widget.ComboBox PertanyaanLanjutan3;
    private widget.ComboBox PertanyaanLanjutan4;
    private widget.ComboBox PertanyaanLanjutan5;
    private widget.ComboBox PertanyaanLanjutan6;
    private widget.ComboBox PertanyaanLanjutan7;
    private widget.ComboBox PertanyaanLanjutan8;
    private widget.ComboBox PertanyaanLanjutan9;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.Tanggal Tanggal;
    private widget.TextBox TanggalRegistrasi;
    private widget.TextBox TglLahir;
    private widget.TextBox TotalHasil;
    private widget.TextBox Umur;
    private widget.Button btnPetugas;
    private javax.swing.ButtonGroup buttonGroup1;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel100;
    private widget.Label jLabel101;
    private widget.Label jLabel102;
    private widget.Label jLabel103;
    private widget.Label jLabel104;
    private widget.Label jLabel105;
    private widget.Label jLabel106;
    private widget.Label jLabel107;
    private widget.Label jLabel108;
    private widget.Label jLabel109;
    private widget.Label jLabel110;
    private widget.Label jLabel111;
    private widget.Label jLabel112;
    private widget.Label jLabel113;
    private widget.Label jLabel114;
    private widget.Label jLabel115;
    private widget.Label jLabel116;
    private widget.Label jLabel117;
    private widget.Label jLabel118;
    private widget.Label jLabel119;
    private widget.Label jLabel120;
    private widget.Label jLabel121;
    private widget.Label jLabel122;
    private widget.Label jLabel123;
    private widget.Label jLabel124;
    private widget.Label jLabel125;
    private widget.Label jLabel126;
    private widget.Label jLabel127;
    private widget.Label jLabel128;
    private widget.Label jLabel129;
    private widget.Label jLabel130;
    private widget.Label jLabel131;
    private widget.Label jLabel132;
    private widget.Label jLabel133;
    private widget.Label jLabel134;
    private widget.Label jLabel135;
    private widget.Label jLabel136;
    private widget.Label jLabel137;
    private widget.Label jLabel138;
    private widget.Label jLabel139;
    private widget.Label jLabel140;
    private widget.Label jLabel141;
    private widget.Label jLabel142;
    private widget.Label jLabel143;
    private widget.Label jLabel144;
    private widget.Label jLabel145;
    private widget.Label jLabel146;
    private widget.Label jLabel147;
    private widget.Label jLabel148;
    private widget.Label jLabel149;
    private widget.Label jLabel150;
    private widget.Label jLabel151;
    private widget.Label jLabel152;
    private widget.Label jLabel153;
    private widget.Label jLabel154;
    private widget.Label jLabel155;
    private widget.Label jLabel156;
    private widget.Label jLabel157;
    private widget.Label jLabel158;
    private widget.Label jLabel159;
    private widget.Label jLabel16;
    private widget.Label jLabel160;
    private widget.Label jLabel161;
    private widget.Label jLabel162;
    private widget.Label jLabel163;
    private widget.Label jLabel164;
    private widget.Label jLabel165;
    private widget.Label jLabel166;
    private widget.Label jLabel167;
    private widget.Label jLabel168;
    private widget.Label jLabel169;
    private widget.Label jLabel170;
    private widget.Label jLabel171;
    private widget.Label jLabel172;
    private widget.Label jLabel173;
    private widget.Label jLabel174;
    private widget.Label jLabel175;
    private widget.Label jLabel176;
    private widget.Label jLabel177;
    private widget.Label jLabel178;
    private widget.Label jLabel179;
    private widget.Label jLabel18;
    private widget.Label jLabel180;
    private widget.Label jLabel181;
    private widget.Label jLabel182;
    private widget.Label jLabel183;
    private widget.Label jLabel184;
    private widget.Label jLabel185;
    private widget.Label jLabel186;
    private widget.Label jLabel187;
    private widget.Label jLabel188;
    private widget.Label jLabel189;
    private widget.Label jLabel19;
    private widget.Label jLabel190;
    private widget.Label jLabel21;
    private widget.Label jLabel4;
    private widget.Label jLabel6;
    private widget.Label jLabel69;
    private widget.Label jLabel7;
    private widget.Label jLabel70;
    private widget.Label jLabel71;
    private widget.Label jLabel72;
    private widget.Label jLabel73;
    private widget.Label jLabel74;
    private widget.Label jLabel75;
    private widget.Label jLabel76;
    private widget.Label jLabel77;
    private widget.Label jLabel78;
    private widget.Label jLabel79;
    private widget.Label jLabel8;
    private widget.Label jLabel80;
    private widget.Label jLabel81;
    private widget.Label jLabel82;
    private widget.Label jLabel83;
    private widget.Label jLabel84;
    private widget.Label jLabel85;
    private widget.Label jLabel86;
    private widget.Label jLabel87;
    private widget.Label jLabel88;
    private widget.Label jLabel89;
    private widget.Label jLabel90;
    private widget.Label jLabel91;
    private widget.Label jLabel92;
    private widget.Label jLabel93;
    private widget.Label jLabel94;
    private widget.Label jLabel95;
    private widget.Label jLabel96;
    private widget.Label jLabel97;
    private widget.Label jLabel98;
    private widget.Label jLabel99;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollInput;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables
    
    public void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            if(TCari.getText().trim().equals("")){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,reg_periksa.umurdaftar,reg_periksa.sttsumur,skrining_kekerasan_pada_perempuan.nip,"+
                    "petugas.nama,skrining_kekerasan_pada_perempuan.tanggal,skrining_kekerasan_pada_perempuan.menggambarkan_hubungan,skrining_kekerasan_pada_perempuan.skor_menggambarkan_hubungan,"+
                    "skrining_kekerasan_pada_perempuan.berdebat_dengan_pasangan,skrining_kekerasan_pada_perempuan.skor_berdebat_dengan_pasangan,skrining_kekerasan_pada_perempuan.pertengkaran_membuat_sedih,"+
                    "skrining_kekerasan_pada_perempuan.skor_pertengkaran_membuat_sedih,skrining_kekerasan_pada_perempuan.pertengkaran_menghasilkan_pukulan,"+
                    "skrining_kekerasan_pada_perempuan.skor_pertengkaran_menghasilkan_pukulan,skrining_kekerasan_pada_perempuan.pernah_merasa_takut_dengan_pasangan,"+
                    "skrining_kekerasan_pada_perempuan.skor_pernah_merasa_takut_dengan_pasangan,skrining_kekerasan_pada_perempuan.pasangan_melecehkan_secara_fisik,"+
                    "skrining_kekerasan_pada_perempuan.skor_pasangan_melecehkan_secara_fisik,skrining_kekerasan_pada_perempuan.pasangan_melecehkan_secara_imosional,"+
                    "skrining_kekerasan_pada_perempuan.skor_pasangan_melecehkan_secara_imosional,skrining_kekerasan_pada_perempuan.pasangan_melecehkan_secara_seksual,"+
                    "skrining_kekerasan_pada_perempuan.skor_pasangan_melecehkan_secara_seksual,skrining_kekerasan_pada_perempuan.totalskor,skrining_kekerasan_pada_perempuan.hasil_skrining "+
                    "from skrining_kekerasan_pada_perempuan inner join reg_periksa on skrining_kekerasan_pada_perempuan.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join petugas on skrining_kekerasan_pada_perempuan.nip=petugas.nip "+
                    "where skrining_kekerasan_pada_perempuan.tanggal between ? and ? order by skrining_kekerasan_pada_perempuan.tanggal ");
            }else{
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,reg_periksa.umurdaftar,reg_periksa.sttsumur,skrining_kekerasan_pada_perempuan.nip,"+
                    "petugas.nama,skrining_kekerasan_pada_perempuan.tanggal,skrining_kekerasan_pada_perempuan.menggambarkan_hubungan,skrining_kekerasan_pada_perempuan.skor_menggambarkan_hubungan,"+
                    "skrining_kekerasan_pada_perempuan.berdebat_dengan_pasangan,skrining_kekerasan_pada_perempuan.skor_berdebat_dengan_pasangan,skrining_kekerasan_pada_perempuan.pertengkaran_membuat_sedih,"+
                    "skrining_kekerasan_pada_perempuan.skor_pertengkaran_membuat_sedih,skrining_kekerasan_pada_perempuan.pertengkaran_menghasilkan_pukulan,"+
                    "skrining_kekerasan_pada_perempuan.skor_pertengkaran_menghasilkan_pukulan,skrining_kekerasan_pada_perempuan.pernah_merasa_takut_dengan_pasangan,"+
                    "skrining_kekerasan_pada_perempuan.skor_pernah_merasa_takut_dengan_pasangan,skrining_kekerasan_pada_perempuan.pasangan_melecehkan_secara_fisik,"+
                    "skrining_kekerasan_pada_perempuan.skor_pasangan_melecehkan_secara_fisik,skrining_kekerasan_pada_perempuan.pasangan_melecehkan_secara_imosional,"+
                    "skrining_kekerasan_pada_perempuan.skor_pasangan_melecehkan_secara_imosional,skrining_kekerasan_pada_perempuan.pasangan_melecehkan_secara_seksual,"+
                    "skrining_kekerasan_pada_perempuan.skor_pasangan_melecehkan_secara_seksual,skrining_kekerasan_pada_perempuan.totalskor,skrining_kekerasan_pada_perempuan.hasil_skrining "+
                    "from skrining_kekerasan_pada_perempuan inner join reg_periksa on skrining_kekerasan_pada_perempuan.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join petugas on skrining_kekerasan_pada_perempuan.nip=petugas.nip "+
                    "where skrining_kekerasan_pada_perempuan.tanggal between ? and ? and (reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or "+
                    "pasien.nm_pasien like ? or skrining_kekerasan_pada_perempuan.nip like ? or petugas.nama like ?) "+
                    "order by skrining_kekerasan_pada_perempuan.tanggal ");
            }
                
            try {
                if(TCari.getText().trim().equals("")){
                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                }else{
                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                }
                    
                rs=ps.executeQuery();
                while(rs.next()){
                    tabMode.addRow(new String[]{
                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("tgl_lahir"),rs.getString("umurdaftar")+" "+rs.getString("sttsumur"),
                        rs.getString("nip"),rs.getString("nama"),rs.getString("tanggal"),rs.getString("menggambarkan_hubungan"),rs.getString("skor_menggambarkan_hubungan"),rs.getString("berdebat_dengan_pasangan"),
                        rs.getString("skor_berdebat_dengan_pasangan"),rs.getString("pertengkaran_membuat_sedih"),rs.getString("skor_pertengkaran_membuat_sedih"),rs.getString("pertengkaran_menghasilkan_pukulan"),
                        rs.getString("skor_pertengkaran_menghasilkan_pukulan"),rs.getString("pernah_merasa_takut_dengan_pasangan"),rs.getString("skor_pernah_merasa_takut_dengan_pasangan"),
                        rs.getString("pasangan_melecehkan_secara_fisik"),rs.getString("skor_pasangan_melecehkan_secara_fisik"),rs.getString("pasangan_melecehkan_secara_imosional"),
                        rs.getString("skor_pasangan_melecehkan_secara_imosional"),rs.getString("pasangan_melecehkan_secara_seksual"),rs.getString("skor_pasangan_melecehkan_secara_seksual"),
                        rs.getString("totalskor"),rs.getString("hasil_skrining")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabMode.getRowCount());
    }
    
    public void emptTeks() {
        Tanggal.setDate(new Date());
        PertanyaanAwal1.setSelectedIndex(0);
        NilaiPertanyaanAwal1.setText("1");
        PertanyaanAwal2.setSelectedIndex(0);
        NilaiPertanyaanAwal2.setText("1");
        PertanyaanLanjutan1.setSelectedIndex(0);
        NilaiPertanyaanLanjutan1.setText("1");
        PertanyaanLanjutan2.setSelectedIndex(0);
        NilaiPertanyaanLanjutan2.setText("1");
        PertanyaanLanjutan3.setSelectedIndex(0);
        NilaiPertanyaanLanjutan3.setText("1");
        PertanyaanLanjutan4.setSelectedIndex(0);
        NilaiPertanyaanLanjutan4.setText("1");
        PertanyaanLanjutan5.setSelectedIndex(0);
        NilaiPertanyaanLanjutan5.setText("1");
        PertanyaanLanjutan6.setSelectedIndex(0);
        NilaiPertanyaanLanjutan6.setText("1");
        TotalHasil.setText("8");
        HasilSkrining.setText("Pasien Tidak Terindikasi Mengalami Kekerasan");
        PertanyaanAwal1.requestFocus();
    } 

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){
            /*
            "Pertanyaan Awal 1"8,
            "N.P.A.1"9,
            "Pertanyaan Awal 2"10,
            "N.P.A.2"11,
            "Pertanyaan Lanjutan 1"12,
            "N.P.L.1"13,
            "Pertanyaan Lanjutan 2"14,
            "N.P.L.2"15,
            "Pertanyaan Lanjutan 3"16,
            "N.P.L.3"17,
            "Pertanyaan Lanjutan 4"18,
            "N.P.L.4"19,
            "Pertanyaan Lanjutan 5"20,
            "N.P.L.5"21,
            "Pertanyaan Lanjutan 6"22,
            "N.P.L.6"23,
            "Total Skor"24,
            "Hasil Skrining"25
            */
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString());
            Umur.setText(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString());
            Jam.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString().substring(11,13));
            Menit.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString().substring(14,15));
            Detik.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString().substring(17,19));
            PertanyaanAwal1.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
            NilaiPertanyaanAwal1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            PertanyaanAwal2.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            NilaiPertanyaanAwal2.setText(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
            PertanyaanLanjutan1.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
            NilaiPertanyaanLanjutan1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());
            PertanyaanLanjutan2.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            NilaiPertanyaanLanjutan2.setText(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString());
            PertanyaanLanjutan3.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),16).toString());
            NilaiPertanyaanLanjutan3.setText(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString());
            PertanyaanLanjutan4.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString());
            NilaiPertanyaanLanjutan4.setText(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString());
            PertanyaanLanjutan5.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString());
            NilaiPertanyaanLanjutan5.setText(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString());
            PertanyaanLanjutan6.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),22).toString());
            NilaiPertanyaanLanjutan6.setText(tbObat.getValueAt(tbObat.getSelectedRow(),23).toString());
            TotalHasil.setText(tbObat.getValueAt(tbObat.getSelectedRow(),24).toString());
            HasilSkrining.setText(tbObat.getValueAt(tbObat.getSelectedRow(),25).toString());
            Valid.SetTgl(Tanggal,tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());  
        }
    }
    
    private void isRawat() {
        try {
            ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,"+
                    "reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.umurdaftar,reg_periksa.sttsumur "+
                    "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "where reg_periksa.no_rawat=?");
            try {
                ps.setString(1,TNoRw.getText());
                rs=ps.executeQuery();
                if(rs.next()){
                    TNoRM.setText(rs.getString("no_rkm_medis"));
                    DTPCari1.setDate(rs.getDate("tgl_registrasi"));
                    TPasien.setText(rs.getString("nm_pasien"));
                    TglLahir.setText(rs.getString("tgl_lahir"));
                    TanggalRegistrasi.setText(rs.getString("tgl_registrasi")+" "+rs.getString("jam_reg"));
                    Umur.setText(rs.getString("umurdaftar")+" "+rs.getString("sttsumur"));
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }
    }
 
    public void setNoRm(String norwt,Date tgl2) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        DTPCari2.setDate(tgl2);    
        isRawat(); 
        ChkInput.setSelected(true);
        isForm();
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,internalFrame1.getHeight()-172));
            FormInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.getskrining_kekerasan_pada_perempuan());
        BtnHapus.setEnabled(akses.getskrining_kekerasan_pada_perempuan());
        BtnEdit.setEnabled(akses.getskrining_kekerasan_pada_perempuan());
        BtnPrint.setEnabled(akses.getskrining_kekerasan_pada_perempuan()); 
        if(akses.getjml2()>=1){
            KdPetugas.setEditable(false);
            btnPetugas.setEnabled(false);
            KdPetugas.setText(akses.getkode());
            NmPetugas.setText(petugas.tampil3(KdPetugas.getText()));
            if(NmPetugas.getText().equals("")){
                KdPetugas.setText("");
                JOptionPane.showMessageDialog(null,"User login bukan petugas...!!");
            }
        }  
        
        if(TANGGALMUNDUR.equals("no")){
            if(!akses.getkode().equals("Admin Utama")){
                Tanggal.setEditable(false);
                Tanggal.setEnabled(false);
                ChkKejadian.setEnabled(false);
                Jam.setEnabled(false);
                Menit.setEnabled(false);
                Detik.setEnabled(false);
            }
        }
    }

    private void jam(){
        ActionListener taskPerformer = new ActionListener(){
            private int nilai_jam;
            private int nilai_menit;
            private int nilai_detik;
            public void actionPerformed(ActionEvent e) {
                String nol_jam = "";
                String nol_menit = "";
                String nol_detik = "";
                
                Date now = Calendar.getInstance().getTime();

                // Mengambil nilaj JAM, MENIT, dan DETIK Sekarang
                if(ChkKejadian.isSelected()==true){
                    nilai_jam = now.getHours();
                    nilai_menit = now.getMinutes();
                    nilai_detik = now.getSeconds();
                }else if(ChkKejadian.isSelected()==false){
                    nilai_jam =Jam.getSelectedIndex();
                    nilai_menit =Menit.getSelectedIndex();
                    nilai_detik =Detik.getSelectedIndex();
                }

                // Jika nilai JAM lebih kecil dari 10 (hanya 1 digit)
                if (nilai_jam <= 9) {
                    // Tambahkan "0" didepannya
                    nol_jam = "0";
                }
                // Jika nilai MENIT lebih kecil dari 10 (hanya 1 digit)
                if (nilai_menit <= 9) {
                    // Tambahkan "0" didepannya
                    nol_menit = "0";
                }
                // Jika nilai DETIK lebih kecil dari 10 (hanya 1 digit)
                if (nilai_detik <= 9) {
                    // Tambahkan "0" didepannya
                    nol_detik = "0";
                }
                // Membuat String JAM, MENIT, DETIK
                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);
                // Menampilkan pada Layar
                //tampil_jam.setText("  " + jam + " : " + menit + " : " + detik + "  ");
                Jam.setSelectedItem(jam);
                Menit.setSelectedItem(menit);
                Detik.setSelectedItem(detik);
            }
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }

    private void ganti() {
        if(Sequel.mengedittf("skrining_kekerasan_pada_perempuan","no_rawat=?","no_rawat=?,tanggal=?,menggambarkan_hubungan=?,skor_menggambarkan_hubungan=?,berdebat_dengan_pasangan=?,skor_berdebat_dengan_pasangan=?,pertengkaran_membuat_sedih=?,"+
                "skor_pertengkaran_membuat_sedih=?,pertengkaran_menghasilkan_pukulan=?,skor_pertengkaran_menghasilkan_pukulan=?,pernah_merasa_takut_dengan_pasangan=?,skor_pernah_merasa_takut_dengan_pasangan=?,pasangan_melecehkan_secara_fisik=?,"+
                "skor_pasangan_melecehkan_secara_fisik=?,pasangan_melecehkan_secara_imosional=?,skor_pasangan_melecehkan_secara_imosional=?,pasangan_melecehkan_secara_seksual=?,skor_pasangan_melecehkan_secara_seksual=?,totalskor=?,hasil_skrining=?,"+
                "nip=?",22,new String[]{
                TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),
                PertanyaanAwal1.getSelectedItem().toString(),NilaiPertanyaanAwal1.getText(),PertanyaanAwal2.getSelectedItem().toString(),NilaiPertanyaanAwal2.getText(), 
                PertanyaanLanjutan1.getSelectedItem().toString(),NilaiPertanyaanLanjutan1.getText(),PertanyaanLanjutan2.getSelectedItem().toString(),NilaiPertanyaanLanjutan2.getText(), 
                PertanyaanLanjutan3.getSelectedItem().toString(),NilaiPertanyaanLanjutan3.getText(),PertanyaanLanjutan4.getSelectedItem().toString(),NilaiPertanyaanLanjutan4.getText(), 
                PertanyaanLanjutan5.getSelectedItem().toString(),NilaiPertanyaanLanjutan5.getText(),PertanyaanLanjutan6.getSelectedItem().toString(),NilaiPertanyaanLanjutan6.getText(), 
                TotalHasil.getText(),HasilSkrining.getText(),KdPetugas.getText(),tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
            })==true){
               tbObat.setValueAt(TNoRw.getText(),tbObat.getSelectedRow(),0);
               tbObat.setValueAt(TNoRM.getText(),tbObat.getSelectedRow(),1);
               tbObat.setValueAt(TPasien.getText(),tbObat.getSelectedRow(),2);
               tbObat.setValueAt(TglLahir.getText(),tbObat.getSelectedRow(),3);
               tbObat.setValueAt(Umur.getText(),tbObat.getSelectedRow(),4);
               tbObat.setValueAt(KdPetugas.getText(),tbObat.getSelectedRow(),5);
               tbObat.setValueAt(NmPetugas.getText(),tbObat.getSelectedRow(),6);
               tbObat.setValueAt(Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),tbObat.getSelectedRow(),7);
               tbObat.setValueAt(PertanyaanAwal1.getSelectedItem().toString(),tbObat.getSelectedRow(),8);
               tbObat.setValueAt(NilaiPertanyaanAwal1.getText(),tbObat.getSelectedRow(),9);
               tbObat.setValueAt(PertanyaanAwal2.getSelectedItem().toString(),tbObat.getSelectedRow(),10);
               tbObat.setValueAt(NilaiPertanyaanAwal2.getText(),tbObat.getSelectedRow(),11);
               tbObat.setValueAt(PertanyaanLanjutan1.getSelectedItem().toString(),tbObat.getSelectedRow(),12);
               tbObat.setValueAt(NilaiPertanyaanLanjutan1.getText(),tbObat.getSelectedRow(),13);
               tbObat.setValueAt(PertanyaanLanjutan2.getSelectedItem().toString(),tbObat.getSelectedRow(),14);
               tbObat.setValueAt(NilaiPertanyaanLanjutan2.getText(),tbObat.getSelectedRow(),15);
               tbObat.setValueAt(PertanyaanLanjutan3.getSelectedItem().toString(),tbObat.getSelectedRow(),16);
               tbObat.setValueAt(NilaiPertanyaanLanjutan3.getText(),tbObat.getSelectedRow(),17);
               tbObat.setValueAt(PertanyaanLanjutan4.getSelectedItem().toString(),tbObat.getSelectedRow(),18);
               tbObat.setValueAt(NilaiPertanyaanLanjutan4.getText(),tbObat.getSelectedRow(),19);
               tbObat.setValueAt(PertanyaanLanjutan5.getSelectedItem().toString(),tbObat.getSelectedRow(),20);
               tbObat.setValueAt(NilaiPertanyaanLanjutan5.getText(),tbObat.getSelectedRow(),21);
               tbObat.setValueAt(PertanyaanLanjutan6.getSelectedItem().toString(),tbObat.getSelectedRow(),22);
               tbObat.setValueAt(NilaiPertanyaanLanjutan6.getText(),tbObat.getSelectedRow(),23);
               tbObat.setValueAt(TotalHasil.getText(),tbObat.getSelectedRow(),24);
               tbObat.setValueAt(HasilSkrining.getText(),tbObat.getSelectedRow(),25);
               emptTeks();
        }
    }

    private void hapus() {
        if(Sequel.queryu2tf("delete from skrining_kekerasan_pada_perempuan where no_rawat=?",1,new String[]{
            tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
        })==true){
            tabMode.removeRow(tbObat.getSelectedRow());
            LCount.setText(""+tabMode.getRowCount());
            emptTeks();
        }else{
            JOptionPane.showMessageDialog(null,"Gagal menghapus..!!");
        }
    }

    private void isTotal() {
        try {
            TotalHasil.setText(""+(Integer.parseInt(NilaiPertanyaanAwal1.getText())+Integer.parseInt(NilaiPertanyaanAwal2.getText())+Integer.parseInt(NilaiPertanyaanLanjutan1.getText())+Integer.parseInt(NilaiPertanyaanLanjutan2.getText())+Integer.parseInt(NilaiPertanyaanLanjutan3.getText())+Integer.parseInt(NilaiPertanyaanLanjutan4.getText())+Integer.parseInt(NilaiPertanyaanLanjutan5.getText())+Integer.parseInt(NilaiPertanyaanLanjutan6.getText())));
            if(Integer.parseInt(TotalHasil.getText())>12){
                HasilSkrining.setText("Pasien Terindikasi Mengalami Kekerasan");
            }else{
                HasilSkrining.setText("Pasien Tidak Terindikasi Mengalami Kekerasan");
            }
        } catch (Exception e) {
            HasilSkrining.setText("Pasien Tidak Terindikasi Mengalami Kekerasan");
        }
    }

    private void simpan() {
        if(Sequel.menyimpantf("skrining_kekerasan_pada_perempuan","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","Data",21,new String[]{
            TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),
            PertanyaanAwal1.getSelectedItem().toString(),NilaiPertanyaanAwal1.getText(),PertanyaanAwal2.getSelectedItem().toString(),NilaiPertanyaanAwal2.getText(), 
            PertanyaanLanjutan1.getSelectedItem().toString(),NilaiPertanyaanLanjutan1.getText(),PertanyaanLanjutan2.getSelectedItem().toString(),NilaiPertanyaanLanjutan2.getText(), 
            PertanyaanLanjutan3.getSelectedItem().toString(),NilaiPertanyaanLanjutan3.getText(),PertanyaanLanjutan4.getSelectedItem().toString(),NilaiPertanyaanLanjutan4.getText(), 
            PertanyaanLanjutan5.getSelectedItem().toString(),NilaiPertanyaanLanjutan5.getText(),PertanyaanLanjutan6.getSelectedItem().toString(),NilaiPertanyaanLanjutan6.getText(), 
            TotalHasil.getText(),HasilSkrining.getText(),KdPetugas.getText()
        })==true){
            tabMode.addRow(new String[]{
                TNoRw.getText(),TNoRM.getText(),TPasien.getText(),TglLahir.getText(),Umur.getText(),KdPetugas.getText(),NmPetugas.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),
                PertanyaanAwal1.getSelectedItem().toString(),NilaiPertanyaanAwal1.getText(),PertanyaanAwal2.getSelectedItem().toString(),NilaiPertanyaanAwal2.getText(),PertanyaanLanjutan1.getSelectedItem().toString(),NilaiPertanyaanLanjutan1.getText(),
                PertanyaanLanjutan2.getSelectedItem().toString(),NilaiPertanyaanLanjutan2.getText(),PertanyaanLanjutan3.getSelectedItem().toString(),NilaiPertanyaanLanjutan3.getText(),PertanyaanLanjutan4.getSelectedItem().toString(),NilaiPertanyaanLanjutan4.getText(), 
                PertanyaanLanjutan5.getSelectedItem().toString(),NilaiPertanyaanLanjutan5.getText(),PertanyaanLanjutan6.getSelectedItem().toString(),NilaiPertanyaanLanjutan6.getText(),TotalHasil.getText(),HasilSkrining.getText()
            });
            LCount.setText(""+tabMode.getRowCount());
            emptTeks();
        } 
    }
    
}