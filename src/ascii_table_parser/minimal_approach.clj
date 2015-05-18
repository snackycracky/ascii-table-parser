;;run with `lein run -m ascii-table-parser.minimal-approach`

(ns ascii-table-parser.minimal-approach)

(def sanitized-data
  (drop 2 (drop-last
            (map #(take 4 %)
                 (map #(clojure.string/split % #"\s+")
                      (clojure.string/split (slurp "resources/weather-ascii-table.txt") #"\n"))))))

(defn deviation-indexed [row]
  (vector (second row) (- (read-string (nth row 2 0)) (read-string (nth row 3 0)))))

(defn -main [& args]
  (print
    (let [resultset (apply min-key second (map #(deviation-indexed %) sanitized-data))]
      (print "The min deviation is " (second resultset) " on day " (first resultset)))))
