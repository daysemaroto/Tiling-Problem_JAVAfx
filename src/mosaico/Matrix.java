package mosaico;

/**
 * A simple implementation of a matrix based on two dimensional array.
 * 
 * @author yaron
 *
 */
public class Matrix {

	// The offset of the X-axis on the data
	int xOffset;

	// The offset of the Y-axis on the data
	int yOffset;

	// The width of the matrix
	int width;

	// The height of the matrix
	int height;

	// Matrix data
	int[][] data;

	public int getxOffset() {
		return xOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int[][] getData() {
		return data;
	}

	/**
	 * Creates a new matrix
	 * 
	 * @param xOffset
	 *            The X offset of this matrix when accessing the data
	 * @param yOffset
	 *            The Y offset of this matrix when accessing the data
	 * @param width
	 *            The width of the matrix
	 * @param height
	 *            The height of the matrix
	 * @param data
	 *            The matrix data store
	 */
	public Matrix(int xOffset, int yOffset, int width, int height, int[][] data) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.data = data;

		if (this.data == null) {
			this.data = new int[width][height];
		}
	}

	public Matrix(int xOffset, int yOffset, int width, int height) {
		this(xOffset, yOffset, width, height, null);
	}

	/**
	 * Create a new matrix that is a subset of this matrix. The new matrix uses
	 * the same data as the original matrix; operations on either matrix will be
	 * reflected in the other.
	 * 
	 * @param xOffset
	 *            X offset of the sub matrix
	 * @param yOffset
	 *            Y offset of the sub matrix
	 * @param width
	 *            The width of the sub matrix
	 * @param height
	 *            The height of the sub matrix
	 * @return
	 */
	public Matrix subMatrix(int xOffset, int yOffset, int width, int height) {

		// Check that the sub matrix range is valid
		if (xOffset + width > this.width || yOffset + height > this.height) {
			throw new ArrayIndexOutOfBoundsException();
		}

		return new Matrix(this.xOffset + xOffset, this.yOffset + yOffset, width, height, data);
	}

	/**
	 * Sets a specific cell with the specified value.
	 * 
	 * @param x
	 *            The X coordinates of the cell
	 * @param y
	 *            The Y coordinates of the cell
	 * @param value
	 *            The value that will be stored in this cell
	 * @return The previous value that was stored in the specified cell
	 */
	public int set(int x, int y, int value) {
		int oldValue = data[xOffset + x][yOffset + y];

		data[xOffset + x][yOffset + y] = value;

		return oldValue;
	}

	/**
	 * Returns the value stored in a specified cell
	 * 
	 * @param x
	 *            The X coordinates of the cell
	 * @param y
	 *            The Y coordinates of the cell
	 * @return The value that is stored in the specified cell
	 * 
	 */
	public int get(int x, int y) {
		return data[xOffset + x][yOffset + y];
	}

        public int getX(){
            return this.xOffset;
            
        }
        public int getY(){
            return this.yOffset;
            
        }
        
        
	/**
	 * Returns the number of cells that have non-zero value
	 * 
	 * @return the number of cells that have non-zero value
	 */
	public int filled() {

		int count = 0;

		for (int x = xOffset; x < xOffset + width; x++) {
			for (int y = yOffset; y < yOffset + height; y++) {
				if (data[x][y] != 0) {
					count++;
				}
			}
		}

		return count;
	}
}
