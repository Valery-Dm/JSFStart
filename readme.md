#Simple JSF project with login-register functionality.

Written in Eclipse 4.6. Based on Java 1.8, Tomcat 8, Mojarra JSF 2.2.8.

Tests: JUnit 4.12, Mockito 1.10.19, Hamcrest 1.3, EclEmma 2.3.3 tests coverage.

Used techniques: input validations, Ajax calls, password hashing, internationalization, 
templates, composite components, etc.

For Faces Flow part GlassFish 4.1.1 server will be used for a start.
CDI annotations will be used in this part instead of legacy JSF annotations
as Faces Flow requires CDI to work

Not using any DB here, but HashMap instead (i.e. virtual DB)