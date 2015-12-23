package java_final_work;
import java.awt.image.BufferedImage;

//扫描种子线算法
public class FloodFillAlgorithm extends AbstractBufferedImageOp {

	private BufferedImage inputImage;
	private int[] inPixels;
	private int width;
	private int height;
	
	// 	stack data structure
	private int maxStackSize = 500; // will be increased as needed
	private int[] xstack = new int[maxStackSize];
	private int[] ystack = new int[maxStackSize];
	private int stackSize;

	public FloodFillAlgorithm(BufferedImage rawImage) {
		this.inputImage = rawImage;
		width = rawImage.getWidth();
        height = rawImage.getHeight();
        inPixels = new int[width*height];
       getRGB( rawImage,0, 0, width, height, inPixels );
	}

	public BufferedImage getInputImage() {
		return inputImage;
	}

	public void setInputImage(BufferedImage inputImage) {
		this.inputImage = inputImage;
	}
	
	public int getColor(int x, int y)
	{
		int index = y * width + x;
		return inPixels[index];
	}
	
	public void setColor(int x, int y, int newColor)
	{
		int index = y * width + x;
		inPixels[index] = newColor;
	}
	
	public void updateResult()
	{
		setRGB( inputImage, 0, 0, width, height, inPixels );
	}
	
	public void floodFillScanLineWithStack(int x, int y, int newColor, int oldColor)
	{
		if(oldColor == newColor) {
			System.out.println("do nothing !!!, filled area!!");
			return;
		}
	    emptyStack();
	    
	    int y1; 
	    boolean spanLeft, spanRight;
	    push(x, y);
	    
	    while(true)
	    {    
	    	x = popx();
	    	if(x == -1) return;
	    	y = popy();
	        y1 = y;
	        while(y1 >= 0 && getColor(x, y1) == oldColor) y1--; // go to line top/bottom
	        y1++; // start from line starting point pixel
	        spanLeft = spanRight = false;
	        while(y1 < height && getColor(x, y1) == oldColor)
	        {
	        	setColor(x, y1, newColor);
	            if(!spanLeft && x > 0 && getColor(x - 1, y1) == oldColor)// just keep left line once in the stack
	            {
	                push(x - 1, y1);
	                spanLeft = true;
	            }
	            else if(spanLeft && x > 0 && getColor(x - 1, y1) != oldColor)
	            {
	                spanLeft = false;
	            }
	            if(!spanRight && x < width - 1 && getColor(x + 1, y1) == oldColor) // just keep right line once in the stack
	            {
	                push(x + 1, y1);
	                spanRight = true;
	            }
	            else if(spanRight && x < width - 1 && getColor(x + 1, y1) != oldColor)
	            {
	                spanRight = false;
	            } 
	            y1++;
	        }
	    }
		
	}
	
	private void emptyStack() {
		while(popx() != - 1) {
			popy();
		}
		stackSize = 0;
	}

	final void push(int x, int y) {
		stackSize++;
		if (stackSize==maxStackSize) {
			int[] newXStack = new int[maxStackSize*2];
			int[] newYStack = new int[maxStackSize*2];
			System.arraycopy(xstack, 0, newXStack, 0, maxStackSize);
			System.arraycopy(ystack, 0, newYStack, 0, maxStackSize);
			xstack = newXStack;
			ystack = newYStack;
			maxStackSize *= 2;
		}
		xstack[stackSize-1] = x;
		ystack[stackSize-1] = y;
	}
	
	final int popx() {
		if (stackSize==0)
			return -1;
		else
            return xstack[stackSize-1];
	}

	final int popy() {
        int value = ystack[stackSize-1];
        stackSize--;
        return value;
	}

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
		// TODO Auto-generated method stub
		return null;
	}

}
