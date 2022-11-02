
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.dictionary.term-handler.subs
    (:require [mid-fruits.map :as map]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-term
  ; @param (keyword) term-id
  ; @param (keyword)(opt) language
  ;
  ; @example
  ;  (r get-term db :my-term)
  ;  =>
  ;  {:en "My term"}
  ;
  ; @example
  ;  (r get-term db :my-term :en)
  ;  =>
  ;  "My term"
  ;
  ; @return (map or string)
  [db [_ term-id language]]
  (if language (get-in db [:dictionary :term-handler/data-items term-id language])
               (get-in db [:dictionary :term-handler/data-items term-id])))

(defn term-exists?
  ; @param (keyword) term-id
  ; @param (keyword)(opt) language
  ;
  ; @usage
  ;  (r term-exists? db :my-term)
  ;
  ; @usage
  ;  (r term-exists? db :my-term :en)
  ;
  ; @return (boolean)
  [db [_ term-id language]]
  (if language (map/contains-key? (get-in db [:dictionary :term-handler/data-items term-id]) language)
               (map/contains-key? (get-in db [:dictionary :term-handler/data-items])         term-id)))
