/* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
*/

package groovy.swing

import org.jdesktop.swingx.JXGraph.Plot

public class GroovyPlot extends Plot {
    def computeClosure
    public GroovyPlot() {}
    public GroovyPlot(compute) {
        computeClosure = compute
    }
    public double compute(double value) {
        computeClosure(value)
    }
}


