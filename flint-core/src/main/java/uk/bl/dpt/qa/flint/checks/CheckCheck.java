/*
 * Copyright 2014 The British Library/SCAPE Project Consortium
 * Authors: William Palmer (William.Palmer@bl.uk)
 *          Alecs Geuder (Alecs.Geuder@bl.uk)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package uk.bl.dpt.qa.flint.checks;

import java.io.PrintWriter;

/**
 * A CheckCheck is the lowest level test in a validation process. It belongs to
 * a * {@link uk.bl.dpt.qa.flint.checks.CheckCategory}.
 *
 * The boolean `result` has to represent the validation outcome in a way that
 * from a policy validation point of view it describes whether the outcome is
 * satisfactory or not ({@link CheckCheck#isHappy()}).
 *
 * In case a defined check has failed multiple times, an
 * {@link uk.bl.dpt.qa.flint.checks.CheckCheck#errorCount} is meant to track
 * this.
 */
public class CheckCheck {

    private String name;
    private Boolean result; // can be null
    private Integer errorCount; // can be null

    public CheckCheck(String name, Boolean result, Integer errorCount) {
        this.name = name;
        this.result = result;
        this.errorCount = errorCount;
    }

    public void toXML(PrintWriter pw, String shift) {
        pw.println(String.format("%s<check name='%s' result='%s'%s/>",
                shift, name, getResult(),
                (errorCount != null ? " errorCount='" + errorCount + "'" : "")));
    }

    public Boolean isHappy() {
        return this.result;
    }

    public boolean isErroneous() {
        return isHappy() == null;
    }

    public String getResult() {
        return this.isErroneous() ? "error" : this.isHappy() ? "passed" : "failed";
    }

    public String toString() {
        return this.name + ": " + getResult();
    }
    public String getName() {
        return this.name;
    }
    public Integer getErrorCount() {
        return this.errorCount;
    }
}
