
package experiments;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlotGenerator {

    private static final String[] NAMES = {
            "Kruskal", "Prim", "Topological DAG",
            "Topological Cyclic", "Kosaraju", "Dijkstra"
    };

    private static final Color[] COLORS = {
            new Color(220, 70, 70),
            new Color(50, 100, 220),
            new Color(50, 160, 80),
            new Color(170, 100, 200),
            new Color(230, 150, 30),
            new Color(40, 170, 190)
    };


    private static final double[][] TIMES = {
            {0.071575, 0.526583, 3.238300},
            {0.113400, 0.562691, 5.596967},
            {0.035217, 0.194080, 1.824334},
            {0.005533, 0.058042, 0.128067},
            {0.049958, 0.320250, 2.492192},
            {0.043375, 0.185917, 1.952150}
    };


    public static void main(String[] args) throws IOException {
        File folder = new File("results/plots");
        folder.mkdirs();

        createPlot(
                "Execution Time vs Number of Vertices",
                new String[]{"10", "100", "1000"},
                "Vertices (V)",
                "results/plots/time_vs_vertices.png"
        );

        createPlot(
                "Execution Time vs Number of Edges",
                new String[]{"30", "500", "5000"},
                "Edges (E)",
                "results/plots/time_vs_edges.png"
        );

        System.out.println("Two plots generated successfully!");
        System.out.println("Saved in results/plots/");
    }

    private static void createPlot(String title,
                                   String[] labels,
                                   String xLabel,
                                   String filename) throws IOException {

        int width = 1100;
        int height = 700;
        int left = 100;
        int right = 270;
        int top = 100;
        int bottom = 100;

        int plotWidth = width - left - right;
        int plotHeight = height - top - bottom;

        BufferedImage image = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_RGB
        );

        Graphics2D g = image.createGraphics();

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.setFont(new Font("Arial", Font.BOLD, 23));
        g.setColor(Color.BLACK);
        g.drawString(title, 100, 45);

        double maxTime = 0;

        for (double[] times : TIMES) {
            for (double time : times) {
                maxTime = Math.max(maxTime, time);
            }
        }

        maxTime = Math.ceil(maxTime + 0.5);

        g.setFont(new Font("Arial", Font.PLAIN, 15));

        for (int i = 0; i <= 5; i++) {
            int y = top + plotHeight - i * plotHeight / 5;

            g.setColor(new Color(225, 225, 225));
            g.drawLine(left, y, left + plotWidth, y);

            g.setColor(Color.BLACK);
            double value = maxTime * i / 5;

            g.drawString(
                    String.format(java.util.Locale.US, "%.1f", value),
                    45, y + 5
            );
        }

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawLine(left, top, left, top + plotHeight);
        g.drawLine(left, top + plotHeight,
                left + plotWidth, top + plotHeight);

        for (int i = 0; i < labels.length; i++) {
            int x = left + i * plotWidth / (labels.length - 1);
            g.drawString(labels[i], x - 15,
                    top + plotHeight + 30);
        }

        g.drawString(xLabel,
                left + plotWidth / 2 - 35, height - 35);
        g.drawString("Time (ms)", 12, 85);

        for (int algorithm = 0; algorithm < TIMES.length; algorithm++) {
            g.setColor(COLORS[algorithm]);
            g.setStroke(new BasicStroke(3));

            for (int i = 0; i < 2; i++) {
                int x1 = left + i * plotWidth / 2;
                int x2 = left + (i + 1) * plotWidth / 2;

                int y1 = top + plotHeight -
                        (int) (TIMES[algorithm][i] / maxTime * plotHeight);

                int y2 = top + plotHeight -
                        (int) (TIMES[algorithm][i + 1] / maxTime * plotHeight);

                g.drawLine(x1, y1, x2, y2);
            }

            for (int i = 0; i < 3; i++) {
                int x = left + i * plotWidth / 2;
                int y = top + plotHeight -
                        (int) (TIMES[algorithm][i] / maxTime * plotHeight);

                g.fillOval(x - 5, y - 5, 10, 10);
            }

            int legendX = 870;
            int legendY = 160 + algorithm * 40;

            g.fillRect(legendX, legendY - 10, 24, 5);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 15));
            g.drawString(NAMES[algorithm], legendX + 32, legendY);
        }

        g.dispose();
        ImageIO.write(image, "png", new File(filename));
    }
}


