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
package uk.bl.dpt.qa.flint;

import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import uk.bl.dpt.qa.flint.checks.CheckResult;
import uk.bl.dpt.qa.flint.utils.PolicyPropertiesCreator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * A controller for the flint-fx GUI that directly runs the flint classes.
 */
public class Controller extends CommonController {

    // content of the logbook, stored and modified in a very odd way
    String logBookContent = "";

    public void askForValidation() {
        Task<Void> task = new Task<Void>() {
            @Override protected Void call() {
                PrintWriter out;
                try {
                    logBookContent += "\n--> processing file " + inputFile.getName();
                    updateMessage(logBookContent);
                    List<CheckResult> results = new FLint(getCheckedPatterns()).check(inputFile);
                    outFile = new File(outputD, "flint_results_" + inputFile.getName() + ".xml");
                    out = new PrintWriter(new FileWriter(outFile));
                    logger.info("Analysis done, results: {}", results);
                    String passed = (results.get(0).isHappy() ? "*passed*." : "*failed*.");
                    logBookContent += "\n    Analysis done, overall result: " + passed;
                    updateMessage(logBookContent);
                    FLint.printResults(results, out);
                    out.close();
                    logger.info("results written to {}", outFile.getPath());
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    updateMessage("an exception ocurred: " + e.getMessage());
                }
                return null;
            }
            @Override protected void succeeded() {
                logBookContent += "\n    results written to " + outFile.getName();
                updateMessage(logBookContent);
            }
        };
        logbook.textProperty().bind(task.messageProperty());
        new Thread(task).start();
    }

    public void queryPolicyPatterns() {
        Task<Map<String, Map<String, Set<String>>>> task = new Task<Map<String, Map<String, Set<String>>>>() {
            @Override protected Map<String, Map<String, Set<String>>> call() {
                try {
                    return PolicyPropertiesCreator.getPolicyMap(getFormat());
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                return null;
            }
            @Override protected void succeeded() {
                resetConfiguration(this.getValue());
            }
        };
        new Thread(task).start();
    }

}
