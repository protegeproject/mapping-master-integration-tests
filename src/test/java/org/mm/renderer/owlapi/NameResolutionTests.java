package org.mm.renderer.owlapi;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.mm.renderer.owlapi.AllRenderingTestSuite.NameResolutionTest;

@RunWith(Categories.class)
@IncludeCategory(NameResolutionTest.class)
@SuiteClasses({ BasicTest.class, ReferenceTest.class })
public class NameResolutionTests
{
   // NO-OP
}
