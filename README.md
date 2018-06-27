## a command-line tool for filtering potentially huge files by lists of terms

### Usage:
1. Create a file named `config.json` mimicking the example configuration file [example-config.json](https://github.com/matanster/file-filterer/blob/master/example-config.json)
2. `lein run` or `java -jar <jar-filename>` if running as an uberjar.

A cartesian product of the input files and term files will be run, producing its outputs to the output directory provided in the config file.
