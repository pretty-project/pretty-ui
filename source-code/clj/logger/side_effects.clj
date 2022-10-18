
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns logger.side-effects
    (:require [io.api        :as io]
              [logger.config :as config]
              [time.api      :as time]))



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
