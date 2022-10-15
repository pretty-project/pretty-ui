
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mongo-db.postparing
    (:require [mid-fruits.candy :refer [return]]))



;; -- Applying document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ; @param (map) options
  ;  {:postpare-f (function)(opt)}
  ;
  ; @return (namespaced map)
  [collection-name document {:keys [postpare-f] :as options}]
  (try (if postpare-f (postpare-f document)
                      (return     document))
       (catch Exception e (println (str e "\n" {:collection-name collection-name :document document :options options})))))
