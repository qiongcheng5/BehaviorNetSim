package genDevs.plots;

import java.awt.*;
import java.util.*;
import GenCol.*;
import genDevs.modeling.*;
import simView.*;
import util.*;

/**
 * A job-queue that interfaces with an associated cell-grid-view to provide
 * a graphical view of the jobs it is processing.
 *
 * @author      Bernard Zeigler, Jeff Mather
 */
public class JobQueueWithCellGridView extends JobQueue
{
    /**
     * This job-queue's cell-grid-view.
     */
    protected CellGridView cellGridView;

    /**
     * The default onscreen location of this job-queue's cell-grid-view.
     */
    protected Point cellGridViewLocation = new Point(500, 50);

    /**
     * The last values passed in as part of draw-i and draw-j jobs.
     */
    protected double lastI = 0, lastJ = 0;

    /**
     * What range each axis in the cell-grid-view covers.
     */
    protected double xRange = 1, yRange = 1, zRange = 1;

    /**
     * The ratio of pixels-to-seconds when the x axis is used as the time
     * axis for a time plot.
     */
    protected double timeScale;

    /**
     * The color to use in cell drawing operations where the color is
     * always the same.
     */
    protected Color defaultColor = Color.blue;

    /**
     * Constructor.
     *
     * @param   name            The name to give the job-queue.
     * @param   jobDueDelay     The length of time after a job arrives until
     *                          it is due.
     * @param   xLabel_         The name to give the x-axis of the cell-grid-view.
     * @param   xRange_         How large a range the x-axis should cover.
     * @param   yLabel_         The name to give the y-axis of the cell-grid-view.
     * @param   yRange_         How large a range the y-axis should cover.
     */
    public JobQueueWithCellGridView(String name, double jobDueDelay,
        String xLabel, double xRange_, String yLabel, double yRange_)
    {
        super(name, jobDueDelay);

        xRange = xRange_;
        yRange = yRange_;

        // append half-range values to the axis labels
        xLabel = xLabel + " " + .5 * xRange;
        yLabel = yLabel + " " + .5 * yRange;

        // create the cell-grid-view
        cellGridView = new CellGridView(name, xLabel, yLabel);

        configureCellGridView();

        // add necessary ports
        addInport("drawString");
        addInport("drawCell");
        addInport("drawCellToScale");
        addInport("drawI");
        addInport("drawJ");
        addInport("draw2D");
        addInport("timePlot");

        // add some test inputs
        addTestInput("drawString",
            new DrawCellEntity("this is a test", 15, 10, Color.red), 0);
        addTestInput("drawCell",
            new DrawCellEntity(15, 11, Color.black, Color.green), 0);
        addTestInput("drawCell", new DrawCellEntity(10, 10, Color.red), 0);
        addTestInput("drawI", new doubleEnt(.5), 0);
        addTestInput("drawJ", new doubleEnt(-.7), 0);
        addTestInput("timePlot", new doubleEnt(-.7), 1);
        addTestInput("timePlot", new doubleEnt(.7), 1);

        computeTimeScale();
    }

    /**
     * Constructor.
     *
     * @param   name            See other constructor.
     * @param   jobDueDelay     See other constructor.
     * @param   cellGridView_   The cell-grid-view to use for this job queue.
     * @param   zRange_         How large a range the z-axis should cover.
     */
    public JobQueueWithCellGridView(String name, double jobDueDelay,
        CellGridView cellGridView_, double zRange_)
    {
        super(name, jobDueDelay);
        cellGridView = cellGridView_;
        zRange = zRange_;
        configureCellGridView();

        computeTimeScale();
    }

    /**
     * A convenience constructor.
     */
    public JobQueueWithCellGridView(String name, double jobDueDelay,
        CellGridView cellGridView)
    {
        this(name, jobDueDelay, cellGridView, 1.0);
    }

    /**
     * A convenience constructor.
     */
    public JobQueueWithCellGridView(String name, double delay,
        double xRange, double yRange)
    {
        this(name, delay, "X", xRange, "Y", yRange);
    }

    /**
     * A convenience constructor.
     */
    public JobQueueWithCellGridView(String name, double delay,
        double xRange, double yRange, double zRange_)
    {
        this(name, delay, "X", xRange, "Y", yRange);
        zRange = zRange_;
    }

    /**
     * A convenience constructor.
     */
    public JobQueueWithCellGridView(String name, double delay,
        String yLabel, double yRange)
    {
        this(name, delay, "time", 100, yLabel, yRange);
    }

    /**
     * A convenience constructor.
     */
    public JobQueueWithCellGridView(String name, double delay, double yRange)
    {
        this(name, delay, "Y", yRange);
    }

    /**
     * A convenience constructor.
     */
    public JobQueueWithCellGridView()
    {
        this("JobQueueWithCellGridView", 10, 2, 2);
    }

    /**
     * Sets the location of this job-queue's cell-grid-view onscreen.
     *
     * @param   x, y    The location onscreen.
     */
    public void setCellGridViewLocation(int x, int y)
    {
        cellGridViewLocation.setLocation(x, y);
        cellGridView.setLocation(x, y);
    }

    /**
     * Sets the size of the cell space to be displayed by the cell-grid-view.
     *
     * @param       width, height       The size of the cell space (in cells).
     */
    public void setSpaceSize(int width, int height)
    {
        cellGridView.setSpaceSize(new Dimension(width, height));
        computeTimeScale();
        cellGridView.setXScale(xRange);
        cellGridView.setYScale(yRange);
    }

    /**
     * See member variable for description of variable computed.
     */
    protected void computeTimeScale()
    {
        timeScale = 2.2 * cellGridView.getSpaceSize().width;
    }

    /**
     * Sets how large each cell should be.
     *
     * @param       cellSize_           The size of each cell (in pixels).
     */
    public void setCellSize(int size)
    {
        cellGridView.setCellSize(size);
    }

    /**
     * Returns this job-queue's time-scale.
     */
    public double getTimeScale() {return timeScale;}

    /**
     * Sets the ratio of pixels-to-seconds when the x-axis is used as the time
     * axis for a time plot.
     *
     * @param   timeScale_      The ratio of pixels to seconds for the x-axis.
     */
    public void setTimeScale(double timeScale_)
    {
        timeScale = timeScale_;
        xRange = timeScale;
        cellGridView.setXLabel("time " + 0.5 * xRange);
    }

    /**
     * Sets the color to use in cell drawing operations where the color is
     * always the same.
     *
     * @param   color       The color to use.
     */
    public void setDefaultColor(Color color)
    {
        defaultColor = color;
    }

    /**
     * Returns a color based on where the given value lies within the
     * cell-grid-view's z-range.
     *
     * @param   value       The value to map to a color.
     * @return              The color mapped to.
     */
    public Color mapValueToColor(double value)
    {
        value *= 2;
        if (value > zRange) return Color.red;
        else if (value > .8 * zRange) return Color.pink;
        else if (value > .6 * zRange) return Color.orange;
        else if (value > .4 * zRange) return Color.yellow;
        else if (value > .2 * zRange) return Color.green;
        else if (value > 0) return Color.green;
        else if (value > -.2 * zRange) return Color.cyan;
        else if (value > -.4 * zRange) return Color.blue;
        else if (value > -.6 * zRange) return Color.magenta;
        else if (value > -.8 * zRange) return Color.gray;
        else return Color.black;
    }

    /**
     * Configures this job-queue's cell-grid-view.
     */
    protected void configureCellGridView()
    {
        // configure the cell-grid-view
        cellGridView.setLocation(cellGridViewLocation);
        cellGridView.setXScale(xRange);
        cellGridView.setYScale(yRange);
        cellGridView.setVisible(true);
    }

    /**
     * Performs additional work beyond that done in the parent class method.
     */
    protected void deltextHook1(message message)
    {
        // for each content in the given message
        doubleEnt dueTime = new doubleEnt(clock + jobDueDelay);
        for (int i = 0; i < message.getLength(); i++) {
            // if this content is on the draw-string port
            if (messageOnPort(message, "drawString", i)) {
                // add this content to the jobs that have arrived
                DrawCellEntity entity = (DrawCellEntity)message.getValOnPort("drawString", i);
                arrived.put(dueTime, entity);

                // tell the cell-grid-view to draw the entity's name
                cellGridView.drawString(
                    (int)Math.rint(entity.i), (int)Math.rint(entity.j),
                    entity.getName(), entity.color);
            }

            // else, if this content is on the draw-cell port
            else if (messageOnPort(message, "drawCell", i)) {
                // add this content to the jobs that have arrived
                DrawCellEntity entity = (DrawCellEntity)message.getValOnPort("drawCell", i);
                arrived.put(dueTime, entity);

                // tell the cell-grid-view to draw the cell specified by the job
                entity.setName("drawCell");
                cellGridView.drawCell(
                    (int)Math.rint(entity.i), (int)Math.rint(entity.j),
                    entity.color);
            }

            // else, if this content is on the draw-cell-to-scale port
            else if (messageOnPort(message, "drawCellToScale", i)) {
                // add this content to the jobs that have arrived
                //DrawCellEntity entity = (DrawCellEntity)message.getValOnPort("drawCellToScale", i);

                  entity en = message.getValOnPort(
                                   "drawCellToScale", i);
 String nm = en.getName();
 int jIndex = nm.indexOf("j ");
 int commaIndex = nm.indexOf(", ");
String xs = nm.substring(jIndex+1,commaIndex);
String ys = nm.substring(commaIndex+1,nm.length());
DrawCellEntity entity =  new DrawCellEntity("drawCellToScale",Double.parseDouble(xs),Double.parseDouble(ys));
//System.out.println("XXXXXXXXXXXXXXXXXXXXX "+entity.getName());

 /**/
                arrived.put(dueTime, entity);
                // tell the cell-grid-view to draw-to-scale the cell
                // specified by the job
                entity.setName("drawCellToScale");
                cellGridView.drawCellToScale(entity.i, entity.j, entity.color);
            }

            // else, if this content is on the draw-2d port
            else if (messageOnPort(message, "draw2D", i)) {
                // add this content to the jobs that have arrived
                DrawCellEntity entity = (DrawCellEntity)message.getValOnPort("draw2D", i);
                arrived.put(dueTime, entity);

                // tell the cell-grid-view to draw-to-scale the cell
                // specified by the job using a color mapped from
                // the job's value for the cell
                entity.setName("drawCellToScale");
                entity.color = mapValueToColor(entity.k);
                cellGridView.drawCellToScale(entity.i, entity.j, entity.color);
            }

            // else, if this content is on the draw-i port
            else if (messageOnPort(message, "drawI", i)) {
                // store the i specified by the cell as the last one received
                doubleEnt entity = (doubleEnt)message.getValOnPort("drawI", i);
                lastI = entity.getv();

                // add this content to the jobs that have arrived
                arrived.put(dueTime, new DrawCellEntity("drawCellToScale",
                    lastI, lastJ));

                // tell the cell-grid-view to draw-to-scale the cell of the last
                // i and j received
                cellGridView.drawCellToScale(lastI, lastJ, Color.black);
            }

            // else, if this content is on the draw-j port
            else if (messageOnPort(message, "drawJ", i)) {
                // store the j specified by the cell as the last one received
                doubleEnt entity = (doubleEnt)message.getValOnPort("drawJ", i);
                lastJ = entity.getv();

                // add this content to the jobs that have arrived
                arrived.put(dueTime,
                    new DrawCellEntity("drawCellToScale", lastI, lastJ));

                // tell the cell-grid-view to draw-to-scale the cell of the last
                // i and j received
                cellGridView.drawCellToScale(lastI, lastJ, Color.blue);
            }

            // else, if this content is on the time-plot port
            else if (messageOnPort(message, "timePlot", i)) {
                // add this content to the jobs that have arrived
                doubleEnt entity = (doubleEnt)message.getValOnPort(
                    "timePlot", i);
                double value = entity.getv();
                arrived.put(dueTime,
                    new DrawCellEntity("drawCellToScale", timeScale, value));

                // tell the cell-grid-view to draw-to-scale the cell whose i value
                // is given by the current clock value, and whose j value
                // is specified by the job
                double time = (-.5 + (clock / timeScale) % 1) * timeScale;
                cellGridView.drawCellToScale(time, value, defaultColor);
            }
        }
    }

    /**
     * Performs additional work beyond that done in the parent class method.
     */
    protected void deltintHook1()
    {
        // for each job that is due
        Iterator i = due.iterator();
        while (i.hasNext()) {
            // if this job is a draw-cell-to-scale job
            DrawCellEntity entity = (DrawCellEntity)i.next();
            String name = entity.getName();
            if (name.startsWith("drawCellToScale")) {
                // dim the cell that was drawn when the job arrived
                cellGridView.drawCellToScale(entity.i, entity.j, entity.dimTo);
            }

            // else, if this job is a draw-cell job
            else if (name.startsWith("drawCell")) {
                // dim the cell that was drawn when the job arrived
                cellGridView.drawCell(
                    (int)Math.rint(entity.i), (int)Math.rint(entity.j),
                    entity.dimTo);
            }

            // else, if the job is a draw-string job
            else if (name.startsWith("drawString")) {
                // dim the string that was drawn when the job arrived
                cellGridView.drawString(
                    (int)Math.rint(entity.i), (int)Math.rint(entity.j),
                    name, entity.dimTo);
            }
        }
    }

    /**
     * See parent method.
     */
    public message out()
    {
        // the parent class's outputting of finished jobs is not desired here
        return new message();
    }

    /**
     * See parent method.
     */
    public String stringState()
    {
        String graphInfo =
            "XRange,YRange : " + xRange + "," + yRange + "\n" +
            "lastI,lastJ : " + lastI + "," + lastJ;
        return super.stringState() + "\n" + graphInfo;
    }

    /**
     * See member variable accessed.
     */
    public CellGridView getCellGridView() {return cellGridView;}
    public void setCellGridView(CellGridView view)
    {
        cellGridView = view;
        configureCellGridView();
    }
}



