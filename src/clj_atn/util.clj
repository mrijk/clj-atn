(ns clj-atn.util)

(defn read-bin-file
  "Read file as a byte array"
  [filename]
  (with-open [input (new java.io.FileInputStream filename) 
              output (new java.io.ByteArrayOutputStream)]
    (io/copy input output)
    (.toByteArray output)))

(defn- read-int [len stream]
  (let [[head rest] (split-at len stream)]
    {:stream rest :val (reduce #(+ (* 256 %1) %2) head)}))

(def read-int-32 (partial read-int 4))
