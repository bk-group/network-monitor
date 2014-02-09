/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2013 Carmen Alvarez (c@rmen.ca)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jraf.android.networkmonitor.app.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import android.content.Context;

import org.jraf.android.networkmonitor.Constants;
import org.jraf.android.networkmonitor.R;
import org.jraf.android.networkmonitor.app.export.FormatterFactory.FormatterStyle;

/**
 * Export the Network Monitor data to an HTML file. The HTML file includes CSS specified in the strings XML file.
 */
public class HTMLExport extends TableFileExport {
    private static final String HTML_FILE = "networkmonitor.html";
    private PrintWriter mPrintWriter;


    /**
     * @param external if true, the file will be exported to the sd card. Otherwise it will written to the app's internal storage.
     */
    public HTMLExport(Context context, boolean external, FileExport.ExportProgressListener listener) throws FileNotFoundException {
        super(context, new File(external ? context.getExternalFilesDir(null) : context.getFilesDir(), HTML_FILE), FormatterStyle.XML, listener);
        mPrintWriter = new PrintWriter(mFile);
    }

    @Override
    void writeHeader(String[] columnNames) {
        mPrintWriter.println("<html>");
        mPrintWriter.println("  <head>");
        mPrintWriter.println(mContext.getString(R.string.css));
        mPrintWriter.println("  </head><body>");
        mPrintWriter.println("<table><thead>");

        mPrintWriter.println("  <tr>");
        for (String columnName : columnNames) {
            mPrintWriter.println("    <th>" + columnName + "</th>");
        }
        mPrintWriter.println("  </tr></thead><tbody>");
    }

    @Override
    void writeRow(int rowNumber, String[] cellValues) {
        // Alternating styles for odd and even rows.
        String trClass = "odd";
        if (rowNumber % 2 == 0) trClass = "even";
        mPrintWriter.println("  <tr class=\"" + trClass + "\">");

        for (String cellValue : cellValues) {
            String tdClass = "";
            // Highlight PASS in green and FAIL in red.
            if (Constants.CONNECTION_TEST_FAIL.equals(cellValue)) tdClass = "fail";
            else if (Constants.CONNECTION_TEST_PASS.equals(cellValue)) tdClass = "pass";
            else if (Constants.CONNECTION_TEST_SLOW.equals(cellValue)) tdClass = "slow";
            mPrintWriter.println("    <td class=\"" + tdClass + "\">" + cellValue + "</td>");
        }
        mPrintWriter.println("  </tr>");
        mPrintWriter.flush();
    }

    @Override
    void writeFooter() {
        mPrintWriter.println("</tbody></table>");
        mPrintWriter.println("</body></html>");
        mPrintWriter.flush();
        mPrintWriter.close();
    }
}
