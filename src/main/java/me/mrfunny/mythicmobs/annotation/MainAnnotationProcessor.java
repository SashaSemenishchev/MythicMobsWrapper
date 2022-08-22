package me.mrfunny.mythicmobs.annotation;

import com.google.auto.service.AutoService;
import me.mrfunny.mythicmobs.wrapper.Mob;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_16)
@SupportedAnnotationTypes("me.mrfunny.mythicmobs.annotation.*")
public class MainAnnotationProcessor extends AbstractProcessor {
    private boolean created = false;
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        String packageName = "";
        Map<String, String[]> generated = new HashMap<>();
        boolean empty = true;
        for(Element element : roundEnvironment.getElementsAnnotatedWith(Signal.class)) {
            if(element.getKind() != ElementKind.CLASS) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@Signals annotation is only supported by classes");
                continue;
            }
            TypeMirror type = element.asType();
            if(!isInstance(type, Mob.class)) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Class " + type + " is annotated with @Signal it doesn't implement Mob class (" + Mob.class.getName() + ")");
            }
            empty = false;
            String className = type.toString();
            int lastDot = className.lastIndexOf('.');
            String simpleClassName = className.substring(lastDot + 1);
            PackageElement packageE = processingEnv.getElementUtils().getPackageOf(element);
            packageName = packageE.getQualifiedName().toString();
            if(processingEnv.getElementUtils().getAllTypeElements(packageName).stream().anyMatch(e ->
                e.getSimpleName().contentEquals(className)
            )) {
                packageName += ".signals";
            }
            String[] generate = element.getAnnotation(Signal.class).value();
            for (String s : generate) {
                if(s.trim().equals("")) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@Signal values can't accept empty values (\"\")");
                }
            }
            generated.put(simpleClassName, generate);
        }

        if(created && empty) {
            return true;
        }

        try {
            String toCreate;
            boolean packageNameEmpty = packageName.trim().equals("");
            if(packageNameEmpty) {
                toCreate = "Signals";
            } else {
                toCreate = packageName + ".Signals";
            }
            print("Choosing " + toCreate + " as Signals class name");
            JavaFileObject enumFile = processingEnv.getFiler().createSourceFile(toCreate);
            try(PrintWriter out = new PrintWriter(enumFile.openWriter())) {
                if(!packageNameEmpty) {
                    out.println("package " + packageName + ";");
                }
                out.println();
                out.println("public class Signals {");
                for(Map.Entry<String, String[]> entry : generated.entrySet()) {
                    out.println("    public static enum For" + entry.getKey() + " implements me.mrfunny.mythicmobs.internal.MobSignal {");
                    out.println("        " + String.join(",", entry.getValue()));
                    out.println("    }");
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

    private boolean isInstance(TypeMirror type, Class<?> clazz) {
        Types types = processingEnv.getTypeUtils();
        Elements elements = processingEnv.getElementUtils();
        TypeMirror tm = elements.getTypeElement(type.toString()).asType();
        TypeMirror comparable = types.erasure(elements.getTypeElement(clazz.getName()).asType());
        return types.isAssignable(tm, comparable);
    }
}
