;;run with `lein run -m ascii-table-parser.minimal-approach`

(ns ascii-table-parser.minimal-approach)

(def sanitized-data
  ; drop top header and empty row and bottom result line.
  (drop 2 (drop-last
            ; take the first 4 columns
            (map #(take 4 %)
                 ;split every row by spaces.
                 (map #(clojure.string/split % #"\s+")
                      ; split file by new lines
                      (clojure.string/split (slurp "resources/weather-ascii-table.txt") #"\n"))))))

; row is an array of columns. The first element is a space, the first actual column is the second element.
(defn deviation-indexed [row]
  (vector (second row) (- (read-string (nth row 2 0)) (read-string (nth row 3 0)))))

(defn -main [& args]
  (print
    ; apply the function min-key on the second entry in the list for the min. deviation.
    (let [resultset (apply min-key second (map #(deviation-indexed %) sanitized-data))]
      (print "The min deviation is " (second resultset) " on day " (first resultset)))))
