
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.components.value.views
    (:require [random.api   :as random]
              [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @param (keyword)(opt) value-id
  ; @param (metamorphic-content) value
  ;  {:content (keyword, hiccup, integer, number or string)
  ;   :prefix (string)(opt)
  ;   :replacements (numbers or strings in vector)(opt)
  ;   :suffix (string)(opt)}
  ;
  ; @usage
  ;  (value {...})
  ;
  ; @usage
  ;  (value :my-value {...})
  ;
  ; @return (string)
  ([value]
   (component (random/generate-keyword) value))

  ([_ value]
   @(r/subscribe [:x.compontents/get-metamorphic-value value])))
