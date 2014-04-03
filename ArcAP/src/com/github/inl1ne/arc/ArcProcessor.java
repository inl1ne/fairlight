package com.github.inl1ne.arc;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Created by ilewis on 4/3/14.
 */
@SupportedAnnotationTypes("com.github.inl1ne.arc.InjectCrud")
public class ArcProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

        return false;
    }
}
