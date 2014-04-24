package com.github.inl1ne.arc;

import com.googlecode.objectify.annotation.Id;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * Created by ilewis on 4/3/14.
 */
@SupportedAnnotationTypes("com.github.inl1ne.arc.InjectCrud")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ArcProcessor extends AbstractProcessor {

    Configuration cfg = new Configuration();

    // temporary hack to get around the fact that I'm hardcoding the output filename
    // and therefore getting collisions
    boolean _continue = true;

    public ArcProcessor() {
        cfg.setClassForTemplateLoading(this.getClass(), "/");
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

        if (!_continue) return false;
        _continue = false;
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.OTHER, "Here I am!!!");

        try {
            for (Element element : roundEnvironment.getElementsAnnotatedWith(InjectCrud.class)) {
                InjectCrud annotation = element.getAnnotation(InjectCrud.class);
                if (annotation == null) {
                    messager.printMessage(Diagnostic.Kind.WARNING, "No annotation found for " + element.getSimpleName() + ", WTF?");
                    continue;
                }
                TypeElement typeElement = (TypeElement)element;

                String apiClassName = null;
                try {
                    annotation.ApiClass();
                    throw new RuntimeException("Execution should not reach here");
                } catch (MirroredTypeException expected){
                    TypeMirror apiClassMirror = expected.getTypeMirror();
                    TypeElement apiClassType = (TypeElement) processingEnv.getTypeUtils().asElement(apiClassMirror);
                    apiClassName = apiClassType.getQualifiedName().toString();
                }

                if (apiClassName == null) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "No ApiClass specified for " + typeElement.getQualifiedName());
                }
                if (annotation.EntityPath() == null) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "No EntityPath specified for " + typeElement.getQualifiedName());
                }

                String idField = null;
                String idType = null;
                for (VariableElement f : ElementFilter.fieldsIn(typeElement.getEnclosedElements())) {
                    if (f.getAnnotation(Id.class) != null) {

                        idField = f.getSimpleName().toString();
                        idType = f.asType().toString();
                    }
                }

                if (idField == null) {
                    messager.printMessage(
                            Diagnostic.Kind.ERROR,
                            "No @Id field specified for Entity class " + typeElement.getQualifiedName().toString());
                }

                String crudClass = typeElement.getSimpleName() + "ArcCrud";
                String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();

                ArcModel model = new ArcModel(
                        packageName,
                        crudClass,
                        apiClassName,
                        annotation.EntityPath(),
                        typeElement.getSimpleName().toString(),
                        idField,
                        idType);
                JavaFileObject file = processingEnv.getFiler().createSourceFile(String.format("%s.%s", packageName, crudClass));
                Writer writer = file.openWriter();
                Template tpl = cfg.getTemplate("ArcCloudEndpointsOfy.ftl");
                tpl.process(model, writer);
                writer.close();
            }
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage() + e.getStackTrace()[0].getLineNumber());
        } catch (TemplateException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage() + e.getStackTrace()[0].getLineNumber());
        } catch (Exception e) {
            messager.printMessage(Diagnostic.Kind.ERROR, String.format("%s at %s line %d",
                    e.getMessage(),
                    e.getStackTrace()[0].getFileName(),
                    e.getStackTrace()[0].getLineNumber()));
            for (StackTraceElement frame: e.getStackTrace()) {
                messager.printMessage(Diagnostic.Kind.NOTE, frame.toString());
            }
        }


        return true;
    }
}
