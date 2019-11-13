package observer.pattern;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;

import observer.CourseRecord;
import observer.LayoutConstants;

@SuppressWarnings("serial")
public class ChartObserver extends JPanel implements Observer {
    private Vector<CourseRecord> courseData;

    /**
     * Creates a BarChartObserver object
     * 
     * @param data a CourseData object to observe
     */

    public ChartObserver(CourseData data) {
        data.attach(this);
        this.courseData = data.getUpdate();
        this.setPreferredSize(new Dimension(
                2 * LayoutConstants.xOffset
                        + (LayoutConstants.barSpacing + LayoutConstants.barWidth) * this.courseData.size(),
                LayoutConstants.graphHeight + 2 * LayoutConstants.yOffset));
        this.setBackground(Color.white);
    }

    public void paint(Graphics g) {
        super.paint(g);
        // first compute the total number of students
        double total = 0.0;
        for (int i = 0; i < courseData.size(); i++) {
            CourseRecord record = (CourseRecord) courseData.elementAt(i);
            total += record.getNumOfStudents();
        }

        System.out.println(total);

        // if total == 0 nothing to draw
        if (total != 0) {
            double startAngle = 0.0;
            for (int i = 0; i < courseData.size(); i++) {
                CourseRecord record = (CourseRecord) courseData.elementAt(i);

                double ratio = (record.getNumOfStudents() / total) * 360.0;

                // draw the rect to draw the arc inside
                g.setColor(Color.white);
                g.drawRect(LayoutConstants.xOffsetPie, LayoutConstants.yOffset, LayoutConstants.graphHeight, 210);

                // draw the arc
                g.setColor(LayoutConstants.courseColours[i]);
                g.fillArc(LayoutConstants.xOffsetPie, LayoutConstants.yOffset, LayoutConstants.graphHeight, 210,
                        (int) startAngle, (int) ratio);

                startAngle += ratio;
            }

            // draw the bar chart
            LayoutConstants.paintBarChartOutline(g, this.courseData.size());
            for (int i = 0; i < courseData.size(); i++) {
                CourseRecord record = (CourseRecord) courseData.elementAt(i);
                g.setColor(LayoutConstants.courseColours[i]);
                g.fillRect(
                        LayoutConstants.xOffset + (i + 1) * LayoutConstants.barSpacing + i * LayoutConstants.barWidth,
                        LayoutConstants.yOffset + LayoutConstants.graphHeight - LayoutConstants.barHeight
                                + 2 * (LayoutConstants.maxValue - record.getNumOfStudents()),
                        LayoutConstants.barWidth, 2 * record.getNumOfStudents());
                g.setColor(Color.red);
                g.drawString(record.getName(),
                        LayoutConstants.xOffset + (i + 1) * LayoutConstants.barSpacing + i * LayoutConstants.barWidth,
                        LayoutConstants.yOffset + LayoutConstants.graphHeight + 20);

            }
        }
    }

    @Override
    public void update(Observable o) {
        CourseData data = (CourseData) o;
        this.courseData = data.getUpdate();
        this.setPreferredSize(new Dimension(
                2 * LayoutConstants.xOffset
                        + (LayoutConstants.barSpacing + LayoutConstants.barWidth) * this.courseData.size(),
                LayoutConstants.graphHeight + 2 * LayoutConstants.yOffset));
        this.revalidate();
        this.repaint();

    }

}