
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.download.validators
    (:require [engines.item-lister.download.subs :as download.subs]
              [re-frame.api                      :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ lister-id server-response]]
  (let [resolver-answer (r download.subs/get-resolver-answer db lister-id :get-items server-response)
        all-item-count  (:all-item-count resolver-answer)
        received-items  (:items          resolver-answer)]
       (and (integer? all-item-count)
            (vector?  received-items))))
