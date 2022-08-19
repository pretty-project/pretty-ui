
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.intersection-observer
    (:require [mid-fruits.candy :refer [return]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn intersection-observer
  ; @param (function) callback-f
  ;
  ; @usage
  ;  (dom/intersection-observer (fn [intersecting?] ...))
  ;
  ; @return (?)
  [callback-f]
  (letfn [(f [%] (callback-f (-> % (aget 0) .-isIntersecting)))]
         (js/IntersectionObserver. f {})))

(defn setup-intersection-observer!
  ; @param (DOM element) element
  ; @param (function) callback-f
  ;
  ; @usage
  ;  (dom/setup-intersection-observer! my-element (fn [intersecting?] ...))
  ;
  ; @return (?)
  [element callback-f]
  (let [observer (intersection-observer callback-f)]
       (.observe observer element)
       (return   observer)))

(defn remove-intersection-observer!
  ; @param (?) observer
  ; @param (DOM element) element
  ;
  ; @usage
  ;  (dom/remove-intersection-observer! my-observer my-element)
  ;
  ; @return (undefined)
  [observer element]
  (.unobserve observer element))
