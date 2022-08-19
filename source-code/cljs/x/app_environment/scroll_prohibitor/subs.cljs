
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.scroll-prohibitor.subs
    (:require [mid-fruits.map :as map]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scroll-prohibiton-added?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [scroll-prohibitions (get-in db [:environment :scroll-prohibitor/data-items])]
       (map/nonempty? scroll-prohibitions)))
