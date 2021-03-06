Title
===========
OpenRedmine

Description
==========
OpenRedmine is an android Redmine client.

Requirement:
* Redmine 1.2 later
* API access key (Change from "My Account")

CAUTION:
* Cache data(downloaded issues) are stored WITHOUT encryption. To remove cache data immediately , open connection list - show menu - remove all cache 
* This is gamma release, so it is not safe for anything. On Android 2.x, the view would be wrong some times.

Connection:
* Allow to connect UNSAFE SSL sites powered by transdroid
* Allow to connect via Basic Authentication
* Get API key from web site semi-automatically
* Connection via gzip(compresstion)

Features:
* View issues offline
* Filter (Status/Tracker/Category/Priority/Author/Assined)
* Sort (IssueID/Created/Modified/... etc)
* Show changelogs, relative issues
* Create or modify issue/timeentry
* Download file related with issues (saved to /sdcard/download/jp.redmine.redmineclient/)
* View wiki

Permissions:
* INTERNET - connect to redmine server
* VIBRATE - notify with vibrate on tap the list item
* WRITE EXTERNAL STORAGE - download item and share to other app

Release planing:
* Get nested tickets
* Show activities
* TimeEntry like stop watch (no plan...)

Note:
* This app is open source(GPL exclude libraries), you can contribute.
* Notice via https://github.com/indication/OpenRedmine or twitter @OpenRedmine if you feel something good or bad.