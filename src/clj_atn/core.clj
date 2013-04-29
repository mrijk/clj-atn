(ns clj-atn.core
  (:use [clj-atn util]))

(defn- version [{:keys [stream tree]}]
  (let [{rest :stream val :val} (read-int-32 stream)]
    {:stream rest 
     :tree (assoc tree :version val)}))

(defn- set-name [{:keys [stream tree]}]
  (let [{rest :stream val :val} (read-unicode-string stream)]
    {:stream rest 
     :tree (assoc tree :set-name val)}))

(defn parse
  "Parse the binary array"
  [stream]
  (-> {:stream stream :tree {}}
      version
      set-name))

(defn read-atn
  "Read Photoshop action file and return map"
  [filename]
  (-> filename read-bin-file parse :tree))