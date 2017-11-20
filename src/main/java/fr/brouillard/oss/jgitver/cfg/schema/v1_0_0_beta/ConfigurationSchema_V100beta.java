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
package fr.brouillard.oss.jgitver.cfg.schema.v1_0_0_beta;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import fr.brouillard.oss.jgitver.cfg.Configuration;
import fr.brouillard.oss.jgitver.cfg.schema.ConfigurationSchema;

@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigurationSchema_V100beta implements ConfigurationSchema {
    @XmlElement(name = "mavenLike")
    public boolean mavenLike = true;
    @XmlElement
    public boolean autoIncrementPatch = true;
    @XmlElement
    public boolean useCommitDistance = false;
    @XmlElement
    public boolean useDirty = false;
    @XmlElement
    public boolean failIfDirty = false;
    @XmlElement
    public boolean useDefaultBranchingPolicy = true;
    @XmlElement
    public boolean useGitCommitId = false;
    @XmlElement
    public int gitCommitIdLength = 8;
    @XmlElement
    public String nonQualifierBranches = "master";
    @XmlElement(name = "regexVersionTag")
    public String regexVersionTag;
    @XmlElementWrapper(name = "exclusions")
    @XmlElement(name = "exclusion")
    public List<String> exclusions = new LinkedList<>();
    @XmlElementWrapper(name = "branchPolicies")
    @XmlElement(name = "branchPolicy")
    public List<BranchPolicySchema_V100beta> branchPolicies = new LinkedList<>();

    /**
     * Converts this instance into a {@link Configuration} one.
     * @return a non null {@link Configuration} object containing the same values than this instance.
     */
    public Configuration asConfiguration() {
        Configuration c = new Configuration();
        c.mavenLike = mavenLike;
        c.autoIncrementPatch = autoIncrementPatch;
        c.useCommitDistance = useCommitDistance;
        c.useDirty = useDirty;
        c.failIfDirty = failIfDirty;
        c.useDefaultBranchingPolicy = useDefaultBranchingPolicy;
        c.useGitCommitId = useGitCommitId;
        c.gitCommitIdLength = gitCommitIdLength;
        c.nonQualifierBranches = nonQualifierBranches;
        c.regexVersionTag = regexVersionTag;

        c.exclusions.addAll(exclusions);
        c.branchPolicies.addAll(branchPolicies.stream().map(BranchPolicySchema_V100beta::asBranchPolicy).collect(Collectors.toList()));

        return c;
    }
}
