
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns pathom.subs
    (:require [x.app-sync.response-handler.subs]
              [re-frame.api :as r :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-sync.response-handler.subs
(def get-request-response x.app-sync.response-handler.subs/get-request-response)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-query-response
  ; @param (keyword) query-id
  ;
  ; @usage
  ;  (r pathom/get-query-response db :my-query)
  ;
  ; @return (map)
  [db [_ query-id]]
  (r get-request-response db query-id))

(defn get-query-answer
  ; @param (keyword) query-id
  ; @param (*) query-question
  ;
  ; @example
  ;  (r pathom/get-query-answer db :my-query :all-users)
  ;  =>
  ;  [{...} {...}]
  ;
  ; @return (*)
  [db [_ query-id query-question]]
  (let [query-response (r get-query-response db query-id)]
       (get query-response query-question)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:pathom/get-query-response :my-query]
(r/reg-sub :pathom/get-query-response get-query-response)

; @usage
;  [:pathom/get-query-answer :my-query :all-users]
;(r/reg-sub :pathom/get-query-answer get-query-answer)
