/*
 * Sonar Java
 * Copyright (C) 2012 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.java.jacoco;

import org.jacoco.agent.rt.IAgent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class JacocoControllerTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private IAgent agent;
  private JacocoController jacoco;

  @Before
  public void setUp() {
    agent = mock(IAgent.class);
    jacoco = new JacocoController(agent);
  }

  @Test
  public void test_onStart() throws Exception {
    jacoco.onTestStart("test");
    verify(agent).setSessionId("test");
    verify(agent).reset();
    verifyNoMoreInteractions(agent);
  }

  @Test
  public void test_onFinish() throws Exception {
    when(agent.getExecutionData(false)).thenReturn(new byte[] {});
    jacoco.onTestFinish("test");
    verify(agent).dump(true);
    verifyNoMoreInteractions(agent);
  }

  @Test
  public void should_throw_exception_when_dump_failed() throws Exception {
    doThrow(IOException.class).when(agent).dump(anyBoolean());
    thrown.expect(RuntimeException.class);
    jacoco.onTestFinish("test");
  }

}