package me.mrfunny.mythicmobs.annotation;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("me.mrfunny.mythicmobs.annotation.*")
public class MainAnnotationProcessor extends AbstractProcessor {
    private boolean created = false;
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        String packageName = "";
        Map<String, String[]> generated = new HashMap<>();
        for(Element rawElement : roundEnvironment.getElementsAnnotatedWith(Signal.class)) {
            if(rawElement.getKind() != ElementKind.CLASS) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@Signals annotation is only supported by classes");
                continue;
            }
            String className = rawElement.asType().toString();
            print("Generating signals for " + className);
            PackageElement packageE = processingEnv.getElementUtils().getPackageOf(rawElement);
            packageName = packageE.getQualifiedName().toString();
            if(processingEnv.getElementUtils().getAllTypeElements(packageName).stream().anyMatch(e ->
                e.getSimpleName().contentEquals(className)
            )) {
                packageName += ".signals";
            }
            String[] generate = rawElement.getAnnotation(Signal.class).value();
            for (String s : generate) {
                if(s.trim().equals("")) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@Signal values can't accept empty values (\"\")");
                }
            }
            generated.put(rawElement.asType().toString(), generate);
        }

        if(created) {
            return true;
        }

        try {

            JavaFileObject enumFile = processingEnv.getFiler().createSourceFile(packageName + "Signals");
            try(PrintWriter out = new PrintWriter(enumFile.openWriter())) {
                if(!packageName.trim().equals("")) {
                    out.println("package " + packageName + ";");
                }
                out.println("public class Signals {");
                for(Map.Entry<String, String[]> entry : generated.entrySet()) {
                    out.println("public static enum For" + entry.getKey() + " implements me.mrfunny.mythicmobs.internal.MobSignal {");
                    out.println(String.join(",", entry.getValue()));
                    out.println("}");
                }

                out.println("}");
            }
            created = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void print(String str) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, str);
    }
}
