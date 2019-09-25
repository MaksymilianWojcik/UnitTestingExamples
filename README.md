
# Tests

- Local Unit tests: JUnit, Mockito
- Instrumentation tests: JUnit, Mockito
- UI tests: espresso

#### Local Unit tests
Run locally on our machine. They are run using JVM, so language Java. They are fast, don't need emulator etc. Its for testing code logic.

#### Instrumentation tests
They are similar to local unit tests, almost the same. They can test android specific functionality, means like Activities, Fragments, Context, Services etc. All lifecycle stuff that is unique for android.
They need to be run on a real device or an emulator. There are tho some libraries that allow to run those tests without a device, for example RoboElectric.

#### UI tests 
Those are for simulating what a user is doing in the application.
So like pressing buttons etc. Obivously they also require real device or an emulator.


### Directories
We have 3 directories in our project java folder (Android view).

- com
- com (androidTest)
- com (test)

or in android studio will look like in src (Project view):

- androidTest (Mockito, JUnit, Espresso) - instrumentation & ui tests
- test (Mockito, JUnit) - local unit tests
- main


## JUnit
First I would like to provide the link to the guide reference: (linke)[https://junit.org/junit5/docs/current/user-guide/#overview-what-is-junit-5]

### Test classes and methods, annotations etc. (general)

- Test Class: any top-level class, static member class, or @Nested class that contains at least one test method.
Test classes must not be abstract and must have a single constructor.

- Test Method: any instance method that is directly annotated or meta-annotated with @Test, @RepeatedTest, @ParameterizedTest, @TestFactory, or @TestTemplate.
Lifecycle Method: any method that is directly annotated or meta-annotated with @BeforeAll, @AfterAll, @BeforeEach, or @AfterEach.

Test classes and test methods must not be private!

TODO

### Assertions

All JUnit Jupiter assertions are static methods in the Assertions class.

- assertEquals
- assertTrue
- assertAll
- assertNotNull
- assertThrows
- assertDoesNotThrow
- assertTimeout
- assertTimeoutPreemptively


### Assumptions
- assumeTrue
- assumingThat

### JRE conditions
- @EnabledOnJre(JAVA_8)
- @EnabledOnJre({JAVA_9, JAVA_10})
- @DisabledOnJre()
...



