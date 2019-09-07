package com.sap.demo.jco;

import java.io.IOException;

import java.io.PrintWriter;

 

import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

 

import com.sap.conn.jco.AbapException;

import com.sap.conn.jco.JCoDestination;

import com.sap.conn.jco.JCoDestinationManager;

import com.sap.conn.jco.JCoException;

import com.sap.conn.jco.JCoFunction;

import com.sap.conn.jco.JCoParameterList;

import com.sap.conn.jco.JCoRepository;

 

/**

 * Sample application that uses the Connectivity
                            service. In particular, it is

 * making use of the capability to invoke a function module in an ABAP system

 * via RFC

 *

 * Note: The JCo APIs are available under <code>com.sap.conn.jco</code>.

 */


@WebServlet("/ConnectivityRFCExample/*")

public class ConnectivityRFCExample extends HttpServlet {

    private static final long serialVersionUID = 1L;

 

    protected void doGet(HttpServletRequest request, HttpServletResponse response)

            throws ServletException, IOException {

        PrintWriter responseWriter = response.getWriter();

        try {

            // access the RFC Destination "JCoDemoSystem"

            JCoDestination destination = JCoDestinationManager.getDestination("JCoDemoSystem");

 

            // make an invocation of STFC_CONNECTION in the backend;

            JCoRepository repo = destination.getRepository();

            JCoFunction stfcConnection = repo.getFunction("STFC_CONNECTION");

 

            JCoParameterList imports = stfcConnection.getImportParameterList();

            imports.setValue("REQUTEXT", "SAP Cloud Platform Connectivity runs with JCo");


            stfcConnection.execute(destination);

            JCoParameterList exports = stfcConnection.getExportParameterList();

            String echotext = exports.getString("ECHOTEXT");

            String resptext = exports.getString("RESPTEXT");


            response.addHeader("Content-type", "text/html");

            responseWriter.println("<html><body>");

            responseWriter.println("<h1>Executed STFC_CONNECTION in system JCoDemoSystem</h1>");

            responseWriter.println("<p>Export parameter ECHOTEXT of STFC_CONNECTION:<br>");

            responseWriter.println(echotext);

            responseWriter.println("<p>Export parameter RESPTEXT of STFC_CONNECTION:<br>");

            responseWriter.println(resptext);

            responseWriter.println("</body></html>");

        } catch (AbapException ae) {

            // just for completeness: As this function module does not have an exception

            // in its signature, this exception cannot occur. But you should always

            // take care of AbapExceptions

        } catch (JCoException e) {

            response.addHeader("Content-type", "text/html");

            responseWriter.println("<html><body>");

            responseWriter

                    .println("<h1>Exception occurred while executing STFC_CONNECTION in system JCoDemoSystem</h1>");

            responseWriter.println("<pre>");

            e.printStackTrace(responseWriter);

            responseWriter.println("</pre>");

            responseWriter.println("</body></html>");

        }

    }

}
