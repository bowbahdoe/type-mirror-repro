package ex;

import java.io.IOException;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes("ex.Marker")
@SupportedSourceVersion(SourceVersion.RELEASE_23)
public final class Processor extends AbstractProcessor {

    @Override
    public boolean process(
            Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv
    ) {
        var filer = this.processingEnv.getFiler();
        var messager = this.processingEnv.getMessager();
        var elementUtils = this.processingEnv.getElementUtils();

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ex.Marker.class);
        for (var element : elements) {
            var typeElement = (TypeElement) element;
            var members = elementUtils.getAllMembers(typeElement);
                var fields = ElementFilter.fieldsIn(members);
                
                var classDecl = "public class GeneratedClass { ";

                for (var field : fields) {
                    classDecl += "\n    ";
                    messager.printMessage(Kind.WARNING, "TypeMirror's toString gave: " + field.asType().toString());
                    classDecl += field.asType().toString();
                    classDecl += " ";
                    classDecl += field.getSimpleName().toString();
                    classDecl += ";";
                    classDecl += "\n";
                }

                classDecl += "}";
                

                try {
                    var file = filer.createSourceFile(
                            "GeneratedClass",
                            element
                    );
                    try (var writer = file.openWriter()) {
                        writer.append(classDecl.toString());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        

        return true;
    }
}
