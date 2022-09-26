package com.holelin.sundry.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;

public class PerfMonXformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] transformed = null;
        System.out.println("Transforming" + className);
        final ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;
        try {
            cl = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
            if (cl.isInterface() == false) {
                final CtBehavior[] methods = cl.getDeclaredBehaviors();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].isEmpty() == false) {
                        doSomeThing(methods[i]);
                    }
                }
                transformed = cl.toBytecode();
            }
        } catch (IOException | CannotCompileException e) {
            System.out.println("Cloud not instrument " + className + ", exception : " + e.getMessage());
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        return transformed;
    }
    private void doSomeThing(CtBehavior method) throws NotFoundException,CannotCompileException{
        method.insertBefore("long stime = System.nanoTime();");
        method.insertAfter("System.out.println(\"leave \"+method.getName()+\" and time: \"+(System.nanoTime() -stime));");
    }
}
