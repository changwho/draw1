package java_final_work;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;

public class Painter extends JFrame implements ActionListener {
	//private	Container c = getContentPane();
	private	String menuBar[]={"文件(F)","编辑(E)","说明(H)"};
	private	String menuItem[][]={
		{"新建(N)|78","打开(O)|79","保存(S)|83","另存为(A)","退出(X)|88"},
		{"撤消(Z)|90","重复(Y)|79","剪切(W)|87","复制(D)|68","粘贴(U)|85"},
		{"关于(A)"}//为使用setAccelerator()方法，|后为数字
	};
	static	JMenuItem jMenuItem[][]=new JMenuItem[4][5];
	static	JMenu jMenu[];
	private	String ButtonName[]={"直线","矩开","椭圆","圆角矩形","贝氏曲线","扇型","多边形","铅笔","橡皮擦","文字","选取","油漆桶"};
	static JToggleButton jToggleButton[];
	static ButtonGroup buttonGroup;
	static	JPanel jPanel[]=new JPanel[5];//1绘图区,2工具箱,3色块,4属性栏
	//工具箱每个工具的名字以图像显示
	static	String toolname[]= 
{"src/img/tool1.gif","src/img/tool2.gif","src/img/tool3.gif","src/img/tool4.gif","src/img/tool5.gif","src/img/tool8.gif","src/img/tool9.gif","src/img/tool7.gif",
		"src/img/tool6.gif","src/img/tool10.gif","src/img/tool11.gif","src/img/tool12.gif"};

	static	Icon tool[]=new ImageIcon[12];
	static	int i,j,show_x,show_y,drawMethod=7,draw_panel_width=700,draw_panel_height=500;
	static Paint color_border,color_inside;
	static SetPanel setPanel;
	static Draw drawPanel;
	static UnderDrawPanel underDrawPanel;
	static ColorPanel colorPanel;
	static Stroke stroke;
	static Shape shape;
	static String isFilled;
	static JButton redo;
	static JButton undo;
	
	public Painter(){
		//设定JMenuBar，并产生MenuItem、并设置快捷键
		JMenuBar bar = new JMenuBar();
		jMenu=new JMenu[menuBar.length];
		for(i=0;i<menuBar.length;i++){
			jMenu[i] = new JMenu(menuBar[i]);
			jMenu[i].setMnemonic(menuBar[i].split("\\(")[1].charAt(0));//为菜单栏各项设置助记键
			
			bar.add(jMenu[i]);
		}
		
		for(i=0;i<menuItem.length;i++){
			for(j=0;j<menuItem[i].length;j++){
				if(i==0 && j==4 || i==1 && j==2) jMenu[i].addSeparator();//为下拉菜单的菜单项间添加分隔符

					jMenuItem[i][j] = new JMenuItem(menuItem[i][j].split("\\|")[0]);//仅提取文字生成菜单项对象
					if(menuItem[i][j].split("\\|").length!=1)
						jMenuItem[i][j].setAccelerator(KeyStroke.getKeyStroke(Integer.parseInt(menuItem[i][j].split("\\|")[1]), 
								ActionEvent.CTRL_MASK) );//设置快捷键
				
					jMenuItem[i][j].addActionListener(this);
					jMenuItem[i][j].setMnemonic(menuItem[i][j].split("\\(")[1].charAt(0));//设置助记键

					jMenu[i].add(jMenuItem[i][j]);
			}
		}
		this.setJMenuBar( bar );
		
		for(i=0;i<5;i++)
			{
			jPanel[i]=new JPanel();
			}
		
		buttonGroup = new ButtonGroup();
		JToolBar jToolBar=new JToolBar("工具箱",JToolBar.VERTICAL);
		jToggleButton=new JToggleButton[ButtonName.length];
		for(i=0;i<ButtonName.length;i++){
			tool[i] = new ImageIcon(toolname[i]);
			jToggleButton[i] = new JToggleButton(tool[i]);
			jToggleButton[i].addActionListener( this );
			jToggleButton[i].setFocusable( false );
			jToggleButton[i].setToolTipText(ButtonName[i]);//设置工具箱名字的TipText
			buttonGroup.add(jToggleButton[i]);
		}
		ImageIcon redoImg = new ImageIcon("src/img/redo.gif");
		ImageIcon undoImg = new ImageIcon("src/img/undo.gif");
		redo = new JButton(redoImg);
		undo = new JButton(undoImg);
		undo.setToolTipText("撤销");
		redo.setToolTipText("重做");
		redo.addActionListener(this);
		undo.addActionListener(this);
		jToolBar.add(undo);
		jToolBar.add(redo);
		jToolBar.add(jToggleButton[7]);
		jToolBar.add(jToggleButton[8]);
		jToolBar.add(jToggleButton[0]);
		jToolBar.add(jToggleButton[4]);
		jToolBar.add(jToggleButton[1]);
		jToolBar.add(jToggleButton[3]);
		jToolBar.add(jToggleButton[2]);
		jToolBar.add(jToggleButton[5]);
		jToolBar.add(jToggleButton[6]);
		jToolBar.add(jToggleButton[9]);
		jToolBar.add(jToggleButton[10]);
		jToolBar.add(jToggleButton[11]);
		jToggleButton[7].setSelected(true);//打开时默认工具为铅笔
		jToolBar.setLayout( new GridLayout( 7, 2, 2, 2 ) );
		jPanel[2].setLayout(new BoxLayout(jPanel[2], BoxLayout.Y_AXIS));
		
		
		jToolBar.setFloatable(false);
		
		colorPanel=new ColorPanel();
		jPanel[3].setLayout(new FlowLayout(FlowLayout.LEFT));
		jPanel[3].add(colorPanel);
		
		jPanel[2].add(jToolBar);
		setPanel=new SetPanel();
		jPanel[2].add(setPanel);
		
		JScrollPane jp=new JScrollPane(jPanel[2]);
		
		drawPanel=new Draw();
		underDrawPanel=new UnderDrawPanel();
		underDrawPanel.setLayout(null);
		underDrawPanel.add(drawPanel);
		drawPanel.setBounds(new Rectangle(2, 2, draw_panel_width, draw_panel_height));
		
		setLayout( new BorderLayout() );
		add(underDrawPanel,BorderLayout.CENTER);
		add(jp,BorderLayout.WEST);
		add(jPanel[3],BorderLayout.SOUTH);
		add(jPanel[4],BorderLayout.EAST);
		
		
		underDrawPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		underDrawPanel.setBackground(new Color(128,128,128));
		jPanel[3].setBorder(BorderFactory.createMatteBorder(1,0,0,0,new Color(172,168,153)));
		
	
		
		setSize(draw_panel_width,draw_panel_height);
		setTitle("静绘");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}




	@SuppressWarnings("deprecation")
	public void save(){
		FileDialog fileDialog = new FileDialog( new Frame() , "请指定一个文件名", FileDialog.SAVE );
		if(fileDialog.getFile()==null) return;
		drawPanel.filename = fileDialog.getDirectory()+fileDialog.getFile();
	}
	
	@SuppressWarnings("deprecation")
	public void actionPerformed( ActionEvent e ){
		for(i=0;i<ButtonName.length;i++){
			if(e.getSource()==jToggleButton[i]){
				drawMethod=i;
				if(drawMethod==5)
					setPanel.pie_add_ctrl();
				else
					setPanel.pie_remove_ctrl();
				if(drawMethod==7 || drawMethod==8)
					setPanel.pencil_add_ctrl();
				else
					setPanel.pencil_remove_ctrl();
				drawPanel.clear();
				drawPanel.repaint();
   				jMenuItem[1][2].setEnabled(false);
   				jMenuItem[1][3].setEnabled(false);
			}
		}
		
		if(e.getSource()==jMenuItem[1][0]||e.getSource()==undo){
			drawPanel.undo();
		}
		else if(e.getSource()==jMenuItem[1][1]||e.getSource()==redo){
			drawPanel.redo();
		}
		else if(e.getSource()==jMenuItem[1][2]){
			drawPanel.cut();
		}
		else if(e.getSource()==jMenuItem[1][3]){
			drawPanel.copy();
		}
		else if(e.getSource()==jMenuItem[1][4]){
			drawPanel.paste();
		}
		else if(e.getSource()==jMenuItem[0][0]){//开新文档
			underDrawPanel.remove(drawPanel);
			drawPanel=null;
			drawPanel=new Draw();
			underDrawPanel.add(drawPanel);
			drawPanel.setBounds(new Rectangle(2, 2, draw_panel_width, draw_panel_height));
			underDrawPanel.ctrl_area.setLocation(draw_panel_width+3,draw_panel_height+3);
			underDrawPanel.ctrl_area2.setLocation(draw_panel_width+3,draw_panel_height/2+3);
			underDrawPanel.ctrl_area3.setLocation(draw_panel_width/2+3,draw_panel_height+3);
			repaint();
		}
		else if(e.getSource()==jMenuItem[0][1]){//开启旧文档
			FileDialog fileDialog = new FileDialog( new Frame() , "选择一个文档", FileDialog.LOAD );
			if(fileDialog.getFile()==null) return;
			
			underDrawPanel.removeAll();
			drawPanel=null;
			drawPanel=new Draw();
			underDrawPanel.add(drawPanel);
			drawPanel.setBounds(new Rectangle(2, 2, draw_panel_width, draw_panel_height));
			
			drawPanel.openfile(fileDialog.getDirectory()+fileDialog.getFile());
		}
		else if(e.getSource()==jMenuItem[0][2]){//存储档案
			if(drawPanel.filename==null){
				save();
			}
			else{
				try{
					int dotpos = drawPanel.filename.lastIndexOf('.');
					ImageIO.write(drawPanel.bufImg, drawPanel.filename.substring(dotpos + 1), new File(drawPanel.filename));
				}
				catch(IOException even) {
					JOptionPane.showMessageDialog(null, even.toString(),"无法存储文档", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		else if(e.getSource()==jMenuItem[0][3]){//另存新档
			save();
			try{
				int dotpos = drawPanel.filename.lastIndexOf('.');
				ImageIO.write(drawPanel.bufImg, drawPanel.filename.substring(dotpos + 1), new File(drawPanel.filename));
			}
			catch(IOException even) {
				JOptionPane.showMessageDialog(null, even.toString(),"无法保存", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(e.getSource()==jMenuItem[0][4]){//离开
			System.exit(0);
		}
		else if(e.getSource()==jMenuItem[2][0]){//关于
			JOptionPane.showMessageDialog(null, "且听风吟 静绘人生", "关于", 1, new ImageIcon("img/paint.gif"));
		}
		
	}
}