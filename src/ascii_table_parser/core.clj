;;run with `lein run -m ascii-table-parser.core`

(ns ascii-table-parser.core
  (:require [ascii-table-parser.parser :as parser])
  (:require [ascii-table-parser.domain :as domain]))

(def ascii-table-file (slurp "resources/weather-ascii-table.txt"))

(def temperature-hash-table
  (map #(domain/colums-as-array %) (parser/table_as_hash ascii-table-file)))

(defn -main [& args]
  (let [resultset (domain/min-deviation-day-number temperature-hash-table)]
    (print "The min deviation is " (second resultset) " on day " (first resultset))))
