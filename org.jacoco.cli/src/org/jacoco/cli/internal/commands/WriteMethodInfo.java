package org.jacoco.cli.internal.commands;

import org.jacoco.report.internal.html.page.AddMethodListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class WriteMethodInfo implements AddMethodListener {
    private StringBuilder info = new StringBuilder();
    private File          html;

    public WriteMethodInfo(File html) {
        this.html = html;
    }

    public void onAdd(String packageName, String vmClassName, String vmMethodName, String methodName) {
        this.info.append(packageName).append(";").append(vmClassName).append(";").append(vmMethodName).append(";").append(methodName).append(System.getProperty("line.separator"));
    }

    public void write() {
        if (this.html != null) {
            File parentFile = this.html.getParentFile();
            if (parentFile != null) {
                File file = new File(parentFile.getAbsolutePath(), "methodInfo.txt");
                if (file.exists()) {
                    file.delete();
                }

                boolean newFile = false;

                try {
                    newFile = file.createNewFile();
                } catch (IOException var16) {
                    var16.printStackTrace();
                }

                if (newFile && file.isFile()) {
                    FileWriter fileWriter = null;

                    try {
                        fileWriter = new FileWriter(file);
                        fileWriter.write(this.info.toString());
                    } catch (IOException var15) {
                        var15.printStackTrace();
                    } finally {
                        if (fileWriter != null) {
                            try {
                                fileWriter.close();
                            } catch (IOException var14) {
                                var14.printStackTrace();
                            }
                        }

                    }
                }

            }
        }
    }
}

