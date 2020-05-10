# language: en

Feature: [2] Test Feature and Scenario Vars

  Scenario: Settings
    * for feature variables errorIfNotExists set to false

  Scenario: Share var - 2
    * assert that $shared.s_1 == [10]

  Scenario: Feature var - 2
    * define feature variable f_2 = [2]
    * evaluate and print $feature.f_2
    * assert that $feature.f_2 == [2]
    * assert that $feature.f_1 == null