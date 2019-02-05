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

import android.util.Log
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.sample.core.MvRxViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

data class TestState(val request: Async<String> = Uninitialized, val value: String = ""): MvRxState

class MainViewModel(initialState: TestState) : MvRxViewModel<TestState>(initialState) {

    init {
        fetchTest()
    }

    fun fetchTest() = withState { state ->
        Observable.just("Hello world!")
                .delay(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { setState { copy(request = Success(""), value = "Warming up...") } }
                .doFinally { Log.e("MainViewModel", "Disposing test") }
                .execute { result ->
                    copy(request = result, value = result.invoke() ?: "")
                }
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("MainViewModel", "Clearing out of this VM")
    }
}