# language: en

Feature: [1] Feature parallel execution

  Scenario Template: [1]-[<index>] Scenario Template parallel execution - cucumber built-in feature
    * preprocessor list of string <string> object <object>
    Examples:
      | index | string     | object          |
      | 1     | [1, 2]     | [key: "value1"] |
      | 2     | ['1', '2'] | [1, 2, 3, 4]    |
      | 3     | ["1", "2"] | 1.0             |