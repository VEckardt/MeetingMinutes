# Meeting Minutes for PTC Integrity
Demo case how to use RESTeasy together with PTC Integrity for Meeting Minutes

If you are interested in "Meeting Minutes" and / or "RESTful services" in Integrity LM, read on ...

At PTC Consulting, we usually analyze different solution alternatives for our customers. During such an evaluation project, we analyzed the possibility of interacting with RESTful Services. As an outcome, a library was created that communicates with Integrity in order to obtain items and to create simple documents. (Since Integrity LM 10.9 and still also in 11.1 we have included RESTeasy (http://resteasy.jboss.org) as one of the well-known RESTful implementations.)

I was then looking for a good example and reminded me of my interest to go away from Excel and Word Meeting Minutes. So I implemented a simple "Meeting Minutes Application" on top of the RESTeasy libs.

I am happy to share this with interested people in the state as it is now.
 
## Please note:
It was only created for demo purposes
It runs on a separate Integrity project server (not on the PROD!)
It meets the minimal requirements for a Meeting Minutes app.
It uses RESTeasy, jQuery, and Java ServerPages
It offers REST Basic Authentication
It uses the server side API connection to communicate with Integrity
It is NOT a supported PTC solution!

## Here are some details:
The Meeting Minutes form:
> MeetingMinutes1_EmptyForm.PNG
The JavaScript for the REST call:
> MeetingMinutes1_JSCode.PNG
The Java REST consumer:
> MeetingMinutes1_RSCode.PNG

An example after submitting the Meeting Minutes into Integrity:

> MeetingMinutes1.PNG
> MeetingMinutes1_Result.PNG
 
## Installation steps (overview):
Configure a Meeting Minutes Document in Integrity (3 Types, Categories, Fields etc.)
Configure a User Group
Review and install the IntegrityREST war file on server
Review and install the MeetingMinutes war file on server.
 
## Possible Improvements:
Please do not expect a feature rich implementation.
 
I can think of the following improvements:

- Meeting Minutes overview and serach capabilities
- Load of existing Meeting Minutes into this form
- New Fields "State" and "Description" on Header, "State" on node
- New Pick list for the target project.
- New Multi Pick List for the attendees
- Automatic e-mails to the attendees when state changed to "Delivered" (Integrity Trigger)
- Automatic task creation when category is task (Integrity Trigger)
- Automatic PDF generation on server
- Additional Comments for each node
- etc etc.
 
If you are interested in the implementation of such features and want to contribute, please also let me know.
 
Looking forward to your interest, comments and feedback.
 
 
### How do you manage Meeting Minutes in your group today?
