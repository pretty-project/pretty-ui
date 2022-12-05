
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.download.validators
    (:require [engines.item-handler.core.subs     :as core.subs]
              [engines.item-handler.download.subs :as download.subs]
              [map.api                            :as map]
              [re-frame.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ handler-id server-response]]
  (let [received-item        (r download.subs/get-resolver-answer db handler-id :get-item server-response)
        received-suggestions (get server-response :item-handler/get-item-suggestions)]
       (and (or (map? received-suggestions)
                (not (r core.subs/download-suggestions? db handler-id)))
            (or (map/namespaced? received-item)
                (not (r core.subs/download-item? db handler-id))))))
