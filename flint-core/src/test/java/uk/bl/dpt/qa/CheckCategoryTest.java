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
package uk.bl.dpt.qa;

import org.junit.Test;
import uk.bl.dpt.qa.flint.checks.CheckCategory;
import uk.bl.dpt.qa.flint.checks.CheckCheck;

import static org.fest.assertions.Assertions.assertThat;


public class CheckCategoryTest {

    @Test
    public void testResultPassed() {
        CheckCheck check1 = new CheckCheck("testCheck1", true, null);
        CheckCheck check2 = new CheckCheck("testCheck2", true, null);
        CheckCategory cc = new CheckCategory("testCc");
        cc.add(check1);
        cc.add(check2);
        assertThat(cc.get("testCheck1")).isEqualTo(check1);
        assertThat(cc.get("testCheck2")).isEqualTo(check2);

        assertThat(cc.isErroneous()).isEqualTo(false);
        assertThat(cc.isHappy()).isEqualTo(true);
        assertThat(cc.getResult()).isEqualTo("passed");
    }

    @Test
    public void testResultFailed() {
        CheckCategory cc = new CheckCategory("testCc");
        cc.add(new CheckCheck("testCheck1", true, null));
        assertThat(cc.isHappy()).isEqualTo(true);
        // adding a failed test makes the category fail
        cc.add(new CheckCheck("testCheck2", false, null));
        assertThat(cc.isErroneous()).isEqualTo(false);
        assertThat(cc.isHappy()).isEqualTo(false);
        assertThat(cc.getResult()).isEqualTo("failed");
    }

    @Test
    public void testResultErroneous() {
        // two cases for a cc being erroneous:
        CheckCategory cc = new CheckCategory("testCc");

        // (1) there aren't any checks at all
        assertThat(cc.isErroneous()).isEqualTo(true);
        assertThat(cc.isHappy()).isEqualTo(null);
        assertThat(cc.getResult()).isEqualTo("error");

        // (2) there are checks, but all of them are erroneous themselves
        cc.add(new CheckCheck("testCheck1", null, null));
        cc.add(new CheckCheck("testCheck2", null, null));
        assertThat(cc.isErroneous()).isEqualTo(true);
        assertThat(cc.isHappy()).isEqualTo(null);
        assertThat(cc.getResult()).isEqualTo("error");
        //    ..if at least one check is not erroneous the category is neither
        cc.add(new CheckCheck("testCheck3", false, null));
        assertThat(cc.isErroneous()).isEqualTo(false);
    }

}
