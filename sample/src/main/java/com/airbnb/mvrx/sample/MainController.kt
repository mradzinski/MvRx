/*
 * Copyright (c) 2019. DNA Software. All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.airbnb.mvrx.sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.airbnb.mvrx.conductor.BaseMvRxController
import com.airbnb.mvrx.controllerViewModel
import com.airbnb.mvrx.withState

class MainController(args: Bundle? = null) : BaseMvRxController(args) {

    private val viewModel: MainViewModel by controllerViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_main, container, false)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        Log.e("MainController", "${viewModel.hashCode()} | ${this.hashCode()}")
        Log.e("MainController", args.getString("A", "No args received"))
    }

    override fun invalidate() {
        withState(viewModel) { state ->
            Log.e("MainController", state.toString())
            view?.findViewById<TextView>(R.id.textViewTest)?.apply {
                text = state.value
            }
        }
    }
}