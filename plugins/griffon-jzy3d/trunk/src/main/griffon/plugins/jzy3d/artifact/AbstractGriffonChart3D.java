package griffon.plugins.jzy3d.artifact;

import org.codehaus.griffon.runtime.core.AbstractGriffonMvcArtifact;
import org.jzy3d.chart.Chart;

/**
 * @author Andres Almiray
 */
public abstract class AbstractGriffonChart3D extends AbstractGriffonMvcArtifact implements GriffonChart3D {
    private final Chart chart;

    public AbstractGriffonChart3D() {
        chart = new Chart("swing");
    }

    public Chart getChart() {
        return chart;
    }

    protected String getArtifactType() {
        return GriffonChart3DClass.TYPE;
    }
}
