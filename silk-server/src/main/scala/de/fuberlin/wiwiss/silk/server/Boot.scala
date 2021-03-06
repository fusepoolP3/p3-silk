/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bootstrap.liftweb

import net.liftweb.http.LiftRules
import de.fuberlin.wiwiss.silk.server.view.RestApi
import de.fuberlin.wiwiss.silk.server.model.Server

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot
{
  def boot
  {
    Server.start()

    LiftRules.addToPackages("de.fuberlin.wiwiss.silk.server.view")
    LiftRules.dispatch.prepend(RestApi.dispatch)
  }
}

