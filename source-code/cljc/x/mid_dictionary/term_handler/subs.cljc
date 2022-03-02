
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.term-handler.subs
    (:require [mid-fruits.map :as map]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-term
  ; @param (keyword) term-id
  ; @param (keyword)(opt) language-id
  ;
  ; @example
  ;  (r dictionary/get-term db :my-term)
  ;  =>
  ;  {:en "My term"}
  ;
  ; @example
  ;  (r dictionary/get-term db :my-term :en)
  ;  =>
  ;  "My term"
  ;
  ; @return (map or string)
  [db [_ term-id language-id]]
  (if language-id (get-in db [:dictionary :term-handler/data-items term-id language-id])
                  (get-in db [:dictionary :term-handler/data-items term-id])))

(defn term-exists?
  ; @param (keyword) term-id
  ; @param (keyword)(opt) language-id
  ;
  ; @usage
  ;  (r dictionary/term-exists? db :my-term)
  ;
  ; @usage
  ;  (r dictionary/term-exists? db :my-term :en)
  ;
  ; @return (boolean)
  [db [_ term-id language-id]]
  (if language-id (map/contains-key? (get-in db [:dictionary :term-handler/data-items term-id]) language-id)
                  (map/contains-key? (get-in db [:dictionary :term-handler/data-items])         term-id)))
