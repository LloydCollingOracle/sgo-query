Provides a simple object-oriented query facility, currently only having
a hibernate implementation.

BUILDING
========
Note that this project uses model implementations from the Echo3 project
(http://echo.nextapp.com).  This is due to the fact that those models
are fully serializable, whereas Swing models are not.

You will need to get the Echo3-App jar in your maven repository, under
the group and artifact id that you see in the POM.  You will need
to check out the correct revision of echo3 and build it and use the
ant targets in its build.xml to install into your local repository.

Hopefully Echo3 jars will be in public maven repositories before too long.
