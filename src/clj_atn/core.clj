(ns clj-atn.core
  (:require [clojure.java.io :as io]))

(defn read-bin-file
  "Read file as a byte array"
  [filename]
  (with-open [input (new java.io.FileInputStream filename) 
              output (new java.io.ByteArrayOutputStream)]
    (io/copy input output)
    (.toByteArray output)))
