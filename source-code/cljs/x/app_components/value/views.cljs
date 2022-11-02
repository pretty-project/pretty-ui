
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.value.views
    (:require [mid-fruits.random :as random]
              [re-frame.api      :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @param (keyword)(opt) value-id
  ; @param (map) value-props
  ;  {:prefix (string)(opt)
  ;   :replacements (numbers or strings in vector)(opt)
  ;   :suffix (string)(opt)
  ;   :value (metamorphic-content)}
  ;
  ; @usage
  ;  (value {...})
  ;
  ; @usage
  ;  (value :my-value {...})
  ;
  ; @return (string)
  ([value-props]
   (component (random/generate-keyword) value-props))

  ([_ value-props]
   @(r/subscribe [:components/get-metamorphic-value value-props])))
