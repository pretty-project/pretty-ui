
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.term-handler.events
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-term!
  ; @param (keyword) term-id
  ; @param (map) term
  ;
  ; @usage
  ;  (r dictionary/add-term! db :my-term {:en "My term"})
  ;
  ; @return (map)
  [db [_ term-id term]]
  (assoc-in db [:dictionary :term-handler/data-items term-id] term))

(defn add-terms!
  ; @param (map) terms
  ;
  ; @usage
  ;  (r dictionary/add-terms! db {:my-term {:en "My term"}})
  ;
  ; @return (map)
  [db [_ terms]]
  (update-in db [:dictionary :term-handler/data-items] merge terms))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) term-id
; @param (map) term
;
; @usage
;  [:dictionary/add-term! :my-term {:en "My term"}]
(r/reg-event-db :dictionary/add-term! add-term!)

; @param (map) terms
;
; @usage
;  [:dictionary/add-terms! {:my-term {:en "My term"}}]
(r/reg-event-db :dictionary/add-terms! add-terms!)
