
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.thumbnail.helpers
    (:require [x.app-elements.engine.api :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {:height (keyword)
  ;   :width (keyword)}
  ;
  ; @return (map)
  ;  {:data-border-radius (keyword)
  ;   :data-height (keyword)
  ;   :data-width (keyword)}
  [thumbnail-id {:keys [border-radius height width] :as thumbnail-props}]
  (merge (engine/element-default-attributes thumbnail-id thumbnail-props)
         (engine/element-indent-attributes  thumbnail-id thumbnail-props)
         {:data-border-radius border-radius
          :data-height        height
          :data-width         width}))
