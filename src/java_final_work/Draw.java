package java_final_work;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Draw extends JPanel implements MouseListener, MouseMotionListener, ItemListener, ActionListener, ChangeListener{//中央画布
		public BufferedImage bufImg;//记录最新画面，并在此上作画
		private BufferedImage bufImg_data[];//记录所有画出图面，索引值越大越新，最大为最新
		private BufferedImage bufImg_cut;
		private ImageIcon img;
		private JLabel jlbImg;
		private int x1=-1,y1=-1,x2,y2,count,redo_lim,press,temp_x1,temp_y1,temp_x2,temp_y2,temp_x3,temp_y3,step,step_chk,step_arc,step_chk_arc,chk,first,click,cut;
		private Arc2D.Double arc2D = new Arc2D.Double();//扇型
		private Line2D.Double line2D = new Line2D.Double();//直线
		private Ellipse2D.Double ellipse2D = new Ellipse2D.Double();//椭圆
		private Rectangle2D.Double rectangle2D = new Rectangle2D.Double();//矩型
		private CubicCurve2D.Double cubicCurve2D = new CubicCurve2D.Double();//贝氏曲线
		private RoundRectangle2D.Double roundRectangle2D = new RoundRectangle2D.Double();//圆角矩型
		private Polygon polygon;//多边型
		private float data[]={5};
		private Rectangle2D.Double rectangle2D_select = new Rectangle2D.Double();//矩型
		private Ellipse2D.Double ellipse2D_pan = new Ellipse2D.Double();
		private BasicStroke basicStroke_pen = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
		private BasicStroke basicStroke_select = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER,10, data, 0);
		private double center_point_x;
		private double center_point_y;
		private double start;
		private double end;
		public String filename;
		private JTextField textField_font = new JTextField("Fixedsys",16), textField_word = new JTextField("文字",16);
		private int size=100;
		private JSpinner fontsize = new JSpinner();
		private JDialog jDialog;
		private JCheckBox bold, italic;
		private JButton ok, cancel;
		public int pie_shape=Arc2D.PIE;
		private int valBold = Font.BOLD;
		private int valItalic = Font.ITALIC;
		private int select_x,select_y,select_w,select_h;
		
		public void resize(){//改变大小
			bufImg = new BufferedImage(Painter.draw_panel_width, Painter.draw_panel_height,BufferedImage.TYPE_3BYTE_BGR);
			jlbImg = new JLabel(new ImageIcon(bufImg));//在JLabel上放置bufImg，用来绘图
			this.removeAll();
			this.add(jlbImg);
			jlbImg.setBounds(new Rectangle(0, 0, Painter.draw_panel_width, Painter.draw_panel_height));
			
    		//画出原本图形//
    		Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
    		g2d_bufImg.setPaint(Color.white);
    		g2d_bufImg.fill(new Rectangle2D.Double(0,0,Painter.draw_panel_width,Painter.draw_panel_height));
    		g2d_bufImg.drawImage(bufImg_data[count],0,0,this);

			//记录可重做最大次数，并让重做不可按//
			redo_lim=count++;
			Painter.jMenuItem[1][1].setEnabled(false);
			Painter.redo.setEnabled(false);
			
   			//新增一张BufferedImage型态至bufImg_data[count]，并将bufImg绘制至bufImg_data[count]//
   			bufImg_data[count] = new BufferedImage(Painter.draw_panel_width, Painter.draw_panel_height, BufferedImage.TYPE_3BYTE_BGR);
   			Graphics2D g2d_bufImg_data = (Graphics2D) bufImg_data[count].getGraphics();
   			g2d_bufImg_data.drawImage(bufImg,0,0,this);
   			
			//判断坐标为新起点//
			press=0;
			
			//让复原MenuItem可以点选//
   			if(count>0)
   				{
   				Painter.jMenuItem[1][0].setEnabled(true);
   				Painter.undo.setEnabled(true);
   				}
		}
		
		public Draw() {
			bufImg_data = new BufferedImage[1000];
			bufImg = new BufferedImage(Painter.draw_panel_width, Painter.draw_panel_height,BufferedImage.TYPE_3BYTE_BGR);
			jlbImg = new JLabel(new ImageIcon(bufImg));//在JLabel上放置bufImg，用来绘图

			this.setLayout(null);
			this.add(jlbImg);
			jlbImg.setBounds(new Rectangle(0, 0, Painter.draw_panel_width, Painter.draw_panel_height));
			
			Painter.jMenuItem[1][0].setEnabled(false);
			Painter.jMenuItem[1][1].setEnabled(false);
			Painter.undo.setEnabled(false);
			Painter.redo.setEnabled(false);
			Painter.jMenuItem[1][2].setEnabled(false);
			Painter.jMenuItem[1][3].setEnabled(false);
			Painter.jMenuItem[1][4].setEnabled(false);
    		
    		//画出空白//
    		Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
    		g2d_bufImg.setPaint(Color.WHITE);
			g2d_bufImg.fill(new Rectangle2D.Double(0,0,Painter.draw_panel_width,Painter.draw_panel_height));
			
    		bufImg_data[count] = new BufferedImage(Painter.draw_panel_width, Painter.draw_panel_height, BufferedImage.TYPE_3BYTE_BGR);
    		Graphics2D g2d_bufImg_data = (Graphics2D) bufImg_data[count].getGraphics();
    		g2d_bufImg_data.drawImage(bufImg,0,0,this);
			
			//Font//
			jDialog = new JDialog( jDialog, "请选择文字、字型、大小与属性", true);
			fontsize.setValue(new Integer(100));
			bold = new JCheckBox( "粗体" ,true);
			italic = new JCheckBox( "斜体" ,true);
			ok = new JButton("确定");
			cancel = new JButton("取消");
			JPanel temp_0 = new JPanel(new GridLayout(5,1));
			JPanel temp_1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JPanel temp_2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JPanel temp_3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JPanel temp_4 = new JPanel(new FlowLayout());
			JPanel temp_5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			Container jDialog_c = jDialog.getContentPane();
				
        	jDialog_c.setLayout(new FlowLayout());
        	jDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE );
        	jDialog.setSize(250, 200);
        	temp_5.add(new JLabel("文字:"));
			temp_5.add(textField_word);
			temp_1.add(new JLabel("字体:"));
			temp_1.add(textField_font);
			temp_2.add(new JLabel("大小:"));
			temp_2.add(fontsize);
			temp_3.add(new JLabel("属性:"));
			temp_3.add(bold);
			temp_3.add(italic);
			temp_4.add(ok);
			temp_4.add(cancel);
			temp_0.add(temp_5);
			temp_0.add(temp_1);
        	temp_0.add(temp_2);
        	temp_0.add(temp_3);
        	temp_0.add(temp_4);
        	jDialog_c.add(temp_0);
        	
        	bold.addItemListener( this );
        	italic.addItemListener( this );
        	fontsize.addChangeListener( this );
        	ok.addActionListener(this);
        	cancel.addActionListener(this);
        	temp_0.setPreferredSize(new Dimension( 180 , 150 ));
        	
			repaint();
    		addMouseListener(this);
    		addMouseMotionListener(this);
		}
		public void stateChanged(ChangeEvent e){
			size = Integer.parseInt(fontsize.getValue().toString());
			if(size <= 0) {
				fontsize.setValue(new Integer(1));
				size = 1;
			}
		}
		
		public void actionPerformed( ActionEvent e ){
			jDialog.dispose();
		}
		public void itemStateChanged( ItemEvent e ){
			if ( e.getSource() == bold )
				if ( e.getStateChange() == ItemEvent.SELECTED )
					valBold = Font.BOLD;
				else
					valBold = Font.PLAIN;
			if ( e.getSource() == italic )
				if ( e.getStateChange() == ItemEvent.SELECTED )
					valItalic = Font.ITALIC;
				else
					valItalic = Font.PLAIN;
		}
		
		public Dimension getPreferredSize(){
			return new Dimension( Painter.draw_panel_width,Painter.draw_panel_height );
		}
		
		public void openfile(String filename){//开启旧档
			Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
			ImageIcon icon = new ImageIcon(filename);
			g2d_bufImg.drawImage(icon.getImage(),0,0,this);
			
			count++;
    		bufImg_data[count] = new BufferedImage(Painter.draw_panel_width,Painter.draw_panel_height, BufferedImage.TYPE_3BYTE_BGR);
    		Graphics2D g2d_bufImg_data = (Graphics2D) bufImg_data[count].getGraphics();
    		g2d_bufImg_data.drawImage(bufImg,0,0,this);
			
			repaint();
		}
		
		public void undo(){//复原
   			count--;
			
   			Painter.draw_panel_width=bufImg_data[count].getWidth();
   			Painter.draw_panel_height=bufImg_data[count].getHeight();
   			Painter.drawPanel.setSize(Painter.draw_panel_width,Painter.draw_panel_height);

			bufImg = new BufferedImage(Painter.draw_panel_width, Painter.draw_panel_height,BufferedImage.TYPE_3BYTE_BGR);
			jlbImg = new JLabel(new ImageIcon(bufImg));//在JLabel上放置bufImg，用来绘图
			this.removeAll();
			this.add(jlbImg);
			jlbImg.setBounds(new Rectangle(0, 0, Painter.draw_panel_width, Painter.draw_panel_height));
			
			Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
    		g2d_bufImg.setPaint(Color.white);
    		g2d_bufImg.fill(new Rectangle2D.Double(0,0,Painter.draw_panel_width,Painter.draw_panel_height));
			g2d_bufImg.drawImage(bufImg_data[count],0,0,this);

			Painter.underDrawPanel.ctrl_area.setLocation(Painter.draw_panel_width+3,Painter.draw_panel_height+3);
			Painter.underDrawPanel.ctrl_area2.setLocation(Painter.draw_panel_width+3,Painter.draw_panel_height/2+3);
			Painter.underDrawPanel.ctrl_area3.setLocation(Painter.draw_panel_width/2+3,Painter.draw_panel_height+3);
			
			Painter.underDrawPanel.x=Painter.draw_panel_width;
			Painter.underDrawPanel.y=Painter.draw_panel_height;
			
	   		if(count<=0)
	   			{
	   			Painter.jMenuItem[1][0].setEnabled(false);
	   			Painter.undo.setEnabled(false);
	   			}
	   		Painter.jMenuItem[1][1].setEnabled(true);
	   		Painter.redo.setEnabled(true);
	    	cut=3;
   			repaint();
   		}

		public void redo(){//重做
			count++;
			
			Painter.draw_panel_width=bufImg_data[count].getWidth();
			Painter.draw_panel_height=bufImg_data[count].getHeight();
			Painter.drawPanel.setSize(Painter.draw_panel_width,Painter.draw_panel_height);

			bufImg = new BufferedImage(Painter.draw_panel_width, Painter.draw_panel_height,BufferedImage.TYPE_3BYTE_BGR);
			jlbImg = new JLabel(new ImageIcon(bufImg));//在JLabel上放置bufImg，用来绘图
			this.removeAll();
			this.add(jlbImg);
			jlbImg.setBounds(new Rectangle(0, 0, Painter.draw_panel_width,Painter. draw_panel_height));
			
			Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
    		g2d_bufImg.setPaint(Color.white);
    		g2d_bufImg.fill(new Rectangle2D.Double(0,0,Painter.draw_panel_width,Painter.draw_panel_height));
			g2d_bufImg.drawImage(bufImg_data[count],0,0,this);

			Painter.underDrawPanel.ctrl_area.setLocation(Painter.draw_panel_width+3,Painter.draw_panel_height+3);
			Painter.underDrawPanel.ctrl_area2.setLocation(Painter.draw_panel_width+3,Painter.draw_panel_height/2+3);
			Painter.underDrawPanel.ctrl_area3.setLocation(Painter.draw_panel_width/2+3,Painter.draw_panel_height+3);
			
			Painter.underDrawPanel.x=Painter.draw_panel_width;
			Painter.underDrawPanel.y=Painter.draw_panel_height;
			
			if(redo_lim<count)
				{
				Painter.jMenuItem[1][1].setEnabled(false);
				Painter.redo.setEnabled(false);
				}
			Painter.jMenuItem[1][0].setEnabled(true);
			Painter.undo.setEnabled(true);
			cut=3;
   			repaint();
   		}
		
		public void cut(){
			bufImg_cut = new BufferedImage((int)rectangle2D_select.getWidth(), (int)rectangle2D_select.getHeight(),  
BufferedImage.TYPE_3BYTE_BGR);
			BufferedImage copy = bufImg.getSubimage((int)rectangle2D_select.getX(),(int)rectangle2D_select.getY(),(int) 
rectangle2D_select.getWidth(),(int)rectangle2D_select.getHeight());
			Graphics2D g2d_bufImg_cut = (Graphics2D) bufImg_cut.createGraphics();
			g2d_bufImg_cut.drawImage(copy,0,0,this);
			
    		Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
    		g2d_bufImg.setPaint(Color.WHITE);
			g2d_bufImg.fill(new Rectangle2D.Double((int)rectangle2D_select.getX(),(int)rectangle2D_select.getY(),(int) 
rectangle2D_select.getWidth(),(int)rectangle2D_select.getHeight()));
			
			redo_lim=count++;
			Painter.jMenuItem[1][1].setEnabled(false);
			Painter.redo.setEnabled(false);
			
   			//新增一张BufferedImage型态至bufImg_data[count]，并将bufImg绘制至bufImg_data[count]//
   			bufImg_data[count] = new BufferedImage(Painter.draw_panel_width, Painter.draw_panel_height, BufferedImage.TYPE_3BYTE_BGR);
   			Graphics2D g2d_bufImg_data = (Graphics2D) bufImg_data[count].getGraphics();
   			g2d_bufImg_data.drawImage(bufImg,0,0,this);

			//判断坐标为新起点//
			press=0;
			
			//让复原MenuItem可以点选//
   			if(count>0)
   				Painter.jMenuItem[1][0].setEnabled(true);
   			Painter.jMenuItem[1][2].setEnabled(false);
   			Painter.jMenuItem[1][3].setEnabled(false);
   			Painter.jMenuItem[1][4].setEnabled(true);
			cut=3;
			repaint();
		}
		public void copy(){
			bufImg_cut = new BufferedImage((int)rectangle2D_select.getWidth(), (int)rectangle2D_select.getHeight(),  
BufferedImage.TYPE_3BYTE_BGR);
			BufferedImage copy = bufImg.getSubimage((int)rectangle2D_select.getX(),(int)rectangle2D_select.getY(),(int) 
rectangle2D_select.getWidth(),(int)rectangle2D_select.getHeight());
			Graphics2D g2d_bufImg_cut = (Graphics2D) bufImg_cut.createGraphics();
			g2d_bufImg_cut.drawImage(copy,0,0,this);
			Painter.jMenuItem[1][4].setEnabled(true);
			cut=1;
			repaint();
		}
		public void paste(){
			cut=2;
			repaint();
		}
		
    	public void mousePressed(MouseEvent e) {
    		x1=e.getX();
    		y1=e.getY();
    		if(first==0){
    			polygon = new Polygon();
				polygon.addPoint(x1, y1);
				first=1;
			}
			//判断坐标为新起点//
			press=1;
			chk=0;
			if(cut!=2) cut=0;
			if (Painter.drawMethod==11)
			{
				FloodFillAlgorithm ffa;
				ffa = new FloodFillAlgorithm(bufImg);
				ffa.floodFillScanLineWithStack(x1, y1, Color.cyan.getRGB(), ffa.getColor(x1, y1)); 
				ffa.updateResult();
				repaint();
			}
		}

    	public void mouseReleased(MouseEvent e) {
    		x2=e.getX();
    		y2=e.getY();
    		
    		if(step_chk==0)//控制贝氏曲线用
    			step=1;
    		else if(step_chk==1)
    			step=2;
    		
    		if(step_chk_arc==0)//控制扇型用
    			chk=step_arc=1;
    		else if(step_chk_arc==1)
    			chk=step_arc=2;
			
			if(Painter.drawMethod==6 && click!=1){
				polygon.addPoint(x2, y2);
				repaint();
			}
			if(Painter.drawMethod==10){
				if(cut!=2) cut=1;
				select_x=(int)rectangle2D_select.getX();
				select_y=(int)rectangle2D_select.getY();
				select_w=(int)rectangle2D_select.getWidth();
				select_h=(int)rectangle2D_select.getHeight();
				Painter.jMenuItem[1][2].setEnabled(true);
				Painter.jMenuItem[1][3].setEnabled(true);
			}

    		if((step_chk==2 && step==2) || (step_chk_arc==2 && step_arc==2) || Painter.drawMethod==0 || Painter.drawMethod==1 || Painter.drawMethod==2 || Painter.drawMethod==3 ||  
    				Painter.drawMethod==7 || Painter.drawMethod==8 || Painter.drawMethod==9 || cut==2){//当不是画贝氏曲线或是已经完成贝氏曲线时画
				toDraw();
    		}
		}
		public void clear(){
			cut=select_x=select_y=select_w=select_h=step_chk_arc=step_arc=first=step_chk=step=0;
			x1=x2=y1=y2=-1;
		}
		
		public void toDraw(){
			if(x1<0 || y1<0) return;//防止误按
			chk=3;
			draw(x1,y1,x2,y2);
			
			//画出图形至bufImg//
			Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
			if(cut!=2){
				if(Painter.color_inside!=null && Painter.drawMethod!=8){
					g2d_bufImg.setPaint(Painter.color_inside);
					g2d_bufImg.fill(Painter.shape);
				}
				if(Painter.color_border!=null && Painter.drawMethod!=8){
					g2d_bufImg.setPaint(Painter.color_border);
					g2d_bufImg.setStroke(Painter.stroke);
					g2d_bufImg.draw(Painter.shape);
				}
			}
			else{
   				g2d_bufImg.drawImage(bufImg_cut,x2,y2,this);
			}
			repaint();
			clear();
			//记录可重做最大次数，并让重做不可按//
			redo_lim=count++;
			Painter.jMenuItem[1][1].setEnabled(false);
			Painter.redo.setEnabled(false);
			
   			//新增一张BufferedImage型态至bufImg_data[count]，并将bufImg绘制至bufImg_data[count]//
   			bufImg_data[count] = new BufferedImage(Painter.draw_panel_width, Painter.draw_panel_height, BufferedImage.TYPE_3BYTE_BGR);
   			Graphics2D g2d_bufImg_data = (Graphics2D) bufImg_data[count].getGraphics();
   			g2d_bufImg_data.drawImage(bufImg,0,0,this);
   			
			//判断坐标为新起点//
			press=0;
			
			//让复原MenuItem可以点选//
   			if(count>0)
   				{
   				Painter.jMenuItem[1][0].setEnabled(true);
   				
   				Painter.undo.setEnabled(true);
   				}
		}
		
    	public void mouseEntered(MouseEvent e){}
    	public void mouseExited(MouseEvent e){}
    	public void mouseClicked(MouseEvent e){
    		if(click==1){//连点两下时
    			toDraw();
    		}
    		click=1;
//    		x1=e.getX();
//			y1=e.getY();
//			FloodFillAlgorithm ffa;
//			ffa = new FloodFillAlgorithm(bufImg);
//			ffa.floodFillScanLineWithStack(x1, y1, Color.GREEN.getRGB(), ffa.getColor(x1, y1)); 
//			ffa.updateResult();
//			repaint();
    		
    	}
    	
    	public void mouseDragged(MouseEvent e){
    		x2=e.getX();
    		y2=e.getY();
    		if(Painter.drawMethod==7 || Painter.drawMethod==8){
				draw(x1,y1,x2,y2);
				x1=e.getX();
				y1=e.getY();
			}
			if(Painter.drawMethod!=9)
    			repaint();
    	}

    	public void mouseMoved(MouseEvent e) {
    		Painter.show_x=x2=e.getX();
    		Painter.show_y=y2=e.getY();
			click=0;
			if(Painter.drawMethod==7 || Painter.drawMethod==8 || cut==2)
				repaint();
    	}
		
		public void draw(int input_x1,int input_y1,int input_x2,int input_y2){
			if(Painter.drawMethod==0){//直线时，让shape为Line2D
				Painter.shape=line2D;
				line2D.setLine(input_x1,input_y1,input_x2,input_y2);
			}
			else if(Painter.drawMethod==1){//矩型时，让shape为Rectangle2D
				Painter.shape=rectangle2D;
				rectangle2D.setRect(Math.min(input_x1,input_x2),Math.min(input_y1,input_y2),Math.abs(input_x1-input_x2),Math.abs(input_y1- 
input_y2));
			}
			else if(Painter.drawMethod==2){//椭圆时
				Painter.shape=ellipse2D;
				ellipse2D.setFrame(Math.min(input_x1,input_x2),Math.min(input_y1,input_y2),Math.abs(input_x1-input_x2),Math.abs(input_y1- 
input_y2));
			}
			else if(Painter.drawMethod==3){//圆角矩型
				Painter.shape=roundRectangle2D;
				roundRectangle2D.setRoundRect(Math.min(input_x1,input_x2),Math.min(input_y1,input_y2),Math.abs(input_x1-input_x2),Math.abs 
(input_y1-input_y2),10.0f,10.0f);
			}
			else if(Painter.drawMethod==4){//贝氏曲线
				Painter.shape=cubicCurve2D;
				if(step==0){
					cubicCurve2D.setCurve(input_x1,input_y1,input_x1,input_y1,input_x2,input_y2,input_x2,input_y2);
					temp_x1=input_x1;
					temp_y1=input_y1;
					temp_x2=input_x2;
					temp_y2=input_y2;
					step_chk=0;
				}
				else if(step==1){
					cubicCurve2D.setCurve(temp_x1,temp_y1,input_x2,input_y2,input_x2,input_y2,temp_x2,temp_y2);
					temp_x3=input_x2;
					temp_y3=input_y2;
					step_chk=1;
				}
				else if(step==2){
					cubicCurve2D.setCurve(temp_x1,temp_y1,temp_x3,temp_y3,input_x2,input_y2,temp_x2,temp_y2);
					step_chk=2;
				}
			}
			else if(Painter.drawMethod==5){//扇型，chk用来防止意外的repaint//
				if(step_arc==0 || chk==1){//步骤控制
					Painter.shape=ellipse2D;
					ellipse2D.setFrame(Math.min(input_x1,input_x2),Math.min(input_y1,input_y2),Math.abs(input_x1-input_x2),Math.abs 
(input_y1-input_y2));
					temp_x1=input_x1;
					temp_y1=input_y1;
					temp_x2=input_x2;
					temp_y2=input_y2;
					step_chk_arc=0;
				}
				else if(step_arc==1 || chk==2){//步骤控制
					Painter.shape=arc2D;

					center_point_x = Math.min(temp_x1,temp_x2)+Math.abs(temp_x1-temp_x2)/2;
					center_point_y = Math.min(temp_y1,temp_y2)+Math.abs(temp_y1-temp_y2)/2;
					
					double a = Math.pow(Math.pow(input_x2-center_point_x,2)+Math.pow(input_y2-center_point_y,2),0.5);
					double b = input_x2-center_point_x;
					if(input_y2>center_point_y)
						start=360+Math.acos(b/a)/Math.PI*-180;
					else
						start=Math.acos(b/a)/Math.PI*180;
					
					arc2D.setArc(Math.min(temp_x1,temp_x2),Math.min(temp_y1,temp_y2),Math.abs(temp_x1-temp_x2),Math.abs(temp_y1- 
temp_y2),start,0,pie_shape);
					step_chk_arc=1;
				}
				else if(step_arc==2 || chk==3){//步骤控制
					Painter.shape=arc2D;
					
					double a = Math.pow(Math.pow(input_x2-center_point_x,2)+Math.pow(input_y2-center_point_y,2),0.5);
					double b = input_x2-center_point_x;
					if(input_y2>center_point_y)
						end=360+Math.acos(b/a)/Math.PI*-180-start;
					else
						end=Math.acos(b/a)/Math.PI*180-start;
					if(end<0){end=360-Math.abs(end);}
					
					arc2D.setArc(Math.min(temp_x1,temp_x2),Math.min(temp_y1,temp_y2),Math.abs(temp_x1-temp_x2),Math.abs(temp_y1- 
temp_y2),start,end,pie_shape);
					step_chk_arc=2;
				}
			}
			else if(Painter.drawMethod==6){//多边型
				Painter.shape=polygon;
			}
			else if(Painter.drawMethod==7 || Painter.drawMethod==8){//任意线＆橡皮擦
    			Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
    			
    			Painter.shape=line2D;
				line2D.setLine(input_x1,input_y1,input_x2,input_y2);
				if(Painter.drawMethod==7)
					g2d_bufImg.setPaint(Painter.color_border);
				else
					g2d_bufImg.setPaint(Color.white);
				g2d_bufImg.setStroke(Painter.stroke);
				g2d_bufImg.draw(Painter.shape);
			}
			
			else if(Painter.drawMethod==9){//文字
				Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
        		FontRenderContext frc = g2d_bufImg.getFontRenderContext();
        		Font f = new Font(textField_font.getText(),valBold + valItalic,size);
        		TextLayout tl = new TextLayout(textField_word.getText(), f, frc);
        		//double sw = tl.getBounds().getWidth();
        		double sh = tl.getBounds().getHeight();

        		AffineTransform Tx = AffineTransform.getScaleInstance(1, 1);
        		Tx.translate(input_x2,input_y2+sh);
        		Painter.shape = tl.getOutline(Tx);
			}
			else if(Painter.drawMethod==10){//选取工具
				Painter.shape=rectangle2D;
				rectangle2D.setRect(Math.min(input_x1,input_x2),Math.min(input_y1,input_y2),Math.abs(input_x1-input_x2),Math.abs(input_y1- 
input_y2));
			}
			if(Painter.color_border instanceof GradientPaint){//使用渐层填色读取拖拉坐标
				Painter.color_border = new GradientPaint( input_x1,input_y1, (Color)((GradientPaint)Painter.color_border).getColor1(), input_x2,input_y2,  
(Color)((GradientPaint)Painter.color_border).getColor2(), true );
			}
			if(Painter.color_inside instanceof GradientPaint){
				Painter.color_inside = new GradientPaint( input_x1,input_y1, (Color)((GradientPaint)Painter.color_inside).getColor1(), input_x2,input_y2,  
(Color)((GradientPaint)Painter.color_inside).getColor2(), true );
			}
		}
		
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			super.paint(g2d);//重绘底层JPanel以及上面所有组件

			if(press==1 && Painter.drawMethod!=10 && !(x1<0 || y1<0)) {//绘图在最上面的JLabel上，并判断是不是起点才画
				draw(x1,y1,x2,y2);
				if(Painter.drawMethod==8) return;
				if(Painter.color_inside!=null){
					g2d.setPaint(Painter.color_inside);
					g2d.fill(Painter.shape);
				}
				if(Painter.color_border!=null){
					g2d.setPaint(Painter.color_border);
					g2d.setStroke(Painter.stroke);
					g2d.draw(Painter.shape);
				}
			}

			if(Painter.drawMethod==10 && cut==0){//选取控制、判断是否选取、剪下、或贴上
				g2d.setPaint(Color.black);
				g2d.setStroke(basicStroke_select);
				rectangle2D_select.setRect(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2));
				g2d.draw(rectangle2D_select);
			}
			if(cut==1){
				g2d.setPaint(Color.black);
				g2d.setStroke(basicStroke_select);
				rectangle2D_select.setRect(select_x,select_y,select_w,select_h);
				g2d.draw(rectangle2D_select);
			}
			if(cut==2){
   				g2d.drawImage(bufImg_cut,x2,y2,this);
   			}

			//跟随游标的圆形//
			if(Painter.drawMethod==7 || Painter.drawMethod==8){
				g2d.setPaint(Color.black);
				g2d.setStroke(basicStroke_pen);
				ellipse2D_pan.setFrame(x2-Painter.setPanel.number/2,y2-Painter.setPanel.number/2,Painter.setPanel.number,Painter.setPanel.number);
				g2d.draw(ellipse2D_pan);
			}
		}
	

	public static void main( String args[] ){
		try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch(Exception e){e.printStackTrace();}
		
		Painter app = new Painter();
		app.setVisible(true);
		app.setExtendedState(Frame.MAXIMIZED_BOTH);
	}
}