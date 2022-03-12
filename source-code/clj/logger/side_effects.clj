
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns logger.side-effects
    (:require [logger.config    :as config]
              [mid-fruits.time  :as time]
              [server-fruits.io :as io]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init!
  ; @param (string) filename
  [filename]
  (println "Logger initializing:" filename)
  (if-not (io/file-exists? filename)
          ; TODO ...
          ()))

(defn write!
  ; @param (string) filename
  ; @param (*) content
  [filename content]
  (let [filepath  (str config/LOG-PATH "/" filename)
        timestamp (time/timestamp-string)
        output    (str timestamp " " content)]
       (io/append-to-file! filepath output {:max-line-count config/MAX-LINE-COUNT})))
