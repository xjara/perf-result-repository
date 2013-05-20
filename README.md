perf-result-repository
======================

Test Result Repository with Web User Interface

This project was developed as master's thesis at FIT VUT Brno for Red Hat company.


Description:
------------
This client-server application allows to test the JBoss platform.
Client gathers during the testing process the performance data and sends them to the server application.
Server application serves as a repository for this data and allows to analyze and compare them.

Project contains:
-----------------

PerfObjects - Objects used in client-server communication.

PerfClient - The client application.

PerfClientServlet - Inicialization servlet for remote client.

PerfRtFilter - Tool for measuring the response time.

PerfServer - The server application.
