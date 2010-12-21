@artifact.package@import java.awt.Color
import java.awt.Font
import org.jfree.chart.labels.PieToolTipGenerator

def largeFont = new Font("Arial", Font.BOLD, 15);

piechart3d(title: "Simple Pie Chart") {
    defaultPieDataset {
        Series1(40.0f)
        Series2(30.0f)
        Series3(30.0f)
    }
    antiAlias = true
    backgroundPaint(Color.WHITE)

    pieplot {
        sectionOutlinesVisible false
        labelFont largeFont
        labelGap 0.02
        toolTipGenerator ({ dataset, key -> return "[${dataset} ${key}]" as String } as PieToolTipGenerator)

        sectionPaint('Series1', paint: new Color(255,0,0))
        sectionPaint('Series2', paint: new Color(0,255,0))
        sectionPaint('Series3', paint: new Color(0,0,255))
    }
}
