(ns clj-atn.util
  (:require [clojure.java.io :as io]))

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

(defn read-byte [stream]
  {:stream (next stream) :val (first stream)})

(def read-int-16 (partial read-int 2))

(def read-int-32 (partial read-int 4))

(defn read-unicode-string [stream]
  (let [{len :val rest :stream} (read-int-32 stream)
        str (take (* 2 len) rest)
        rev-str (flatten (map #(reverse %) (partition 2 str)))
        utf16-str (String. (byte-array (map byte rev-str)))]
    {:stream (drop (* 2 len) rest) :val utf16-str}))
