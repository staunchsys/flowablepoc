# FlowablePOC

Flowable Proof Of Concept for Accounts Payable Process

#Prerequisties

1. Apache Tomcat 9.0.69
2. Apache Maven 3.8.2
3. Flowable 6.7.2
4. Postgresql 14

#Steps to Install Flowable POC

1. Upload the zip file of Flowable POC to Flowable UI via "import" button in the "Apps" Tab. You can find the app file at \src\main\resources\apps
2. Publish the App
3. An App named "PIQNIC POC" will be added in the dashboard.
4. Add the "PicqnicFlowablePOC.jar" from \src\main\resources\jars folder to the lib of Apache Tomcat / Flowable-ui webapps app.
5. Restart the Tomcat Server once.
6. Create 2 Users names piqnic and piqnic1 in the IDM from flowable Dashboard and provide Task / Workflow privileges.
7. From the App "PIQNIC POC" go to Processes click on 'Start Process' and then Start "AP Header Process".
