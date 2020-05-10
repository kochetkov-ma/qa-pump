# language: en

Feature: [1] Test Feature and Scenario Vars

  Scenario: Scenario var - 1
    * define scenario variable max_1 = [1, 2, 3] + [4]
    * evaluate and print argument $scenario.max_1
    * assert that $scenario.max_1 == [1, 2, 3, 4]

  Scenario: Scenario var - 2
    * for scenario variables errorIfNotExists set to false
    * define scenario variable max_2 = [1, 2, 3] + [4]
    * evaluate and print argument $scenario.max_1
    * evaluate and print argument $scenario.max_2
    * assert that $scenario.max_1 == null
    * assert that $scenario.max_2 == [1, 2, 3, 4]