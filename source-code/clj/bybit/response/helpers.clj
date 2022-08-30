
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns bybit.response.helpers
    (:require [mid-fruits.candy  :refer [return]]
              [mid-fruits.json   :as json]
              [mid-fruits.reader :as reader]
              [mid-fruits.string :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn time-now->epoch-ms
  ; @param (string) n
  ;
  ; @example
  ;  (bybit/time-now->epoch-ms "1645550000.123456")
  ;  =>
  ;  1645550000123
  ;
  ; @return (integer)
  [n]
  (let [s  (string/before-first-occurence n ".")
        ms (string/after-first-occurence  n ".")]
       (reader/read-str (str s (subs ms 0 3)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn GET-response->body
  ; @param (map) response
  ;  {:body (string)}
  ;
  ; @example
  ;  (bybit/GET-response->body {... :body "{\"result\":[{...},{...}]}"})
  ;  =>
  ;  {:result [{...} {...}]}
  ;
  ; @return (map)
  ;  {}
  [{:keys [body]}]
  ; XXX#0147
  ; A reader/string->mixed függvény ...
  ; ... nem szereti így: {\"my-key\":\"My value\", ...}
  ; ... így szereti:     {\"my-key\" \"My value\", ...}
  (-> (string/replace-part body #"\":" "\" ")
      (reader/string->mixed)
      (json/keywordize-keys)
      (json/hyphenize-keys)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn POST-response->headers
  ; @param (map) response
  ;  {:headers (string)}
  ;
  ; @example
  ;  (bybit/POST-response->headers {...})
  ;  =>
  ;  {...}
  ;
  ; @return (map)
  ;  {}
  [{:keys [headers]}]
  (return headers))

(defn POST-response->body
  ; @param (map) response
  ;  {:body (string)}
  ;
  ; @example
  ;  (bybit/POST-response->body {...})
  ;  =>
  ;  {...}
  ;
  ; @return (map)
  ;  {}
  [{:keys [body]}]
  ; XXX#0147
  (-> (string/replace-part body #"\":" "\" ")
      (reader/string->mixed)
      (json/keywordize-keys)
      (json/hyphenize-keys)))
