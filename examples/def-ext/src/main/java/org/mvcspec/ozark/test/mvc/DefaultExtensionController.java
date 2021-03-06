/*
 * Copyright © 2017 Ivar Grimstad (ivar.grimstad@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mvcspec.ozark.test.mvc;

import javax.mvc.Controller;
import javax.mvc.View;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.mvcspec.ozark.engine.Viewable;

/**
 * DefaultExtensionController test.
 *
 * @author Dmytro Maidaniuk
 */
@Path("def-ext")
@Controller
public class DefaultExtensionController {

    @GET
    @Path("string")
    public String getString() {
        return "extension";
    }
    
    @GET
    @Path("void")
    @View("extension")
    public void getView() { }
    
    @GET
    @Path("viewable")
    public Viewable getViewAble() {
        return new Viewable("extension");
    }
    
    @GET
    @Path("response")
    public Response getResponse() {
        return Response.ok().entity("extension").build();
    }
}
