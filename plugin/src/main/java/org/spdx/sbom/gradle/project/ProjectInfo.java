/*
 * Copyright 2023 The Project Authors.
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
package org.spdx.sbom.gradle.project;

import java.io.File;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.immutables.serial.Serial;
import org.immutables.value.Value.Immutable;

@Immutable
@Serial.Version(1)
public interface ProjectInfo {
  String getName();

  Optional<String> getDescription();

  String getVersion();

  File getProjectDirectory();

  String getPath();

  String getGroup();

  static ProjectInfo from(Project project) {
    if (project.getVersion().toString().equals("unspecified")) {
      throw new GradleException(
          "spdx sboms require a version but project: "
              + project.getName()
              + " has no specified version");
    }
    return ImmutableProjectInfo.builder()
        .name(project.getName())
        .description(Optional.ofNullable(project.getDescription()))
        .version(project.getVersion().toString())
        .projectDirectory(project.getProjectDir())
        .path(project.getPath())
        .group(project.getGroup().toString())
        .build();
  }

  static Set<ProjectInfo> from(Set<Project> projects) {
    return projects.stream().map(ProjectInfo::from).collect(Collectors.toSet());
  }
}
