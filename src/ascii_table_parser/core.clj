;;run with `lein run -m ascii-table-parser.core`

(ns ascii-table-parser.core
  (:require [ascii-table-parser.parser :as parser]))

(def ascii-table-file (slurp "resources/weather-ascii-table.txt"))

(defn -main [& args]
  (print (parser/table_as_hash ascii-table-file)))