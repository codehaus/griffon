package griffon.plugins.jzy3d.artifact;

import griffon.core.GriffonMvcArtifact;
import org.jzy3d.chart.Chart;

/**
 * @author Andres Almiray
 */
public interface GriffonChart3D extends GriffonMvcArtifact {
    Chart getChart();
    
    void initChart();
}
