(ns clj-atn.util)

(defn- read-int [len stream]
  (let [[head rest] (split-at len stream)]
    {:stream rest :val (reduce #(+ (* 256 %1) %2) head)}))

(def read-int-32 (partial read-int 4))
