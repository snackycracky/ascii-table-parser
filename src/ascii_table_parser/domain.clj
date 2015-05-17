(ns ascii-table-parser.domain)

(defn diff [a b]
  ;; checks a if it matches the regex of a float or a real number
  (if-not (= nil (re-matches #"\d+|\d+\.\d+" (str a)))
    (if (< a b) (- b a) (- a b))))

(defn colums-as-array [row-hash]
  (vector (get row-hash :Dy) (get row-hash :MxT) (get row-hash :MnT)))

(defn min-deviation-day-number [set]
  ;; finds the min deviation of the map below
  (apply min-key second
         ;; drop 2 because the first two rows in the file do not contain data.
         (drop 2
               ;; creates seq of arrays with each 2 items 1. the day num. and 2. the temperature deviation (diff max min)
               (map #(vector (first %) (diff (second %) (nth % 2))) set))))
