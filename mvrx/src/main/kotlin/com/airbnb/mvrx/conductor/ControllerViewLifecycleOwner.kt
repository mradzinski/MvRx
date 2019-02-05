/*
 * Copyright (c) 2019. DNA Software. All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.airbnb.mvrx.conductor

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.view.View
import com.bluelinelabs.conductor.Controller

/**
 * A [LifecycleOwner] which follows the lifecycle to the View attached to the [Controller].
 *
 * This is the default [LifecycleOwner] all Opus MVVM-enabled Controllers use for observing [LiveData].
 */
class ControllerViewLifecycleOwner<T>(lifecycleController: T) : LifecycleOwner where T : Controller, T : LifecycleOwner {
    private var lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(lifecycleController)

    init {
        lifecycleController.addLifecycleListener(object : Controller.LifecycleListener() {
            override fun preCreateView(controller: Controller) {
                lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            }

            override fun postAttach(controller: Controller, view: View) {
                lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
                lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            }

            override fun preDetach(controller: Controller, view: View) {
                lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            }

            override fun postDestroyView(controller: Controller) {
                lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            }
        })
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}