# language: en

Feature: [1] Test Feature and Scenario Vars

  Scenario: Settings
    * for feature variables errorIfNotExists set to false

  Scenario: Share var - 1
    * define shared variable s_1 = [10]
    * assert that $shared.s_1 == [10]

  Scenario: Feature var - 1
    * define feature variable f_1 = [1]
    * evaluate and print $feature.f_1
    * assert that $feature.f_1 == [1]

  Scenario: Scenario var - 1
    * define scenario variable max_1 = [1, 2, 3] + [4]
    * evaluate and print $scenario.max_1
    * assert that $scenario.max_1 == [1, 2, 3, 4]
    * assert that $feature.f_1 == [1]

  Scenario: Scenario var - 2
    * for scenario variables errorIfNotExists set to false
    * define scenario variable max_2 = [1, 2, 3] + [4]
    * evaluate and print $scenario.max_1
    * evaluate and print $scenario.max_2
    * assert that $scenario.max_1 == null
    * assert that $scenario.max_2 == [1, 2, 3, 4]
    * assert that $feature.f_1 == [1]