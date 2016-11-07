package com.prova.nonogram;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class GameDisplay extends SurfaceView {
    private Context context;
    private int maxModelX, maxModelY;
    private float ratio;
    private SurfaceHolder holder;
    private LoopJoc jocLoopThread;

    // Fancy screen
    private double s_x;
    private double s_y;
    private double f_x;
    private double f_y;
    private double delta_x;
    private double delta_y;
    private int n_cols;
    private int n; //Number of cells in the x axe.
    private int m; //Number of cells in the y axe.
    private double cellSizeX;
    private double cellSizeY;


    private double lastPressedTime;
    private int lastPressedX;
    private int lastPressedY;
    private ImageHandler[] buttons;

    private Grid grid;
    private Grid gameGrid;
    private TopLeftIndicators tli;
    private TopLeftIndicators gameTli;
    private int presentCol;
    private Paint paintErr, gray, indicatorPainter;

    private double delays[][];
    private double maxPreassureTime = 100;

    /**
     * GameDisplay constructor.
     * @param cont Context of the application
     * @param g Grid of the game
     * @param t TopLeftIndicator of the game
     */
    public GameDisplay(Context cont, Grid g, TopLeftIndicators t) {
        super(cont);
        this.context = cont;
        this.gameTli = new TopLeftIndicators(t);
        this.gameGrid = g;
        jocLoopThread = new LoopJoc(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            public void surfaceCreated(SurfaceHolder arg0) {
                initiateGame();
                partir();
                jocLoopThread.start();
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            public void surfaceDestroyed(SurfaceHolder arg0) {
                aturar();
            }
        });
    }

    /**
     * This method defines the size of the Grid.
     */
    public void sizeCalculus() {
        maxModelX = 1080;
        maxModelY = 1583;
        s_x = 0.05 * maxModelX;
        s_y = 0.2 * maxModelX;
        f_x = 0.95 * maxModelX;
        f_y = 0.8 * maxModelY;
        delta_x = f_x - s_x;
        delta_y = f_y - s_y;
        n_cols = grid.getN_cols();
        n = grid.getCols() + grid.getN_cols();
        m = grid.getRows() + grid.getN_cols();
        cellSizeX = delta_x / n;
        cellSizeY = delta_y / m;
    }

    private void initiateTLI() {
        for (int x = 0; x < tli.getLefIndCirc().length; x++) {
            for (int y = 0; y < tli.getLefIndCirc()[0].length; y++) {
                tli.getLefIndCirc()[x][y] = false;
            }
        }
        for (int x = 0; x < tli.getTopIndCirc().length; x++) {
            for (int y = 0; y < tli.getTopIndCirc()[0].length; y++) {
                tli.getTopIndCirc()[x][y] = false;
            }
        }

    }


    /**
     * This method initialises the colors used in to draw the grid.
     */
    public void initiateColors() {
        gray = new Paint();
        gray.setColor(0xaaadadad);
        gray.setStyle(Paint.Style.FILL);
        paintErr = new Paint();
        paintErr.setColor(Color.RED);
        paintErr.setStyle(Paint.Style.FILL);
        indicatorPainter = new Paint();
        indicatorPainter.setStyle(Paint.Style.STROKE);
        indicatorPainter.setStrokeWidth(4);
    }

    /**
     * This method initiates all the variables required to play the game.
     */
    public void initiateGame() {
        this.grid = new Grid(gameGrid);// gameGrid;
        this.tli = new TopLeftIndicators(gameTli);

        delays = new double[grid.getArray_cols().length][grid.getArray_cols()[0].length];

        presentCol = 0;
        iniciateButton();
        sizeCalculus();
        initiateColors();
        initiateTLI();
    }


    /**
     * This method paints the game on screen.
     * @param canvas Canvas where it paints.
     */
    protected void drawGame(Canvas canvas) {
        //float densitat = context.getResources().getDisplayMetrics().scaledDensity;

        if (canvas == null) return;

        // Quin és el ratio de conversió per la resolució d'aquesta pantalla ?
        float ratio1 = maxModelX / (float) this.getWidth();
        float ratio2 = maxModelY / (float) this.getHeight();
        if (ratio1 > ratio2) ratio = 1 / ratio1;
        else ratio = 1 / ratio2;


        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, paint);
        try {
            // Draw Content
            drawGrid(canvas);
            drawGridContent(canvas);
            drawLeftIndicator(canvas);
            drawTopIndicator(canvas);
            drawButton(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //drawNumbers(canvas);
    }

    /**
     * Listener for touch events.
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {

            }
            case MotionEvent.ACTION_MOVE: {
                int x = fromScreen((int) event.getX());
                int y = fromScreen((int) event.getY());
                if (x > s_x && x < f_x && y > s_y && y < f_y) {
                    int p = (int) ((x - s_x) / cellSizeX);
                    int q = (int) ((y - s_y) / cellSizeY);
                    touchOnGrid(p, q);
                    if (gameWon()) {
                        Toast toast = Toast.makeText(context, "Game won", Toast.LENGTH_LONG);
                        toast.show();
                    }

                    return true;
                } else {
                    x = (int) event.getX();
                    y = (int) event.getY();
                    for (int i = 0; i < buttons.length; i++) {
                        if (buttons[i].contePunt(x, y)) {
                            presentCol = i - 1;
                        }
                    }

                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                lastPressedX = -1;
                lastPressedY = -1;
                break;
            }

        }
        return super.onTouchEvent(event);
    }

    /**
     * This method handles touches on the game grid.
     * @param p Column pressed.
     * @param q Row pressed.
     */
    public void touchOnGrid(int p, int q) {

        if (p >= n_cols && q >= n_cols) {
            int col = p - n_cols;
            int row = q - n_cols;
            if (col == lastPressedX && row == lastPressedY) {
                return;
            }
            eraseGrillCell(col, row);
            if (presentCol != -1) {
                setGrillCell(col, row);
            }
            isCircleError(col, row);
        }
    }

    /**
     * This method deletes the content of a cell.
     * @param col Column of the cell to delete.
     * @param row Row of the cell to delete.
     */
    public void eraseGrillCell(int col, int row) {
        Col c = grid.getArray_cols()[col][row];
        if (c.getF() != Form.EMPTY) {
            int index = grid.getColIndex(c);
            if (index > -1) {
                grid.eraseGridCell(col, row);
                tli.getTopInd()[col][index]++;
                tli.getLeftInd()[row][index]++;
            }
        }
    }

    /**
     * This methods sets the content of a cell with the present color.
     * @param col Column of the cell to set.
     * @param row Row of the cell to set.
     */
    public void setGrillCell(int col, int row) {
        delays[col][row] = System.currentTimeMillis();
        lastPressedX = col;
        lastPressedY = row;
        grid.setCol(col, row, grid.getColors()[presentCol]);
        tli.getLeftInd()[row][presentCol]--;
        tli.getTopInd()[col][presentCol]--;
    }

    /**
     * This method paints the game grid.
     * @param canvas Canvas where it paints the game grid.
     */
    public void drawGridContent(Canvas canvas) {
        for (int j = n_cols; j < m; j++) {
            for (int i = n_cols; i < n; i++) {
                if (lastPressedY == j - n_cols && lastPressedX == i - n_cols) {
                    drawLastPressedCol(i, j, canvas);
                } else {
                    drawPressedCol(i, j, canvas);
                }
                /*if (j == lastPressedY+n_cols && i ==lastPressedX+n_cols){

                }else{
                    grid.getArray_cols()[i - n_cols][j - n_cols].drawColOnCanvas(canvas,
                            toScreen((int) ((i) * cellSizeX + s_x)), toScreen((int) ((j) * cellSizeY + s_y)),
                            toScreen((int) (cellSizeX)), toScreen((int) (cellSizeY)),
                            5);
                }*/
            }
        }
    }

    /**
     * This method paints all set cells.
     * @param i
     * @param j
     * @param canvas
     */
    public void drawPressedCol(int i, int j, Canvas canvas) {
        double relative = System.currentTimeMillis() - delays[i - n_cols][j - n_cols];
        double factor = 1;
        if (relative < 2 * maxPreassureTime) {
            factor = 3 - relative / maxPreassureTime;
            if (relative < 1.5 * maxPreassureTime)
                factor = relative / maxPreassureTime;
        }
        grid.getArray_cols()[i - n_cols][j - n_cols].drawColOnCanvas(canvas,
                toScreen((int) ((i + (1 - factor) / 2) * cellSizeX + s_x)), toScreen((int) ((j + (1 - factor) / 2) * cellSizeY + s_y)),
                toScreen((int) (cellSizeX * factor)), toScreen((int) (cellSizeY * factor)),
                5);
    }

    /**
     * This method paints the last set cell.
     * @param i
     * @param j
     * @param canvas
     */
    public void drawLastPressedCol(int i, int j, Canvas canvas) {
        double relative = System.currentTimeMillis() - delays[i - n_cols][j - n_cols];
        double factor = 1.5;
        if (relative < 1.5 * maxPreassureTime)
            factor = relative / maxPreassureTime;
        grid.getArray_cols()[i - n_cols][j - n_cols].drawColOnCanvas(canvas,
                toScreen((int) ((i + (1 - factor) / 2) * cellSizeX + s_x)), toScreen((int) ((j + (1 - factor) / 2) * cellSizeY + s_y)),
                toScreen((int) (cellSizeX * factor)), toScreen((int) (cellSizeY * factor)),
                5);
    }

    /**
     * This method paints the lines of the grid.
     * @param canvas
     */
    public void drawGrid(Canvas canvas) {
        int nColumn = grid.getCols();
        int nRow = grid.getRows();
        int nCol = grid.getN_cols();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        double cellSizeX = (f_x - s_x) / (nCol + nColumn);
        double cellSizeY = (f_y - s_y) / (nCol + nRow);
        //Paint vertical lines
        for (int i = 1; i < nCol + nColumn + 1; i++) {
            if (i < nCol) {
                canvas.drawLine(toScreen((int) (s_x + i * cellSizeX)), toScreen((int) (s_y + cellSizeY * nCol)), toScreen((int) (s_x + i * cellSizeX)), toScreen((int) (f_y)), paint);
            } else {
                if ((i + nCol + nColumn) % 2 == 1) {
                    canvas.drawRect(toScreen((int) (s_x + i * cellSizeX)), toScreen((int) (s_y)), toScreen((int) (s_x + (1 + i) * cellSizeX)), toScreen((int) (f_y)), gray);
                }
                canvas.drawLine(toScreen((int) (s_x + i * cellSizeX)), toScreen((int) (s_y)), toScreen((int) (s_x + i * cellSizeX)), toScreen((int) (f_y)), paint);
            }
        }
        //Paint horizontal lines
        for (int i = 1; i < nRow + nCol + 1; i++) {
            if (i < nCol) {
                canvas.drawLine(toScreen((int) (s_x + cellSizeX * nCol)), toScreen((int) (s_y + i * cellSizeY)), toScreen((int) (f_x)), toScreen((int) (s_y + i * cellSizeY)), paint);
            } else {
                if ((i + nRow + nCol) % 2 == 1 ) {
                    canvas.drawRect(toScreen((int) (s_x)), toScreen((int) (s_y + i * cellSizeY)), toScreen((int) (f_x)), toScreen((int) (s_y + (1 + i) * cellSizeY)), gray);
                }
                //canvas.drawLine(toScreen((int) (s_x)), toScreen((int)(s_y+i*cellSizeY)), toScreen((int) (f_x)), toScreen((int) (s_y+i*cellSizeY)), paint);
            }
        }
    }


    /**
     * This method paints the left hints.
     * @param canvas
     */
    public void drawLeftIndicator(Canvas canvas) {
        int[][] leftInd = tli.getLeftInd();
        boolean[][] leftIndCirc = gameTli.getLefIndCirc();
        //Text Size
        int textSize;
        if (cellSizeX < cellSizeY) {
            textSize = (int) cellSizeX / 2;
        } else {
            textSize = (int) cellSizeY / 2;
        }
        int x, y;
        //Left Indicators
        for (int i = 0; i < grid.getRows(); i++) {
            // Set y
            y = (int) (s_y + (grid.getN_cols() + i + 1) * cellSizeY - 2 * cellSizeY / 5);
            for (int j = 0; j < grid.getN_cols(); j++) {
                // Set x
                x = (int) (s_x + cellSizeX * grid.getN_cols() - (j + 1) * cellSizeX + cellSizeX / 4);

                int aux = tli.getLeftInd()[i][j];
                int gameAux = gameTli.getLeftInd()[i][j];
                if (aux > 0) {
                    escriuNormal(canvas, "" + gameAux, toScreen(textSize), grid.getColors()[j].getC(), toScreen(x), toScreen(y));
                    // Paint Circle
                    if (leftIndCirc[i][j] ) {
                        indicatorPainter.setColor(grid.getColors()[j].getC());
                        indicatorPainter.setAlpha(255);
                        canvas.drawCircle(toScreen((int) (s_x + cellSizeX * (grid.getN_cols() - j - 0.5))), toScreen((int) (s_y + (grid.getN_cols() + i + 0.5) * cellSizeY)), toScreen(textSize - 2), indicatorPainter);
                    }
                } else if (aux < 0 || tli.getLefIndCirc()[i][j]) {
                    canvas.drawRect(toScreen((int) (s_x + cellSizeX * (grid.getN_cols() - j - 1))), toScreen((int) (s_y + (grid.getN_cols() + i) * cellSizeY)), toScreen((int) (s_x + cellSizeX * (grid.getN_cols() - j))), toScreen((int) (s_y + (grid.getN_cols() + i + 1) * cellSizeY)), paintErr);
                    escriuNormal(canvas, "" + gameAux, toScreen(textSize), grid.getColors()[j].getC(), toScreen(x), toScreen(y));
                    // Paint Circle
                    if (leftIndCirc[i][j] ) {
                        indicatorPainter.setColor(grid.getColors()[j].getC());
                        indicatorPainter.setAlpha(255);
                        canvas.drawCircle(toScreen((int) (s_x + cellSizeX * (grid.getN_cols() - j - 0.5))), toScreen((int) (s_y + (grid.getN_cols() + i + 0.5) * cellSizeY)), toScreen(textSize - 2), indicatorPainter);
                    }
                }
            }
        }
    }


    /**
     * This method paints the top hints.
     * @param canvas
     */
    public void drawTopIndicator(Canvas canvas) {
        int[][] topInd = tli.getTopInd();
        boolean[][] topIndCirc = gameTli.getTopIndCirc();
        //Text Size
        int textSize;
        if (cellSizeX < cellSizeY) {
            textSize = (int) cellSizeX / 2;
        } else {
            textSize = (int) cellSizeY / 2;
        }
        int x, y;
        //Top Indicators
        for (int i = 0; i < grid.getCols(); i++) {
            x = (int) (s_x + cellSizeX * grid.getN_cols() + i * cellSizeX + cellSizeX / 4);
            for (int j = 0; j < grid.getN_cols(); j++) {
                y = (int) (s_y + (grid.getN_cols() - j) * cellSizeY - 2 * cellSizeY / 5);
                int aux = topInd[i][j];
                int gameAux = gameTli.getTopInd()[i][j];
                if (aux > 0) {
                    escriuNormal(canvas, "" + gameAux, toScreen(textSize), grid.getColors()[j].getC(), toScreen(x), toScreen(y));
                    if (topIndCirc[i][j]) {
                        indicatorPainter.setColor(grid.getColors()[j].getC());
                        indicatorPainter.setAlpha(255);
                        canvas.drawCircle(toScreen((int) (s_x + cellSizeX * (grid.getN_cols() + i + 0.5))), toScreen((int) (s_y + (grid.getN_cols() - j - 0.5) * cellSizeY)), toScreen(textSize - 2), indicatorPainter);
                    }
                } else if (aux < 0 || tli.getTopIndCirc()[i][j]) {
                    canvas.drawRect(toScreen((int) (s_x + cellSizeX * (grid.getN_cols() + i))), toScreen((int) (s_y + (grid.getN_cols() - j - 1) * cellSizeY)), toScreen((int) (s_x + cellSizeX * (grid.getN_cols() + i + 1))), toScreen((int) (s_y + (grid.getN_cols() - j) * cellSizeY)), paintErr);
                    escriuNormal(canvas, "" + gameAux, toScreen(textSize), grid.getColors()[j].getC(), toScreen(x), toScreen(y));
                    if (topIndCirc[i][j]) {
                        indicatorPainter.setColor(grid.getColors()[j].getC());
                        indicatorPainter.setAlpha(255);
                        canvas.drawCircle(toScreen((int) (s_x + cellSizeX * (grid.getN_cols() + i + 0.5))), toScreen((int) (s_y + (grid.getN_cols() - j - 0.5) * cellSizeY)), toScreen(textSize - 2), indicatorPainter);
                    }
                }

//                if ( topInd[j][i]<0){
//                    aux = 0;
//                    //Draw Red Rect on Cell
//                    canvas.drawRect(toScreen((int) (s_x + cellSizeX*(grid.getN_cols()+j))),toScreen((int) (s_y + (grid.getN_cols()-i-1)*cellSizeY)),toScreen((int) (s_x + cellSizeX*(grid.getN_cols()+j+1))),toScreen((int) (s_y + (grid.getN_cols()-i)*cellSizeY)),paintErr);
//                }
//                escriuNormal(canvas, ""+ aux, toScreen(textSize), grid.getColors()[i].getC(),toScreen(x),toScreen(y));
            }
        }
    }


    public boolean gameWon() {
        boolean fullGrid = true;
        boolean noError = true;
        for (int i = 0; i < tli.getLeftInd().length ; i++) {
            for (int j = 0; j < tli.getLeftInd()[0].length; j++) {
                if (tli.getLeftInd()[i][j] != 0){
                    fullGrid = false;
                    break;
                }
                if (tli.getLefIndCirc()[i][j] ) {
                    Log.v("Error", "Checks errors " + i + " "+ j );
                    noError = false;
                    break;
                }
            }
            if (!(fullGrid && noError)) break;
        }

        if (!(fullGrid && noError)) return false;

        for (int i = 0; i < tli.getTopInd().length ; i++) {
            for (int j = 0; j < tli.getTopInd()[0].length; j++) {
                if (tli.getTopInd()[i][j] != 0){
                    fullGrid = false;
                    break;
                }
                if (tli.getTopIndCirc()[i][j]) {
                    Log.v("Error", "Checks errors " + i + " "+ j );
                    noError = false;
                    break;
                }
            }
            if (!(fullGrid && noError)) break;
        }
        return fullGrid && noError;
    }

    private void isCircleError(int col, int row) {
        // Clear erros.
        for (int i = 0; i < n_cols; i++) {
            tli.getLefIndCirc()[row][i] = false;
            tli.getTopIndCirc()[col][i] = false;
        }
        // Check for errors.
        for (int aux = 0; aux < n_cols; aux++) {
            Col color = grid.getColors()[aux];
            int status = 0;
            int leftInd = 0;
            for (int c = 0; c < grid.getArray_cols().length; c++) {
                switch (status) {
                    case 0:
                        if (color == grid.getArray_cols()[c][row]) {
                            leftInd++;
                            status = 1;
                        }
                        break;
                    case 1:
                        if (color == grid.getArray_cols()[c][row]) {
                            leftInd++;
                        } else {
                            status = 2;
                        }
                        break;
                    case 2:
                        if (color == grid.getArray_cols()[c][row]) {
                            leftInd++;
                            status = 3;
                        }
                        break;
                    case 3:
                        if (color == grid.getArray_cols()[c][row]) {
                            leftInd++;
                        }
                        break;
                }
            }
            tli.getLefIndCirc()[row][aux] = ((status == 1 || status == 2) && leftInd > 1) != gameTli.getLefIndCirc()[row][aux];
        }
        for (int aux = 0; aux < n_cols; aux++) {
            Col color = grid.getColors()[aux];
            int status = 0;
            int topInd = 0;
            for (int r = 0; r < grid.getArray_cols()[0].length; r++) {
                switch (status) {
                    case 0:
                        if (color == grid.getArray_cols()[col][r]) {
                            topInd++;
                            status = 1;
                        }
                        break;
                    case 1:
                        if (color == grid.getArray_cols()[col][r]) {
                            topInd++;
                        } else {
                            status = 2;
                        }
                        break;
                    case 2:
                        if (color == grid.getArray_cols()[col][r]) {
                            topInd++;
                            status = 3;
                        }
                        break;
                    case 3:
                        if (color == grid.getArray_cols()[col][r]) {
                            topInd++;
                        }
                        break;
                }
            }
            tli.getTopIndCirc()[col][aux] = ((status == 1 || status == 2) && topInd > 1)!= gameTli.getTopIndCirc()[col][aux];
        }

    }

    public void iniciateButton() {
        int nColors = grid.getN_cols() + 1;
        buttons = new ImageHandler[nColors];
        for (int i = 0; i < buttons.length; i++) {
            if (i == 0) {
                buttons[i] = new ImageHandler(this, R.drawable.triangle);
            } else {
                if (grid.getColors()[i - 1].getF() == Form.CIRCLE) {
                    buttons[i] = new ImageHandler(this, R.drawable.round_button);
                } else if (grid.getColors()[i - 1].getF() == Form.SQUARE) {
                    buttons[i] = new ImageHandler(this, R.drawable.button);
                }
            }
        }
    }


    public void drawButton(Canvas canvas) {
        int nColors = grid.getN_cols() + 1;
        int startY = (int) (f_y + (f_y - s_y) / 5);
        int padding = (int) ((f_x - s_x) / ((nColors + 1)));
        int startX = (int) (s_x + padding);
        for (int i = 0; i < nColors; i++) {
            if (i == 0) {
                buttons[i].pinta(canvas, toScreen(startX), toScreen(startY), toScreen(100), toScreen(100));
            } else {
                buttons[i].pintaAmbFiltre(canvas, toScreen(startX + padding * i), toScreen(startY), toScreen(100), toScreen(100), grid.getColors()[i - 1].getC(), 255, grid.getColors()[i - 1].getF());
            }
        }

    }

    public int toScreen(int in) {
        return (int) (in * ratio);
    }

    public int fromScreen(int in) {
        return (int) (in / ratio);
    }

    public void escriuCentrat(Canvas canvas, String missatge, int size, int color, int posicio) {
        Paint paint = new Paint();
        paint.setTypeface(((JocProvaActivity) context).fontJoc);
        paint.setTextSize(size);

        // Centrat horitzontal
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(color);
        paint.setAlpha(200);

        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) (posicio - ((paint.descent() + paint.ascent())) / 4);

        canvas.drawText(missatge, xPos, yPos, paint);
    }

    public void escriuNormal(Canvas canvas, String missatge, int size, int color, int posicioX, int posicioY) {
        Paint paint = new Paint();
        paint.setTypeface(((JocProvaActivity) context).fontJoc);
        paint.setTextSize(size);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(color);
        paint.setAlpha(200);

        int yPos = (int) (posicioY - ((paint.descent() + paint.ascent())) / 4);

        canvas.drawText(missatge, posicioX, yPos, paint);
    }

    public void aturar() {
        jocLoopThread.setRunning(false);
    }

    public void partir() {
        jocLoopThread.setRunning(true);
    }

}