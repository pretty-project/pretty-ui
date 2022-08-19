
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.observe-handler.side-effects
    (:require [dom.api                                 :as dom]
              [x.app-environment.observe-handler.state :as observe-handler.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn setup-intersection-observer!
  ; @param (string) element-id
  ; @param (function) callback-f
  ;
  ; @usage
  ;  (environment/setup-intersection-observer! "my-element" (fn [intersecting?] ...))
  [element-id callback-f]
  (if-let [element (dom/get-element-by-id element-id)]
          (let [observer (dom/setup-intersection-observer! element callback-f)]
               (swap! observe-handler.state/INTERSECTION-OBSERVERS assoc element-id observer))))

(defn remove-intersection-observer!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (environment/remove-intersection-observer! "my-element")
  ;
  ; @return (undefined)
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (when-let [observer (get @observe-handler.state/INTERSECTION-OBSERVERS element-id)]
                    (swap! observe-handler.state/INTERSECTION-OBSERVERS dissoc element-id)
                    (dom/remove-intersection-observer! observer element))))
