(ns clj-atn.core
  (:use [clj-atn util])
  (:require [clojure.java.io :as io]))

(defn- version [{:keys [stream tree]}]
  (let [{rest :stream val :val} (read-int-32 stream)]
    {:stream rest 
     :tree (assoc tree :version val)}))

(defn parse
  "Parse the binary array"
  [stream]
  (-> {:stream stream :tree {}} version))

(defn read-atn
  "Read Photoshop action file and return map"
  [filename]
  (-> filename read-bin-file parse :tree))