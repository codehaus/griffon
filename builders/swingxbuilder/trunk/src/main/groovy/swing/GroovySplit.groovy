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

import org.jdesktop.swingx.MultiSplitLayout.Leaf
import org.jdesktop.swingx.MultiSplitLayout.Node
import org.jdesktop.swingx.MultiSplitLayout.Split


public class GroovySplit extends Split {
    def nodes = []

    public GroovySplit() {
        super([new Leaf("center")])
    }

    public GroovySplit(Node[] children) {
        super(children)
    }

    public setChildren() {
        super.setChildren(nodes)
    }
}


