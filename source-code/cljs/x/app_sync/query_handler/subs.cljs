
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.query-handler.subs
    (:require [x.app-core.api                   :as a :refer [r]]
              [x.app-sync.response-handler.subs :as response-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-query-response
  ; @param (keyword) query-id
  ;
  ; @usage
  ;  (r sync/get-query-response db :my-query)
  ;
  ; @return (map)
  [db [_ query-id]]
  (r response-handler.subs/get-request-response db query-id))

(defn get-query-answer
  ; @param (keyword) query-id
  ; @param (*) query-question
  ;
  ; @example
  ;  (r sync/get-query-answer db :my-query :all-users)
  ;  =>
  ;  [{...} {...}]
  ;
  ; @return (*)
  [db [_ query-id query-question]]
  (let [query-response (r get-query-response db query-id)]
       (get query-response query-question)))
