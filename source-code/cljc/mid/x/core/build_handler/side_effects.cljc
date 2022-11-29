
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.core.build-handler.side-effects
    (:require [re-frame.api :as r]))



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

(defn build-version
  ; @usage
  ;  (build-version)
  ;
  ; @example
  ;  (build-version)
  ;  =>
  ;  "0.4.2"
  ;
  ; @return (string)
  []
 @(r/subscribe [:x.core/get-build-version]))
