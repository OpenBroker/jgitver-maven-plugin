// @formatter:off
/**
 * Copyright (C) 2016 Matthieu Brouillard [http://oss.brouillard.fr/jgitver-maven-plugin] (matthieu@brouillard.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// @formatter:on
package fr.brouillard.oss.jgitver.mojos;

import java.io.IOException;
import java.util.Objects;

import javax.xml.bind.JAXBException;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.InstantiationStrategy;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import fr.brouillard.oss.jgitver.JGitverSession;
import fr.brouillard.oss.jgitver.JGitverUtils;

/**
 * Works in conjunction with JGitverModelProcessor.
 */
@Mojo(name = JGitverAttachModifiedPomsMojo.GOAL_ATTACH_MODIFIED_POMS,
        instantiationStrategy = InstantiationStrategy.SINGLETON, threadSafe = true)
public class JGitverAttachModifiedPomsMojo extends AbstractMojo {
    public static final String GOAL_ATTACH_MODIFIED_POMS = "attach-modified-poms";

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession mavenSession;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (Objects.isNull(mavenSession.getUserProperties().get(JGitverUtils.SESSION_MAVEN_PROPERTIES_KEY))) {
            getLog().warn(GOAL_ATTACH_MODIFIED_POMS + "shouldn't be executed alone. The Mojo "
                    + "is a part of the plugin and executed automatically.");
            return;
        }

        String content = mavenSession.getUserProperties().getProperty((JGitverUtils.SESSION_MAVEN_PROPERTIES_KEY));
        if ("-".equalsIgnoreCase(content)) {
            // We don't need to attach modified poms anymore.
            return;
        }

        try {
            JGitverSession jgitverSession = JGitverSession.serializeFrom(content);
            JGitverUtils.attachModifiedPomFilesToTheProject(mavenSession.getAllProjects(),
                    jgitverSession.getProjects(), jgitverSession.getVersion(), new
                            ConsoleLogger());
            mavenSession.getUserProperties().setProperty(JGitverUtils.SESSION_MAVEN_PROPERTIES_KEY, "-");
        } catch (XmlPullParserException | IOException | JAXBException ex) {
            throw new MojoExecutionException("Unable to execute goal: "
                    + JGitverAttachModifiedPomsMojo.GOAL_ATTACH_MODIFIED_POMS, ex);
        }
    }
}
