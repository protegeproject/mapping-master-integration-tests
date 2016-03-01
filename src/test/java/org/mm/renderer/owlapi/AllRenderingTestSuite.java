package org.mm.renderer.owlapi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ClassRenderingTests.class,
   ClassExpressionRenderingTests.class,
   NamedIndividualRenderingTests.class,
   NameResolutionTests.class,
   CellProcessingTests.class,
   DirectiveTests.class })
public class AllRenderingTestSuite
{
   public interface TestCategory {}
   public interface ClassTest extends TestCategory {}
   public interface ClassExpressionTest extends TestCategory {}
   public interface NamedIndividualTest extends TestCategory {}
   public interface NameResolutionTest extends TestCategory {}
   public interface CellProcessingTest extends TestCategory {}
   public interface DirectiveTest extends TestCategory {}
}
