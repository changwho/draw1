package java_final_work;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.sql.rowset.JdbcRowSet;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;

public class ColorPanel extends JPanel implements MouseListener,ActionListener{//调色盘class
		private	JPanel jPanel_color0[]=new JPanel[5];
		private	JPanel jPanel_color1[]=new JPanel[32];
		private	JPanel jPanel_color2[]=new JPanel[32];
		private	ImageIcon special_color[]= new ImageIcon[4];
		private BufferedImage bufImg = new BufferedImage(12 ,12,BufferedImage.TYPE_3BYTE_BGR) ,bufImg2 = new BufferedImage(12  
,12,BufferedImage.TYPE_3BYTE_BGR);
		private JLabel jlbImg=new JLabel() ,jlbImg2=new JLabel();
		private	ImageIcon icon;
		private JDialog jDialog;
		private JButton ok, cancel,left,right;
		private Gradient center = new Gradient();
		
		private	int rgb[][]={
			{0,255,128,192,128,255,128,255,  0,  0,  0,  0,  0,  0,128,255,128,255,  0,  0,  0,128,  0,128,128,255,128,255,255,255,255,255},
			{0,255,128,192,  0,  0,128,255,128,255,128,255,  0,  0,  0,  0,128,255, 64,255,128,255, 64,128,  0,  0, 64,128,255,255,255,255},
			{0,255,128,192,  0,  0,  0,  0,  0,  0,128,255,128,255,128,255, 64,128, 64,128,255,255,128,255,255,128,  0, 64,255,255,255,255}
		};
		
		public ColorPanel(){//产生版面//
			addMouseListener( this );
			jlbImg.setIcon(new ImageIcon(bufImg));
			jlbImg2.setIcon(new ImageIcon(bufImg2));

			special_color[0] = new ImageIcon( "src/img/icon1.gif" );
			special_color[1] = new ImageIcon( "src/img/icon2.gif" );
			special_color[2] = new ImageIcon( "src/img/icon3.gif" );
			special_color[3] = new ImageIcon( "src/img/icon4.gif" );
			
			this.setLayout(null);
			Painter.color_border=new Color(0,0,0);
			Painter.color_inside=null;
			
			for(int i=0;i<jPanel_color0.length;i++){
				jPanel_color0[i]=new JPanel();
				if(i<=2){
					jPanel_color0[i].setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
					jPanel_color0[i].setLayout(null);
				}
				else{
					jPanel_color0[i].setBackground(new Color(rgb[0][i-3],rgb[1][i-3],rgb[2][i-3]));
					jPanel_color0[i].setLayout(new GridLayout(1,1));
					jPanel_color0[i-2].add(jPanel_color0[i]);
				}
			}
			for(int i=0;i<jPanel_color2.length;i++){
				jPanel_color2[i]=new JPanel();
				jPanel_color2[i].setLayout(new GridLayout(1,1));
				jPanel_color2[i].setBounds(new Rectangle(2, 2, 12, 12));
				jPanel_color2[i].setBackground(new Color(rgb[0][i],rgb[1][i],rgb[2][i]));
				if(i>=28)
					jPanel_color2[i].add(new JLabel(special_color[i-28]));
			}
			
			for(int i=0;i<jPanel_color1.length;i++){
				jPanel_color1[i]=new JPanel();
				jPanel_color1[i].setLayout(null);
				jPanel_color1[i].add(jPanel_color2[i]);
				this.add(jPanel_color1[i]);
				if(i%2==0){jPanel_color1[i].setBounds(new Rectangle(32+i/2*16, 0, 16, 16));}
				else{jPanel_color1[i].setBounds(new Rectangle(32+i/2*16, 16, 16, 16));}
				jPanel_color1[i].setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
			}
			
			jPanel_color0[3].add(jlbImg);
			jPanel_color0[4].add(jlbImg2);
			
			Graphics2D g2d = bufImg2.createGraphics();
			g2d.setPaint( Color.white );
			g2d.fill( new Rectangle2D.Double( 0, 0, 12, 12 ) );
			g2d.setPaint( Color.red ); 
			g2d.draw( new Line2D.Double( 0, 0, 12, 12 ) );
			g2d.draw( new Line2D.Double( 11, 0, 0, 11 ) );
			repaint();
			
			this.add(jPanel_color0[1]);
			this.add(jPanel_color0[2]);
			this.add(jPanel_color0[0]);

			jPanel_color0[0].setBounds(new Rectangle(0, 0, 32, 32));
			jPanel_color0[1].setBounds(new Rectangle(4, 4, 16, 16));
			jPanel_color0[2].setBounds(new Rectangle(12,12,16, 16));
			jPanel_color0[3].setBounds(new Rectangle(2, 2, 12, 12));
			jPanel_color0[4].setBounds(new Rectangle(2, 2, 12, 12));
			
			jDialog = new JDialog( jDialog, "请选择两种颜色渐变", true);
        	jDialog.getContentPane().setLayout(new FlowLayout());
        	jDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE );
        	jDialog.setSize(250, 110);
        	JPanel temp = new JPanel(new GridLayout(2,1));
        	JPanel up = new JPanel(new FlowLayout());
        	JPanel down = new JPanel(new FlowLayout());
        	
			ok = new JButton("确定");
			cancel = new JButton("取消");
			left = new JButton(" ");
			right = new JButton(" ");
			center.setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
			up.add(left);
			up.add(center);
			up.add(right);
			down.add(ok);
			down.add(cancel);
			temp.add(up);
			temp.add(down);
			jDialog.getContentPane().add(temp);
			
			ok.addActionListener(this);
			cancel.addActionListener(this);
			left.addActionListener(this);
			right.addActionListener(this);
		}
		public void actionPerformed( ActionEvent e ){
			if(e.getSource() == left){
				center.G_color_left = JColorChooser.showDialog(jDialog, "请选择边线颜色", center.G_color_left );
				center.repaint();
			}
			else if(e.getSource() == right){
				center.G_color_right = JColorChooser.showDialog( jDialog, "请选择边线颜色", center.G_color_right );
				center.repaint();
			}
			else{
				jDialog.dispose();
			}
		}
		
		public Dimension getPreferredSize(){
			return new Dimension( 300, 32 );
		}
		public void mouseClicked( MouseEvent e ){}
		public void mousePressed( MouseEvent e ){
			Graphics2D g2d;
			if(e.getX()>=5 && e.getX()<=20 && e.getY()>=5 && e.getY()<=20){
				g2d = bufImg.createGraphics();
				Painter.color_border = JColorChooser.showDialog( jDialog, "请选择边线颜色", (Color)Painter.color_border );
				g2d.setPaint(Painter.color_border);
				g2d.fill( new Rectangle2D.Double( 0, 0, 12, 12 ) );
				repaint();
			}
			else if(e.getX()>=13 && e.getX()<=28 && e.getY()>=13 && e.getY()<=28){
				g2d = bufImg2.createGraphics();
				Painter.color_inside = JColorChooser.showDialog( jDialog, "请选择填充颜色", (Color)Painter.color_inside );
				g2d.setPaint(Painter.color_inside);
				g2d.fill( new Rectangle2D.Double( 0, 0, 12, 12 ) );
				repaint();
			}
			
			if(!(e.getX()>=32 && e.getX()<=288)) return;
			int choose=(e.getX()-32)/16*2+e.getY()/16;
			
			if(e.getButton()==1)//判断填充边框或填满内部
				g2d = bufImg.createGraphics();
			else
				g2d = bufImg2.createGraphics();
			
			if(choose==28){//填充无颜色
				g2d.setPaint( Color.white );
				g2d.fill( new Rectangle2D.Double( 0, 0, 12, 12 ) );
				g2d.setPaint( Color.red ); 
				g2d.draw( new Line2D.Double( 0, 0, 12, 12 ) );
				g2d.draw( new Line2D.Double( 11, 0, 0, 11 ) );
				repaint();
					
				if(e.getButton()==1)
					Painter.color_border=null;
				else
					Painter.color_inside=null;
			}
			else if(choose==29){//填充渐变
				jDialog.show();
				
				g2d.setPaint( new GradientPaint( 0, 0, center.G_color_left, 12, 12, center.G_color_right, true ) );
				g2d.fill( new Rectangle2D.Double( 0, 0, 12, 12 ) );
				repaint();
				
				if(e.getButton()==1)
					Painter.color_border=new GradientPaint( 0, 0, center.G_color_left, 12, 12, center.G_color_right, true );
				else
					Painter.color_inside=new GradientPaint( 0, 0, center.G_color_left, 12, 12, center.G_color_right, true );
			}
			else if(choose==30){//填充图案
				FileDialog fileDialog = new FileDialog( new Frame() , "选择一个图档", FileDialog.LOAD );//利用FileDialog抓取档名
				fileDialog.show();//秀出视窗
				if(fileDialog.getFile()==null) return;//按取消的处理
				
				g2d.drawImage(special_color[2].getImage(), 0, 0,this);//把调色盘左方换成『图片』
				
				icon = new ImageIcon(fileDialog.getDirectory()+fileDialog.getFile());//利用FileDialog传进来的档名读取图片
				BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(),BufferedImage.TYPE_3BYTE_BGR);//创一 张新的BufferedImage，为了要读取读进来的图片长宽，以免有空白
				bufferedImage.createGraphics().drawImage(icon.getImage(),0,0,this);//把icon画到BufferedImage上
				repaint();//重绘屏幕
				
				if(e.getButton()==1)//判断边线颜色或内部填满色
					Painter.color_border=new TexturePaint(bufferedImage, new Rectangle( icon.getIconWidth(), icon.getIconHeight() ) );//把这张BufferedImage设成TexturePaint来填满 

				else
					Painter.color_inside=new TexturePaint(bufferedImage, new Rectangle( icon.getIconWidth(), icon.getIconHeight() ) );
			}
			else if(choose==31){//填充文字
				String text=JOptionPane.showInputDialog("请输入文字","文字");//输入文字
				if(text==null) return;//按取消时的处理
				
				Color FontColor=new Color(0,0,0);//给这个字颜色
				FontColor = JColorChooser.showDialog( jDialog, "请选择一个颜色当文字颜色", FontColor );//请使用者选择颜色
				
				g2d.drawImage(special_color[3].getImage(), 0, 0,this);//把调色盘左方换成『字』
				
				BufferedImage bufferedImage = new BufferedImage(Painter.draw_panel_width,Painter.draw_panel_height,BufferedImage.TYPE_3BYTE_BGR);//创一张新的 
Graphics2D
				g2d_bufferedImage = bufferedImage.createGraphics();//把Graphics拿出来书
				
				FontRenderContext frc = g2d_bufferedImage.getFontRenderContext();//读Graphics中的Font
				Font f = new Font("新细明体",Font.BOLD,10);//新Font
				TextLayout tl = new TextLayout(text, f, frc);//创新的TextLayout，并利用f(Font)跟画至于frc(FontRenderContext)
				
				int sw = (int) (tl.getBounds().getWidth()+tl.getCharacterCount());//计算TextLayout的长
				int sh = (int) (tl.getBounds().getHeight()+3);//计算TextLayout的高
				
				bufferedImage = new BufferedImage(sw,sh,BufferedImage.TYPE_3BYTE_BGR);//再创一张新的BufferedImage，这里利用相同指标指向不同记忆体
				g2d_bufferedImage = bufferedImage.createGraphics();//拿出Graphics来画，前一张BufferedImage只是为了计算文字长度与高度，这样才 能完整填满
				g2d_bufferedImage.setPaint(Color.WHITE);//设定颜色为白色
				g2d_bufferedImage.fill(new Rectangle(0,0,sw,sh));//画一个填满白色矩型
				g2d_bufferedImage.setPaint(FontColor);//设定颜色为之前选择文字颜色
				g2d_bufferedImage.drawString(text,0,10);//画一个String于BufferedImage上
				repaint();//更新画面
				
				if(e.getButton()==1)//判断边线颜色或内部填满色
					Painter.color_border=new TexturePaint(bufferedImage, new Rectangle(sw,sh) );//把这张BufferedImage设成TexturePaint来填满
				else
					Painter.color_inside=new TexturePaint(bufferedImage, new Rectangle(sw,sh) );
			}
			else{//填充一般色
				g2d.setPaint(new Color(rgb[0][choose],rgb[1][choose],rgb[2][choose]));
				g2d.fill( new Rectangle2D.Double( 0, 0, 12, 12 ) );
				repaint();
				
				if(e.getButton()==1)
					Painter.color_border=new Color(rgb[0][choose],rgb[1][choose],rgb[2][choose]);
				else
					Painter.color_inside=new Color(rgb[0][choose],rgb[1][choose],rgb[2][choose]);
			}
		}

		public void mouseReleased( MouseEvent e ){}
		public void mouseEntered( MouseEvent e ){}
		public void mouseExited( MouseEvent e ){}
	}
	