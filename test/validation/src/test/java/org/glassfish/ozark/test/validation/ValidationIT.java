/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2014-2015 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.ozark.test.validation;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ValidationIT {

    private String webUrl;
    private WebClient webClient;

    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
    }

    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }

    @Test
    public void testFormControllerOk() throws Exception {
        final HtmlPage page = webClient.getPage(webUrl);
        final HtmlForm form = page.getFormByName("form");
        final HtmlTextInput name = form.getInputByName("name");
        final HtmlTextInput age = form.getInputByName("age");
        final HtmlSubmitInput button = form.getInputByName("button");
        name.setValueAttribute("john");
        age.setValueAttribute("21");
        final HtmlPage page2 = button.click();
        final Iterator<HtmlElement> it = page2.getDocumentElement().getHtmlElementsByTagName("p").iterator();
        assertTrue(it.next().asText().contains("john"));
        assertTrue(it.next().asText().contains("21"));
    }

    @Test
    public void testFormControllerFail() throws Exception {
        final HtmlPage page = webClient.getPage(webUrl);
        final HtmlForm form = page.getFormByName("form");
        final HtmlTextInput name = form.getInputByName("name");
        final HtmlTextInput age = form.getInputByName("age");
        final HtmlSubmitInput button = form.getInputByName("button");
        name.setValueAttribute("john");
        age.setValueAttribute("2");         // Not old enough!
        try {
            button.click();
            fail("Validation error expected in form submission");
        } catch (FailingHttpStatusCodeException e) {
            assertTrue(e.getStatusCode() == 400);
            assertTrue(e.getResponse().getContentAsString().contains("<h1>Form Error</h1>"));
            assertTrue(e.getResponse().getContentAsString().contains("<p>Param: age</p>"));
        }
    }

    @Test
    public void testFormControllerPropertyOk() throws Exception {
        final HtmlPage page = webClient.getPage(webUrl + "/indexprop.html");
        final HtmlForm form = page.getFormByName("form");
        final HtmlTextInput name = form.getInputByName("name");
        final HtmlTextInput age = form.getInputByName("age");
        final HtmlSubmitInput button = form.getInputByName("button");
        name.setValueAttribute("john");
        age.setValueAttribute("21");
        final HtmlPage page2 = button.click();
        final Iterator<HtmlElement> it = page2.getDocumentElement().getHtmlElementsByTagName("p").iterator();
        assertTrue(it.next().asText().contains("john"));
        assertTrue(it.next().asText().contains("21"));
    }

    @Test
    public void testFormControllerPropertyFail() throws Exception {
        final HtmlPage page = webClient.getPage(webUrl + "/indexprop.html");
        final HtmlForm form = page.getFormByName("form");
        final HtmlTextInput name = form.getInputByName("name");
        final HtmlTextInput age = form.getInputByName("age");
        final HtmlSubmitInput button = form.getInputByName("button");
        name.setValueAttribute("john");
        age.setValueAttribute("2");         // Not old enough!
        try {
            button.click();
            fail("Validation error expected in form submission");
        } catch (FailingHttpStatusCodeException e) {
            assertTrue(e.getStatusCode() == 400);
            assertTrue(e.getResponse().getContentAsString().contains("<h1>Form Error</h1>"));
            assertTrue(e.getResponse().getContentAsString().contains("<p>Param: age</p>"));
        }
    }

    @Test
    @Ignore         // Waiting for Jersey 2.22
    public void testBindingErrorFail() throws Exception {
        final HtmlPage page = webClient.getPage(webUrl + "resources/form?n=j");
        final Iterator<HtmlElement> it = page.getDocumentElement().getHtmlElementsByTagName("p").iterator();
        assertTrue(it.next().asText().contains("Param: n"));
        assertTrue(it.next().asText().contains("Message: java.lang.NumberFormatException: For input string: \"j\""));
    }
}
