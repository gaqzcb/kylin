/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.kylin.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class ZipFileUtils {
    public static void compressZipFile(String sourceDir, String zipFilename) throws IOException {
        if (!validateZipFilename(zipFilename)) {
            throw new RuntimeException("Zipfile must end with .zip");
        }
        ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(zipFilename));
        compressDirectoryToZipfile(normDir(new File(sourceDir).getParent()), normDir(sourceDir), zipFile);
        IOUtils.closeQuietly(zipFile);
    }

    private static void compressDirectoryToZipfile(String rootDir, String sourceDir, ZipOutputStream out) throws IOException {
        for (File sourceFile : new File(sourceDir).listFiles()) {
            if (sourceFile.isDirectory()) {
                compressDirectoryToZipfile(rootDir, sourceDir + normDir(sourceFile.getName()), out);
            } else {
                ZipEntry entry = new ZipEntry(normDir(StringUtils.isEmpty(rootDir) ? sourceDir : sourceDir.replace(rootDir, "")) + sourceFile.getName());
                out.putNextEntry(entry);

                FileInputStream in = new FileInputStream(sourceDir + sourceFile.getName());
                IOUtils.copy(in, out);
                IOUtils.closeQuietly(in);
            }
        }
    }

    private static boolean validateZipFilename(String filename) {
        if (!StringUtils.isEmpty(filename) && filename.trim().toLowerCase().endsWith(".zip")) {
            return true;
        }

        return false;
    }

    private static String normDir(String dirName) {
        if (!StringUtils.isEmpty(dirName) && !dirName.endsWith(File.separator)) {
            dirName = dirName + File.separator;
        }
        return dirName;
    }
}
