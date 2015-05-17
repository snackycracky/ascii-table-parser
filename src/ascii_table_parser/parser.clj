(ns ascii-table-parser.parser)

(def raw_header_metadata
  {:Dy     {:width 4 :order 0 :numeric true}
   :MxT    {:width 6 :order 1 :numeric true}
   :MnT    {:width 6 :order 2 :numeric true}
   :AvT    {:width 6 :order 3}
   :HDDay  {:width 6 :order 4}
   :AvDP   {:width 6 :order 5}
   :1HrP   {:width 5 :order 6}
   :TPcpn  {:width 6 :order 7}
   :WxType {:width 6 :order 8}
   :PDir   {:width 6 :order 9}
   :AvSp   {:width 5 :order 10}
   :Dir    {:width 4 :order 11}
   :MxS    {:width 4 :order 12}
   :SkyC   {:width 5 :order 13}
   :MxR    {:width 4 :order 14}
   :MnR    {:width 4 :order 15}
   :AvSLP  {:width 6 :order 16}})

;; order header metadata by order number (found http://clojuredocs.org/clojure.core/sorted-map-by)
(def header_metadata (into (sorted-map-by (fn [key2 key1]
                                            (compare (get (get raw_header_metadata key2) :order)
                                                     (get (get raw_header_metadata key1) :order))))
                           raw_header_metadata))


(defn first_n_column_metadata [header_key metadata]
  (take (.indexOf (keys metadata) header_key) (vals metadata)))


(defn column_start [col_key table_metadata]
  (reduce + (map #(get % :width) (first_n_column_metadata col_key table_metadata))))

(def cols (keys header_metadata))

(defn col_width [metadata key]
  (get (get metadata key) :width))

(defn start_end_index [metadata col_key]
  [(column_start col_key metadata) (+ (column_start col_key metadata) (col_width metadata col_key))])

(defn data_from_row_string [row metadata col_key]
  (subs row (first (start_end_index metadata col_key))
           (second (start_end_index metadata col_key))))

(defn col_numeric? [metadata col_key]
  (get (get metadata col_key) :numeric))

(defn split_row_by_column_size [row]
  (for [col_key cols]
    (if (> (count row) 0)
      (if (= true (col_numeric? header_metadata col_key))
        (read-string
          (data_from_row_string row header_metadata col_key))
        (data_from_row_string row header_metadata col_key))
      "no parsing")))

(defn assign_header_key_to_cell [row]
  (apply hash-map (flatten
                    (map-indexed (fn [i v] [(nth cols i) v]) (split_row_by_column_size row)))))

(defn table_as_hash [ascii-table-file]
  (map #(assign_header_key_to_cell %) (clojure.string/split ascii-table-file #"\n")))