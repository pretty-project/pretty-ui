
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.download.queries
    (:require [engines.item-handler.body.subs     :as body.subs]
              [engines.item-handler.core.subs     :as core.subs]
              [engines.item-handler.download.subs :as download.subs]
              [re-frame.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-suggestions-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  ;  {:handler-id (keyword)
  ;   :suggestion-keys (keywords in vector)}
  [db [_ handler-id]]
  (let [suggestion-keys (r body.subs/get-body-prop db handler-id :suggestion-keys)]
       (r core.subs/use-query-params db handler-id {:handler-id      handler-id
                                                    :suggestion-keys suggestion-keys})))

(defn get-request-item-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  ;  {:item-id (string)}
  [db [_ handler-id]]
  (let [current-item-id (r core.subs/get-current-item-id db handler-id)]
       (r core.subs/use-query-params db handler-id {:item-id current-item-id})))

(defn get-request-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (vector)
  [db [_ handler-id]]
  (let [query [(if (r core.subs/download-item? db handler-id)
                   ; If download item ...
                   (let [resolver-id    (r download.subs/get-resolver-id   db handler-id :get-item)
                         resolver-props (r get-request-item-resolver-props db handler-id)]
                       `(~resolver-id ~resolver-props)))
               (if (r core.subs/download-suggestions? db handler-id)
                   ; If download suggestions ...
                   (let [resolver-id    :item-handler/get-item-suggestions
                         resolver-props (r get-request-suggestions-resolver-props db handler-id)]
                       `(~resolver-id ~resolver-props)))]]
       (r core.subs/use-query-prop db handler-id query)))
