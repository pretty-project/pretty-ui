
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.thumbnail.helpers
    (:require [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]
              [x.app-environment.api     :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [disabled? on-click]}]
  (if disabled? {:disabled       true}
                {:data-clickable true
                 :on-click       #(a/dispatch on-click)
                 :on-mouse-up    #(environment/blur-element!)}))

(defn thumbnail-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {:border-radius (keyword)
  ;   :height (keyword)
  ;   :width (keyword)}
  ;
  ; @return (map)
  ;  {:data-border-radius (keyword)
  ;   :data-height (keyword)
  ;   :data-width (keyword)}
  [thumbnail-id {:keys [background-size border-radius height width] :as thumbnail-props}]
  (merge (engine/element-default-attributes thumbnail-id thumbnail-props)
         (engine/element-indent-attributes  thumbnail-id thumbnail-props)
         {:data-border-radius   border-radius
          :data-height          height
          :data-width           width}))
