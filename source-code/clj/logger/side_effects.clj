
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns logger.side-effects
    (:require [mid-fruits.time  :as time]
              [server-fruits.io :as io]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def LOG-PATH "monoset-environment/log")

; @constant (integer)
(def MAX-LINE-COUNT 500)



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
  (let [filepath  (str LOG-PATH "/" filename)
        timestamp (time/timestamp-string)
        output    (str timestamp " " content)]
       (io/append-to-file! filepath output {:max-line-count MAX-LINE-COUNT})))
