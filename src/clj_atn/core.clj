(ns clj-atn.core
  (:use [clj-atn util])
  (:require [clojure.java.io :as io]))

(defn read-bin-file
  "Read file as a byte array"
  [filename]
  (with-open [input (new java.io.FileInputStream filename) 
              output (new java.io.ByteArrayOutputStream)]
    (io/copy input output)
    (.toByteArray output)))

(defn- version [{:keys [stream tree]}]
  (let [{rest :stream val :val} (read-int-32 stream)]
    {:stream rest 
     :tree (assoc tree :version val)}))

(defn parse
  "Parse the binary array"
  [stream]
  (-> {:stream stream :tree {}} version))
