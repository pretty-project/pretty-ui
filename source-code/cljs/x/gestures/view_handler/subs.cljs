
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.gestures.view-handler.subs
    (:require [re-frame.api :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-view-id
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r get-current-view-id db :my-view-handler)
  ;
  ; @return (keyword)
  [db [_ handler-id]]
  (get-in db [:x.gestures :view-handler/data-items handler-id :view-id]))

(defn view-selected?
  ; @param (keyword) handler-id
  ; @param (keyword) view-id
  ;
  ; @usage
  ;  (r view-selected? db :my-view-handler :my-view)
  ;
  ; @return (boolean)
  [db [_ handler-id view-id]]
  (= view-id (r get-current-view-id db handler-id)))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.gestures/get-current-view-id :my-view-handler]
(r/reg-sub :x.gestures/get-current-view-id get-current-view-id)

; @usage
;  [:x.gestures/view-selected? :my-view-handler :my-view]
(r/reg-sub :x.gestures/view-selected? view-selected?)
